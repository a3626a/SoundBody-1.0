package com.soundbody.properties.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
		newStats.setFoodLevel(oldFoodLevel);
		newStats.setFoodSaturationLevel(oldSaturationLevel);
		if (oldFoodLevel < 0.5 * oldMaxFoodLevel) {
			int diff = (int) (0.5 * oldMaxFoodLevel) - oldFoodLevel;
			newStats.setFoodLevel((int)(0.5*oldMaxFoodLevel));
			newStats.setFoodSaturationLevel(oldSaturationLevel);
			oldFitnessLevel-=diff;
		}
		oldFitnessLevel-=ExtendedPropertyPlayer.fitnessLossOnDeath;
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
