package com.buuz135.sushigocrafting;

import com.buuz135.sushigocrafting.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = SushiGoCrafting.MOD_ID,
        name = SushiGoCrafting.MOD_NAME,
        version = SushiGoCrafting.VERSION
)
public class SushiGoCrafting {

    public static final String MOD_ID = "sushigocrafting";
    public static final String MOD_NAME = "SushiGoCrafting";
    public static final String VERSION = "1.0-SNAPSHOT";

    @Mod.Instance(MOD_ID)
    public static SushiGoCrafting INSTANCE;
    @SidedProxy(clientSide = "com.buuz135.sushigocrafting.proxy.client.ClientProxy", serverSide = "com.buuz135.sushigocrafting.proxy.CommonProxy")
    private static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

}
