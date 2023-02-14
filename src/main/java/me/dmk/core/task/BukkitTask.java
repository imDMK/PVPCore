package me.dmk.core.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by DMK on 09.02.2023
 */

public abstract class BukkitTask implements Runnable {
    private final int taskId;

    public BukkitTask(JavaPlugin javaPlugin, int initialDelay, int repeatDelay) {
        this.taskId = Bukkit.getScheduler().scheduleAsyncRepeatingTask(javaPlugin, this, initialDelay, repeatDelay);
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(this.taskId);
    }
}
