package com.soundbody.modifiers;

import java.util.UUID;

import com.soundbody.lib.Strings;
import com.soundbody.properties.ExtendedPropertyPlayer;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;

public class AttributeModifierFitness extends AttributeModifier {

	private EntityPlayer player;
	
	public AttributeModifierFitness(String p_i1606_2_, EntityPlayer player) {
		super(UUID.randomUUID(), p_i1606_2_, 0, 2);
		this.player = player;
	}

	public ExtendedPropertyPlayer getProperty() {
		ExtendedPropertyPlayer property = (ExtendedPropertyPlayer) player.getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null) {
			return property;
		} else {
			property = new ExtendedPropertyPlayer(this.player);
			player.registerExtendedProperties(Strings.extendedPropertiesKey, property);
			return property;
		}
	}
	
	@Override
	public double getAmount() {
		
		return super.getAmount();
	}
	
}
