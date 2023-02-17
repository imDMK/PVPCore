package me.dmk.core.util;

import lombok.experimental.UtilityClass;
import me.dmk.core.CorePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by DMK on 17.01.2023
 */

@UtilityClass
public class ComponentUtil {

    private static final MiniMessage miniMessage = CorePlugin.getCorePlugin().getMiniMessage();

    public static Component text(String text) {
        return miniMessage.deserialize("<!italic>" + text);
    }

    public static List<Component> asList(String... strings) {
        return Arrays.stream(strings)
                .filter(Objects::nonNull)
                .map(ComponentUtil::text)
                .collect(Collectors.toList());
    }
}
