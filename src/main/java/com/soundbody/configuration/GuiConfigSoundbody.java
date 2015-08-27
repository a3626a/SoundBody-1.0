package com.soundbody.configuration;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import com.google.common.collect.Lists;
import com.soundbody.lib.SBReferences;

public class GuiConfigSoundbody extends GuiConfig {
	
	public GuiConfigSoundbody(GuiScreen parent) {
		super(parent, getElement(), SBReferences.MODID, false, false, SBReferences.NAME);
	}
	
	private static List<IConfigElement> getElement() {
		List<IConfigElement> ret = Lists.newArrayList();
		Configuration config = ConfigurationHandler.getInstance().getConfiguration();
		for(String name : config.getCategoryNames())
			ret.add(new ConfigElement(config.getCategory(name)));
		return ret;
	}

}
