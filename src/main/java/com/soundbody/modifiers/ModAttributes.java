package com.soundbody.modifiers;

import java.lang.reflect.Field;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import com.soundbody.foodstats.ModFoodStats;
import com.soundbody.lib.Strings;

public class ModAttributes {

	public static IAttribute digspeedFactor = new RangedAttribute((IAttribute)null, Strings.attribute_digspeedfactor_name, 1.0, 0.0, Double.MAX_VALUE).setShouldWatch(true);
	public static IAttribute jumpFactor = new RangedAttribute((IAttribute)null, Strings.attribute_jumpfactor_name, 1.0, 0.0, Double.MAX_VALUE).setShouldWatch(true);
	static {
		try {
			((RangedAttribute)ObfuscationReflectionHelper.getPrivateValue(SharedMonsterAttributes.class, null, "attackDamage", "field_111264_e")).setShouldWatch(true);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
	}
}
