package me.restcraft.com.managers;

import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.Chunk;

import java.util.List;

public class ChunkManager {
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
