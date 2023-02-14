package me.dmk.core.task.executor;

import java.util.concurrent.TimeUnit;

/**
 * Created by DMK on 29.12.2022
 */

public interface TaskExecutor {

    void run(Runnable runnable);

    void runLater(Runnable runnable, long time, TimeUnit unit);

    void runAsync(Runnable runnable);

    void runLaterAsync(Runnable runnable, long time, TimeUnit unit);

    void runTimerAsync(Runnable runnable, long time, TimeUnit unit);

    void runTimer(Runnable runnable, long time, TimeUnit unit);

    void shutdownNow();
}
