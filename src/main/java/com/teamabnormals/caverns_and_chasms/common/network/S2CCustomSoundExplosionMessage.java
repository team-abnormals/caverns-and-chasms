package com.teamabnormals.caverns_and_chasms.common.network;

import com.google.common.collect.Lists;
import com.teamabnormals.caverns_and_chasms.common.level.CustomSoundExplosion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

public class S2CCustomSoundExplosionMessage {
	public float posX;
	public float posY;
	public float posZ;
	public float strength;
	public List<BlockPos> affectedBlockPositions;
	public SoundEvent sound;

	public S2CCustomSoundExplosionMessage(float x, float y, float z, float strength, List<BlockPos> affectedBlockPositions, SoundEvent sound) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.strength = strength;
		this.affectedBlockPositions = Lists.newArrayList(affectedBlockPositions);
		this.sound = sound;
	}

	public static S2CCustomSoundExplosionMessage deserialize(FriendlyByteBuf buf) {
		float posX = buf.readFloat();
		float posY = buf.readFloat();
		float posZ = buf.readFloat();
		float strength = buf.readFloat();
		int blockPositionsSize = buf.readInt();
		List<BlockPos> affectedBlockPositions = Lists.newArrayListWithCapacity(blockPositionsSize);
		SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation((buf.readUtf())));

		for (int i = 0; i < blockPositionsSize; i++) {
			int x = buf.readByte() + Mth.floor(posX);
			int y = buf.readByte() + Mth.floor(posY);
			int z = buf.readByte() + Mth.floor(posZ);
			affectedBlockPositions.add(new BlockPos(x, y, z));
		}

		return new S2CCustomSoundExplosionMessage(posX, posY, posZ, strength, affectedBlockPositions, sound);
	}

	public void serialize(FriendlyByteBuf buf) {
		buf.writeFloat(this.posX);
		buf.writeFloat(this.posY);
		buf.writeFloat(this.posZ);
		buf.writeFloat(this.strength);
		buf.writeInt(this.affectedBlockPositions.size());
		buf.writeUtf(this.sound.getLocation().toString());

		for (BlockPos blockpos : this.affectedBlockPositions) {
			int x = blockpos.getX() - Mth.floor(this.posX);
			int y = blockpos.getY() - Mth.floor(this.posY);
			int z = blockpos.getZ() - Mth.floor(this.posZ);
			buf.writeByte(x);
			buf.writeByte(y);
			buf.writeByte(z);
		}
	}

	public static void handle(S2CCustomSoundExplosionMessage message, Supplier<Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		LocalPlayer player = Minecraft.getInstance().player;
		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			CustomSoundExplosion boom = new CustomSoundExplosion(player.getCommandSenderWorld(), null, message.posX, message.posY, message.posZ, message.strength, message.sound);
			boom.finalizeExplosion(true);
			context.setPacketHandled(true);
		}
	}
}