package me.dmk.core.guild;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.dmk.core.database.data.entity.DataEntity;
import me.dmk.core.guild.member.GuildMember;
import me.dmk.core.guild.statistics.GuildStatistics;
import me.dmk.core.guild.treasury.GuildTreasury;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by DMK on 07.01.2023
 */

@Data
@NoArgsConstructor

@DataEntity(collection = "guilds")
public class Guild {

    private String tag;
    private String name;
    private UUID creator;
    private Date createdAt = new Date();

    private Date expireAt = Date.from(Instant.now().plus(14, ChronoUnit.DAYS));

    private UUID leader;
    private UUID coLeader = null;

    private Map<UUID, GuildMember> members = new ConcurrentHashMap<>();
    private Set<String> alliances = new HashSet<>();

    private GuildStatistics guildStatistics = new GuildStatistics();
    private GuildTreasury guildTreasury = new GuildTreasury();

    private final transient Cache<UUID, Boolean> memberInvites = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();
    private final transient Cache<Guild, Boolean> allianceInvites = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();

    public Guild(String tag, String name, UUID creator) {
        this.tag = tag;
        this.name = name;
        this.creator = creator;

        this.leader = creator;

        this.members.put(creator, new GuildMember(creator));
    }

    public boolean isCreator(UUID uuid) {
        return this.creator.equals(uuid);
    }

    public boolean isLeader(UUID uuid) {
        return this.leader.equals(uuid);
    }

    public boolean isCoLeader(UUID uuid) {
        return this.coLeader != null && this.coLeader.equals(uuid);
    }

    public boolean isLeaderOrCoLeader(UUID uuid) {
        return this.isLeader(uuid) || this.isCoLeader(uuid);
    }

    public void extend(int days) {
        this.expireAt = Date.from(
                this.expireAt.toInstant().plus(days, ChronoUnit.DAYS)
        );
    }

    public void join(UUID uuid) {
        this.members.put(uuid, new GuildMember(uuid));
    }

    public boolean isMember(UUID uuid) {
        return this.members.containsKey(uuid);
    }

    public void leave(UUID uuid) {
        this.members.remove(uuid);
    }

    public List<Player> getOnlineMembers() {
        return this.members.values()
                .stream()
                .map(member -> Bukkit.getServer().getPlayer(member.getUuid()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void invite(UUID uuid) {
        this.memberInvites.put(uuid, Boolean.TRUE);
    }

    public void acceptInvite(UUID uuid) {
        this.memberInvites.asMap().remove(uuid);

        this.join(uuid);
    }

    public boolean isInvited(UUID uuid) {
        return Optional.ofNullable(this.memberInvites.asMap().get(uuid)).isPresent();
    }

    public void cancelInvite(UUID uuid) {
        this.memberInvites.asMap().remove(uuid);
    }

    public boolean hasAlliance(Guild guild) {
        return this.alliances.contains(guild.getTag());
    }

    public void inviteToAlliance(Guild guild) {
        this.allianceInvites.put(guild, Boolean.TRUE);
    }

    public boolean isInvitedToAlliance(Guild guild) {
        return this.allianceInvites.asMap().containsKey(guild);
    }

    public void cancelInviteToAlliance(Guild guild) {
        this.allianceInvites.asMap().remove(guild);
    }

    public void acceptAllianceInvite(Guild guild) {
        this.allianceInvites.asMap().remove(guild);

        this.alliances.add(guild.getTag());
    }

    public void breakAlliance(Guild guild) {
        this.alliances.remove(guild.getTag());
    }
}
