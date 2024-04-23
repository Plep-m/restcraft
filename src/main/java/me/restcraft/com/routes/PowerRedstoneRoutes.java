package me.restcraft.com.routes;


import spark.Spark;
import com.google.gson.Gson;

import me.restcraft.com.annotations.Route;
import me.restcraft.com.annotations.UseGson;
import me.restcraft.com.annotations.UseRedstoneManager;

import me.restcraft.com.interfaces.SetupRoutes;
import me.restcraft.com.managers.RedstoneManager;

@Route
public class PowerRedstoneRoutes implements SetupRoutes {
    private final Gson gson;
    private final RedstoneManager redstoneManager;

    public PowerRedstoneRoutes(@UseGson Gson gson, @UseRedstoneManager RedstoneManager redstoneManager) {
        this.gson = gson;
        this.redstoneManager = redstoneManager;
    }

    @Override
    public void setupRoutes() {
        Spark.get("/poweron", (req, res) -> {
            res.type("application/json");
            redstoneManager.powerOn();
            return "";
        });

        Spark.get("/poweroff", (req, res) -> {
            res.type("application/json");
            redstoneManager.powerOff();
            return "";
        });
    }
}
