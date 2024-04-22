package me.restcraft.com.routes;

import me.restcraft.com.annotations.Route;
import me.restcraft.com.annotations.UseDatabase;
import me.restcraft.com.annotations.UseGson;
import me.restcraft.com.Database;
import spark.Spark;
import com.google.gson.Gson;
import me.restcraft.com.interfaces.SetupRoutes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Route
public class TestRoutes implements SetupRoutes {
    private final Gson gson;
    private final Database databaseManager;

    public TestRoutes(@UseGson Gson gson, @UseDatabase Database databaseManager) {
        this.gson = gson;
        this.databaseManager = databaseManager;
    }

    @Override
    public void setupRoutes() {
        Spark.get("/test", (req, res) -> {
            res.type("application/json"); // Set response content type

            String sql = "SELECT NOW()"; // Example query to get current timestamp
            ResultSet resultSet = null;
            List<Map<String, Object>> resultList = new ArrayList<>();

            try {
                resultSet = databaseManager.query(sql);

                resultList = databaseManager.resultSetToList(resultSet); // Convert to list of maps

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
