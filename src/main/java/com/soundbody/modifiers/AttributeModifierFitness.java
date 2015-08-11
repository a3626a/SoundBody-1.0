package com.soundbody.modifiers;

import java.util.UUID;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;

public class AttributeModifierFitness extends AttributeModifier {

	private EntityPlayer player;
	
	public AttributeModifierFitness(String p_i1606_2_, EntityPlayer player) {
		super(UUID.randomUUID(), p_i1606_2_, 0, 2);
		this.player = player;
	}

	@Override
	public double getAmount() {
		return super.getAmount();
	}
	
}
