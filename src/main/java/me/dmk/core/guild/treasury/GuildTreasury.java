package me.dmk.core.guild.treasury;

import com.google.common.collect.Lists;
import lombok.Data;
import me.dmk.core.guild.treasury.payment.GuildPayment;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DMK on 01.02.2023
 */

@Data
public class GuildTreasury implements Serializable {

    private int coins = 0;

    private final List<GuildPayment> guildPayments = Lists.newCopyOnWriteArrayList();

    public void removeCoins(int coins) {
        this.coins -= coins;
    }

    public void addPayment(GuildPayment guildPayment) {
        this.coins += guildPayment.getAmountCoins();
        this.guildPayments.add(guildPayment);
    }
}
