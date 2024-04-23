package me.restcraft.com.routes;

import spark.Spark;
import com.google.gson.Gson;

import me.restcraft.com.annotations.UseDatabase;
import me.restcraft.com.annotations.UseGson;
import me.restcraft.com.annotations.Route;
import me.restcraft.com.interfaces.SetupRoutes;
import me.restcraft.com.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


@Route
public class WhitelistRoutes implements SetupRoutes {
    private final Gson gson;
    private final Database database;
    Logger logger = Logger.getLogger(WhitelistRoutes.class.getName());

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
            List<Map<String, Object>> resultList;

            try {
                resultSet = database.query(sql);
                resultList = database.resultSetToList(resultSet);
                return gson.toJson(resultList);

            } catch (SQLException e) {
                res.status(500);
                return "{\"error\": \"Database query failed\"}";

            } finally {
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException ex) {
                        logger.severe("Error closing ResultSet: " + ex.getMessage());
                    }
                }
            }
        });
    }
}
