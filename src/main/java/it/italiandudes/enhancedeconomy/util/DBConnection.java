package it.italiandudes.enhancedeconomy.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;

@SuppressWarnings("unused")
public final class DBConnection {

    // Attributes
    private static Connection dbConnection = null;

    // Default Constructor
    public DBConnection() {
        throw new RuntimeException("Can't instantiate this class!");
    }

    // Methods
    public static void createConnection(@NotNull String jdbcConnectionString) {
        try {
            dbConnection = DriverManager.getConnection(jdbcConnectionString);
            dbConnection.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                if (dbConnection != null) dbConnection.close();
            } catch (Exception ignored){}
            dbConnection = null;
        }
    }
    public static PreparedStatement getPreparedStatement(@NotNull String sql) throws SQLException {
        return dbConnection.prepareStatement(sql);
    }
    @Nullable
    public static ResultSet executePreparedStatement(@NotNull PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.execute();
        ResultSet result = preparedStatement.getResultSet();
        preparedStatement.close();
        return result;
    }
    public static void closeConnection() {
        try {
            if(dbConnection != null) dbConnection.close();
        }catch (SQLException ignored){}
    }
}
