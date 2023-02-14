package me.dmk.core.guild.member;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

/**
 * Created by DMK on 01.02.2023
 */

@Data
public class Member {

    private final UUID uuid;
    private final Date joinDate = new Date();
    private int addedCoinsToTreasury = 0;

    public void addCoinsToTreasury(int coins) {
        this.addedCoinsToTreasury += coins;
    }
}
