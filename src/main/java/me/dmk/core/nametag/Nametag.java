package me.dmk.core.nametag;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.AdventureComponentConverter;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.dmk.core.CorePlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by DMK on 04.04.2023
 */

public class Nametag {

    private final PacketContainer packetContainer;

    private final MiniMessage miniMessage = CorePlugin.getCorePlugin().getMiniMessage();

    public Nametag(Player player) {
        this.packetContainer = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        this.packetContainer.getModifier().writeDefaults();

        //Write mode (0 = create team)
        this.packetContainer.getIntegers().write(0, 0);

        //Set team name
        this.packetContainer.getStrings().write(0, player.getName());

        //Set color
        this.packetContainer.getOptionalStructures().read(0)
                .map(internalStructure ->
                        internalStructure.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat"))
                                .write(0, ChatColor.WHITE)
                );

        // Add the player to the team
        this.packetContainer.getSpecificModifier(Collection.class).write(0, Collections.singletonList(player.getName()));
    }

    /* Getters */
    public String getVisibility() {
        return this.packetContainer.getOptionalStructures()
                .read(0).map(internalStructure -> internalStructure.getStrings().read(0))
                .orElse("");
    }

    /* Setters */
    public void setVisibility(String visibility) {
        this.packetContainer.getOptionalStructures().read(0)
                .map(internalStructure -> internalStructure.getStrings().write(0, visibility));
    }

    public void setPrefix(String prefix) {
        WrappedChatComponent chatComponent = AdventureComponentConverter.fromComponent(
                this.miniMessage.deserialize(prefix + " ")
        );

        this.packetContainer.getOptionalStructures().read(0)
                .map(internalStructure ->
                        internalStructure.getChatComponents().write(1, chatComponent)
                );
    }

    public void setSuffix(String suffix) {
        WrappedChatComponent chatComponent = AdventureComponentConverter.fromComponent(
                this.miniMessage.deserialize(" " + suffix)
        );

        this.packetContainer.getOptionalStructures().read(0)
                .map(internalStructure ->
                        internalStructure.getChatComponents().write(2, chatComponent)
                );
    }

    /* Reset */
    public void resetPrefix() {
        this.packetContainer.getOptionalStructures().read(0)
                .map(internalStructure -> internalStructure.getChatComponents().write(1, WrappedChatComponent.fromText("")));
    }

    public void resetSuffix() {
        this.packetContainer.getOptionalStructures().read(0)
                .map(internalStructure -> internalStructure.getChatComponents().write(2, WrappedChatComponent.fromText("")));
    }

    /* Send packet */
    public void send(Player player) {
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, this.packetContainer);
    }
}
