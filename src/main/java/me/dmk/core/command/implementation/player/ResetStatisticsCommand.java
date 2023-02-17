package me.dmk.core.command.implementation.player;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import lombok.AllArgsConstructor;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.configuration.PluginConfiguration;
import me.dmk.core.gui.ConfirmationGui;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.statistics.ProfileStatistics;
import me.dmk.core.task.executor.TaskExecutor;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.StringUtil;
import me.dmk.core.util.string.SymbolUtil;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 19.01.2023
 */

@AllArgsConstructor

@Route(name = "resetstatistics")
public class ResetStatisticsCommand {

    private final PluginConfiguration pluginConfiguration;
    private final NotificationController notificationController;
    private final ProfileController profileController;
    private final ProfileCache profileCache;
    private final TaskExecutor taskExecutor;

    @Execute
    void execute(Player player) {
        Profile profile = this.profileCache.getOrElseThrow(player.getUniqueId());
        ProfileStatistics statistics = profile.getProfileStatistics();

        int coinsToResetStatistics = this.pluginConfiguration.getCoinsToResetStatistics();

        if (statistics.getCoins() < coinsToResetStatistics) {
            this.notificationController.sendMessage(player,
                    StringFormatter.formatError() + " <red>Aby zresetować statystyki potrzebujesz <gold>" + coinsToResetStatistics + " <red>monet<dark_gray>."
            );
            return;
        }

        new ConfirmationGui(player)
                .create(SymbolUtil.getCircle("<dark_gray>") + " <light_purple>Resetowanie statystyk " + SymbolUtil.getCircle("<dark_gray>"))
                .afterConfirm(confirmEvent -> {
                    statistics.setEntrances(0);
                    statistics.setTimeSpent(0);

                    statistics.removeCoins(coinsToResetStatistics);

                    statistics.setKills(0);
                    statistics.setKillStreak(0);
                    statistics.setHighestKillStreak(0);
                    statistics.setDeaths(0);
                    statistics.setPoints(this.pluginConfiguration.getDefaultPoints());

                    player.setStatistic(Statistic.PLAY_ONE_MINUTE, 0);

                    this.taskExecutor.runAsync(
                            () -> this.profileController.save(profile)
                    );

                    this.notificationController.sendMessage(player,
                            StringFormatter.formatSuccess() + StringUtil.getGreenGradient() + " Zresetowano <gray>twoje statystyki<dark_gray>."
                    );
                    player.closeInventory();
                })
                .closeAfterCancel()
                .open();
    }
}
