package me.dmk.core.listener;

import lombok.AllArgsConstructor;
import me.dmk.core.chat.GlobalChatCache;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.guild.Guild;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.punishment.Punishment;
import me.dmk.core.profile.punishment.PunishmentType;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.profile.statistics.ProfileStatistics;
import me.dmk.core.util.StyleUtil;
import me.dmk.core.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by DMK on 02.01.2023
 */

@AllArgsConstructor
public class AsyncPlayerChatListener implements Listener {

    private final LuckPermsController luckPermsController;
    private final NotificationController notificationController;
    private final ProfileCache profileCache;
    private final GlobalChatCache globalChatCache;

    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Profile profile = this.profileCache.getOrElseThrow(uuid);
        ProfileSettings profileSettings = profile.getProfileSettings();
        ProfileStatistics profileStatistics = profile.getProfileStatistics();

        Optional<Punishment> punishment = profile.getActivePunishment(PunishmentType.MUTE);
        if (punishment.isPresent()) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Wyciszono cię <dark_gray>- <red>wygasa <gold>" + (punishment.get().isPermanent() ? "nigdy" : "za " + TimeUtil.instantToString(punishment.get().getExpireAt().toInstant(), true)) + "<dark_gray>."
            );
            return;
        }

        if (!profileSettings.isGlobalMessages()) {
            this.notificationController.sendMessage(player,
                    StyleUtil.getError() + " <red>Posiadasz wyłączone globalne wiadomości<dark_gray>."
            );
            return;
        }

        if (!player.hasPermission("core.chat.bypass")) {
            if (!this.globalChatCache.getGlobalChatSettings().isEnabled()) {
                this.notificationController.sendMessage(player,
                        StyleUtil.getError() + " <red>Globalny czat jest aktualnie " + StyleUtil.getRedGradient() + "wyłączony</gradient><dark_gray>."
                );
                return;
            }

            if (!this.globalChatCache.canUseChat(uuid)) {
                this.notificationController.sendMessage(player,
                        StyleUtil.getError() + " <red>Zwolnij... Następną wiadomość możesz wysłać za <gold>" + TimeUtil.instantToString(this.globalChatCache.get(uuid), true) + "<dark_gray>."
                );
                return;
            }

            this.globalChatCache.put(uuid);
        }

        boolean isAdmin = player.hasPermission("core.chat.admin");

        String group = this.luckPermsController.getHighestGroupPrefix(uuid)
                .map(g -> g + " ")
                .orElse("");

        String brackedStart = StyleUtil.getSquareBracketStart();
        String brackedEnd = StyleUtil.getSquareBracketEnd();

        String level = brackedStart + "<gray>" + player.getLevel() + brackedEnd;
        String points = brackedStart + "<gray>" + profileStatistics.getPoints() + brackedEnd;
        String nameAndMessage = profileSettings.getColorName().getFormat() + player.getName() + "<dark_gray>: " + (isAdmin ? "<red>" : "<white>") + event.getMessage();

        Optional<Guild> guildOptional = profile.getGuild();

        String format;
        if (isAdmin) {
            format = group + nameAndMessage;
        } else {
            format = level + " " + points + " " + "<guild>" + group + nameAndMessage;
        }

        Bukkit.getOnlinePlayers()
                .stream()
                .map(online -> this.profileCache.get(online.getUniqueId()))

                .filter(Optional::isPresent)
                .map(Optional::get)

                .filter(onlineProfile -> onlineProfile.getProfileSettings().isGlobalMessages())
                .filter(onlineProfile -> !onlineProfile.getProfileSettings().getIgnoredPlayers().contains(uuid))

                .forEachOrdered(online -> {
                    if (online.getPlayer().isEmpty()) {
                        return;
                    }

                    Player onlinePlayer = online.getPlayer().get();

                    String formatGuildTag = StyleUtil.formatGuildTag(onlinePlayer, guildOptional.orElse(null), online.getGuild().orElse(null))
                            .map(g -> g + " ")
                            .orElse("");

                    this.notificationController.sendMessage(onlinePlayer,
                            format.replace("<guild>", formatGuildTag)
                    );
                });
    }
}
