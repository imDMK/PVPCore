package me.dmk.core.chat.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.dmk.core.CorePlugin;
import me.dmk.core.guild.Guild;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by DMK on 29.12.2022
 */

@AllArgsConstructor
public class NotificationController {

    private final AudienceProvider audienceProvider;
    @Getter
    private final MiniMessage miniMessage;

    /* CommandSender */
    public void sendMessage(CommandSender sender, String message) {
        if (sender instanceof Player player) {
            this.audienceProvider.player(player.getUniqueId()).sendMessage(this.miniMessage.deserialize(message));
        } else {
            sender.sendMessage(message);
        }
    }

    public void sendMessage(CommandSender sender, List<String> stringList) {
        if (stringList.isEmpty()) {
            return;
        }

        String message = String.join("\n", stringList);
        this.sendMessage(sender, message);
    }

    /* Player */
    public void sendMessage(Player player, TextComponent textComponent) {
        this.audienceProvider.player(player.getUniqueId())
                .sendMessage(textComponent);
    }

    public void sendTitle(Player player, String title, String subTitle) {
        Title titleMessage = Title.title(
                this.miniMessage.deserialize(title),
                this.miniMessage.deserialize(subTitle),
                Title.DEFAULT_TIMES
        );

        this.audienceProvider.player(player.getUniqueId())
                .showTitle(titleMessage);
    }

    public void sendActionBar(Player player, String message) {
        this.audienceProvider.player(player.getUniqueId())
                .sendActionBar(this.miniMessage.deserialize(message));
    }

    public void showBossBar(Player player, BossBar bossBar) {
        this.audienceProvider.player(player.getUniqueId())
                .showBossBar(bossBar);
    }

    public void hideBossBar(Player player, BossBar bossBar) {
        this.audienceProvider.player(player.getUniqueId())
                .hideBossBar(bossBar);
    }

    /* Global messages */
    public void sendGlobalMessage(String message) {
        this.audienceProvider.all()
                .sendMessage(this.miniMessage.deserialize(message));
    }

    public void sendGlobalMessage(String message, String permission) {
        this.audienceProvider.permission(permission)
                .sendMessage(this.miniMessage.deserialize(message));
    }

    public void sendGlobalTitle(String title, String subTitle) {
        Title titleMessage = Title.title(
                this.miniMessage.deserialize(title),
                this.miniMessage.deserialize(subTitle),
                Title.DEFAULT_TIMES
        );

        this.audienceProvider.all()
                .showTitle(titleMessage);
    }

    public void showGlobalBossBar(BossBar bossBar) {
        this.audienceProvider.all()
                .showBossBar(bossBar);
    }

    public void hideGlobalBossBar(BossBar bossBar) {
        this.audienceProvider.all()
                .hideBossBar(bossBar);
    }

    /* Guild players */
    public void sendMessage(Guild guild, String message) {
        guild.getOnlineMembers().forEach(p -> this.sendMessage(p, message));
    }

    /* Plugin messages */
    public void sendGlobalPluginMessage(PluginMessageType messageType, String message) {
        ProfileController profileController = CorePlugin.getCorePlugin().getProfileController();

        switch (messageType) {
            case ACHIEVEMENT -> Bukkit.getOnlinePlayers().forEach(player -> profileController.get(player.getUniqueId())
                            .map(Profile::getProfileSettings)
                            .ifPresent(profileSettings -> {
                                if (profileSettings.isAchievementsMessages()) {
                                    this.sendMessage(player, message);
                                }
                            })
            );

            case DEATH -> Bukkit.getOnlinePlayers().forEach(player -> profileController.get(player.getUniqueId())
                    .map(Profile::getProfileSettings)
                    .ifPresent(profileSettings -> {
                        if (profileSettings.isDeathMessages()) {
                            this.sendMessage(player, message);
                        }
                    })
            );

            case SYSTEM -> Bukkit.getOnlinePlayers().forEach(player -> profileController.get(player.getUniqueId())
                    .map(Profile::getProfileSettings)
                    .ifPresent(profileSettings -> {
                        if (profileSettings.isSystemMessages()) {
                            this.sendMessage(player, message);
                        }
                    })
            );

            case GUILD -> Bukkit.getOnlinePlayers().forEach(player -> profileController.get(player.getUniqueId())
                    .map(Profile::getProfileSettings)
                    .ifPresent(profileSettings -> {
                        if (profileSettings.isGuildMessages()) {
                            this.sendMessage(player, message);
                        }
                    })
            );
        }
    }
}
