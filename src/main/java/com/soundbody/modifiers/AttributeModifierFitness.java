package com.soundbody.modifiers;

import java.util.UUID;

import com.soundbody.lib.Strings;
import com.soundbody.properties.ExtendedPropertyPlayer;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;

public class AttributeModifierFitness extends AttributeModifier {

	private EntityPlayer player;
	private ExtendedPropertyPlayer property;
	private static double factor = Math.log(1.5) / 20.0;

	public AttributeModifierFitness(UUID uuid, String p_i1606_2_, int operation, EntityPlayer player) {
		super(uuid, p_i1606_2_, 0, operation);
		this.player = player;
		property = (ExtendedPropertyPlayer) player.getExtendedProperties(Strings.extendedPropertiesKey);
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
		switch (this.getOperation()) {
		case 0:
			return property.getFitness() / 100;
		case 1:
			return Math.exp(factor * property.getFitness()) - 1;
		}
		return super.getAmount();
	}

}
