package me.restcraft.com.parsers;
import lombok.Getter;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.Nullable;

@Getter
public class PlaceBody {
    private int x;
    private int y;
    private int z;
    private String blockName;


    public static @Nullable Block getBlockByName(String name) {
        return Block.fromNamespaceId("minecraft:" + name.toLowerCase());
    }
}
