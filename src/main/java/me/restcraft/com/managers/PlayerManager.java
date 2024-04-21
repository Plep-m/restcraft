package me.restcraft.com.managers;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.coordinate.Pos;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    private final List<Player> players = new ArrayList<>();
    private final InstanceContainer instanceContainer;

    public PlayerManager(InstanceContainer instanceContainer) {
        this.instanceContainer = instanceContainer;
        setGlobalEventHandlers();
    }

    private void setGlobalEventHandlers() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, this::onPlayerJoin);
        globalEventHandler.addListener(PlayerBlockBreakEvent.class, this::onPlayerBlockBreak);
        globalEventHandler.addListener(PlayerBlockPlaceEvent.class, this::onPlayerBlockPlace);
    }

    private void onPlayerJoin(AsyncPlayerConfigurationEvent event) {
        Player player = event.getPlayer();
        event.setSpawningInstance(instanceContainer);
        player.setRespawnPoint(new Pos(0, 42, 0));
        player.setGameMode(GameMode.CREATIVE);
        players.add(player);
    }

    private void onPlayerBlockBreak(PlayerBlockBreakEvent event) {
        // Player not "Plep_fr"
        if (!event.getPlayer().getUsername().equals("Plep_fr")) {
            event.setCancelled(true);
            return;
        }
    }

    private void onPlayerBlockPlace(PlayerBlockPlaceEvent event) {
        // Player not "Plep_fr"
        if (!event.getPlayer().getUsername().equals("Plep_fr")) {
            event.setCancelled(true);
            return;
        }
    }

    public List<Player> getPlayers() {
        return players;
    }
}
