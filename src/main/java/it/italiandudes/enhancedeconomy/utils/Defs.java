package it.italiandudes.enhancedeconomy.utils;

import it.italiandudes.enhancedeconomy.EnhancedEconomy;

import java.io.File;
import java.net.URISyntaxException;

@SuppressWarnings("unused")
public final class Defs {

    // Plugin Info
    public static final class PluginInfo {
        public static final String PLUGIN_JAR_PATH;
        static {
            try {
                PLUGIN_JAR_PATH = new File(EnhancedEconomy.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Module Names
    public static final class ModuleNames {
        public static final String MODULE_LOCALIZATION = "localization";
        public static final String MODULE_CONFIG = "config";
        public static final String MODULE_DBCONNECTION = "dbconnection";
        public static final String MODULE_COMMANDS = "commands";
    }

    // Config Constants
    public static final class Config {

        // Config Files Identifier
        public static final class Identifiers {
            public static final String GENERAL_CONFIG = "general";
        }

        // Config Keys
        public static final class Keys {

            // General Config File
            public static final class General {
                public static final String KEY_LANG = "language_pack";
                public static final String KEY_DATABASE_URL = "database_url";
            }
        }
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

    // Localization Constants
    public static final class Localization {

        // Langs
        public static final class Langs {
            public static final String EN_US = "en-US";
            public static final String IT_IT = "it-IT";
        }

        // Lang Keys
        public static final class Keys {
            public static final String TEST_ENTRY = "test_entry";
        }
    }

    // Command Constants
    public static final class Commands {
        public static final class EnhancedEconomy {
            public static final String[] COMMAND_NAME = {"enhancedeconomy", "ee"};
            public static final String EE_LOAD = "load";
            public static final String EE_UNLOAD = "unload";
            public static final String EE_RELOAD = "reload";
        }
    }
}
