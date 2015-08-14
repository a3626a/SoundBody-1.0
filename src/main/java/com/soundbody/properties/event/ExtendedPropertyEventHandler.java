package com.soundbody.properties.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.soundbody.configuration.Constants;
import com.soundbody.foodstats.ModFoodStats;
import com.soundbody.lib.Strings;
import com.soundbody.properties.ExtendedPropertyPlayer;

public class ExtendedPropertyEventHandler {

	@SubscribeEvent
	public void onEntityConing(PlayerEvent.Clone event) {
		ExtendedPropertyPlayer propertyOld = (ExtendedPropertyPlayer) event.original.getExtendedProperties(Strings.extendedPropertiesKey);
		ExtendedPropertyPlayer propertyNew = (ExtendedPropertyPlayer) event.entityPlayer.getExtendedProperties(Strings.extendedPropertiesKey);
		propertyNew.setClone(propertyOld);

		ModFoodStats oldStats = (ModFoodStats) event.original.getFoodStats();
		int oldFoodLevel = oldStats.getFoodLevel();
		int oldMaxFoodLevel = oldStats.getMaxFoodLevel();
		float oldSaturationLevel = oldStats.getSaturationLevel();
		int oldFitnessLevel = propertyOld.getFitness();

		FoodStats newStats = event.entityPlayer.getFoodStats();
		int newFitness = oldFitnessLevel;
		int newFoodLevel = oldFoodLevel;
		float newSaturationLevel = oldSaturationLevel;

		int diff = 0;
		
		if (oldFoodLevel < 0.5 * oldMaxFoodLevel) {
			diff = (int) (0.5 * oldMaxFoodLevel) - oldFoodLevel;
			newFoodLevel = (int)(0.5*oldMaxFoodLevel);
			newSaturationLevel = oldSaturationLevel;
		}
		
		newFitness -= Math.max(diff, Constants.fitnessLoss);
		
		newStats.setFoodLevel(newFoodLevel);
		newStats.setFoodSaturationLevel(newSaturationLevel);
		propertyNew.setFitness(oldFitnessLevel);
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			if (player.getExtendedProperties(Strings.extendedPropertiesKey) == null)
				player.registerExtendedProperties(Strings.extendedPropertiesKey, new ExtendedPropertyPlayer(player));
		}
	}

	@SubscribeEvent
	public void onLivingEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase entity = event.entityLiving;

		IExtendedEntityProperties property = entity.getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null) {
			((ExtendedPropertyPlayer) property).update();
		}
	}
}
