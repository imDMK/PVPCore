package me.dmk.core.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by DMK on 05.02.2023
 */

@Getter
@RequiredArgsConstructor
public class GlobalChatSettings {

    private final GlobalChatCache globalChatCache;

    private boolean enabled = true;
    private long delay = 3;

    public void switchStatus() {
        this.enabled = !this.enabled;
    }

    public void setDelay(long delay) {
        this.delay = delay;
        this.globalChatCache.rebuildCache(delay);
    }
}
