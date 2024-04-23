package me.restcraft.com;

import me.restcraft.com.parsers.PlaceBody;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;

import java.util.Objects;

public class InstanceCreator {

    private InstanceCreator() {}

    public static InstanceContainer createInstance() {
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instance = instanceManager.createInstanceContainer();
        instance.setChunkSupplier(LightingChunk::new);
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Objects.requireNonNull(PlaceBody.getBlockByName("stone"))));
        return instance;
    }
}
