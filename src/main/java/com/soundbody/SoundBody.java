package com.soundbody;

import java.util.logging.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.soundbody.client.RenderFoodBarHandler;
import com.soundbody.foodstats.ModFoodStatsHandler;
import com.soundbody.lib.SBReferences;
import com.soundbody.modifiers.AttributeEventHandler;
import com.soundbody.properties.event.ExtendedPropertyEventHandler;

@Mod(modid = SBReferences.MODID, name = SBReferences.NAME, version = SBReferences.VERSION, canBeDeactivated = true)
public class SoundBody {
	
	@Instance(SBReferences.MODID)
	public static SoundBody instance;
	
	public static Logger logger = Logger.getLogger(SBReferences.MODID);
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    }
	
    @EventHandler
    public void init(FMLInitializationEvent event) throws NoSuchFieldException, SecurityException
    {
    	ModFoodStatsHandler.init();
    	
    	MinecraftForge.EVENT_BUS.register(new AttributeEventHandler());
    	MinecraftForge.EVENT_BUS.register(new ExtendedPropertyEventHandler());
    	MinecraftForge.EVENT_BUS.register(new ModFoodStatsHandler());
    	MinecraftForge.EVENT_BUS.register(new RenderFoodBarHandler());
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    }
    
}
