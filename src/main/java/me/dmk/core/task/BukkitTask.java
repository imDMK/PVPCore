package me.dmk.core.task;

import lombok.Setter;
import me.dmk.core.CorePlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by DMK on 09.02.2023
 */

@Setter
public abstract class BukkitTask {

    private boolean canceled;

    public BukkitTask(long initialDelay, long time) {
        AtomicLong atomicLong = new AtomicLong(time);

        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (canceled) {
                    this.cancel();
                    return;
                }

                if (atomicLong.decrementAndGet() == -1) {
                    onFinish();
                    this.cancel();
                    return;
                }

                onRun(atomicLong.get());
            }
        };

        bukkitRunnable.runTaskTimerAsynchronously(CorePlugin.getCorePlugin(), initialDelay, 20L);
    }

    public abstract void onRun(long time);

    public abstract void onFinish();
}
