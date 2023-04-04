package me.dmk.core.task.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by DMK on 29.12.2022
 */

public class TaskExecutorImpl implements TaskExecutor {

    private final ExecutorService executorService = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );
    private final ScheduledExecutorService scheduledExecutorService  = Executors.newScheduledThreadPool(
            Runtime.getRuntime().availableProcessors()
    );

    @Override
    public void runAsync(Runnable runnable) {
        this.executorService.execute(runnable);
    }

    @Override
    public void runLaterAsync(Runnable runnable, long delay, TimeUnit unit) {
        this.scheduledExecutorService.schedule(runnable, delay, unit);
    }

    @Override
    public void runTimerAsync(Runnable runnable, long time, TimeUnit unit) {
        this.scheduledExecutorService.scheduleWithFixedDelay(runnable, 0L, time, unit);
    }

    @Override
    public void shutdownNow() {
        this.executorService.shutdownNow();
        this.scheduledExecutorService.shutdownNow();
    }
}
