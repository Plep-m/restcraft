package me.restcraft.com.routes;

import me.restcraft.com.Database;
import me.restcraft.com.annotations.UseDatabase;
import spark.Spark;
import com.google.gson.Gson;

import me.restcraft.com.annotations.Route;

import me.restcraft.com.interfaces.SetupRoutes;

// import me.restcraft.com.managers.BlockManager;
//import me.restcraft.com.managers.PlayerManager;
//import me.restcraft.com.managers.ChunkManager;

//import me.restcraft.com.annotations.UseBlockManager;
//import me.restcraft.com.annotations.UsePlayerManager;
//import me.restcraft.com.annotations.UseChunkManager;
import me.restcraft.com.annotations.UseGson;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Route
public class WhitelistRoutes implements SetupRoutes {
    private final Gson gson;
    private final Database database;

    public WhitelistRoutes(@UseGson Gson gson , @UseDatabase Database database) {
        this.gson = gson;
        this.database = database;
    }

    @Override
    public void setupRoutes() {
        Spark.get("/whitelist", (req, res) -> {
            res.type("application/json");
            String sql = "SELECT * FROM whitelist";
            ResultSet resultSet = null;
            List<Map<String, Object>> resultList = new ArrayList<>();

            try {
                resultSet = database.query(sql);

                resultList = database.resultSetToList(resultSet); // Convert to list of maps

                // Return the JSON representation of the result list
                return gson.toJson(resultList);

            } catch (SQLException e) {
                res.status(500); // Internal server error
                return "{\"error\": \"Database query failed\"}";

            } finally {
                if (resultSet != null) {
                    try {
                        resultSet.close(); // Close ResultSet to avoid resource leaks
                    } catch (SQLException ex) {
                        System.err.println("Error closing ResultSet: " + ex.getMessage());
                    }
                }
            }
        });
    }
}
