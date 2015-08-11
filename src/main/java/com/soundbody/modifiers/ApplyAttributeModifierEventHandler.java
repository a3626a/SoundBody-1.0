package com.soundbody.modifiers;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.soundbody.lib.Strings;

public class ApplyAttributeModifierEventHandler {
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.entity;
			player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(new AttributeModifierFitness(Strings.modifier_movespeed_name, player));
		}
	}
}
