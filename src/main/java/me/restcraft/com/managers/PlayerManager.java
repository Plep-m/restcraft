package me.restcraft.com.managers;

import lombok.Getter;
import me.restcraft.com.Database;
import me.restcraft.com.interfaces.Manager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.coordinate.Pos;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerManager implements Manager {
    @Getter
    private final List<Player> players = new ArrayList<>();
    private final InstanceContainer instanceContainer;
    private final Database database = new Database();


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

    @SuppressWarnings("CallToPrintStackTrace")
    private void onPlayerJoin(AsyncPlayerConfigurationEvent event) {
        Player player = event.getPlayer();
        // sql query * from whitelist where username = player.getUsername() if not found kick player
        String sql = "SELECT * FROM whitelist WHERE username = '" + player.getUsername() + "'";
        ResultSet resultSet = null;
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            resultSet = database.query(sql);
            resultList = database.resultSetToList(resultSet);
            // resultList contain  player.getUsername()
            if (!resultList.isEmpty()) {
                event.setSpawningInstance(instanceContainer);
                player.setRespawnPoint(new Pos(0, 42, 0));
                player.setGameMode(GameMode.CREATIVE);
                players.add(player);
            } else {
                player.kick("You are not whitelisted");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

}
