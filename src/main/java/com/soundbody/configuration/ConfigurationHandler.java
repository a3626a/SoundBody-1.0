package com.soundbody.configuration;

import java.io.File;
import java.util.logging.Level;

import com.soundbody.SoundBody;
import com.soundbody.lib.EnumAttribute;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigurationHandler {

	private Configuration config;
	
	private static ConfigurationHandler instance;
	private static String CATEGORY_GENERAL = "GENERAL";
	private static String CATEGORY_MOD = "MODIFICATION";
	private static String KEY_FITNESS_MAX = "Max Fitenss";
	private static String KEY_FITNESS_MIN = "Min Fitness";
	private static String KEY_MOD_FOOD_MAX_POS = "Max Food on Max Fitness";
	private static String KEY_MOD_FOOD_MAX_NEG = "Max Food on Min Fitness";
	private static String KEY_MOD_FOOD_USAGE_POS = "Food Usage on Max Fitness";
	private static String KEY_MOD_FOOD_USAGE_NEG = "Food Usage on Min Fitness";
	private static String KEY_FITNESS_UPDATETICK = "Fitness Update Period in Minecraft Tick";
	private static String KEY_FITNESS_LOSSONDEATH = "Fitness Loos on Death";
	
	private static String KEY_MOD_POS = " on Min Fitness";
	private static String KEY_MOD_NEG = " on Min Fitness";
	
	public void init(FMLPreInitializationEvent event) {
		config = new Configuration(event.getModConfigurationDirectory());
		config.load();
	}
	
	private ConfigurationHandler() {}
	
	public static ConfigurationHandler getInstance() {
		if (instance==null) {
			instance = new ConfigurationHandler();
		}
		return instance;
	}

	public void sync() {
		Constants.maxfitness=config.get(CATEGORY_GENERAL, KEY_FITNESS_MAX, 20).getInt();
		if (Constants.maxfitness<0) {
			SoundBody.logger.log(Level.SEVERE, "Max Fitness must be Postitive. Automatically set to 0.");
			Constants.maxfitness=0;
		}
		Constants.minfitness=config.get(CATEGORY_GENERAL, KEY_FITNESS_MIN, -20).getInt();
		if (Constants.maxfitness>0) {
			SoundBody.logger.log(Level.SEVERE, "Min Fitness must be Negative. Automatically set to 0.");
			Constants.maxfitness=0;
		}
		Constants.fitnessLoss=config.get(CATEGORY_GENERAL, KEY_FITNESS_UPDATETICK, 5*60*20).getInt();
		
		Constants.fitnessCounter=config.get(CATEGORY_GENERAL, KEY_FITNESS_LOSSONDEATH, 2).getInt();
		if (Constants.fitnessCounter<0) {
			SoundBody.logger.log(Level.SEVERE, "Fitness Loos on Death must be Positive. Automatically set to 0.");
			Constants.fitnessCounter=0;
		}
		Constants.foodusage_pos=Math.log(config.get(CATEGORY_MOD, KEY_MOD_FOOD_USAGE_POS, 1.5).getDouble())/(double)Constants.maxfitness;
		Constants.foodusage_neg=Math.log(1/config.get(CATEGORY_MOD, KEY_MOD_FOOD_USAGE_NEG, 0.7).getDouble())/(double)(-Constants.minfitness);
		Constants.maxfood_pos=Math.log(config.get(CATEGORY_MOD, KEY_MOD_FOOD_MAX_POS, 1.5).getDouble())/(double)Constants.maxfitness;
		Constants.maxfood_neg=Math.log(1/config.get(CATEGORY_MOD, KEY_MOD_FOOD_MAX_NEG, 0.7).getDouble())/(double)(-Constants.minfitness);
		for (EnumAttribute i : EnumAttribute.values()) {
			i.setFactorPos(config.get(CATEGORY_MOD, i.getName()+KEY_MOD_POS, 1.4).getDouble());
			i.setFactorNeg(config.get(CATEGORY_MOD, i.getName()+KEY_MOD_NEG, 0.7).getDouble());
		}
		
		config.save();
	}
	
}
