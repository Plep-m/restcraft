package me.restcraft.com.managers;

import lombok.Getter;
import me.restcraft.com.annotations.UseBlockManager;
import me.restcraft.com.classes.Position;
import me.restcraft.com.interfaces.Manager;
import net.minestom.server.instance.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RedstoneManager implements Manager {
    Logger logger = Logger.getLogger(RedstoneManager.class.getName());
    @Getter
    private HashMap<Position, Block> redstoneWires;
    private final BlockManager blockManager;

    public RedstoneManager(@UseBlockManager BlockManager blockManager) {
        this.redstoneWires = new HashMap<>();
        this.blockManager = blockManager;
    }

    public void addRedstoneWire(Position position, Block redstoneWire) {
        this.redstoneWires.put(position, redstoneWire);
        logger.info("Added redstone wire at position: " + position);
    }

    public void removeRedstoneWire(Position position) {
        this.redstoneWires.remove(position);
        logger.info("Removed redstone wire at position: " + position);
    }

    public void powerOn() {
        blockManager.setRedstoneManager(this);
        try {
            for (Map.Entry<Position, Block> entry : redstoneWires.entrySet()) {
                Block block = entry.getValue();
                Map<String, Object> properties = new HashMap<>(block.properties());
                properties.put("power", 5);
                blockManager.placeBlock(entry.getKey().x, entry.getKey().y, entry.getKey().z, block, properties);
            }
        } catch (Exception e) {
            logger.severe("Could not power on redstone wires: " + e.getMessage());
        }
    }

    public void powerOff() {
        for (Map.Entry<Position, Block> entry : redstoneWires.entrySet()) {
            Block block = entry.getValue();
            Map<String, Object> properties = new HashMap<>(block.properties());
            properties.put("power", 0);
            blockManager.placeBlock(entry.getKey().x, entry.getKey().y, entry.getKey().z, block, properties);
        }
    }

}