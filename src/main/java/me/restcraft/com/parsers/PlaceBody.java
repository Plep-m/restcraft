package me.restcraft.com.parsers;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.Nullable;

public class PlaceBody {
    private int x;
    private int y;
    private int z;
    private String blockName;

    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getZ() {
        return z;
    }

    public String getBlockName() {
        return blockName;
    }

    public static @Nullable Block getBlockByName(String name) {
        return Block.fromNamespaceId("minecraft:" + name.toLowerCase());
    }
}
