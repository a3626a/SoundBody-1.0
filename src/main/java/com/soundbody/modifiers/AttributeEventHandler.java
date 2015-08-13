package com.soundbody.modifiers;

import java.util.UUID;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.soundbody.lib.Strings;
import com.soundbody.properties.ExtendedPropertyPlayer;

public class AttributeEventHandler {

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			player.getAttributeMap().registerAttribute(ModAttributes.digspeedFactor);
			player.getAttributeMap().registerAttribute(ModAttributes.jumpFactor);
		}
	}

	@SubscribeEvent
	public void applyModifier(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			if (player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(UUID.fromString(Strings.modifier_movementSpeed_uuid)) == null)
				player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(new AttributeModifierFitness(UUID.fromString(Strings.modifier_movementSpeed_uuid), Strings.modifier_movespeed_name, 1, player));
			if (player.getEntityAttribute(SharedMonsterAttributes.maxHealth).getModifier(UUID.fromString(Strings.modifier_maxHealth_uuid)) == null)
				player.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifierFitness(UUID.fromString(Strings.modifier_maxHealth_uuid), Strings.modifier_maxHealth_name, 1, player));
			if (player.getEntityAttribute(SharedMonsterAttributes.attackDamage).getModifier(UUID.fromString(Strings.modifier_attackdamage_uuid)) == null)
				player.getEntityAttribute(SharedMonsterAttributes.attackDamage).applyModifier(new AttributeModifierFitness(UUID.fromString(Strings.modifier_attackdamage_uuid), Strings.modifier_attackdamage_name, 1, player));
			if (player.getEntityAttribute(ModAttributes.digspeedFactor).getModifier(UUID.fromString(Strings.modifier_digspeedFactor_uuid)) == null)
				player.getEntityAttribute(ModAttributes.digspeedFactor).applyModifier(new AttributeModifierFitness(UUID.fromString(Strings.modifier_digspeedFactor_uuid), Strings.modifier_digspeed_name, 0, player));
			if (player.getEntityAttribute(ModAttributes.jumpFactor).getModifier(UUID.fromString(Strings.modifier_jumpFactor_uuid)) == null)
				player.getEntityAttribute(ModAttributes.jumpFactor).applyModifier(new AttributeModifierFitness(UUID.fromString(Strings.modifier_jumpFactor_uuid), Strings.modifier_jump_name, 0, player));
		}
	}

	@SubscribeEvent
	public void attributeDigSpeed(BreakSpeed event) {
		event.newSpeed = (float) (event.originalSpeed * event.entityPlayer.getEntityAttribute(ModAttributes.digspeedFactor).getAttributeValue());
	}

	@SubscribeEvent
	public void attributeJump(LivingJumpEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			player.motionY *= player.getEntityAttribute(ModAttributes.jumpFactor).getAttributeValue();
		}
	}
}
