package me.dmk.core.listener;

import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.fight.Fight;
import me.dmk.core.util.StyleUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

import java.util.Optional;

/**
 * Created by DMK on 17.01.2023
 */

@AllArgsConstructor
public class EntityResurrectListener implements Listener {

    private final NotificationController notificationController;
    private final ProfileCache profileCache;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityResurrect(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        Profile profile = this.profileCache.getOrElseThrow(player.getUniqueId());

        profile.getProfileStatistics().increaseUsedTotemOfUndying();

        if (profile.hasFight()) {
            Fight fight = profile.getFight();

            fight.getLastAttacker()
                    .flatMap(uuid -> Optional.ofNullable(Bukkit.getPlayer(uuid)))
                    .ifPresent(attacker ->
                            this.notificationController.sendMessage(attacker,
                                    StyleUtil.getWarning() + " <gray>Gracz<dark_gray>, <gray>z którym walczyłeś/aś użył <gold>totemu nieśmiertelności<dark_gray>!"
                            )
                    );
        }
    }
}
