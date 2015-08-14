package com.soundbody.modifiers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
			player.motionY *= Math.sqrt(player.getEntityAttribute(ModAttributes.jumpFactor).getAttributeValue());
		}
	}

}
