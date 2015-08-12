package com.soundbody.properties;

import com.soundbody.SoundBody;
import com.soundbody.foodstats.ModFoodStats;
import com.soundbody.network.PacketGeneralClient;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ExtendedPropertyPlayer implements IExtendedEntityProperties {

	private EntityPlayer player;
	private int fitness;
	private int lastFitness;

	private int fitnessCounter;

	public ExtendedPropertyPlayer(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setInteger("fitness", fitness);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		fitness = compound.getInteger("fitness");
	}

	@Override
	public void init(Entity entity, World world) {
		player = (EntityPlayer) entity;
	}

	public void update() {

		if (!player.worldObj.isRemote) {
			lastFitness = fitness;

			FoodStats foodStats = player.getFoodStats();
			if (foodStats instanceof ModFoodStats) {
				ModFoodStats modFoodStats = (ModFoodStats) foodStats;
				double hungerPercent = foodStats.getFoodLevel() / (double) modFoodStats.getMaxFoodLevel();
				if (hungerPercent > 0.75) {
					if (fitnessCounter == 0) {
						foodStats.addExhaustion(4.0F);
						fitness += 1;
						fitnessCounter = 60 * 20;
					}
				}
				if (hungerPercent < 0.50) {
					foodStats.setFoodSaturationLevel(foodStats.getSaturationLevel() + 1.0F);
					fitness -= 1;
					fitnessCounter = 60 * 20;
				}
			}
			if (fitnessCounter > 0)
				fitnessCounter--;

			if (lastFitness != fitness) {
				PacketGeneralClient msg = new PacketGeneralClient(0);
				msg.setInt(fitness);
				SoundBody.simpleChannel.sendTo(msg, MinecraftServer.getServer().getConfigurationManager().getPlayerByUUID(player.getUniqueID()));
			}
		}
	}

	public int getFitness() {
		return fitness;
	}

	public void sync(PacketGeneralClient packet) {
		fitness = packet.getInt();
	}

}
