package me.dmk.core.profile.statistics;

import lombok.Data;
import me.dmk.core.CorePlugin;

import java.io.Serializable;

/**
 * Created by DMK on 29.12.2022
 */

@Data
public class ProfileStatistics implements Serializable {

    private int entrances = 0;
    private int timeSpent = 0;

    private int level = 0;
    private int coins = 0;

    private int points = CorePlugin.getCorePlugin().getPluginConfiguration().getDefaultPoints();
    private int kills = 0;
    private int killStreak = 0;
    private int highestKillStreak = 0;
    private int deaths = 0;

    private int eats = 0;
    private int eatenGoldenApples = 0;
    private int eatenEnchantedGoldenApples = 0;
    private int thrownEnderPearl = 0;
    private int usedTotemOfUndying = 0;

    private transient long lastEatTime = 0L;

    public void increaseEntrances() {
        this.entrances += 1;
    }

    public void removeCoins(int coins) {
        this.coins -= coins;
    }

    public void addCoins(int coins) {
        this.coins += coins;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void removePoints(int points) {
        this.points -= points;
    }

    public void increaseKills() {
        this.kills += 1;
        this.killStreak += 1;

        if (this.highestKillStreak < this.killStreak) {
            this.setHighestKillStreak(this.getKillStreak());
        }
    }

    public void increaseDeaths() {
        this.deaths += 1;
        this.killStreak = 0;
    }

    public void increaseEats() {
        this.eats += 1;
    }

    public void increaseEatenGoldenApples() {
        this.eatenGoldenApples += 1;
        this.increaseEats();
    }

    public void increaseEatenEnchantedGoldenApples() {
        this.eatenEnchantedGoldenApples += 1;
        this.increaseEats();
    }

    public void increaseThrownEnderPearl() {
        this.thrownEnderPearl += 1;
    }

    public void increaseUsedTotemOfUndying() {
        this.usedTotemOfUndying += 1;
    }
}
