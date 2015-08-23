package com.soundbody.client;

import net.minecraft.entity.player.EntityPlayer;

public interface IRenderRate {

	public double getRate(EntityPlayer player, int exponent);
}
