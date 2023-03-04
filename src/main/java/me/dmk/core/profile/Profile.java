package me.dmk.core.profile;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.dmk.core.CorePlugin;
import me.dmk.core.database.data.entity.DataEntity;
import me.dmk.core.guild.Guild;
import me.dmk.core.profile.fight.Fight;
import me.dmk.core.profile.punishment.Punishment;
import me.dmk.core.profile.punishment.PunishmentType;
import me.dmk.core.profile.settings.ProfileSettings;
import me.dmk.core.profile.statistics.ProfileStatistics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by DMK on 28.12.2022
 */

@Data
@NoArgsConstructor

@DataEntity(collection = "profiles")
public class Profile implements Serializable {

    private UUID uuid;
    private String name;

    private Date firstJoin = new Date();
    private Date lastJoin = new Date();

    private List<Punishment> punishments = Lists.newCopyOnWriteArrayList();

    private ProfileSettings profileSettings = new ProfileSettings();
    private ProfileStatistics profileStatistics = new ProfileStatistics();

    private String guildTag = null;

    private transient Fight fight = new Fight();

    public Profile(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getServer().getPlayer(this.uuid));
    }

    public boolean isOnline() {
        return this.getPlayer().map(Player::isOnline).orElse(false);
    }

    public String getColoredName() {
        return this.profileSettings.getColorName().getFormat() + this.name;
    }

    public Optional<Punishment> getActivePunishment(PunishmentType type) {
        for (Punishment punishment : this.punishments) {
            if (!punishment.getType().equals(type) || !punishment.isActive()) {
                continue;
            }

            return Optional.of(punishment);
        }

        return Optional.empty();
    }

    public boolean wasPunished() {
        if (this.punishments.isEmpty()) {
            return false;
        }

        return !this.punishments
                .stream()
                .filter(p -> !p.isRemoved())
                .toList()
                .isEmpty();
    }

    public void clearAllPunishments() {
        this.punishments = Lists.newCopyOnWriteArrayList();
    }

    public void refreshVanish(Player player, Player other, Profile otherProfile) {
        boolean isVanish = this.getProfileSettings().isVanish();

        boolean isOtherAdmin = other.hasPermission("core.command.vanish");
        boolean isOtherVanish = otherProfile.getProfileSettings().isVanish();

        CorePlugin corePlugin = CorePlugin.getCorePlugin();

        if (isVanish) {
            if (other.canSee(player) && !isOtherAdmin) {
                other.hidePlayer(corePlugin, player);
            }
        } else {
            if (!other.canSee(player)) {
                other.showPlayer(corePlugin, player);
            }

            if (player.canSee(other) && isOtherVanish) {
                player.hidePlayer(corePlugin, other);
            }
        }
    }

    public boolean hasFight() {
        if (this.fight.getExpireAt() == null) {
            return false;
        }

        return Instant.now().isBefore(this.fight.getExpireAt());
    }

    public boolean wasFight() {
        return this.fight.getLastAttacker().isPresent();
    }

    public Optional<Guild> getGuild() {
        if (this.guildTag == null) {
            return Optional.empty();
        }

        Optional<Guild> guild = CorePlugin.getCorePlugin().getGuildCache().getOrElseLoad(this.guildTag);
        if (guild.isEmpty()) {
            this.guildTag = null;
        }

        return guild;
    }
}
