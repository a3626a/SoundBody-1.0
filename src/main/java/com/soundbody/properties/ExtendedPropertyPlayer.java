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
import com.soundbody.configuration.Constants;
import com.soundbody.foodstats.ModFoodStats;
import com.soundbody.lib.Strings;
import com.soundbody.modifiers.ModAttributes;
import com.soundbody.network.PacketGeneralClient;

public class ExtendedPropertyPlayer implements IExtendedEntityProperties {

	private EntityPlayer player;
	private int fitness;
	private int lastFitness = -1;

	private int fitnessCounter;

	public void setClone(ExtendedPropertyPlayer property) {
		this.fitness = property.fitness;
		this.fitnessCounter = property.fitnessCounter;
	}

	public ExtendedPropertyPlayer(EntityPlayer player) {
		this.player = player;
		this.fitnessCounter = Constants.fitnessCounter;
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
					if (getFitness() < Constants.maxfitness) {
						if (fitnessCounter == 0) {
							foodStats.addExhaustion(4.0F);
							setFitness(getFitness() + 1);
							fitnessCounter = Constants.fitnessCounter;
						}
					}
				}
				if (hungerPercent < 0.50) {
					if (getFitness() > Constants.minfitness) {
						if (fitnessCounter == 0) {
							foodStats.setFoodSaturationLevel(foodStats.getSaturationLevel() + 1.0F);
							setFitness(getFitness() - 1);
							fitnessCounter = Constants.fitnessCounter;
						}
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

	public double getAmount(double factor_pos, double factor_neg, int operation) {
		double factor;
		if (getFitness() > 0)
			factor = factor_pos;
		else
			factor = factor_neg;
		switch (operation) {
		case 0:
			return factor * getFitness();
		case 1:
			return Math.exp(factor * getFitness()) - 1;
		}
		return 0;
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
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

		player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(new AttributeModifier(UUID.fromString(Strings.modifier_movementSpeed_uuid), Strings.modifier_movespeed_name, 0, 1));
		player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(
				new AttributeModifier(UUID.fromString(Strings.modifier_movementSpeed_uuid), Strings.modifier_movespeed_name, getAmount(Constants.movespeed_pos, Constants.movespeed_neg, 1), 1));

		player.getEntityAttribute(SharedMonsterAttributes.maxHealth).removeModifier(new AttributeModifier(UUID.fromString(Strings.modifier_maxHealth_uuid), Strings.modifier_maxHealth_name, 0, 1));
		player.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(
				new AttributeModifier(UUID.fromString(Strings.modifier_maxHealth_uuid), Strings.modifier_maxHealth_name, getAmount(Constants.maxhealth_pos, Constants.maxhealth_neg, 1), 1));

		player.getEntityAttribute(SharedMonsterAttributes.attackDamage).removeModifier(new AttributeModifier(UUID.fromString(Strings.modifier_attackdamage_uuid), Strings.modifier_attackdamage_name, 0, 1));
		player.getEntityAttribute(SharedMonsterAttributes.attackDamage).applyModifier(
				new AttributeModifier(UUID.fromString(Strings.modifier_attackdamage_uuid), Strings.modifier_attackdamage_name, getAmount(Constants.attackdamage_pos, Constants.attackdamage_neg, 1), 1));

		player.getEntityAttribute(ModAttributes.digspeedFactor).removeModifier(new AttributeModifier(UUID.fromString(Strings.modifier_digspeedFactor_uuid), Strings.modifier_digspeed_name, 0, 0));
		player.getEntityAttribute(ModAttributes.digspeedFactor).applyModifier(
				new AttributeModifier(UUID.fromString(Strings.modifier_digspeedFactor_uuid), Strings.modifier_digspeed_name, getAmount(Constants.digspeed_pos, Constants.digspeed_neg, 0), 0));

		player.getEntityAttribute(ModAttributes.jumpFactor).removeModifier(new AttributeModifier(UUID.fromString(Strings.modifier_jumpFactor_uuid), Strings.modifier_jump_name, 0, 0));
		player.getEntityAttribute(ModAttributes.jumpFactor).applyModifier(new AttributeModifier(UUID.fromString(Strings.modifier_jumpFactor_uuid), Strings.modifier_jump_name, getAmount(Constants.jump_pos, Constants.jump_neg, 0), 0));
	}

}
