package me.restcraft.com.managers;

import lombok.Setter;
import me.restcraft.com.annotations.UseInstanceContainer;
import me.restcraft.com.annotations.UseRedstoneManager;
import me.restcraft.com.classes.BlockData;
import me.restcraft.com.interfaces.Manager;
import me.restcraft.com.parsers.PlaceBody;
import me.restcraft.com.classes.Position;
import me.restcraft.com.classes.Structure;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class BlockManager implements Manager {
    private final InstanceContainer instanceContainer;
    Logger logger = Logger.getLogger(BlockManager.class.getName());
    @Setter
    private RedstoneManager redstoneManager;

    public BlockManager(@UseInstanceContainer InstanceContainer instanceContainer, @UseRedstoneManager RedstoneManager redstoneManager) {
        this.instanceContainer = instanceContainer;
        this.redstoneManager = redstoneManager;
    }

    public void placeBlock(int x, int y, int z, Block block, Map<String, Object> properties) {
        if (block == null) {
            throw new IllegalArgumentException("Block cannot be null");
        }

        if(properties != null) {
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value != null) {
                    block = block.withProperty(key, value.toString().toLowerCase());
                }

                if ("minecraft:redstone_wire".equals(block.name())) {
                    redstoneManager.addRedstoneWire(new Position(x, y, z), block);
                }
            }
            instanceContainer.setBlock(x, y, z, block);
        } else {
            instanceContainer.setBlock(x, y, z, block);
        }
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
            Map<String, Object> properties = new HashMap<>();
            if (blockData.facing != null) {
                properties.put("facing", blockData.facing);
            }
            if (blockData.power > 0) {
                properties.put("power", blockData.power);
            }
            if (blockData.west != null) {
                properties.put("west", blockData.west);
            }
            if (blockData.east != null) {
                properties.put("east", blockData.east);
            }
            if (blockData.north != null) {
                properties.put("north", blockData.north);
            }
            if (blockData.south != null) {
                properties.put("south", blockData.south);
            }
            logger.info("Placing block: " + blockData.name + " at " + x + ", " + y + ", " + z);
            placeBlock(x, y, z, block, properties);
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.severe("Block placement was interrupted: " + e.getMessage());
            }
        }
    }


}
