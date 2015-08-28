package com.soundbody.foodstats;

import java.lang.reflect.Field;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ModFoodStatsHandler {
	
	@SubscribeEvent
	public void onEntityConstructing(EntityJoinWorldEvent event) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.entity;
			ObfuscationReflectionHelper.setPrivateValue(EntityPlayer.class, player, new ModFoodStats(player, player.getFoodStats()), "foodStats");
		}
	}
	
}
