package me.restcraft.com.routes;

import spark.Spark;
import com.google.gson.Gson;

import me.restcraft.com.annotations.*;
import me.restcraft.com.classes.Structure;
import me.restcraft.com.interfaces.SetupRoutes;
import me.restcraft.com.managers.BlockManager;

@Route
public class StructureRoutes implements SetupRoutes {
    private final BlockManager blockManager;
    private final Gson gson;

    public StructureRoutes(@UseBlockManager BlockManager blockManager, @UseGson Gson gson) {
        this.blockManager = blockManager;
        this.gson = gson;
    }

    @Override
    public void setupRoutes() {
        Spark.post("/structure", (req, res) -> {
            Structure structure = gson.fromJson(req.body(), Structure.class);
            blockManager.buildStructure(structure);
            return "Structure built!";
        });
    }
}
