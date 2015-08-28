package com.soundbody.foodstats;

import java.lang.reflect.Field;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ModFoodStatsHandler {
	
	private static Field foodStats;
	
	public static void init() {
		foodStats = ReflectionHelper.findField(EntityPlayer.class, ObfuscationReflectionHelper.remapFieldNames(EntityPlayer.class.getName(), "foodStats", "field_71100_bB"));
	}
	
	@SubscribeEvent
	public void onEntityConstructing(EntityJoinWorldEvent event) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.entity;
			foodStats.set(player, new ModFoodStats(player, player.getFoodStats()));
		}
	}
	
}
