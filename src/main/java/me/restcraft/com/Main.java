package me.restcraft.com;

import me.restcraft.com.commands.TimeCommand;
import me.restcraft.com.managers.BlockManager;
import me.restcraft.com.managers.ChunkManager;
import me.restcraft.com.managers.CommandsManager;
import me.restcraft.com.managers.PlayerManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;

public class Main {
    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();

        InstanceContainer instanceContainer = InstanceCreator.createInstance();

        PlayerManager playerManager = new PlayerManager(instanceContainer);
        BlockManager blockManager = new BlockManager(instanceContainer);
        ChunkManager chunkManager = new ChunkManager(instanceContainer);

        // Set up HTTP routes
        HttpRoutes httpRoutes = new HttpRoutes(playerManager, blockManager, chunkManager);
        httpRoutes.setupRoutes();

        CommandsManager commandManager = new CommandsManager();
        commandManager.addCommand(new TimeCommand(instanceContainer));
        // commandManager.addCommand(new CustomCommand(playerManager)); // Example of another command using a dependency
        commandManager.registerCommands();

        minecraftServer.start("0.0.0.0", 25565);
    }
}
