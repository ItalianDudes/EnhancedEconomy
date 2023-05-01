package it.italiandudes.enhancedeconomy;

import it.italiandudes.enhancedeconomy.handler.RegistryHandler;
import it.italiandudes.enhancedeconomy.proxy.CommonProxy;
import it.italiandudes.enhancedeconomy.util.Defs;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.File;

@Mod(modid = Defs.MOD_ID, name = Defs.MOD_NAME, version = Defs.VERSION)
public class EnhancedEconomy {

    //Attributes
    public static File configs;

    @Mod.Instance(Defs.MOD_ID)
    public static EnhancedEconomy INSTANCE;

    @SidedProxy(clientSide = Defs.CLIENT, serverSide = Defs.COMMON) @SuppressWarnings("unused")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        RegistryHandler.preInitRegistries(event);
    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        RegistryHandler.initRegistries();
    }
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        RegistryHandler.postInitRegistries();
    }
    @Mod.EventHandler
    public void serverRegistries(FMLServerStartingEvent event){
        RegistryHandler.serverRegistries(event);
    }
}
