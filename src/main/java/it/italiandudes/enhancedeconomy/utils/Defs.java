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
        public static final String PLUGIN_VERSION = "0.18.4A";
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
            public static final String FALLBACK_LANGUAGE = EN_US;
        }

        // Lang Keys
        public static final class Keys {
            public static final String YES = "yes";
            public static final String NO = "no";
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
            public static final String COMMAND_DBCONNECTION_MODULE_NOT_LOADED = "command_dbconnection_module_not_loaded";
            public static final String COMMAND_MISSING_PERMISSIONS = "command_missing_permissions";
            public static final String COMMAND_USER_NOT_REGISTERED = "command_user_not_registered";

            // Currency
            public static final String COMMAND_EECURRENCY_LIST_HEADER = "command_eecurrency_list_header";
            public static final String COMMAND_EECURRENCY_LIST_SEPARATOR = "command_eecurrency_list_separator";
            public static final String COMMAND_EECURRENCY_LIST_EMPTY = "command_eecurrency_list_empty";
            public static final String CURRENCY_NAME = "currency_name";
            public static final String CURRENCY_SYMBOL = "currency_symbol";
            public static final String CURRENCY_ISO = "currency_iso";
            public static final String CURRENCY_CREATION_DATE = "currency_creation_date";
            public static final String COMMAND_EECURRENCY_GET_NOT_FOUND = "command_eecurrency_get_not_found";
            public static final String COMMAND_EECURRENCY_NEW_ALREADY_EXIST = "command_eecurrency_new_already_exist";
            public static final String COMMAND_EECURRENCY_NEW_SUCCESS = "command_eecurrency_new_success";
            public static final String COMMAND_EECURRENCY_DELETE_NOT_FOUND = "command_eecurrency_delete_not_found";
            public static final String COMMAND_EECURRENCY_DELETE_SUCCESS = "command_eecurrency_delete_success";

            // Country
            public static final String COMMAND_EECOUNTRY_LIST_HEADER = "command_eecountry_list_header";
            public static final String COMMAND_EECOUNTRY_LIST_SEPARATOR = "command_eecountry_list_separator";
            public static final String COMMAND_EECOUNTRY_GET_NOT_FOUND = "command_eecountry_get_not_found";
            public static final String COUNTRY_NAME = "country_name";
            public static final String COUNTRY_OWNER = "country_owner";
            public static final String COUNTRY_CREATION_DATE = "country_creation_date";
            public static final String COMMAND_EECOUNTRY_LIST_EMPTY = "command_eecountry_list_empty";
            public static final String COMMAND_EECOUNTRY_NEW_ALREADY_EXIST = "command_eecountry_new_already_exist";
            public static final String COMMAND_EECOUNTRY_NEW_SUCCESS = "command_eecountry_new_success";
            public static final String COMMAND_EECOUNTRY_DELETE_NOT_FOUND = "command_eecountry_delete_not_found";
            public static final String COMMAND_EECOUNTRY_DELETE_SUCCESS = "command_eecountry_delete_success";
            public static final String COMMAND_EECOUNTRY_DELETE_NOT_OWNER = "command_eecountry_delete_not_owner";

            // User
            public static final String COMMAND_EEUSER_CONSOLE = "command_eeuser_console";
            public static final String COMMAND_EEUSER_ALREADY_EXIST = "command_eeuser_already_exist";
            public static final String COMMAND_EEUSER_SUCCESS = "command_eeuser_success";

            // Bank
            public static final String COMMAND_EEBANK_LIST_HEADER = "command_eebank_list_header";
            public static final String COMMAND_EEBANK_LIST_SEPARATOR = "command_eebank_list_separator";
            public static final String BANK_NAME = "bank_name";
            public static final String BANK_HEADQUARTER_COUNTRY = "bank_headquarter_country";
            public static final String BANK_OWNER = "bank_owner";
            public static final String BANK_IS_PRIVATE = "is_private";
            public static final String BANK_CREATION_DATE = "bank_creation_date";
            public static final String COMMAND_EEBANK_LIST_EMPTY = "command_eebank_list_empty";
            public static final String COMMAND_EEBANK_GET_NOT_FOUND = "command_eebank_get_not_found";
            public static final String COMMAND_EEBANK_NEW_ALREADY_EXIST = "command_eebank_new_already_exist";
            public static final String COMMAND_EEBANK_NEW_COUNTRY_DOES_NOT_EXIST = "command_eebank_new_country_does_not_exist";
            public static final String COMMAND_EEBANK_NEW_SUCCESS = "command_eebank_new_success";
            public static final String COMMAND_EEBANK_NEW_PUBLIC_NOT_COUNTRY_OWNER = "command_eebank_new_public_not_country_owner";
            public static final String COMMAND_EEBANK_DELETE_NOT_FOUND = "command_eebank_delete_not_found";
            public static final String COMMAND_EEBANK_DELETE_SUCCESS = "command_eebank_delete_success";
            public static final String COMMAND_EEBANK_DELETE_NOT_OWNER = "command_eebank_delete_not_owner";

            // Bank Currency
            public static final String COMMAND_EEBANK_CURRENCY_LIST_HEADER = "command_eebank_currency_list_header";
            public static final String COMMAND_EEBANK_CURRENCY_LIST_SEPARATOR = "command_eebank_currency_list_separator";
            public static final String BANK_BALANCE = "bank_balance";
            public static final String COMMAND_EEBANK_CURRENCY_GET_HEADER = "command_eebank_currency_get_header";

        }
    }
}
