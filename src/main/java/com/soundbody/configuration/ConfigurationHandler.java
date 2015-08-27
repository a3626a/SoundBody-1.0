package com.soundbody.configuration;

import java.util.logging.Level;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.soundbody.SoundBody;
import com.soundbody.lib.EnumAttribute;
import com.soundbody.lib.SBReferences;

public class ConfigurationHandler {

	private Configuration config;
	
	private static ConfigurationHandler instance;
	private static String CATEGORY_GENERAL = "GENERAL";
	private static String CATEGORY_MOD = "MODIFICATION";
	private static String CATEGORY_GUI = "GRAPHIC USER INTERFACE(GUI)";
	private static String KEY_FITNESS_MAX = "Max Fitenss";
	private static String KEY_FITNESS_MIN = "Min Fitness";
	private static String KEY_MOD_FOOD_MAX_POS = "Max Food on Max Fitness";
	private static String KEY_MOD_FOOD_MAX_NEG = "Max Food on Min Fitness";
	private static String KEY_MOD_FOOD_USAGE_POS = "Food Usage on Max Fitness";
	private static String KEY_MOD_FOOD_USAGE_NEG = "Food Usage on Min Fitness";
	private static String KEY_FITNESS_UPDATETICK = "Fitness Update Period in Minecraft Tick";
	private static String KEY_FITNESS_LOSSONDEATH = "Fitness Loss on Death";
	
	private static String KEY_MOD_POS = " on Max Fitness";
	private static String KEY_MOD_NEG = " on Min Fitness";
	
	private static String KEY_BAR_ENABLED = "Enable Attribute Bar GUI";
	private static String KEY_POLYGON_ENABLED = "Enable Attribute Polygon GUI";
	
	public void init(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
	}
	
	private ConfigurationHandler() {}
	
	public static ConfigurationHandler getInstance() {
		if (instance==null) {
			instance = new ConfigurationHandler();
		}
		return instance;
	}
	
	public Configuration getConfiguration() {
		return this.config;
	}

	public void sync() {
		Constants.maxfitness=getPropReqRestart(CATEGORY_GENERAL, KEY_FITNESS_MAX, 20).getInt();
		if (Constants.maxfitness<0) {
			SoundBody.logger.log(Level.SEVERE, "Max Fitness must be Postitive. Automatically set to 0.");
			Constants.maxfitness=0;
		}
		Constants.minfitness=getPropReqRestart(CATEGORY_GENERAL, KEY_FITNESS_MIN, -20).getInt();
		if (Constants.minfitness>0) {
			SoundBody.logger.log(Level.SEVERE, "Min Fitness must be Negative. Automatically set to 0.");
			Constants.minfitness=0;
		}
		Constants.fitnessLoss=getPropReqRestart(CATEGORY_GENERAL, KEY_FITNESS_LOSSONDEATH, 2).getInt();
		
		Constants.fitnessCounter=getPropReqRestart(CATEGORY_GENERAL, KEY_FITNESS_UPDATETICK, 5*60*20).getInt();
		if (Constants.fitnessCounter<0) {
			SoundBody.logger.log(Level.SEVERE, "Fitness Counter must be Positive. Automatically set to 0.");
			Constants.fitnessCounter=0;
		}
		Constants.foodusage_pos=Math.log(getPropReqRestart(CATEGORY_MOD, KEY_MOD_FOOD_USAGE_POS, 1.5).getDouble())/(double)Constants.maxfitness;
		Constants.foodusage_neg=Math.log(1/getPropReqRestart(CATEGORY_MOD, KEY_MOD_FOOD_USAGE_NEG, 0.7).getDouble())/(double)(-Constants.minfitness);
		Constants.maxfood_pos=Math.log(getPropReqRestart(CATEGORY_MOD, KEY_MOD_FOOD_MAX_POS, 1.5).getDouble())/(double)Constants.maxfitness;
		Constants.maxfood_neg=Math.log(1/getPropReqRestart(CATEGORY_MOD, KEY_MOD_FOOD_MAX_NEG, 0.7).getDouble())/(double)(-Constants.minfitness);
		for (EnumAttribute i : EnumAttribute.values()) {
			i.setFactorPos(config.get(CATEGORY_MOD, i.getName()+KEY_MOD_POS, 1.4).getDouble());
			i.setFactorNeg(config.get(CATEGORY_MOD, i.getName()+KEY_MOD_NEG, 0.7).getDouble());
		}

		Constants.guiBarEnabled = config.get(CATEGORY_GUI, KEY_BAR_ENABLED, false).getBoolean();
		Constants.guiPolygonEnabled = config.get(CATEGORY_GUI, KEY_POLYGON_ENABLED, true).getBoolean();
		
		config.save();
	}
	
	private Property getPropReqRestart(String category, String key, int defaultValue) {
		return config.get(category, key, defaultValue).setRequiresWorldRestart(true);
	}
	
	private Property getPropReqRestart(String category, String key, double defaultValue) {
		return config.get(category, key, defaultValue).setRequiresWorldRestart(true);
	}
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if(event.modID == SBReferences.MODID) {
			this.sync();
		}
	}
	
}
