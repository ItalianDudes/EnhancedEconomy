package it.italiandudes.enhancedeconomy.util;

import it.italiandudes.enhancedeconomy.EnhancedEconomy;

import java.io.File;
import java.net.URISyntaxException;

@SuppressWarnings("unused")
public final class Defs {

    //Mod Info
    public static final class ModInfo {
        public static final String MOD_JAR_PATH;
        static {
            try {
                MOD_JAR_PATH = new File(EnhancedEconomy.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        public static final String MOD_ID = "enhancedeconomy";
        public static final String MOD_NAME = "EnhancedEconomy";
        public static final String VERSION = "0.0.1A";
    }

    //Proxy
    public static final String CLIENT = "it.italiandudes.enhancedeconomy.proxy.ClientProxy";
    public static final String COMMON = "it.italiandudes.enhancedeconomy.proxy.CommonProxy";

    // Module Names
    public static final class ModuleNames {
        public static final String MODULE_DBCONNECTION = "dbconnection";
        public static final String MODULE_COMMANDS = "commands";
        public static final String MODULE_LOCALIZATION = "localization";
    }

    // DBConnection Constants
    public static final class DBConnection {
        public static final String JDBC_MYSQL_CONNECTOR_STRING_START = "jdbc:mysql";
        public static final String JDBC_SQLITE_CONNECTOR_STRING_START = "jdbc:sqlite";
        public static final String JDBC_POSTGRESQL_CONNECTOR_STRING_START = "jdbc:postgresql";
        public static final String MYSQL_CONNECTOR = "mysql";
        public static final String SQLITE_CONNECTOR = "sqlite";
        public static final String POSTGRESQL_CONNECTOR = "postgresql";
    }

    // Lang Keys
    public static final class LangKeys {
        public static final String EE_VERSION = "ee_version";
        public static final String EE_INFO = "ee_info";
        public static final String COMMAND_EXECUTION_ERROR = "command_execution_error";
        public static final String COMMAND_SYNTAX_ERROR = "command_syntax_error";
        public static final String COMMAND_LOADING_STARTED = "command_loading_started";
        public static final String COMMAND_LOADING_SUCCESS = "command_loading_success";
        public static final String COMMAND_LOADING_FAIL = "command_loading_fail";
        public static final String COMMAND_UNLOADING_STARTED = "command_unloading_started";
        public static final String COMMAND_UNLOADING_SUCCESS = "command_unloading_success";
        public static final String COMMAND_UNLOADING_FAIL = "command_unloading_fail";
        public static final String COMMAND_RELOADING_STARTED = "command_reloading_started";
        public static final String COMMAND_RELOADING_SUCCESS = "command_reloading_success";
        public static final String COMMAND_RELOADING_FAIL = "command_reloading_fail";
        public static final String COMMAND_MODULE_NOT_LOADED = "command_module_not_loaded";
        public static final String COMMAND_MISSING_PERMISSIONS = "command_missing_permissions";
        public static final String COMMAND_USAGE_EE = "usage_ee";
        public static final String COMMAND_USAGE_EELOAD = "command_eeload_usage";
        public static final String COMMAND_USAGE_EEUNLOAD = "command_eeunload_usage";
        public static final String COMMAND_USAGE_EERELOAD = "command_eereload_usage";
    }

    // Command Constants
    public static final class Commands {
        public static final class EnhancedEconomy {
            public static final String[] COMMAND_NAME = {"enhancedeconomy", "ee"};
            public static final String EE_LOAD = "eeload";
            public static final String EE_UNLOAD = "eeunload";
            public static final String EE_RELOAD = "eereload";
        }
    }
}
