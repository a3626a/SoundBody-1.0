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
