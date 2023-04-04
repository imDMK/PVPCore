package me.dmk.core.nametag.task;

import lombok.AllArgsConstructor;
import me.dmk.core.nametag.updater.NametagUpdater;

/**
 * Created by DMK on 04.04.2023
 */

@AllArgsConstructor
public class NametagUpdateTask implements Runnable {

    private final NametagUpdater nametagUpdater;

    @Override
    public void run() {
        this.nametagUpdater.updateAll();
    }
}