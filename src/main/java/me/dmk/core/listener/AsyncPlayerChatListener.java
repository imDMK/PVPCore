package me.dmk.core.listener;

import lombok.AllArgsConstructor;
import me.dmk.core.chat.GlobalChatCache;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.chat.waiting.ChatWaiterCache;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.cache.GuildCache;
import me.dmk.core.luckperms.LuckPermsController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.punishment.Punishment;
import me.dmk.core.profile.punishment.PunishmentType;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.profile.statistics.ProfileStatistics;
import me.dmk.core.util.TimeUtil;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.SymbolUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
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

    private final MiniMessage miniMessage;
    private final LuckPermsController luckPermsController;
    private final NotificationController notificationController;
    private final ProfileCache profileCache;
    private final GuildCache guildCache;
    private final GlobalChatCache globalChatCache;
    private final ChatWaiterCache chatWaiterCache;

    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        String message = (player.hasPermission("core.chat.message.color") ? event.getMessage() : this.miniMessage.escapeTags(event.getMessage()))
                .replace("<3", SymbolUtil.getHeart("<red>"));

        Profile profile = this.profileCache.getOrElseThrow(player);
        ProfileSettings profileSettings = profile.getProfileSettings();
        ProfileStatistics profileStatistics = profile.getProfileStatistics();

        if (this.chatWaiterCache.isWaitingForResponse(player)) {
            this.chatWaiterCache.remove(player).execute(message);
            return;
        }

        Optional<Punishment> punishment = profile.getActivePunishment(PunishmentType.MUTE);
        if (punishment.isPresent()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Wyciszono cię <dark_gray>- <red>wygasa <gold>" + (punishment.get().isPermanent() ? "nigdy" : "za " + TimeUtil.instantToString(punishment.get().getExpireAt().toInstant(), true)) + "<dark_gray>."
            );
            return;
        }

        Optional<Guild> guildOptional = profile.getGuild();
        if (guildOptional.isPresent()) {
            Guild guild = guildOptional.get();

            boolean isAllianceMessage = event.getMessage().startsWith("!!");
            boolean isGuildMessage = event.getMessage().startsWith("!");

            if (isAllianceMessage || isGuildMessage) {
                String guildMessage = (isAllianceMessage ? StringFormatter.formatAlliance() : StringFormatter.formatGuild())
                        + " " + player.getName() + "<dark_gray>: <gold>"
                        + message.replaceFirst((isAllianceMessage ? "!!" : "!"), "");

                if (isAllianceMessage) {
                    guild.getAlliances().forEach(guildTag -> this.guildCache.getByTag(guildTag).ifPresent(allianceGuild ->
                            this.notificationController.sendMessage(allianceGuild, guildMessage))
                    );
                }

                this.notificationController.sendMessage(guild, guildMessage);
                return;
            }
        }

        if (!profileSettings.isGlobalMessages()) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Posiadasz wyłączone globalne wiadomości<dark_gray>."
            );
            return;
        }

        if (!player.hasPermission("core.chat.cooldown.bypass")) {
            if (!this.globalChatCache.getGlobalChatSettings().isEnabled()) {
                this.notificationController.sendMessage(player,
                        StringFormatter.formatError() + " <red>Globalny czat jest aktualnie " + StringFormatter.formatRedGradient() + "wyłączony</gradient><dark_gray>."
                );
                return;
            }

            if (!this.globalChatCache.canUseChat(uuid)) {
                this.notificationController.sendMessage(player,
                        StringFormatter.formatError() + " <red>Zwolnij... Następną wiadomość możesz wysłać za <gold>" + TimeUtil.instantToString(this.globalChatCache.get(uuid), true) + "<dark_gray>."
                );
                return;
            }

            this.globalChatCache.put(uuid);
        }

        boolean useAdminFormat = player.hasPermission("core.chat.format.admin");

        String openingSquareBracket = StringFormatter.formatOpeningSquareBracket();
        String closingSquareBracket = StringFormatter.formatClosingSquareBracket();

        String group = this.luckPermsController.getHighestGroupPrefix(uuid)
                .map(g -> g + " ")
                .orElse("");

        String level = openingSquareBracket + "<gray>" + player.getLevel() + closingSquareBracket;
        String points = openingSquareBracket + "<gray>" + profileStatistics.getPoints() + closingSquareBracket;
        String nameAndMessage = profileSettings.getColorName().getFormat() + player.getName() + "<dark_gray>: " + (useAdminFormat ? "<red>" : "<white>") + message;

        String format = (useAdminFormat ? group : level + " " + points + " " + "<guild>" + group) + nameAndMessage;

        for (Player online : Bukkit.getOnlinePlayers()) {
            this.profileCache.get(online.getUniqueId()).ifPresent(onlineProfile -> {
                ProfileSettings onlineSettings = onlineProfile.getProfileSettings();

                if (!onlineSettings.isGlobalMessages()) {
                    return;
                }

                if (onlineSettings.getIgnoredPlayers().contains(player.getUniqueId())) {
                    return;
                }

                String formatGuildTag = StringFormatter.formatGuildTag(online, guildOptional.orElse(null), onlineProfile.getGuild().orElse(null))
                        .map(g -> g + " ")
                        .orElse("");

                this.notificationController.sendMessage(online,
                        format.replace("<guild>", formatGuildTag)
                );
            });
        }
    }
}
