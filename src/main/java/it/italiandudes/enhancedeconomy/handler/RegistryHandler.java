package it.italiandudes.enhancedeconomy.handler;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.modules.ConfigModule;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import it.italiandudes.enhancedeconomy.modules.LocalizationModule;
import it.italiandudes.enhancedeconomy.util.ServerLogger;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public final class RegistryHandler {

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    public static void preInitRegistries(FMLPreInitializationEvent event){
        ServerLogger.initLogger(event.getModLog());
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    public static void initRegistries(FMLInitializationEvent event){

    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    public static void postInitRegistries(FMLPostInitializationEvent event){

    }

    public static void serverRegistries(FMLServerStartingEvent event){
        try {
            LocalizationModule.load(ConfigModule.LOCALIZATION);
        } catch (ModuleException e) {
            throw new RuntimeException(e);
        }
        CommandsModule.load(event);
        try {
            DBConnectionModule.load(ConfigModule.JDBC_CONNECTION_STRING);
        }catch (ModuleException e) {
            ServerLogger.getLogger().error(e);
        }
    }
}