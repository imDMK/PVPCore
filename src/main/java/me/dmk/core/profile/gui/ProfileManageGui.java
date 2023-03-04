package me.dmk.core.profile.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.CorePlugin;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.gui.confirmation.ConfirmationGui;
import me.dmk.core.profile.Profile;
import me.dmk.core.profile.cache.ProfileCache;
import me.dmk.core.profile.controller.ProfileController;
import me.dmk.core.profile.statistics.ProfileStatistics;
import me.dmk.core.task.executor.TaskExecutor;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.StringUtil;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.Collections;

/**
 * Created by DMK on 02.03.2023
 */

public class ProfileManageGui extends PluginGui {

    private final NotificationController notificationController = CorePlugin.getCorePlugin().getNotificationController();
    private final ProfileController profileController = CorePlugin.getCorePlugin().getProfileController();
    private final ProfileCache profileCache = CorePlugin.getCorePlugin().getProfileCache();
    private final TaskExecutor taskExecutor = CorePlugin.getCorePlugin().getTaskExecutor();

    public ProfileManageGui(Player player, Profile profile) {
        super(player, profile, "Zarządzanie profilem " + profile.getName(), 3, true, true);
    }

    @Override
    public void build() {
        GuiItem deleteProfileItem = ItemBuilder.from(Material.BARRIER)
                .name(ComponentUtil.text("<red>Usunięcie proflu"))
                .lore(ComponentUtil.asList(
                        "",
                        this.circle + " <gray>Nazwa profilu<dark_gray>: <gold>" + this.profile.getName(),
                        this.circle + " <gray>UUID<dark_gray>: <gold>" + this.profile.getUuid().toString(),
                        "",
                        StringFormatter.formatWarning() + " <gold>Spowoduje to całkowite usunięcie profilu gracza.",
                        ""
                ))
                .asGuiItem(event -> new ConfirmationGui(this.player)
                        .title("Usuwanie profilu " + this.profile.getName())
                        .afterConfirm(confirmEvent -> {
                            this.taskExecutor.runAsync(() -> this.profileController.delete(this.profile));
                            this.profileCache.remove(this.profile);

                            this.profile.getPlayer().ifPresent(p ->
                                    p.kickPlayer(StringUtil.colorLegacy("&cTwój profil został usunięty.\nAdministrator: " + this.player.getName()))
                            );

                            this.notificationController.sendMessage(this.player,
                                    StringFormatter.formatSuccess() + " <red>Usunięto <gray>profil gracza <light_purple>" + this.profile.getName() + " <dark_gray>(<light_purple>UUID " + this.profile.getUuid().toString() + "<dark_gray>)."
                            );

                            this.close();
                        })
                        .afterCancel(cancelEvent -> this.open())
                        .open()
                );

        GuiItem resetStatisticsItem = ItemBuilder.from(Material.PAPER)
                .name(ComponentUtil.text("<red>Zresetowanie statystyk"))
                .lore(ComponentUtil.asList(
                        "",
                        StringFormatter.formatWarning() + " <gold>Spowoduje to zresetowanie wszystkich statystyk gracza.",
                        this.circle + " <gray>Obejmuje<dark_gray>:",
                        "<dark_gray>- <gold>wejścia na serwer<dark_gray>,",
                        "<dark_gray>- <gold>spędzony czas<dark_gray>,",
                        "<dark_gray>- <gold>monety gracza<dark_gray>,",
                        "<dark_gray>- <gold>wszystkie statystyki związane z walkami<dark_gray>,",
                        "<dark_gray>- <gold>wszystkie statystyki związane z jedzeniem<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> new ConfirmationGui(this.player)
                        .title("Resetowanie statystyk " + this.profile.getName())
                        .afterConfirm(confirmEvent -> {
                            ProfileStatistics statistics = this.profile.getProfileStatistics();

                            statistics.setEntrances(0);

                            statistics.setTimeSpent(0);
                            Bukkit.getOfflinePlayer(this.profile.getUuid()).setStatistic(Statistic.PLAY_ONE_MINUTE, 0);

                            statistics.setLevel(0);
                            statistics.setCoins(0);
                            statistics.setKitLevel(0);

                            statistics.setKills(0);
                            statistics.setKillStreak(0);
                            statistics.setHighestKillStreak(0);
                            statistics.setDeaths(0);
                            statistics.setPoints(CorePlugin.getCorePlugin().getPluginConfiguration().getDefaultPoints());

                            statistics.setEats(0);
                            statistics.setEatenGoldenApples(0);
                            statistics.setEatenEnchantedGoldenApples(0);
                            statistics.setThrownEnderPearl(0);
                            statistics.setUsedTotemOfUndying(0);

                            this.taskExecutor.runAsync(() -> this.profileController.save(this.profile));

                            this.notificationController.sendMessage(this.player,
                                    StringFormatter.formatSuccess() + " <red>Zresetowano <gray>statystyki profilu gracza <light_purple>" + this.profile.getName() + "<dark_gray>."
                            );

                            this.close();
                        })
                        .afterCancel(cancelEvent -> this.open())
                        .open()
                );

        GuiItem clearPunishmentsItem = ItemBuilder.from(Material.REDSTONE_BLOCK)
                .name(ComponentUtil.text("<red>Wyczyszczenie wszystkich kar"))
                .lore(ComponentUtil.asList(
                        "",
                        StringFormatter.formatWarning() + " <gold>Spowoduje to wyszczyszczenie wszystkich kar gracza.",
                        ""
                ))
                .asGuiItem(event -> new ConfirmationGui(this.player)
                        .title("Wyczyszczenie kar " + this.profile.getName())
                        .afterConfirm(confirmEvent -> {
                            this.profile.clearAllPunishments();

                            this.taskExecutor.runAsync(() -> this.profileController.save(this.profile));

                            this.notificationController.sendMessage(this.player,
                                    StringFormatter.formatSuccess() + " <red>Wyczyszczono <gray>wszystkie kary profilu <light_purple>" + this.profile.getName() + "<dark_gray>."
                            );

                            this.close();
                        })
                        .afterCancel(cancelEvent -> this.open())
                        .open()
                );

        GuiItem setCoinsItem = ItemBuilder.from(Material.SUNFLOWER)
                .name(ComponentUtil.text("<red>Zmiana monet"))
                .lore(ComponentUtil.asList(
                        "",
                        StringFormatter.formatWarning() + " <gold>Spowoduje to zmianę monet gracza.",
                        ""
                ))
                .asGuiItem(event -> new AnvilGUI.Builder()
                        .title("Wprowadź ilość monet")
                        .text("1000")

                        .onLeftInputClick(p -> this.open())
                        .onRightInputClick(p -> this.open())

                        .onComplete(completion -> {
                            if (!StringUtil.isInteger(completion.getText())) {
                                this.notificationController.sendMessage(this.player,
                                        StringFormatter.formatError() + " <red>Wprowadzono nieprawidłową ilość monet<dark_gray>."
                                );

                                return Collections.singletonList(AnvilGUI.ResponseAction.close());
                            }

                            int providedCoins = Integer.parseInt(completion.getText());

                            if (providedCoins < 0) {
                                this.notificationController.sendMessage(this.player,
                                        StringFormatter.formatError() + " <red>Wprowadzono nieprawidłową ilość monet<dark_gray>."
                                );

                                return Collections.singletonList(AnvilGUI.ResponseAction.close());
                            }

                            this.taskExecutor.runAsync(() -> {
                                this.profile.getProfileStatistics().setCoins(providedCoins);
                                this.profileController.save(this.profile);
                            });

                            this.notificationController.sendMessage(this.player,
                                    StringFormatter.formatSuccess() + " <green>Zmieniono <gray>monety gracza <light_purple>" + this.profile.getName() + " <gray>na <light_purple>" + providedCoins + "<dark_gray>."
                            );

                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
                        })

                        .plugin(CorePlugin.getCorePlugin())
                        .open(this.player)
                );

        GuiItem setLevelItem = ItemBuilder.from(Material.EXPERIENCE_BOTTLE)
                .name(ComponentUtil.text("<red>Zmień poziom doświadczenia"))
                .lore(ComponentUtil.asList(
                        "",
                        StringFormatter.formatWarning() + " <gold>Spowoduje to zmianę poziomu doświadczenia gracza.",
                        ""
                ))
                .asGuiItem(event -> new AnvilGUI.Builder()
                        .title("Wprowadź poziom doświadczenia")
                        .text("10")

                        .onLeftInputClick(p -> this.open())
                        .onRightInputClick(p -> this.open())

                        .onComplete(completion -> {
                            if (!StringUtil.isInteger(completion.getText())) {
                                this.notificationController.sendMessage(this.player,
                                        StringFormatter.formatError() + " <red>Wprowadzono nieprawidłowy poziom doświadczenia<dark_gray>."
                                );

                                return Collections.singletonList(AnvilGUI.ResponseAction.close());
                            }

                            int providedLevel = Integer.parseInt(completion.getText());

                            if (providedLevel < 0) {
                                this.notificationController.sendMessage(this.player,
                                        StringFormatter.formatError() + " <red>Wprowadzono nieprawidłowy poziom doświadczenia<dark_gray>."
                                );

                                return Collections.singletonList(AnvilGUI.ResponseAction.close());
                            }

                            this.taskExecutor.runAsync(() -> {
                                this.profile.getProfileStatistics().setLevel(providedLevel);
                                this.profileController.save(this.profile);
                            });

                            this.profile.getPlayer().ifPresent(p -> p.setLevel(providedLevel));

                            this.notificationController.sendMessage(this.player,
                                    StringFormatter.formatSuccess() + " <green>Zmieniono <gray>poziom doświadczenia gracza <light_purple>" + this.profile.getName() + " <gray>na <light_purple>" + providedLevel + "<dark_gray>."
                            );

                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
                        })

                        .plugin(CorePlugin.getCorePlugin())
                        .open(this.player)
                );

        this.gui.setItem(11, deleteProfileItem);
        this.gui.setItem(12, resetStatisticsItem);
        this.gui.setItem(13, clearPunishmentsItem);
        this.gui.setItem(14, setCoinsItem);
        this.gui.setItem(15, setLevelItem);
    }
}
