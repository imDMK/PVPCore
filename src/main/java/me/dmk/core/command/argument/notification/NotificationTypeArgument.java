package me.dmk.core.command.argument.notification;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import me.dmk.core.chat.notification.NotificationType;
import me.dmk.core.util.string.StringFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import panda.std.Result;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by DMK on 29.12.2022
 */

@ArgumentName("type")
public class NotificationTypeArgument implements OneArgument<NotificationType> {

    private final Component errorMessage;

    public NotificationTypeArgument(MiniMessage miniMessage) {
        this.errorMessage = miniMessage.deserialize(StringFormatter.formatError() + " <red>Podano nieprawidłowy typ wiadomości<dark_gray>.");
    }

    @Override
    public Result<NotificationType, ?> parse(LiteInvocation liteInvocation, String argument) {
        return Result.supplyThrowing(IllegalArgumentException.class,
                        () -> NotificationType.valueOf(argument)
                )
                .mapErr(error -> this.errorMessage);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return Arrays.stream(NotificationType.values())
                .map(NotificationType::name)
                .map(Suggestion::of)
                .collect(Collectors.toList());
    }
}
