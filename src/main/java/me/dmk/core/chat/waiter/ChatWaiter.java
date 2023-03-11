package me.dmk.core.chat.waiter;

/**
 * Created by DMK on 11.03.2023
 */

@FunctionalInterface
public interface ChatWaiter {

    void execute(final String message);
}

