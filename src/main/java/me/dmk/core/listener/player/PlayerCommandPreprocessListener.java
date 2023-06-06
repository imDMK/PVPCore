package me.dmk.core.listener.player;

import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.help.HelpTopic;

import java.util.Optional;

/**
 * Created by DMK on 06.01.2023
 */

@AllArgsConstructor
public class PlayerCommandPreprocessListener implements Listener {

    private final PluginConfiguration pluginConfiguration;
    private final NotificationController notificationController;
    private final ProfileController profileController;

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.profileController.getOrElseThrow(player);

        String command = event.getMessage().toLowerCase().split(" ")[0];

        if (profile.getFight().hasFight()) {
            boolean anyMatch = this.pluginConfiguration.getFightBlockedCommands()
                    .stream()
                    .anyMatch(command::startsWith);

            if (anyMatch) {
                event.setCancelled(true);

                this.notificationController.sendMessage(player,
                        StringFormatter.formatError() + " <red>Nie możesz użyć tej komendy podczas walki<dark_gray>."
                );
            }
        }

        Optional<HelpTopic> helpTopicOptional = Optional.ofNullable(
                Bukkit.getServer().getHelpMap().getHelpTopic(command)
        );

        if (helpTopicOptional.isEmpty()) {
            event.setCancelled(true);

            this.notificationController.sendMessage(player, StringFormatter.formatError() + " <red>Komenda nie istnieje<dark_gray>.");
        }
    }
}
