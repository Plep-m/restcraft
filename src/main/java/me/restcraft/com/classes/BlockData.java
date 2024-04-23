package me.restcraft.com.classes;

import com.google.gson.annotations.SerializedName;

public class BlockData {
    @SerializedName("blockName")
    public String name;

    @SerializedName("facing")
    public String facing;

    @SerializedName("power")
    public int power;

    @SerializedName("west")
    public String west;

    @SerializedName("east")
    public String east;

    @SerializedName("north")
    public String north;

    @SerializedName("south")
    public String south;

    @SerializedName("relativePosition")
    public Position position;

    public BlockData(String name, Position position, String facing) {
        this.name = name;
        this.position = position;
        this.facing = facing;
    }
}
