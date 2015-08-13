package com.soundbody.properties.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.soundbody.lib.Strings;
import com.soundbody.properties.ExtendedPropertyPlayer;

public class ExtendedPropertyEventHandler {

	@SubscribeEvent
	public void onEntityConing(PlayerEvent.Clone event) {
		ExtendedPropertyPlayer propertyOld = (ExtendedPropertyPlayer) event.original.getExtendedProperties(Strings.extendedPropertiesKey);
		ExtendedPropertyPlayer propertyNew = (ExtendedPropertyPlayer) event.entityPlayer.getExtendedProperties(Strings.extendedPropertiesKey);
		propertyNew.set(propertyOld);
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			if (player.getExtendedProperties(Strings.extendedPropertiesKey) == null)
				player.registerExtendedProperties(Strings.extendedPropertiesKey, new ExtendedPropertyPlayer(player));
		}
	}

	@SubscribeEvent
	public void onLivingEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase entity = event.entityLiving;

		IExtendedEntityProperties property = entity.getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null) {
			((ExtendedPropertyPlayer) property).update();
		}
	}
}
