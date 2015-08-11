package com.soundbody.modifiers;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.soundbody.lib.Strings;

public class ApplyAttributeModifierEventHandler {
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.entity;
			player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(new AttributeModifierFitness(Strings.modifier_movespeed_name, player));
			player.getEntityAttribute(SharedMonsterAttributes.attackDamage).applyModifier(new AttributeModifierFitness(Strings.modifier_attackdamage_name, player));
			player.getAttributeMap().registerAttribute(ModAttributes.digspeed);
			player.getAttributeMap().registerAttribute(ModAttributes.jump);
			player.getEntityAttribute(ModAttributes.digspeed).applyModifier(new AttributeModifierFitness(Strings.modifier_digspeed_name, player));
			player.getEntityAttribute(ModAttributes.jump).applyModifier(new AttributeModifierFitness(Strings.modifier_jump_name, player));
		}
	}
}
