package me.dmk.core.guild.rank.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.dmk.core.CorePlugin;
import me.dmk.core.chat.notification.NotificationController;
import me.dmk.core.chat.waiter.ChatWaiter;
import me.dmk.core.chat.waiter.ChatWaiterCache;
import me.dmk.core.gui.PluginGui;
import me.dmk.core.gui.confirmation.ConfirmationGui;
import me.dmk.core.guild.Guild;
import me.dmk.core.guild.rank.GuildRank;
import me.dmk.core.profile.Profile;
import me.dmk.core.util.ComponentUtil;
import me.dmk.core.util.string.StringFormatter;
import me.dmk.core.util.string.StringUtil;
import me.dmk.core.util.string.SymbolUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by DMK on 11.03.2023
 */

public class GuildRankEditGui extends PluginGui {

    private final NotificationController notificationController = CorePlugin.getCorePlugin().getNotificationController();
    private final ChatWaiterCache chatWaiterCache = CorePlugin.getCorePlugin().getChatWaiterCache();

    private final Guild guild;
    private final GuildRank guildRank;

    public GuildRankEditGui(Player player, Profile profile, Guild guild, GuildRank guildRank) {
        super(player, profile, "Edytowanie rangi " + guildRank.getName(), 6, true, true);

        this.guild = guild;
        this.guildRank = guildRank;
    }

    @Override
    public void build() {
        GuiItem nameItem = ItemBuilder.from(Material.NAME_TAG)
                .name(ComponentUtil.text("<light_purple>Zmiana nazwy"))
                .lore(ComponentUtil.asList(
                        "",
                        StringFormatter.formatWarning() + " <gold>Kliknij<dark_gray>, <gray>aby <gold>zmienić <gray>nazwę rangi<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    this.notificationController.sendMessage(this.player,
                            StringFormatter.formatWarning() + " <gold>Wprowadż nazwę<dark_gray>"
                    );

                    ChatWaiter chatWaiter = (message) -> {
                        if (message.length() > 10) {
                            this.notificationController.sendMessage(this.player,
                                    StringFormatter.formatError() + " <red>Nazwa nie może przekraczać długość 10 znaków<dark_gray>."
                            );

                            this.open();
                            return;
                        }

                        this.guildRank.setName(message);
                        this.gui.updateTitle("Edytowanie rangi " + message);
                        this.open();
                    };

                    this.close();
                    this.chatWaiterCache.put(this.player, chatWaiter);
                });

        GuiItem priorityItem = ItemBuilder.from(Material.SOUL_TORCH)
                .name(ComponentUtil.text("<light_purple>Zmiana priorytetu"))
                .lore(ComponentUtil.asList(
                        "",
                        SymbolUtil.getCircle("<dark_gray>") + " <gray>Aktualnie<dark_gray>: <light_purple>" + this.guildRank.getPriority(),
                        "",
                        StringFormatter.formatWarning() + " <gold>Kliknij<dark_gray>, <gray>aby <gold>zmienić <gray>priorytet rangi<dark_gray>.",
                        StringFormatter.formatWarning() + " <gray>Jest on używany do sortowania listy członków <dark_gray>(<gray>niższy = wyższe pozycjonowanie członka<dark_gray>).",
                        ""
                ))
                .asGuiItem(event -> {
                    this.notificationController.sendMessage(this.player,
                            StringFormatter.formatWarning() + " <gold>Wprowadź priorytet (liczba)<dark_gray>."
                    );

                    ChatWaiter chatWaiter = (message) -> {
                        if (!StringUtil.isInteger(message)) {
                            this.notificationController.sendMessage(this.player,
                                    StringFormatter.formatError() + " <red>Wprowadzono nieprawidłową ilość priorytetu<dark_gray>."
                            );

                            this.open();
                            return;
                        }

                        int priority = Integer.parseInt(message);
                        if (priority < 0) {
                            this.notificationController.sendMessage(this.player,
                                    StringFormatter.formatError() + " <red>Wprowadzono nieprawidłową ilość priorytetu<dark_gray>."
                            );

                            this.open();
                            return;
                        }

                        this.guildRank.setPriority(priority);
                        this.open();
                    };

                    this.close();
                    this.chatWaiterCache.put(this.player, chatWaiter);
                });

        GuiItem iconItem = ItemBuilder.from(this.guildRank.getIcon())
                .name(ComponentUtil.text("<light_purple>Zmiana ikony"))
                .lore(ComponentUtil.asList(
                        "",
                        StringFormatter.formatWarning() + " <gold>Kliknij<dark_gray>, <gray>aby <gold>zmienić <gray>ikonę rangi<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> {
                    this.notificationController.sendMessage(this.player,
                            StringFormatter.formatWarning() + " <gold>Wprowadź nazwę materiału<dark_gray>."
                    );

                    ChatWaiter chatWaiter = (message) -> {
                        Material icon = Material.getMaterial(message.toUpperCase());
                        if (icon == null) {
                            this.notificationController.sendMessage(this.player,
                                    StringFormatter.formatError() + " <red>Wprowadzono nieprawidłową nazwę materiału<dark_gray>."
                            );

                            this.open();
                            return;
                        }

                        this.guildRank.setIcon(icon);
                        this.open();
                    };

                    this.close();
                    this.chatWaiterCache.put(this.player, chatWaiter);
                });

        GuiItem manageMembersItem = ItemBuilder.from(Material.PLAYER_HEAD)
                .name(ComponentUtil.text("<light_purple>Zarządzanie członkami"))
                .lore(ComponentUtil.asList(
                        "",
                        StringFormatter.formatWarning() + " <gold>Kliknij<dark_gray>, <gray>aby <gold>zmienić <gray>możlwość zarządzania członkami gildii<dark_gray>.",
                        StringFormatter.formatWarning() + " <gray>Przyznano<dark_gray>: " + StringFormatter.formatBooleanYesOrNo(this.guildRank.isCanManageMembers()) + "<dark_gray>.",
                        ""
                ))
                .glow(this.guildRank.isCanManageMembers())
                .asGuiItem(event -> {
                    this.guildRank.setCanManageMembers(!this.guildRank.isCanManageMembers());
                    this.open();
                });

        GuiItem manageAlliancesItem = ItemBuilder.from(Material.SHIELD)
                .name(ComponentUtil.text("<light_purple>Zarządzanie sojuszami"))
                .lore(ComponentUtil.asList(
                        "",
                        StringFormatter.formatWarning() + " <gold>Kliknij<dark_gray>, <gray>aby <gold>zmienić <gray>możlwość zarządzania sojuszami gildii<dark_gray>.",
                        StringFormatter.formatWarning() + " <gray>Przyznano<dark_gray>: " + StringFormatter.formatBooleanYesOrNo(this.guildRank.isCanManageAlliances()) + "<dark_gray>.",
                        ""
                ))
                .glow(this.guildRank.isCanManageAlliances())
                .asGuiItem(event -> {
                    this.guildRank.setCanManageAlliances(!this.guildRank.isCanManageAlliances());
                    this.open();
                });

        GuiItem manageRanksItem = ItemBuilder.from(Material.TURTLE_HELMET)
                .name(ComponentUtil.text("<light_purple>Zarządzanie rangami"))
                .lore(ComponentUtil.asList(
                        "",
                        StringFormatter.formatWarning() + " <gold>Kliknij<dark_gray>, <gray>aby <gold>zmienić <gray>możlwość zarządzania rangami gildii<dark_gray>.",
                        StringFormatter.formatWarning() + " <gray>Przyznano<dark_gray>: " + StringFormatter.formatBooleanYesOrNo(this.guildRank.isCanManageRanks()) + "<dark_gray>.",
                        ""
                ))
                .glow(this.guildRank.isCanManageRanks())
                .asGuiItem(event -> {
                    this.guildRank.setCanManageRanks(!this.guildRank.isCanManageRanks());
                    this.open();
                });

        GuiItem extendItem = ItemBuilder.from(Material.CLOCK)
                .name(ComponentUtil.text("<light_purple>Przedłużanie gildii"))
                .lore(ComponentUtil.asList(
                        "",
                        StringFormatter.formatWarning() + " <gold>Kliknij<dark_gray>, <gray>aby <gold>zmienić <gray>możlwość przedłużenia gildii<dark_gray>.",
                        StringFormatter.formatWarning() + " <gray>Przyznano<dark_gray>: " + StringFormatter.formatBooleanYesOrNo(this.guildRank.isCanExtend()) + "<dark_gray>.",
                        ""
                ))
                .glow(this.guildRank.isCanExtend())
                .asGuiItem(event -> {
                    this.guildRank.setCanExtend(!this.guildRank.isCanExtend());
                    this.open();
                });

        GuiItem deleteRankItem = ItemBuilder.from(Material.BARRIER)
                .name(ComponentUtil.text("<light_purple>Usunięcie rangi"))
                .lore(ComponentUtil.asList(
                        "",
                        StringFormatter.formatWarning() + " <red>Kliknij<dark_gray>, <gray>aby <red>usunąć <gray>rangę<dark_gray>.",
                        ""
                ))
                .asGuiItem(event -> new ConfirmationGui(this.player)
                        .title("Usunięcie rangi " + this.guildRank.getName())
                        .afterConfirm(e -> {
                            this.guild.getMembers().values()
                                    .stream()
                                    .filter(guildMember -> guildMember.getGuildRankUuid().equals(this.guildRank.getUuid()))
                                    .forEachOrdered(guildMember -> guildMember.setGuildRankUuid(this.guild.getDefaultRank().getUuid()));

                            this.guild.getGuildRanks().values().remove(this.guildRank);

                            this.notificationController.sendMessage(this.player,
                                    StringFormatter.formatSuccess() + " <green>Usunięto <gray>rangę <light_purple>" + this.guildRank.getName() + "<dark_gray>."
                            );

                            this.close();
                        })
                        .afterCancel(e -> this.open())
                        .open());

        GuiItem backButton = this.createBackButton(event ->
                        new GuildRanksGui(this.player, this.profile, this.guild).open(),
                "",
                this.warning + " <light_purple>Kliknij<dark_gray>, <gray>aby powrócić do listy rang<dark_gray>.",
                ""
        );

        this.gui.setItem(13, nameItem);
        this.gui.setItem(21, priorityItem);
        this.gui.setItem(22, iconItem);
        this.gui.setItem(23, manageMembersItem);
        this.gui.setItem(30, manageAlliancesItem);
        this.gui.setItem(31, manageRanksItem);
        this.gui.setItem(32, extendItem);

        if (!this.guild.getDefaultRank().equals(this.guildRank)) { //Default rank cannot be deleted
            this.gui.setItem(40, deleteRankItem);
        }

        this.gui.setItem(49, backButton);
    }
}
