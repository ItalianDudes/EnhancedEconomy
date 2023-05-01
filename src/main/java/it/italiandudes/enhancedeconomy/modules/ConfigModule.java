package it.italiandudes.enhancedeconomy.modules;

import it.italiandudes.enhancedeconomy.util.Defs;
import net.minecraftforge.common.config.Config;

@SuppressWarnings("unused")
@Config(modid = Defs.ModInfo.MOD_ID)
public final class ConfigModule {

    // Attributes
    @Config.Name("JDBC Connection String")
    public static String JDBC_CONNECTION_STRING;


    // Default Constructor
    private ConfigModule() {
        throw new RuntimeException("Can't instantiate this class!");
    }
}
