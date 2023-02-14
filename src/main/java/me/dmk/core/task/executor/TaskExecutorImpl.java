package me.dmk.core.task.executor;

import java.util.concurrent.*;

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
    public void run(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void runLater(Runnable runnable, long time, TimeUnit unit) {
        this.runLaterAsync(runnable, time, unit);
    }

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
        this.scheduledExecutorService.scheduleWithFixedDelay(runnable, 0, time, unit);
    }

    @Override
    public void runTimer(Runnable runnable, long time, TimeUnit unit) {
        this.runTimerAsync(runnable, time, unit);
    }

    @Override
    public void shutdownNow() {
        this.executorService.shutdownNow();
        this.scheduledExecutorService.shutdownNow();
    }
}
