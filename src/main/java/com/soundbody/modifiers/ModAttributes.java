package com.soundbody.modifiers;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

import com.soundbody.lib.Strings;

public class ModAttributes {

	public static IAttribute digspeedFactor = new RangedAttribute((IAttribute)null, Strings.attribute_digspeedfactor_name, 10.0, 0.0, Double.MAX_VALUE).setShouldWatch(true);
	public static IAttribute jumpFactor = new RangedAttribute((IAttribute)null, Strings.attribute_jumpfactor_name, 1.0, 0.0, Double.MAX_VALUE).setShouldWatch(true);
	
}
