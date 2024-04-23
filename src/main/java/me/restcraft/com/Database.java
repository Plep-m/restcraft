package me.restcraft.com;

import io.github.cdimascio.dotenv.Dotenv;

import me.restcraft.com.interfaces.Manager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database implements Manager {
    static Dotenv dotenv = Dotenv.load();
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/restcraft_db";
    private static final String USER = "restcraft_rw";
    private static final String PASS = dotenv.get("DB_PASSWORD");


    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public ResultSet query(String sql) throws SQLException {
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            return statement.executeQuery(sql);
        }
    }

    public List<Map<String, Object>> resultSetToList(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            Map<String, Object> row = new HashMap<>();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object columnValue = resultSet.getObject(i);
                row.put(columnName, columnValue);
            }

            resultList.add(row);
        }

        return resultList;
    }
}
