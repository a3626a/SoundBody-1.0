package com.soundbody.foodstats;

import java.lang.reflect.Field;

import scala.collection.script.Update;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;

import com.soundbody.lib.Strings;
import com.soundbody.properties.ExtendedPropertyPlayer;

public class ModFoodStats extends FoodStats {

	private EntityPlayer player;
	private ExtendedPropertyPlayer property;
	private static Field foodLevel;
	private static Field foodSaturationLevel;
	private static double factor = Math.log(2) / 20.0;

	public ModFoodStats(EntityPlayer player, FoodStats foodStats) throws NoSuchFieldException, SecurityException {
		this.player = player;
		property = (ExtendedPropertyPlayer) this.player.getExtendedProperties(Strings.extendedPropertiesKey);

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
		setFoodLevel(Math.min(foodLevelIn + this.getFoodLevel(), getMaxFoodLevel()));
		setFoodSaturationLevel(Math.min(this.getSaturationLevel() + (float) foodLevelIn * foodSaturationModifier * 2.0F, (float) this.getFoodLevel()));
	}

	@Override
	public boolean needFood() {
		return getFoodLevel() < getMaxFoodLevel();
	}

	@Override
	public void addExhaustion(float p_75113_1_) {
		super.addExhaustion((float) (p_75113_1_ * Math.exp(factor * property.getFitness())));
	}

	@Override
	public void onUpdate(EntityPlayer player) {
		super.onUpdate(player);
		if (player.worldObj.getWorldTime() % 60 == 0) {
			System.out.println(getFoodLevel() + " / " + getMaxFoodLevel());
			System.out.println(getSaturationLevel() + " / " + getFoodLevel());
			System.out.println(getFoodLevel() + " : " + property.getFitness());
		}
	}

	public int getMaxFoodLevel() {
		return (int) (20*Math.exp(factor * property.getFitness()));
	}
}
