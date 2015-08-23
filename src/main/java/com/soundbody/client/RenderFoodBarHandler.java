package com.soundbody.client;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.google.common.collect.Lists;
import com.soundbody.foodstats.ModFoodStats;
import com.soundbody.lib.EnumAttribute;
import com.soundbody.lib.SBReferences;

public class RenderFoodBarHandler extends Gui {

	public static int left_height = 39;
	public static int right_height = 39;
	private static ResourceLocation backgroundTexture = SBReferences.getResourceLocation("textures/gui/guibackground_cir.png");

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
			
			float tick = mc.theWorld.getWorldTime() + event.partialTicks;
			
			float rotationAngle = (float) (- Math.PI / 2 + tick / 20.0);
			
			double maxRate = 0.0;
			List<EnumAttribute> attributeList = EnumAttribute.getEnabledAttributeList();
			
			if(attributeList.isEmpty())
				return;
			
			for(EnumAttribute attribute : attributeList)
				maxRate = Math.max(maxRate, attribute.getRate(player, 0));
			
			int exponent = Math.getExponent(maxRate);
			
			this.renderAttributeBars(player, mc, attributeList, exponent, 0, 0, 50, 8);
			this.renderAttributePolygon(player, mc, attributeList, exponent, 50.0, 90.0, 40.0);
		}
	}

	private void renderAttributeBars(EntityPlayer player, Minecraft mc, List<EnumAttribute> attributeList, int exponent, int left, int top, int width, int height) {
		int left2 = left + 35;
		
		for(EnumAttribute attribute : attributeList) {
			this.drawCenteredString(mc.fontRendererObj, attribute.getName(), left + 17, top, attribute.getColor());
			
			mc.renderEngine.bindTexture(attribute.getBarTexture());
			this.drawTexturedRect(left2, top, (int) (width * attribute.getRate(player, exponent)), height);
			
			drawRect(left2 + width - 1, top, left2 + width + 1, top + height, 0x80000000);
			//drawRect(left2 + 2*width - 1, top, left2 + 2*width + 1, top + height, 0xa0000000);
			top += height;
		}
	}
	
	private void drawTexturedRect(int x, int y, int width, int height) {
		this.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
	}
	
	private void renderAttributePolygon(EntityPlayer player, Minecraft mc, List<EnumAttribute> attributeList, int exponent, double centerX, double centerY, double radius) {
		double angle = -Math.PI / 2;
		double x = 0.0, y = 0.0;
		double x2, y2;
		double rate, maxRate = 0.0;
		radius /= 2;
		
		List<IRenderRate> standardLineList = Lists.newArrayList();
		List<IRenderRate> standardLineList2 = Lists.newArrayList();
		
		for(int i = 0; i < attributeList.size(); i++) {
			standardLineList.add(new IRenderRate() {
				@Override
				public double getRate(EntityPlayer player, int exponent) {
					return 1.0;
				}
			});
		}
		
		for(int i = 0; i < attributeList.size(); i++) {
			standardLineList2.add(new IRenderRate() {
				@Override
				public double getRate(EntityPlayer player, int exponent) {
					return 2.0;
				}
			});
		}
		
		GlStateManager.color(1.0f, 1.0f, 1.0f, 0.5f);
		mc.renderEngine.bindTexture(this.backgroundTexture);
		this.drawTexturedRect((int)(centerX - radius * 2.4), (int)(centerY - radius * 2.4), (int)(radius * 4.8), (int)(radius * 4.8));
		
		this.drawPolygon(attributeList, player, centerX, centerY, radius, exponent, 0xffffffff);
		this.drawPolygon(standardLineList, player, centerX, centerY, radius, exponent, 0xddaaaaaa);
		this.drawPolygon(standardLineList2, player, centerX, centerY, radius, exponent, 0xdd555555);
		
		for(EnumAttribute attribute : attributeList) {
			rate = attribute.getRate(player, exponent);
			
			x2 = centerX + 1.9 * radius * Math.cos(angle);
			y2 = centerY + 1.9 * radius * Math.sin(angle);
			
			this.drawLine(centerX, centerY, x2, y2, 0xddcccccc);
			
			x = centerX + rate * (radius) * Math.cos(angle);
			y = centerY + rate * (radius) * Math.sin(angle);
			
			GlStateManager.color(1.0f, 1.0f, 1.0f);
			mc.renderEngine.bindTexture(attribute.getGuiTexture());
			this.drawTexturedRect((int)(x-8), (int)(y-8), 16, 16);
			
			angle += 2 * Math.PI / attributeList.size();
		}
	}
	
	private void drawPolygon(List<? extends IRenderRate> rateList, EntityPlayer player, double centerX, double centerY, double radius, int exponent, int color) {
		double rate, angle = -Math.PI / 2;
		boolean flag = false;
		double preX = 0.0, preY = 0.0;
		double x, y;
		
		for(IRenderRate renderRate : rateList) {
			rate = renderRate.getRate(player, exponent);
			
			x = centerX + rate * radius * Math.cos(angle);
			y = centerY + rate * radius * Math.sin(angle);
			
			angle += 2 * Math.PI / rateList.size();
			
			if(flag)
				drawLine(preX, preY, x, y, color);
			
			preX = x;
			preY = y;
			
			flag = true;
		}
		
		IRenderRate firstRate = rateList.get(0);
		rate = firstRate.getRate(player, exponent);
		x = centerX + rate * radius * Math.cos(angle);
		y = centerY + rate * radius * Math.sin(angle);
		
		drawLine(preX, preY, x, y, color);
	}
	
	private void drawLine(double x1, double y1, double x2, double y2, int color) {
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
