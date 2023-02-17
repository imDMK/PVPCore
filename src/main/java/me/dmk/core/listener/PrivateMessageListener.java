package me.dmk.core.listener;

import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.event.PrivateMessageEvent;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Created by DMK on 13.02.2023
 */

@AllArgsConstructor
public class PrivateMessageListener implements Listener {

    private final NotificationController notificationController;

    @EventHandler
    public void onPrivateMessage(PrivateMessageEvent event) {
        Player sender = event.getSender();
        Profile senderProfile = event.getSenderProfile();

        Player receiving = event.getReceiving();
        Profile receivingProfile = event.getReceivingProfile();

        ProfileSettings senderSettings = senderProfile.getProfileSettings();
        ProfileSettings receivingSettings = receivingProfile.getProfileSettings();

        //if (sender.getUniqueId().equals(receiving.getUniqueId())) {
            //event.setCancelMessage("<red>Nie możesz wysyłać prywatnych wiadomości do samego siebie<dark_gray>.");
            //event.setCancelled(true);
            //return;
        //}

        if (!senderSettings.isPrivateMessages()) {
            event.setCancelMessage("<red>Posiadasz wyłączone prywatne wiadomości<dark_gray>.");
            event.setCancelled(true);
            return;
        }

        if (!receivingSettings.isPrivateMessages()) {
            event.setCancelMessage("<red>Gracz posiada wyłączone prywatne wiadomości<dark_gray>.");
            event.setCancelled(true);
            return;
        }

        if (senderSettings.getIgnoredPlayers().contains(receiving.getUniqueId())) {
            event.setCancelMessage("<red>Ignorujesz tego gracza<dark_gray>.");
            event.setCancelled(true);
            return;
        }

        if (receivingSettings.getIgnoredPlayers().contains(sender.getUniqueId())) {
            event.setCancelMessage("<red>Gracz zablokował cię<dark_gray>.");
            event.setCancelled(true);
            return;
        }

        String senderName = senderProfile.getColoredName();
        String receivingName = receivingProfile.getColoredName();

        this.notificationController.sendMessage(sender,
                StringFormatter.formatPrivateMessage(senderName, receivingName, event.getMessage())
        );

        this.notificationController.sendMessage(receiving,
                StringFormatter.formatPrivateMessage(senderName, receivingName, event.getMessage())
        );

        senderSettings.setLastPrivateMessage(receiving.getUniqueId());
        receivingSettings.setLastPrivateMessage(sender.getUniqueId());
    }
}
