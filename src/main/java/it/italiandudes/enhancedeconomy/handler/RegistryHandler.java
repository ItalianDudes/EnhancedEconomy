package it.italiandudes.enhancedeconomy.handler;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;
import it.italiandudes.enhancedeconomy.modules.CommandsModule;
import it.italiandudes.enhancedeconomy.modules.ConfigModule;
import it.italiandudes.enhancedeconomy.modules.DBConnectionModule;
import it.italiandudes.enhancedeconomy.modules.LocalizationModule;
import it.italiandudes.enhancedeconomy.util.ServerLogger;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;

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

    public static void initServerRegistries(FMLServerStartingEvent event){
        try {
            if (!LocalizationModule.isModuleLoaded()) LocalizationModule.load(ConfigModule.LOCALIZATION);
        } catch (ModuleException e) {
            LocalizationModule.handleLoadFail(e);
        }
        if (!CommandsModule.isModuleLoaded()) CommandsModule.load(event);
        try {
            if (!DBConnectionModule.isModuleLoaded()) DBConnectionModule.load(ConfigModule.JDBC_CONNECTION_STRING);
        }catch (ModuleException e) {
            DBConnectionModule.handleLoadFail(e);
        }
    }
    public static void stoppingServerRegistries(FMLServerStoppingEvent event){
        if (CommandsModule.isModuleLoaded()) CommandsModule.unload();
        try {
            if (DBConnectionModule.isModuleLoaded()) DBConnectionModule.unload();
        }catch (ModuleException e) {
            throw new RuntimeException(e);
        }
        try {
            if (LocalizationModule.isModuleLoaded()) LocalizationModule.unload();
        }catch (ModuleException e) {
            throw new RuntimeException(e);
        }
    }
}