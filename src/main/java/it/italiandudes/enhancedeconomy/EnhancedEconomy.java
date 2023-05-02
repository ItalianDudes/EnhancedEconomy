package it.italiandudes.enhancedeconomy;

import it.italiandudes.enhancedeconomy.handler.RegistryHandler;
import it.italiandudes.enhancedeconomy.proxy.CommonProxy;
import it.italiandudes.enhancedeconomy.util.Defs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@SuppressWarnings("unused")
@Mod(modid = Defs.ModInfo.MOD_ID, useMetadata = true)
public final class EnhancedEconomy {

    // Mod Instance
    @Mod.Instance(Defs.ModInfo.MOD_ID)
    public static EnhancedEconomy INSTANCE;

    @SidedProxy(clientSide = Defs.CLIENT, serverSide = Defs.COMMON)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        RegistryHandler.preInitRegistries(event);
    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        RegistryHandler.initRegistries(event);
    }
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        RegistryHandler.postInitRegistries(event);
    }
    @Mod.EventHandler
    public void serverinit(FMLServerStartingEvent event){
        RegistryHandler.serverRegistries(event);
    }
}