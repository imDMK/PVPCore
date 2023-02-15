package me.dmk.core.listener.motd;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import me.dmk.core.CorePlugin;
import me.dmk.core.configuration.MotdConfiguration;
import me.dmk.core.util.StyleUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MotdPacketListener {

    private final CorePlugin corePlugin;
    private final MotdConfiguration motdConfiguration;
    private final ProtocolManager protocolManager;

    public MotdPacketListener(CorePlugin corePlugin, MotdConfiguration motdConfiguration, ProtocolManager protocolManager) {
        this.corePlugin = corePlugin;
        this.motdConfiguration = motdConfiguration;
        this.protocolManager = protocolManager;

        this.addListener();
    }

    public void addListener() {
        this.protocolManager.addPacketListener(
                new PacketAdapter(
                        this.corePlugin,
                        ListenerPriority.NORMAL,
                        Collections.singletonList(PacketType.Status.Server.SERVER_INFO),
                        ListenerOptions.ASYNC
                ) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        WrappedServerPing wrappedServerPing = event.getPacket().getServerPings().read(0);
                        handlePing(wrappedServerPing);
                    }
                });
    }

    private void handlePing(WrappedServerPing ping) {
        List<String> fakePlayers = this.motdConfiguration.getFakePlayers();
        String motdLine = this.motdConfiguration.getMotdLine().replace("{NL}", "\n");

        int activePlayers = this.motdConfiguration.getActivePlayers();
        int maxPlayers = this.motdConfiguration.getMaxPlayers();

        if (!fakePlayers.isEmpty()) {
            List<WrappedGameProfile> players = new ArrayList<>();

            fakePlayers.stream()
                    .map(StyleUtil::colored)
                    .map(string -> new WrappedGameProfile(UUID.randomUUID(), string))
                    .forEachOrdered(players::add);

            ping.setPlayers(players);
            ping.setPlayersVisible(true);
        }

        if (!motdLine.isEmpty()) {
            ping.setMotD(StyleUtil.colored(motdLine));
        }

        if (activePlayers > 0) {
            ping.setPlayersOnline(activePlayers);
        }

        if (maxPlayers > 0) {
            ping.setPlayersMaximum(maxPlayers);
        }
    }
}
