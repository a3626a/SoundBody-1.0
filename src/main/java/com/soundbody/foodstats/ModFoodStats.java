package com.soundbody.foodstats;

import java.lang.reflect.Field;

import net.minecraft.util.FoodStats;

public class ModFoodStats extends FoodStats {

	private int maxFoodLevel = 40;
	private static Field foodLevel;
	private static Field foodSaturationLevel;
	
	public ModFoodStats() throws NoSuchFieldException, SecurityException {
		foodLevel = FoodStats.class.getDeclaredField("foodLevel");
		foodLevel.setAccessible(true);
		foodSaturationLevel = FoodStats.class.getDeclaredField("foodSaturationLevel");
		foodSaturationLevel.setAccessible(true);
	}
	
	@Override
	public void addStats(int foodLevelIn, float foodSaturationModifier) {
		try {
			foodLevel.set(this, Math.min(foodLevelIn + this.getFoodLevel(), maxFoodLevel));
			foodSaturationLevel.set(this, Math.min(this.getSaturationLevel() + (float)foodLevelIn * foodSaturationModifier * 2.0F, (float)this.getFoodLevel()));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean needFood() {
		return getFoodLevel() < maxFoodLevel;
	}
	
}
