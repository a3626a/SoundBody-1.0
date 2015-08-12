package com.soundbody.client;

import com.soundbody.foodstats.ModFoodStats;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.FoodStats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderFoodBarHandler extends Gui {

	public static int left_height = 39;
    public static int right_height = 39;
	
	@SubscribeEvent
	public void renderFoodBar(RenderGameOverlayEvent event) {
		if (event.type == ElementType.FOOD) {
			right_height = 39;
	        left_height = 39;
			
			Minecraft mc = Minecraft.getMinecraft();
			int width = event.resolution.getScaledWidth();
			int height = event.resolution.getScaledHeight();
			
			mc.mcProfiler.startSection("food");

	        EntityPlayer player = (EntityPlayer)mc.getRenderViewEntity();
	        GlStateManager.enableBlend();
	        int left = width / 2 + 91;
	        int top = height - right_height;
	        right_height += 10;
	        boolean unused = false;// Unused flag in vanilla, seems to be part of a 'fade out' mechanic

	        FoodStats stats = mc.thePlayer.getFoodStats();
	        ModFoodStats modStats = null;
	        if (stats instanceof ModFoodStats) {
	        	modStats = (ModFoodStats)stats;
	        }
		
	        int level = stats.getFoodLevel();
	        int levelLast = stats.getPrevFoodLevel();
	        
	        int cell = 20;
	        if (modStats != null) cell = modStats.getMaxFoodLevel()/2;
	        
	        for (int i = 0; i < 20; ++i)
	        {
	            int idx = i * 2 + 1;
	            int x = left - (i%10) * 8 - 9;
	            int y = top - 8*(i/10);
	            int icon = 16;
	            byte backgound = 0;

	            if (mc.thePlayer.isPotionActive(Potion.hunger))
	            {
	                icon += 36;
	                backgound = 13;
	            }
	            if (unused) backgound = 1; //Probably should be a += 1 but vanilla never uses this

	            /*
	            if (player.getFoodStats().getSaturationLevel() <= 0.0F && updateCounter % (level * 3 + 1) == 0)
	            {
	                y = top + (rand.nextInt(3) - 1);
	            }
	            */
	            
	            drawTexturedModalRect(x, y, 16 + backgound * 9, 27, 9, 9);

	            if (unused)
	            {
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
	        event.setCanceled(true);
		}
	}
	
}
