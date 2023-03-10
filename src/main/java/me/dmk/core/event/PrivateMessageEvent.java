package me.dmk.core.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.dmk.core.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Created by DMK on 13.02.2023
 */

@Getter
@Setter
@RequiredArgsConstructor
public class PrivateMessageEvent extends Event implements Cancellable {

    private final Player sender;
    private final Profile senderProfile;

    private final Player receiving;
    private final Profile receivingProfile;

    private final String message;

    private boolean cancelled;
    private String cancelMessage;

    private static final HandlerList handlerList = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() { //Required for work
        return handlerList;
    }
}
