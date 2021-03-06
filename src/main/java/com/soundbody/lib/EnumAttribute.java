package com.soundbody.lib;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

import com.google.common.collect.Lists;
import com.soundbody.client.IRenderRate;
import com.soundbody.configuration.Constants;
import com.soundbody.modifiers.ModAttributes;
import com.soundbody.properties.ExtendedPropertyPlayer;

public enum EnumAttribute implements IRenderRate {
	maxHealth(SharedMonsterAttributes.maxHealth, "MaxHP", "health", Color.red, "soundbody.maxhealth", "beb90ee1-f19c-4847-9458-084634f381ee", 1),
	movementSpeed(SharedMonsterAttributes.movementSpeed, "Speed", "movespeed", Color.green, "soundbody.movespeed", "88c81e2b-a258-457b-92a4-64bd7dfd5607", 1),
	attackDamage(SharedMonsterAttributes.attackDamage, "Damage", "attackdamage", Color.blue, "soundbody.attackdamage", "b30c409a-f66a-4ee3-bb3c-cd2b679a3491", 1),
	digspeedFactor(ModAttributes.digspeedFactor, "DigSp.", "digspeed", Color.yellow, "soundbody.digspeed", "801bc4dd-5bd9-4ad7-b85d-be00b9aa33f4", 0),
	jumpFactor(ModAttributes.jumpFactor, "Jump", "jump", Color.lightGray, "soundbody.jump","06a19c5a-6687-4ee1-b9fb-5ddc3764fe84", 0);

	private final IAttribute attribute;
	private final String name;
	private final String textureName;
	private final int color;
	private final String modifierName;
	private final UUID modifierUUID;
	private final int operation;
	private double factor_pos;
	private double factor_neg;
	private boolean isEnabled = true;

	EnumAttribute(IAttribute attribute, String name, String textureName, Color color, String modifierName, String modifierUUIDStr, int operation) {
		this.attribute = attribute;
		this.name = name;
		this.textureName = textureName;
		this.color = color.getRGB();
		this.modifierName = modifierName;
		this.modifierUUID = UUID.fromString(modifierUUIDStr);
		this.operation = operation;

		this.factor_pos = this.factor_neg = (operation == 0) ? 1 / 40.0 : Math.log(1.5) / 20.0;
	}

	public void setFactorPos(double factor) {
		switch (this.operation) {
		case 0:
			this.factor_pos = (Constants.maxfitness == 0)? 0 : (factor - 1) / (double) Constants.maxfitness;
			break;
		case 1:
			this.factor_pos = (Constants.maxfitness == 0)? 1 : Math.log(factor) / (double) Constants.maxfitness;
			break;
		}
	}

	public void setFactorNeg(double factor) {
		switch (this.operation) {
		case 0:
			this.factor_neg = (Constants.minfitness == 0)? 0 : (1 - factor) / (double)(-Constants.minfitness);
			break;
		case 1:
			this.factor_neg = (Constants.minfitness == 0)? 1 : Math.log(1/factor)/(double)(-Constants.minfitness);
			break;
		}
	}

	public String getName() {
		return this.name;
	}

	public int getColor() {
		return this.color;
	}

	public ResourceLocation getGuiTexture() {
		return SBReferences.getResourceLocation("textures/gui/guiattributeicon_" + this.textureName + ".png");
	}

	public ResourceLocation getBarTexture() {
		return SBReferences.getResourceLocation("textures/gui/guibar_" + this.textureName + ".png");
	}

	public IAttributeInstance getAttributeInstance(EntityPlayer player) {
		return player.getEntityAttribute(this.attribute);
	}

	public void resetAttribute(EntityPlayer player, ExtendedPropertyPlayer property) {
		if(!this.isEnabled)
			return;
		
		double amount = property.getAmount(this.factor_pos, this.factor_neg, this.operation);

		this.getAttributeInstance(player).removeModifier(new AttributeModifier(this.modifierUUID, this.modifierName, 0, this.operation));
		this.getAttributeInstance(player).applyModifier(new AttributeModifier(this.modifierUUID, this.modifierName, amount, this.operation));
	}
	
	@Override
	public double getRate(EntityPlayer player, int exponent) {
		ExtendedPropertyPlayer property = (ExtendedPropertyPlayer) player.getExtendedProperties(Strings.extendedPropertiesKey);
		double amount = property.getAmount(this.factor_pos, this.factor_neg, this.operation);
		return ((this.operation == 0)? 1 + amount / attribute.getDefaultValue() : 1 + amount) * Math.pow(2.0, -exponent);
	}
	
	
	public static List<EnumAttribute> getEnabledAttributeList() {
		ArrayList<EnumAttribute> ret = Lists.newArrayList();
		
		for(EnumAttribute attribute : EnumAttribute.values())
			if(attribute.isEnabled)
				ret.add(attribute);
		
		return ret;
	}
	
	public double getRate(EntityPlayer player) {
		ExtendedPropertyPlayer property = (ExtendedPropertyPlayer) player.getExtendedProperties(Strings.extendedPropertiesKey);
		double amount = property.getAmount(this.factor_pos, this.factor_neg, this.operation);
		return (this.operation == 0) ? 1 + amount / attribute.getDefaultValue() : 1 + amount;
	}

}
