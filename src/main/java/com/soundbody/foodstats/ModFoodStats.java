package com.soundbody.foodstats;

import java.lang.reflect.Field;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;

public class ModFoodStats extends FoodStats {

	private int maxFoodLevel = 20;
	private EntityPlayer player;
	private static Field foodLevel;
	private static Field foodSaturationLevel;
	
	public ModFoodStats(EntityPlayer player, FoodStats foodStats) throws NoSuchFieldException, SecurityException {
		this.player = player;
		
		foodLevel = FoodStats.class.getDeclaredField("foodLevel");
		foodLevel.setAccessible(true);
		foodSaturationLevel = FoodStats.class.getDeclaredField("foodSaturationLevel");
		foodSaturationLevel.setAccessible(true);

		for (Field i : FoodStats.class.getDeclaredFields()) {
			try {
				i.setAccessible(true);
				i.set(this, i.get(foodStats));
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
		}
	}

	@Override
	public void addStats(int foodLevelIn, float foodSaturationModifier) {
		setFoodLevel(Math.min(foodLevelIn + this.getFoodLevel(), maxFoodLevel));
		setFoodSaturationLevel(Math.min(this.getSaturationLevel() + (float) foodLevelIn * foodSaturationModifier * 2.0F, (float) this.getFoodLevel()));
	}

	@Override
	public boolean needFood() {
		return getFoodLevel() < maxFoodLevel;
	}

	public int getMaxFoodLevel() {
		return maxFoodLevel;
	}

}
