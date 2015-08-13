package com.soundbody.foodstats;

import java.lang.reflect.Field;

import scala.collection.script.Update;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.world.EnumDifficulty;

import com.soundbody.lib.Strings;
import com.soundbody.properties.ExtendedPropertyPlayer;

public class ModFoodStats extends FoodStats {

	private EntityPlayer player;
	private ExtendedPropertyPlayer property;
	private static Field prevFoodLevel;
	private static Field foodExhaustionLevel;
	private static Field foodTimer;
	private static double factor = Math.log(2) / 20.0;

	public ModFoodStats(EntityPlayer player, FoodStats foodStats) throws NoSuchFieldException, SecurityException {
		this.player = player;
		property = (ExtendedPropertyPlayer) this.player.getExtendedProperties(Strings.extendedPropertiesKey);

		prevFoodLevel = FoodStats.class.getDeclaredField("prevFoodLevel");
		prevFoodLevel.setAccessible(true);
		foodExhaustionLevel = FoodStats.class.getDeclaredField("foodExhaustionLevel");
		foodExhaustionLevel.setAccessible(true);
		foodTimer = FoodStats.class.getDeclaredField("foodTimer");
		foodTimer.setAccessible(true);
		
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

		EnumDifficulty enumdifficulty = player.worldObj.getDifficulty();
		setPrevFoodLevel(this.getFoodLevel());

        if (getFoodExaustionLevel() > 4.0F)
        {
        	setFoodExaustionLevel(getFoodExaustionLevel()-4.0F);

            if (getSaturationLevel() > 0.0F)
            {
            	setFoodSaturationLevel(Math.max(getSaturationLevel() - 1.0F, 0.0F));
            }
            else if (enumdifficulty != EnumDifficulty.PEACEFUL)
            {
            	setFoodLevel(Math.max(getFoodLevel() - 1, 0));
            }
        }

        if (player.worldObj.getGameRules().getGameRuleBooleanValue("naturalRegeneration") && getFoodLevel() >= getMaxFoodLevel()*0.9 && player.shouldHeal())
        {
           incrementFoodTimer();

            if (getFoodTimer() >= 80)
            {
                player.heal(1.0F);
                this.addExhaustion(3.0F);
                initFoodTimer();
            }
        }
        else if (getFoodLevel() <= 0)
        {
        	incrementFoodTimer();

            if (getFoodTimer() >= 80)
            {
                if (player.getHealth() > 10.0F || enumdifficulty == EnumDifficulty.HARD || player.getHealth() > 1.0F && enumdifficulty == EnumDifficulty.NORMAL)
                {
                    player.attackEntityFrom(DamageSource.starve, 1.0F);
                }

                initFoodTimer();
            }
        }
        else
        {
        	initFoodTimer();
        }
		
		if (player.worldObj.getWorldTime() % 60 == 0) {
			System.out.println(getFoodLevel() + " / " + getMaxFoodLevel());
			System.out.println(getSaturationLevel() + " / " + getFoodLevel());
			System.out.println(getFoodLevel() + " : " + property.getFitness());
		}
	}

	public int getMaxFoodLevel() {
		return (int) (20*Math.exp(factor * property.getFitness()));
	}
	
	public void setPrevFoodLevel(int level) {
		try {
			prevFoodLevel.setInt(this, level);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public void setFoodExaustionLevel(float level) {
		try {
			foodExhaustionLevel.setFloat(this, level);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public float getFoodExaustionLevel() {
		try {
			return foodExhaustionLevel.getFloat(this);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return 0.0F;
	}
	
	public void incrementFoodTimer() {
		try {
			foodTimer.setInt(this, getFoodTimer()+1);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void initFoodTimer() {
		try {
			foodTimer.setInt(this, 0);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public int getFoodTimer() {
		try {
			return foodTimer.getInt(this);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
