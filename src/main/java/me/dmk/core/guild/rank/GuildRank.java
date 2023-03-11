package me.dmk.core.guild.rank;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by DMK on 11.03.2023
 */

@Data
@AllArgsConstructor
public class GuildRank implements Serializable {

    private final UUID uuid = UUID.randomUUID();

    private String name;
    private int priority;
    private Material icon;

    private boolean defaultRank;
    private boolean canManageMembers;
    private boolean canManageAlliances;
    private boolean canManageRanks;
    private boolean canExtend;

    public GuildRank(String name, int priority, Material icon, boolean defaultRank) {
        this.name = name;
        this.priority = priority;
        this.icon = icon;
        this.defaultRank = defaultRank;
    }
}
