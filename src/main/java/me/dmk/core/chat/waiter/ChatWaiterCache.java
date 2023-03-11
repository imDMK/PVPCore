package me.dmk.core.chat.waiter;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by DMK on 11.03.2023
 */

public class ChatWaiterCache {

    private final Cache<UUID, ChatWaiter> chatWaiterMap = Caffeine.newBuilder()
            .expireAfterWrite(30L, TimeUnit.SECONDS)
            .build();

    public void put(Player player, ChatWaiter chatWaiter) {
        this.chatWaiterMap.put(player.getUniqueId(), chatWaiter);
    }

    public ChatWaiter remove(Player player) {
        return this.chatWaiterMap.asMap().remove(player.getUniqueId());
    }

    public boolean isWaitingForResponse(Player player) {
        return this.chatWaiterMap.asMap().containsKey(player.getUniqueId());
    }
}
