package me.dmk.core.murder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by DMK on 05.01.2023
 */

@Getter
@RequiredArgsConstructor
public enum MurderType {

    DEFAULT(1.0),
    WHILE_VICTIM_RUNNING(2.0),
    WHILE_VICTIM_EATING(2.0),
    REVENGE(3.0);

    private final double pointsMultiplier;
}
