package me.dmk.core.guild;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.dmk.core.database.data.entity.DataEntity;
import me.dmk.core.guild.member.GuildMember;
import me.dmk.core.guild.rank.GuildRank;
import me.dmk.core.guild.statistics.GuildStatistics;
import me.dmk.core.guild.treasury.GuildTreasury;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by DMK on 07.01.2023
 */

@Data
@NoArgsConstructor

@DataEntity(collection = "guilds")
public class Guild implements Serializable {

    private String tag;
    private String name;

    private final Date createdAt = new Date();
    private Date expireAt = Date.from(Instant.now().plus(14, ChronoUnit.DAYS));

    private UUID creator;
    private UUID leader;

    private final Map<UUID, GuildRank> guildRanks = Maps.newConcurrentMap();
    private final Map<UUID, GuildMember> members = Maps.newConcurrentMap();

    private final Set<String> alliances = new HashSet<>();

    private final GuildStatistics guildStatistics = new GuildStatistics();
    private final GuildTreasury guildTreasury = new GuildTreasury();

    private final transient Cache<UUID, Boolean> memberInvitations = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();
    private final transient Cache<String, Boolean> allianceInvitations = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();

    public Guild(String tag, String name, UUID creator) {
        this.tag = tag;
        this.name = name;

        this.creator = creator;
        this.leader = creator;

        GuildRank defaultRank = new GuildRank("Członek", 2, Material.GRAY_BANNER, true);
        GuildRank liderRank = new GuildRank("Lider", 1, Material.RED_BANNER, false, true, true, true, true);

        this.guildRanks.put(defaultRank.getUuid(), defaultRank);
        this.guildRanks.put(liderRank.getUuid(), liderRank);

        this.members.put(creator, new GuildMember(creator, liderRank));
    }

    public boolean isLeader(UUID uuid) {
        return this.leader.equals(uuid);
    }

    public void extend(int days) {
        this.expireAt = Date.from(
                this.expireAt.toInstant().plus(days, ChronoUnit.DAYS)
        );
    }

    public GuildRank getDefaultRank() {
        Optional<GuildRank> defaultRank = this.guildRanks.values()
                .stream()
                .filter(GuildRank::isDefaultRank)
                .findFirst();

        return defaultRank.orElseGet(
                () -> new GuildRank("Członek", 1, Material.RED_BANNER, true)
        );
    }

    public GuildRank getGuildRank(UUID uuid) {
        GuildMember guildMember = this.members.get(uuid);

        return Optional.ofNullable(this.guildRanks.get(guildMember.getGuildRankUuid()))
                .orElseGet(this::getDefaultRank);
    }

    public void joinToMembership(UUID uuid) {
        this.members.put(uuid, new GuildMember(uuid, this.getDefaultRank()));
    }

    public boolean isMember(UUID uuid) {
        return this.members.containsKey(uuid);
    }

    public void leaveMembership(UUID uuid) {
        this.members.remove(uuid);
    }

    public List<Player> getOnlineMembers() {
        return this.members.keySet()
                .stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .filter(Player::isOnline)
                .collect(Collectors.toList());
    }

    public void inviteToMembership(UUID uuid) {
        this.memberInvitations.put(uuid, Boolean.TRUE);
    }

    public boolean isInvitedToMembership(UUID uuid) {
        return this.memberInvitations.asMap().containsKey(uuid);
    }

    public void declineInviteToMembership(UUID uuid) {
        this.memberInvitations.asMap().remove(uuid);
    }

    public void joinToAlliance(Guild guild) {
        this.alliances.add(guild.getTag());
    }

    public boolean hasAlliance(Guild guild) {
        return this.alliances.contains(guild.getTag());
    }

    public void breakAlliance(Guild guild) {
        this.alliances.remove(guild.getTag());
    }

    public void receiveInviteToAlliance(Guild guild) {
        this.allianceInvitations.put(guild.getTag(), Boolean.TRUE);
    }

    public boolean hasInviteToAllianceFrom(Guild guild) {
        return this.allianceInvitations.asMap().containsKey(guild.getTag());
    }

    public void acceptInviteToAlliance(Guild guild) {
        this.allianceInvitations.asMap().remove(guild.getTag());
        this.joinToAlliance(guild);
    }

    public void removeInviteToAlliance(Guild guild) {
        this.allianceInvitations.asMap().remove(guild.getTag());
    }
}
