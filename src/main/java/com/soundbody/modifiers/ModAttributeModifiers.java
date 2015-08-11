package com.soundbody.modifiers;

import net.minecraft.entity.ai.attributes.AttributeModifier;

public class ModAttributeModifiers {

	public static AttributeModifierFitness movespeed;
	public static AttributeModifierFitness digspeed;
	public static AttributeModifierFitness attackdamage;
	public static AttributeModifierFitness jump;
	
	public void init() {
		fitness_movespeed = new AttributeModifier();
	}
	
}
