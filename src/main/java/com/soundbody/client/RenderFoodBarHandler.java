package com.soundbody.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.soundbody.foodstats.ModFoodStats;
import com.soundbody.lib.EnumAttribute;
import com.soundbody.modifiers.ModAttributes;

public class RenderFoodBarHandler extends Gui {

	public static int left_height = 39;
	public static int right_height = 39;

	@SubscribeEvent
	public void renderFoodBar(RenderGameOverlayEvent.Pre event) {
		if (event.type == ElementType.FOOD) {
			event.setCanceled(true);

			right_height = 39;
			left_height = 39;

			Minecraft mc = Minecraft.getMinecraft();
			int width = event.resolution.getScaledWidth();
			int height = event.resolution.getScaledHeight();

			mc.mcProfiler.startSection("food");

			GlStateManager.enableBlend();
			int left = width / 2 + 91;
			int top = height - right_height;
			right_height += 10;
			boolean unused = false;// Unused flag in vanilla, seems to be part
									// of a 'fade out' mechanic

			FoodStats stats = mc.thePlayer.getFoodStats();
			ModFoodStats modStats = null;
			if (stats instanceof ModFoodStats) {
				modStats = (ModFoodStats) stats;
			}

			int level = stats.getFoodLevel();
			int levelLast = stats.getPrevFoodLevel();
			
			int cell = 10;
			if (modStats != null)
				cell = (modStats.getMaxFoodLevel() + 1) / 2;
			
			int rowNum = MathHelper.ceiling_double_int(cell / 10.0);
			
	        int rowHeight = Math.max(10 - (rowNum - 2), 3);

			GuiIngameForge.right_height += (rowNum * rowHeight);
	        if (rowHeight != 10) right_height += 10 - rowHeight;

			for (int i = 0; i < cell; ++i) {
				int idx = i * 2 + 1;
				int x = left - (i % 10) * 8 - 9;
				int y = top - rowHeight * (i / 10);
				int icon = 16;
				byte backgound = 0;

				if (mc.thePlayer.isPotionActive(Potion.hunger)) {
					icon += 36;
					backgound = 13;
				}
				if (unused)
					backgound = 1; // Probably should be a += 1 but vanilla
									// never uses this

				/*
				 * if (player.getFoodStats().getSaturationLevel() <= 0.0F &&
				 * updateCounter % (level * 3 + 1) == 0) { y = top +
				 * (rand.nextInt(3) - 1); }
				 */
				// This code make 'jumping' icons. but it is very difficult to
				// define updateCounter... should do

				drawTexturedModalRect(x, y, 16 + backgound * 9, 27, 9, 9);

				if (unused) {
					if (idx < levelLast)
						drawTexturedModalRect(x, y, icon + 54, 27, 9, 9);
					else if (idx == levelLast)
						drawTexturedModalRect(x, y, icon + 63, 27, 9, 9);
				}

				if (idx < level)
					drawTexturedModalRect(x, y, icon + 36, 27, 9, 9);
				else if (idx == level)
					drawTexturedModalRect(x, y, icon + 45, 27, 9, 9);
			}
			GlStateManager.disableBlend();
			mc.mcProfiler.endSection();
		}
	}

	@SubscribeEvent
	public void renderFoodAttributeBar(RenderGameOverlayEvent.Post event) {
		if (event.type == ElementType.ALL) {
			Minecraft mc = Minecraft.getMinecraft();
			EntityPlayer player = mc.thePlayer;
			
			this.drawAttributebarAt(player.getEntityAttribute(SharedMonsterAttributes.movementSpeed), mc, "Speed", 0, 0, 50, 8, 0xff8080ff);
			this.drawAttributebarAt(player.getEntityAttribute(SharedMonsterAttributes.attackDamage), mc, "Damage", 0, 8, 50, 8, 0xffff8080);
			this.drawAttributebarAt(player.getEntityAttribute(ModAttributes.digspeedFactor), mc, "DigSp", 0, 16, 50, 8, 0xff00ff00);
			this.drawAttributebarAt(player.getEntityAttribute(ModAttributes.jumpFactor), mc, "Jump", 0, 24, 50, 8, 0xffdddddd);
			
			double centerX = 50.0;
			double centerY = 50.0;
			double radius = 30.0;
			double angle = 0.0;
			double x = 0.0, y = 0.0;
			double preX = 0.0, preY = 0.0;
			double rate;
			boolean flag = false;
						
			for(EnumAttribute attribute : EnumAttribute.values()) {
				rate = attribute.getRate(player);

				x = centerX + rate * radius * Math.cos(angle);
				y = centerY + rate * radius * Math.sin(angle);
								
				angle += 2 * Math.PI / EnumAttribute.values().length;
								
				if(flag)
					drawLine(preX, preY, x, y, 0xffffffff);
				
				preX = x;
				preY = y;
				
				flag = true;
			}
			
			EnumAttribute attribute2 = EnumAttribute.values()[0];
			rate = attribute2.getRate(player);
			x = centerX + rate * radius * Math.cos(angle);
			y = centerY + rate * radius * Math.sin(angle);
			
			drawLine(preX, preY, x, y, 0xffffffff);
			
			
			for(EnumAttribute attribute : EnumAttribute.values()) {
				rate = attribute.getRate(player);
				
				x = centerX + rate * (radius+2.0) * Math.cos(angle);
				y = centerY + rate * (radius+2.0) * Math.sin(angle);
				
				drawRect((int)(x-3), (int)(y-3), (int)(x+3), (int)(y+3), attribute.getColor());
				this.drawCenteredString(mc.fontRendererObj, attribute.getName(), (int)x, (int)y, attribute.getColor());
				
				angle += 2 * Math.PI / EnumAttribute.values().length;
			}
		}
	}
	
	public void drawAttributebarAt(IAttributeInstance attribute, Minecraft mc, String text, int left, int top, int width, int height, int color) {
		double rate = attribute.getAttributeValue() / attribute.getBaseValue();
		this.drawString(mc.fontRendererObj, text, left, top, color);
		left += 20;
		drawRect(left, top, left + (int) (width * rate), top + height, color);
		drawRect(left + width - 1, top, left + width + 1, top + height, 0x80000000);
	}
	
	public void drawLine(double x1, double y1, double x2, double y2, int color) {
        double xd = y1 - y2;
        double yd = x2 - x1;
        double dist = Math.sqrt(xd * xd + yd * yd);
        xd /= dist;
        yd /= dist;
        
        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
		
		Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.startDrawingQuads();
        worldrenderer.addVertex(x1 + xd, y1 + yd, 0.0D);
        worldrenderer.addVertex(x2 + xd, y2 + yd, 0.0D);
        worldrenderer.addVertex(x2 - xd, y2 - yd, 0.0D);
        worldrenderer.addVertex(x1 - xd, y1 - yd, 0.0D);
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
	}
}
