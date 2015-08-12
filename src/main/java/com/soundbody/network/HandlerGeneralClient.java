package com.soundbody.network;

import com.soundbody.lib.Strings;
import com.soundbody.properties.ExtendedPropertyPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HandlerGeneralClient implements IMessageHandler<PacketGeneralClient, PacketGeneralServer> {

	@Override
	public PacketGeneralServer onMessage(PacketGeneralClient message, MessageContext ctx) {
		switch (message.index) {
		case 0:
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			if (player != null) {
				ExtendedPropertyPlayer property = (ExtendedPropertyPlayer) Minecraft.getMinecraft().thePlayer.getExtendedProperties(Strings.extendedPropertiesKey);
				if (property != null) {
					property.sync(message);
				}
			}
			break;
		}
		return null;
	}
}
