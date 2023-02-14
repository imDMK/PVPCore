package me.dmk.core.guild.statistics;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by DMK on 07.01.2023
 */

@Data
public class GuildStatistics implements Serializable {

    private int rank = 0;
    private int kills = 0;
    private int killStreak = 0;
    private int highestKillStreak = 0;
    private int deaths = 0;

    public void addRank(int rank) {
        this.rank += rank;
    }

    public void removeRank(int rank) {
        this.rank -= rank;
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


}
