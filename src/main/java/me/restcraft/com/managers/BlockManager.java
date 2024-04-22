package me.restcraft.com.managers;

import me.restcraft.com.classes.BlockData;
import me.restcraft.com.interfaces.Manager;
import me.restcraft.com.parsers.PlaceBody;
import me.restcraft.com.classes.Position;
import me.restcraft.com.classes.Structure;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;

public class BlockManager implements Manager {
    private final InstanceContainer instanceContainer;

    public BlockManager(InstanceContainer instanceContainer) {
        this.instanceContainer = instanceContainer;
    }

    public void placeBlock(int x, int y, int z, Block block, String facing) {


        if (facing != null) { // If 'facing' property is provided
            assert block != null;
            block = block.withProperty("facing", facing.toLowerCase());
        }

        assert block != null;
        instanceContainer.setBlock(x, y, z, block);
    }

    public void breakBlock(int x, int y, int z) {
        instanceContainer.setBlock(x, y, z, Block.AIR);
    }

    public void buildStructure(Structure structure) {
        Position start = structure.startingPosition;
        long delayMillis = 100;

        for (BlockData blockData : structure.blocks) {
            int x = start.x + blockData.position.x;
            int y = start.y + blockData.position.y;
            int z = start.z + blockData.position.z;

            Block block = PlaceBody.getBlockByName(blockData.name);
            placeBlock(x, y, z, block, blockData.facing);
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Block placement was interrupted: " + e.getMessage());
            }
        }
    }


}
