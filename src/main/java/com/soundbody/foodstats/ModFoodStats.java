package com.soundbody.foodstats;

import java.lang.reflect.Field;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;

public class ModFoodStats extends FoodStats {

	private int maxFoodLevel = 40;
	private static Field foodLevel;
	private static Field foodSaturationLevel;
	
	public ModFoodStats(FoodStats foodStats) throws NoSuchFieldException, SecurityException {
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
	
	public int getMaxFoodLevel() {
		return maxFoodLevel;
	}
	
}
