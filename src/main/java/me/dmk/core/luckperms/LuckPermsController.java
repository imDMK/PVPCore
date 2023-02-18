package me.dmk.core.luckperms;

import lombok.AllArgsConstructor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Created by DMK on 01.01.2023
 */

@AllArgsConstructor
public class LuckPermsController {

    private final LuckPerms luckPerms;

    public Optional<User> get(UUID uuid) {
        return Optional.ofNullable(this.luckPerms.getUserManager().getUser(uuid));
    }

    public CompletableFuture<User> load(UUID uuid) {
        return this.luckPerms.getUserManager().loadUser(uuid);
    }

    public Optional<User> getOrElseLoad(UUID uuid) {
        return Optional.ofNullable(
                this.get(uuid).orElseGet(() -> this.load(uuid).join()) //Block thread
        );
    }

    public Optional<Group> getHighestGroup(UUID uuid) {
        return this.get(uuid)
                .flatMap(user -> user.getInheritedGroups(user.getQueryOptions())
                        .stream()
                        .max(Comparator.comparingInt(g -> g.getWeight().orElse(0)))
                );
    }

    public Optional<String> getHighestGroupPrefix(UUID uuid) {
        Optional<Group> group = this.getHighestGroup(uuid);
        if (group.isEmpty()) {
            return Optional.empty();
        }

        CachedMetaData cachedMetaData = group.get().getCachedData().getMetaData();
        if (cachedMetaData.getPrefix() == null) {
            return Optional.empty();
        }

        return cachedMetaData.getPrefix().describeConstable();
    }

    public Optional<String> getHighestGroupDisplayNameOrName(UUID uuid) {
        Optional<Group> groupOptional = this.getHighestGroup(uuid);
        if (groupOptional.isEmpty()) {
            return Optional.empty();
        }

        Group group = groupOptional.get();

        if (group.getDisplayName() == null || group.getDisplayName().isEmpty()) {
            return group.getName().describeConstable();
        }

        return group.getDisplayName().describeConstable();
    }

    public Collection<InheritanceNode> getTemponaryGroups(UUID uuid) {
        return this.get(uuid).map(nodes ->
                nodes.getNodes(NodeType.INHERITANCE)
                        .stream()
                        .filter(Node::hasExpiry)
                        .filter(node -> !node.hasExpired())
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
}
