package me.dmk.core.profile.fight;

import lombok.Getter;
import lombok.Setter;
import me.dmk.core.CorePlugin;
import net.kyori.adventure.bossbar.BossBar;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by DMK on 05.01.2023
 */

@Getter
public class Fight {

    private final int fightTime = CorePlugin.getCorePlugin().getPluginConfiguration().getFightTime();

    private UUID lastAttacker;
    private Instant expire;
    @Setter
    private BossBar bossBar;

    public void put(UUID attacker) {
        this.lastAttacker = attacker;
        this.expire = Instant.now().plus(this.fightTime, ChronoUnit.SECONDS);
    }

    public void clear() {
        this.lastAttacker = null;
        this.expire = null;
        this.bossBar = null;
    }

    public Optional<UUID> getLastAttacker() {
        return Optional.ofNullable(this.lastAttacker);
    }

    public Duration getDurationBetween() {
        return Duration.between(Instant.now(), this.expire);
    }

    public long expireToSeconds() {
        return this.getDurationBetween().toSeconds();
    }

    public float expireToBossBarFloat() {
        return (float) this.expireToSeconds() / this.fightTime;
    }
}
