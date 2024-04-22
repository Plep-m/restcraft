package me.restcraft.com.managers;

import me.restcraft.com.interfaces.Manager;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.Chunk;

import java.util.List;

public class ChunkManager implements Manager {
    private final InstanceContainer instanceContainer;

    public ChunkManager(InstanceContainer instanceContainer) {
        this.instanceContainer = instanceContainer;
    }

    public Chunk getChunk(int x, int z) {
        return instanceContainer.getChunk(x, z);
    }

    public List<Chunk> getAllLoadedChunks() {
        return (List<Chunk>) instanceContainer.getChunks();
    }

}
