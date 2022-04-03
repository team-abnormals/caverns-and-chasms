package com.teamabnormals.caverns_and_chasms.common.network;

import com.google.common.collect.Lists;
import com.teamabnormals.blueprint.client.ClientInfo;
import com.teamabnormals.caverns_and_chasms.common.level.SpinelBoom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class MessageS2CSpinelBoom {
	/*
	public float posX;
	public float posY;
	public float posZ;
	public float strength;
	public List<BlockPos> affectedBlockPositions;

	public MessageS2CSpinelBoom(float x, float y, float z, float strength, List<BlockPos> affectedBlockPositions) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.strength = strength;
		this.affectedBlockPositions = Lists.newArrayList(affectedBlockPositions);
	}

	public static MessageS2CSpinelBoom deserialize(FriendlyByteBuf buf) {
		float posX = buf.readFloat();
		float posY = buf.readFloat();
		float posZ = buf.readFloat();
		float strength = buf.readFloat();
		int blockPositionsSize = buf.readInt();
		List<BlockPos> affectedBlockPositions = Lists.newArrayListWithCapacity(blockPositionsSize);

		for (int i = 0; i < blockPositionsSize; i++) {
			int x = buf.readByte() + Mth.floor(posX);
			int y = buf.readByte() + Mth.floor(posY);
			int z = buf.readByte() + Mth.floor(posZ);
			affectedBlockPositions.add(new BlockPos(x, y, z));
		}
		return new MessageS2CSpinelBoom(posX, posY, posZ, strength, affectedBlockPositions);
	}

	public void serialize(FriendlyByteBuf buf) {
		buf.writeFloat(this.posX);
		buf.writeFloat(this.posY);
		buf.writeFloat(this.posZ);
		buf.writeFloat(this.strength);
		buf.writeInt(this.affectedBlockPositions.size());

		for (BlockPos blockpos : this.affectedBlockPositions) {
			int x = blockpos.getX() - Mth.floor(this.posX);
			int y = blockpos.getY() - Mth.floor(this.posY);
			int z = blockpos.getZ() - Mth.floor(this.posZ);
			buf.writeByte(x);
			buf.writeByte(y);
			buf.writeByte(z);
		}
	}

	public static void handle(MessageS2CSpinelBoom message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			SpinelBoom boom = new SpinelBoom(ClientInfo.getClientPlayerLevel(), null, message.posX, message.posY, message.posZ, message.strength);
			boom.finalizeExplosion(true);
		}
		context.setPacketHandled(true);
	}
	*/
}