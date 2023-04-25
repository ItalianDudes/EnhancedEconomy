package it.italiandudes.enhancedeconomy.modules;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleAlreadyLoadedException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleLoadingException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleNotLoadedException;
import it.italiandudes.enhancedeconomy.exceptions.modules.ModuleOperationException;
import it.italiandudes.enhancedeconomy.utils.Resource;
import it.italiandudes.enhancedeconomy.utils.ServerLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.sql.*;
import java.util.Scanner;

@SuppressWarnings("unused")
public final class DBConnectorModule {

    // Attributes
    private static Connection dbConnection = null;
    private static boolean isDBConnecting = false;

    // Default Constructor
    public DBConnectorModule() {
        throw new RuntimeException("Can't instantiate this class!");
    }

    // Query Retriever
    public static String getQueryFromResourcesFileSQL(@NotNull final String QUERY_PATH) {
        InputStream inStream = Resource.getResourceAsStream(QUERY_PATH);

        StringBuilder queryBuilder = new StringBuilder();

        Scanner queryReader = new Scanner(inStream);
        while(queryReader.hasNext()) {
            queryBuilder.append(queryReader.nextLine()).append('\n');
        }
        queryReader.close();

        return queryBuilder.toString();
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
            if (!disableLog) ServerLogger.getLogger().warning("DBConnect Module Load: Canceled! (Reason: Another thread is executing a dbconnection loading command)");
            throw new ModuleLoadingException("DBConnect Module Load: Canceled! (Reason: Another thread is executing a dbconnection loading command)");
        }
        if (isModuleLoaded()) {
            if (!disableLog) ServerLogger.getLogger().severe("DBConnect Module Load: Failed! (Reason: the module has already been loaded)");
            throw new ModuleAlreadyLoadedException("DBConnect Module Load: Failed! (Reason: the module has already been loaded)");
        }

        isDBConnecting = true;

        if (jdbcConnectionString.contains("allowMultiQueries=false")) {
            jdbcConnectionString = jdbcConnectionString.replace("?allowMultiQueries=false", "allowMultiQueries=true");
        }else if (!jdbcConnectionString.contains("allowMultiQueries=true")) {
            if (!jdbcConnectionString.contains("?")) {
                jdbcConnectionString += '?';
            }else {
                jdbcConnectionString += '&';
            }
            jdbcConnectionString+="allowMultiQueries=true";
        }

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

        isDBConnecting = false;
        if (!disableLog) ServerLogger.getLogger().info("DBConnect Module Load: Successful!");
    }
    public static void unload() throws ModuleException {
        unload(true);
    }
    public static void unload(boolean disableLog) throws ModuleException {

        if (isDBConnecting) {
            if (!disableLog) ServerLogger.getLogger().warning("DBConnection Module Unload: Canceled! (Reason: Another thread is executing a dbconnection loading command)");
            throw new ModuleLoadingException("DBConnection Module Unload: Canceled! (Reason: Another thread is executing a dbconnection loading command)");
        }
        if (!isModuleLoaded()) {
            if (!disableLog) ServerLogger.getLogger().severe("DBConnection Module Unload: Failed! (Reason: the module isn't loaded)");
            throw new ModuleNotLoadedException("DBConnection Module Unload: Failed! (Reason: the module isn't loaded)");
        }

        try {
            if(dbConnection != null) dbConnection.close();
        }catch (SQLException ignored){}

        if (!disableLog) ServerLogger.getLogger().info("DBConnection Module Unload: Successful!");
    }
    public static void reload(@NotNull String jdbcConnectionString) throws ModuleException {

        if (isDBConnecting) {
            ServerLogger.getLogger().warning("DBConnect Module Reload: Canceled! (Reason: Another thread is executing a dbconnection loading command)");
            throw new ModuleLoadingException("DBConnect Module Reload: Canceled! (Reason: Another thread is executing a dbconnection loading command)");
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
            ServerLogger.getLogger().warning("GetPreparedStatement Operation: Canceled! (Reason: Another thread is executing a dbconnection loading command)");
            throw new ModuleLoadingException("GetPreparedStatement Operation: Canceled! (Reason: Another thread is executing a dbconnection loading command)");
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
    @Nullable @SuppressWarnings("UnusedReturnValue")
    public static ResultSet executeStatementFromQuery(@NotNull String query) throws ModuleException {

        if (isDBConnecting) {
            ServerLogger.getLogger().warning("ExecuteStatementFromQuery Operation: Canceled! (Reason: Another thread is executing a dbconnection loading command)");
            throw new ModuleLoadingException("ExecuteStatementFromQuery Operation: Canceled! (Reason: Another thread is executing a dbconnection loading command)");
        }

        if (!isModuleLoaded()) {
            ServerLogger.getLogger().severe("ExecuteStatementFromQuery Operation: Failed! (Reason: the module isn't loaded)");
            throw new ModuleNotLoadedException("ExecuteStatementFromQuery Operation: Failed! (Reason: the module isn't loaded)");
        }

        Statement statement;
        try {
            statement = dbConnection.createStatement();
        } catch (SQLException e) {
            ServerLogger.getLogger().severe("ExecuteStatementFromQuery Operation: Failed! (Reason: an error has occurred with the sql query)");
            throw new ModuleOperationException("ExecuteStatementFromQuery Operation: Failed! (Reason: an error has occurred with the sql query)", e);
        }

        try {
            statement.execute(query);
            ResultSet resultSet = statement.getResultSet();
            statement.close();
            return resultSet;
        } catch (SQLException e) {
            try {
                statement.close();
            } catch (Exception ignored) {}
            ServerLogger.getLogger().severe("ExecuteStatementFromQuery Operation: Failed! (Reason: an error has occurred with the sql query)");
            throw new ModuleOperationException("ExecuteStatementFromQuery Operation: Failed! (Reason: an error has occurred with the sql query)", e);
        }
    }
    public static void executeStatementFromQueryIgnoreResult(@NotNull String query) throws ModuleException {
        ResultSet resultSet = executeStatementFromQuery(query);
        try {
            if (resultSet != null) resultSet.close();
        }catch (SQLException ignored) {}
    }
    @Nullable @SuppressWarnings("UnusedReturnValue")
    public static ResultSet executePreparedStatement(@NotNull PreparedStatement preparedStatement, boolean closeStatementAfterExecute) throws ModuleException {

        if (isDBConnecting) {
            ServerLogger.getLogger().warning("ExecutePreparedStatement Operation: Canceled! (Reason: Another thread is executing a dbconnection loading command)");
            throw new ModuleLoadingException("ExecutePreparedStatement Operation: Canceled! (Reason: Another thread is executing a dbconnection loading command)");
        }

        if (!isModuleLoaded()) {
            ServerLogger.getLogger().severe("ExecutePreparedStatement Operation: Failed! (Reason: the module isn't loaded)");
            throw new ModuleNotLoadedException("ExecutePreparedStatement Operation: Failed! (Reason: the module isn't loaded)");
        }

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (closeStatementAfterExecute) preparedStatement.close();
            return resultSet;
        } catch (SQLException e) {
            try {
                preparedStatement.close();
            } catch (Exception ignored) {}
            ServerLogger.getLogger().severe("ExecutePreparedStatement Operation: Failed! (Reason: an error has occurred with the sql query)");
            throw new ModuleOperationException("ExecutePreparedStatement Operation: Failed! (Reason: an error has occurred with the sql query)", e);
        }
    }
    @Nullable @SuppressWarnings("UnusedReturnValue")
    public static ResultSet executePreparedStatementFromQuery(@NotNull String query) throws ModuleException {

        if (isDBConnecting) {
            ServerLogger.getLogger().warning("ExecuteStatementFromQuery Operation: Canceled! (Reason: Another thread is executing a dbconnection loading command)");
            throw new ModuleLoadingException("ExecuteStatementFromQuery Operation: Canceled! (Reason: Another thread is executing a dbconnection loading command)");
        }

        if (!isModuleLoaded()) {
            ServerLogger.getLogger().severe("ExecuteStatementFromQuery Operation: Failed! (Reason: the module isn't loaded)");
            throw new ModuleNotLoadedException("ExecuteStatementFromQuery Operation: Failed! (Reason: the module isn't loaded)");
        }

        PreparedStatement preparedStatement;
        try {
            preparedStatement = dbConnection.prepareStatement(query);
        } catch (SQLException e) {
            ServerLogger.getLogger().severe("ExecuteStatementFromQuery Operation: Failed! (Reason: an error has occurred with the sql query)");
            throw new ModuleOperationException("ExecuteStatementFromQuery Operation: Failed! (Reason: an error has occurred with the sql query)", e);
        }

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            preparedStatement.close();
            return resultSet;
        } catch (SQLException e) {
            try {
                preparedStatement.close();
            } catch (Exception ignored) {}
            ServerLogger.getLogger().severe("ExecuteStatementFromQuery Operation: Failed! (Reason: an error has occurred with the sql query)");
            throw new ModuleOperationException("ExecuteStatementFromQuery Operation: Failed! (Reason: an error has occurred with the sql query)", e);
        }
    }
    public static void executePreparedStatementFromQueryIgnoreResult(@NotNull String query) throws ModuleException {
        ResultSet resultSet = executePreparedStatementFromQuery(query);
        try {
            if (resultSet != null) resultSet.close();
        }catch (SQLException ignored){}
    }
}
