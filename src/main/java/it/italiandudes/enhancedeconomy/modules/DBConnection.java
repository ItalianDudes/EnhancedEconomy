package it.italiandudes.enhancedeconomy.modules;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleAlreadyLoadedException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleLoadingException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleNotLoadedException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleOperationException;
import it.italiandudes.enhancedeconomy.utils.ServerLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;

@SuppressWarnings("unused")
public final class DBConnection {

    // Attributes
    private static Connection dbConnection = null;
    private static boolean isDBConnecting = false;

    // Default Constructor
    public DBConnection() {
        throw new RuntimeException("Can't instantiate this class!");
    }

    // Module Checker
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isModuleLoaded() {
        return dbConnection != null;
    }

    // Methods
    public synchronized static void load(@NotNull String jdbcConnectionString) throws ModuleException {
        load(jdbcConnectionString, false);
    }
    private static void load(@NotNull String jdbcConnectionString, boolean disableLog) throws ModuleException {

        if (isDBConnecting) {
            if (!disableLog) ServerLogger.getLogger().warning("DBConnect Module Load: Canceled! (Reason: Another thread is executing a dbconnection connect command)");
            throw new ModuleLoadingException("DBConnect Module Load: Canceled! (Reason: Another thread is executing a dbconnection connect command)");
        }
        if (isModuleLoaded()) {
            if (!disableLog) ServerLogger.getLogger().severe("DBConnect Module Load: Failed! (Reason: the module has already been loaded)");
            throw new ModuleAlreadyLoadedException("DBConnect Module Load: Failed! (Reason: the module has already been loaded)");
        }

        isDBConnecting = true;

        try {
            dbConnection = DriverManager.getConnection(jdbcConnectionString);
            dbConnection.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                if (dbConnection != null) dbConnection.close();
            } catch (Exception ignored){}
            dbConnection = null;
            isDBConnecting = false;
            if (!disableLog) ServerLogger.getLogger().severe("DBConnect Module Load: Failed! (Reason: an error during connection has occurred)");
            throw new ModuleLoadingException("DBConnect Module Load: Failed! (Reason: an error during connection has occurred)", e);
        }
    }
    public static void unload() throws ModuleException {

        if (isDBConnecting) {
            ServerLogger.getLogger().warning("DBConnection Module Unload: Canceled! (Reason: Another thread is executing a config loading command)");
            throw new ModuleLoadingException("DBConnection Module Unload: Canceled! (Reason: Another thread is executing a config loading command)");
        }
        if (!isModuleLoaded()) {
            ServerLogger.getLogger().severe("DBConnection Module Unload: Failed! (Reason: the module isn't loaded)");
            throw new ModuleNotLoadedException("DBConnection Module Unload: Failed! (Reason: the module isn't loaded)");
        }

        try {
            if(dbConnection != null) dbConnection.close();
        }catch (SQLException ignored){}

        ServerLogger.getLogger().info("DBConnection Module Unload: Successful!");
    }
    public static void reload(@NotNull String jdbcConnectionString) throws ModuleException {

        if (isDBConnecting) {
            ServerLogger.getLogger().warning("DBConnect Module Reload: Canceled! (Reason: Another thread is executing a dbconnection connect command)");
            throw new ModuleLoadingException("DBConnect Module Reload: Canceled! (Reason: Another thread is executing a dbconnection connect command)");
        }
        if (isModuleLoaded()) {
            ServerLogger.getLogger().severe("DBConnect Module Reload: Failed! (Reason: the module has already been loaded)");
            throw new ModuleAlreadyLoadedException("DBConnect Module Reload: Failed! (Reason: the module has already been loaded)");
        }

        try {
            unload();
        } catch (ModuleException e) {
            ServerLogger.getLogger().severe("DBConnect Module Reload: Failed! (Reason: an error has occurred in the unload routine)");
            throw new ModuleAlreadyLoadedException("DBConnect Module Reload: Failed! (Reason: an error has occurred in the unload routine)");
        }

        try {
            load(jdbcConnectionString, true);
        } catch (ModuleException e) {
            ServerLogger.getLogger().severe("DBConnect Module Reload: Failed! (Reason: the load routine has failed)");
            throw new ModuleAlreadyLoadedException("DBConnect Module Reload: Failed! (Reason: the load routine has failed)", e);
        }

        ServerLogger.getLogger().info("DBConnect Module Reload: Successful!");

    }
    @NotNull
    public static PreparedStatement getPreparedStatement(@NotNull String sql) throws ModuleException {

        if (isDBConnecting) {
            ServerLogger.getLogger().warning("GetPreparedStatement Operation: Canceled! (Reason: Another thread is executing a config loading command)");
            throw new ModuleLoadingException("GetPreparedStatement Operation: Canceled! (Reason: Another thread is executing a config loading command)");
        }

        if (!isModuleLoaded()) {
            ServerLogger.getLogger().severe("GetPreparedStatement Operation: Failed! (Reason: the module isn't loaded)");
            throw new ModuleNotLoadedException("GetPreparedStatement Operation: Failed! (Reason: the module isn't loaded)");
        }

        try {
            return dbConnection.prepareStatement(sql);
        } catch (SQLException e) {
            ServerLogger.getLogger().severe("GetPreparedStatement Operation: Failed! (Reason: an error has occurred with the sql query)");
            throw new ModuleOperationException("GetPreparedStatement Operation: Failed! (Reason: an error has occurred with the sql query)", e);
        }
    }
    @Nullable
    public static ResultSet executePreparedStatement(@NotNull PreparedStatement preparedStatement) throws ModuleException {

        if (isDBConnecting) {
            ServerLogger.getLogger().warning("ExecutePreparedStatement Operation: Canceled! (Reason: Another thread is executing a config loading command)");
            throw new ModuleLoadingException("ExecutePreparedStatement Operation: Canceled! (Reason: Another thread is executing a config loading command)");
        }

        if (!isModuleLoaded()) {
            ServerLogger.getLogger().severe("ExecutePreparedStatement Operation: Failed! (Reason: the module isn't loaded)");
            throw new ModuleNotLoadedException("ExecutePreparedStatement Operation: Failed! (Reason: the module isn't loaded)");
        }

        try {
            preparedStatement.execute();
            ResultSet result = preparedStatement.getResultSet();
            preparedStatement.close();
            return result;
        } catch (SQLException e) {
            try {
                preparedStatement.close();
            } catch (Exception ignored) {}
            ServerLogger.getLogger().severe("ExecutePreparedStatement Operation: Failed! (Reason: an error has occurred with the sql query)");
            throw new ModuleOperationException("ExecutePreparedStatement Operation: Failed! (Reason: an error has occurred with the sql query)", e);
        }
    }
}
