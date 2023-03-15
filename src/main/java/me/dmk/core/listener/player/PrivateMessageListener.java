package me.dmk.core.listener.player;

import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.event.PrivateMessageEvent;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.punishment.Punishment;
import me.dmk.core.profile.punishment.PunishmentType;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.util.TimeUtil;
import me.dmk.core.util.string.StringFormatter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

/**
 * Created by DMK on 13.02.2023
 */

@AllArgsConstructor
public class PrivateMessageListener implements Listener {

    private final NotificationController notificationController;

    @EventHandler
    public void onPrivateMessage(PrivateMessageEvent event) {
        Player sender = event.getSender();
        Player receiving = event.getReceiving();

        Profile senderProfile = event.getSenderProfile();
        ProfileSettings senderSettings = senderProfile.getProfileSettings();

        Profile receivingProfile = event.getReceivingProfile();
        ProfileSettings receivingSettings = receivingProfile.getProfileSettings();

        Optional<Punishment> punishment = senderProfile.getActivePunishment(PunishmentType.MUTE);
        if (punishment.isPresent()) {
            event.setCancelMessage("<red>Wyciszono cię <dark_gray>- <red>wygasa <gold>" + (punishment.get().isPermanent() ? "nigdy" : "za " + TimeUtil.instantToString(punishment.get().getExpireAt().toInstant(), true)) + "<dark_gray>.");
            event.setCancelled(true);
            return;
        }

        if (sender.getUniqueId().equals(receiving.getUniqueId())) {
            event.setCancelMessage("<red>Nie możesz wysyłać prywatnych wiadomości do samego siebie<dark_gray>.");
            event.setCancelled(true);
            return;
        }

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

        String message = this.notificationController.getMiniMessage().escapeTags(event.getMessage());

        this.notificationController.sendMessage(sender,
                StringFormatter.formatPrivateMessage("<gray>Ja", receivingName, message)
        );

        this.notificationController.sendMessage(receiving,
                StringFormatter.formatPrivateMessage(senderName, "<gray>Ja", message)
        );

        senderSettings.setLastPrivateMessage(receiving.getUniqueId());
        receivingSettings.setLastPrivateMessage(sender.getUniqueId());
    }
}
