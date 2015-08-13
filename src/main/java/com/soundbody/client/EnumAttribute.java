package com.soundbody.client;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;

import com.soundbody.modifiers.ModAttributes;

public enum EnumAttribute {
	maxHealth(SharedMonsterAttributes.maxHealth, "MaxHP", 0xffff3333),
	movementSpeed(SharedMonsterAttributes.movementSpeed, "Speed", 0xff8080ff),
	attackDamage(SharedMonsterAttributes.attackDamage, "Damage", 0xffff8080),
	digspeedFactor(ModAttributes.digspeedFactor, "DigSp.", 0xff00ff00),
	jumpFactor(ModAttributes.jumpFactor, "Jump", 0xffdddddd);
	
	private IAttribute attribute;
	private String name;
	private int color;
	
	EnumAttribute(IAttribute attribute, String name, int color) {
		this.attribute = attribute;
		this.name = name;
		this.color = color;
	}
	
	public double getRate(EntityPlayer player) {
		IAttributeInstance instance = player.getEntityAttribute(attribute);
		return instance.getAttributeValue() / instance.getAttributeValue();
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getColor() {
		return this.color;
	}

}
