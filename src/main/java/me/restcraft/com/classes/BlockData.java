package me.restcraft.com.classes;

import com.google.gson.annotations.SerializedName;

public class BlockData {
    @SerializedName("blockName")
    public String name;

    @SerializedName("facing") // New property for direction
    public String facing;

    @SerializedName("relativePosition")
    public Position position;

    public BlockData(String name, Position position, String facing) {
        this.name = name;
        this.position = position;
        this.facing = facing;
    }
}
