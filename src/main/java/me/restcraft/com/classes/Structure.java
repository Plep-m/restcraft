package me.restcraft.com.classes;

import com.google.gson.annotations.SerializedName;

public class Structure {
    @SerializedName("startingPosition")
    public Position startingPosition;

    @SerializedName("blocks")
    public BlockData[] blocks;

    public Structure(Position startingPosition, BlockData[] blocks) {
        this.startingPosition = startingPosition;
        this.blocks = blocks;
    }
}
