package com.soundbody.lib;

import net.minecraft.util.ResourceLocation;

public class SBReferences {

	public static final String MODID = "soundbody";
	public static final String NAME = "Sound Body";
	public static final String VERSION = "1.0";
	
	public static ResourceLocation getResourceLocation(String location) {
		return new ResourceLocation(MODID, location);
	}
}
