package me.dmk.core.guild.treasury.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

/**
 * Created by DMK on 01.02.2023
 */

@Data
@AllArgsConstructor
public class GuildPayment {

    private final String payingName;
    private final UUID payingUuid;

    private final int amountCoins;
    private final Date paymentDate = new Date();

    private final int balanceAfterPayment;
}
