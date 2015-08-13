package com.soundbody.properties;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.soundbody.SoundBody;
import com.soundbody.foodstats.ModFoodStats;
import com.soundbody.lib.Strings;
import com.soundbody.modifiers.ModAttributes;
import com.soundbody.network.PacketGeneralClient;

public class ExtendedPropertyPlayer implements IExtendedEntityProperties {

	private EntityPlayer player;
	private int fitness;
	private int lastFitness=-1;

	private int fitnessCounter = fitnessCounterPeriod;

	private static double factor = Math.log(1.5) / 20.0;
	public static int fitnessLossOnDeath = 4;
	public static int fitnessCounterPeriod = 20;

	public void setClone(ExtendedPropertyPlayer property) {
		this.fitness = property.fitness-fitnessLossOnDeath;
		this.fitnessCounter = property.fitnessCounter;
	}
	
	public ExtendedPropertyPlayer(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setInteger("fitness", fitness);
		compound.setInteger("fitnessCounter", fitnessCounter);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		fitness = compound.getInteger("fitness");
		fitnessCounter = compound.getInteger("fitnessCounter");
	}

	@Override
	public void init(Entity entity, World world) {
		player = (EntityPlayer) entity;
	}

	public void update() {
		if (!player.worldObj.isRemote) {
			FoodStats foodStats = player.getFoodStats();
			if (foodStats instanceof ModFoodStats) {
				ModFoodStats modFoodStats = (ModFoodStats) foodStats;
				double hungerPercent = foodStats.getFoodLevel() / (double) modFoodStats.getMaxFoodLevel();
				if (hungerPercent > 0.75) {
					if (fitnessCounter == 0) {
						foodStats.addExhaustion(4.0F);
						fitness += 1;
						fitnessCounter = fitnessCounterPeriod;
					}
				}
				if (hungerPercent < 0.50) {
					if (fitnessCounter == 0) {
						foodStats.setFoodSaturationLevel(foodStats.getSaturationLevel() + 1.0F);
						fitness -= 1;
						fitnessCounter = fitnessCounterPeriod;
					}
				}
			}
			if (fitnessCounter > 0)
				fitnessCounter--;
			
			if (lastFitness != fitness) {
				sync();
			}
			lastFitness = fitness;
		}
	}

	public double getAmount(int operation) {
		switch (operation) {
		case 0:
			return getFitness() / 100.0;
		case 1:
			return Math.exp(factor * getFitness()) - 1;
		}
		return 0;
	}

	public int getFitness() {
		return fitness;
	}

	public void sync(PacketGeneralClient packet) {
		fitness = packet.getInt();
	}
	
	private void sync() {
		PacketGeneralClient msg = new PacketGeneralClient(0);
		msg.setInt(fitness);
		SoundBody.simpleChannel.sendTo(msg, MinecraftServer.getServer().getConfigurationManager().getPlayerByUUID(player.getUniqueID()));
		
		player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(new AttributeModifier(UUID.fromString(Strings.modifier_movementSpeed_uuid), Strings.modifier_movespeed_name, getAmount(1), 1));
		player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(new AttributeModifier(UUID.fromString(Strings.modifier_movementSpeed_uuid), Strings.modifier_movespeed_name, getAmount(1), 1));

		player.getEntityAttribute(SharedMonsterAttributes.maxHealth).removeModifier(new AttributeModifier(UUID.fromString(Strings.modifier_maxHealth_uuid), Strings.modifier_maxHealth_name, getAmount(1), 1));
		player.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifier(UUID.fromString(Strings.modifier_maxHealth_uuid), Strings.modifier_maxHealth_name, getAmount(1), 1));
		
		player.getEntityAttribute(SharedMonsterAttributes.attackDamage).removeModifier(new AttributeModifier(UUID.fromString(Strings.modifier_attackdamage_uuid), Strings.modifier_attackdamage_name, getAmount(1), 1));
		player.getEntityAttribute(SharedMonsterAttributes.attackDamage).applyModifier(new AttributeModifier(UUID.fromString(Strings.modifier_attackdamage_uuid), Strings.modifier_attackdamage_name, getAmount(1), 1));
		
		player.getEntityAttribute(ModAttributes.digspeedFactor).removeModifier(new AttributeModifier(UUID.fromString(Strings.modifier_digspeedFactor_uuid), Strings.modifier_digspeed_name, getAmount(0), 0));
		player.getEntityAttribute(ModAttributes.digspeedFactor).applyModifier(new AttributeModifier(UUID.fromString(Strings.modifier_digspeedFactor_uuid), Strings.modifier_digspeed_name, getAmount(0), 0));
	
		player.getEntityAttribute(ModAttributes.jumpFactor).removeModifier(new AttributeModifier(UUID.fromString(Strings.modifier_jumpFactor_uuid), Strings.modifier_jump_name, getAmount(0), 0));
		player.getEntityAttribute(ModAttributes.jumpFactor).applyModifier(new AttributeModifier(UUID.fromString(Strings.modifier_jumpFactor_uuid), Strings.modifier_jump_name, getAmount(0), 0));
	}

}
