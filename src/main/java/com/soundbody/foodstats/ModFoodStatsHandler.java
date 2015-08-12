package com.soundbody.foodstats;

import java.lang.reflect.Field;

import com.soundbody.modifiers.ModAttributes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModFoodStatsHandler {
	
	private static Field foodstats;
	
	public static void init() throws NoSuchFieldException, SecurityException {
		foodstats = EntityPlayer.class.getDeclaredField("foodStats");
		foodstats.setAccessible(true);
	}
	
	@SubscribeEvent
	public void onEntityConstructing(EntityJoinWorldEvent event) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.entity;
			foodstats.set(player, new ModFoodStats(player.getFoodStats()));
		}
	}
}
