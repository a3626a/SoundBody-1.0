package com.soundbody.modifiers;

import java.lang.reflect.Field;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

import com.soundbody.lib.Strings;

public class ModAttributes {

	public static IAttribute digspeedFactor = new RangedAttribute((IAttribute)null, Strings.attribute_digspeedfactor_name, 1.0, 0.0, Double.MAX_VALUE).setShouldWatch(true);
	public static IAttribute jumpFactor = new RangedAttribute((IAttribute)null, Strings.attribute_jumpfactor_name, 1.0, 0.0, Double.MAX_VALUE).setShouldWatch(true);
	static {
		Field attackDamage;
		try {
			attackDamage = SharedMonsterAttributes.class.getDeclaredField("attackDamage");
			attackDamage.setAccessible(true);
			((RangedAttribute)attackDamage.get(null)).setShouldWatch(true);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
}
