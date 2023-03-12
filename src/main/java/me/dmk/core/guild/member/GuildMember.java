package me.dmk.core.guild.member;

import lombok.Data;
import me.dmk.core.guild.rank.GuildRank;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by DMK on 01.02.2023
 */

@Data
public class GuildMember implements Serializable {

    private final UUID uuid;
    private UUID guildRankUuid;
    private int addedCoinsToTreasury = 0;

    private final Date joinDate = new Date();

    public GuildMember(UUID uuid, GuildRank guildRank) {
        this.uuid = uuid;
        this.guildRankUuid = guildRank.getUuid();
    }

    public void addCoinsToTreasury(int coins) {
        this.addedCoinsToTreasury += coins;
    }
}
