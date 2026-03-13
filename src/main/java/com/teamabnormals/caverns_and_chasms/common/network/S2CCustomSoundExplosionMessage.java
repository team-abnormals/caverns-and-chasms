package com.teamabnormals.caverns_and_chasms.common.network;

import com.google.common.collect.Lists;
import com.teamabnormals.caverns_and_chasms.common.level.CustomExplosion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Explosion.BlockInteraction;
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
	public ParticleOptions emitter;
	public ParticleOptions particle;

	public S2CCustomSoundExplosionMessage(float x, float y, float z, float strength, List<BlockPos> affectedBlockPositions, SoundEvent sound, ParticleOptions emitter, ParticleOptions particle) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.strength = strength;
		this.affectedBlockPositions = Lists.newArrayList(affectedBlockPositions);
		this.sound = sound;
		this.particle = particle;
		this.emitter = emitter;
	}

	public static S2CCustomSoundExplosionMessage deserialize(FriendlyByteBuf buf) {
		float posX = buf.readFloat();
		float posY = buf.readFloat();
		float posZ = buf.readFloat();
		float strength = buf.readFloat();
		int blockPositionsSize = buf.readInt();
		List<BlockPos> affectedBlockPositions = Lists.newArrayListWithCapacity(blockPositionsSize);
		SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation((buf.readUtf())));
		ParticleOptions emitter = readParticle(buf, buf.readById(BuiltInRegistries.PARTICLE_TYPE));
		ParticleOptions particle = readParticle(buf, buf.readById(BuiltInRegistries.PARTICLE_TYPE));

		for (int i = 0; i < blockPositionsSize; i++) {
			int x = buf.readByte() + Mth.floor(posX);
			int y = buf.readByte() + Mth.floor(posY);
			int z = buf.readByte() + Mth.floor(posZ);
			affectedBlockPositions.add(new BlockPos(x, y, z));
		}

		return new S2CCustomSoundExplosionMessage(posX, posY, posZ, strength, affectedBlockPositions, sound, emitter, particle);
	}

	private static <T extends ParticleOptions> T readParticle(FriendlyByteBuf buf, ParticleType<T> particle) {
		return particle.getDeserializer().fromNetwork(particle, buf);
	}

	public void serialize(FriendlyByteBuf buf) {
		buf.writeFloat(this.posX);
		buf.writeFloat(this.posY);
		buf.writeFloat(this.posZ);
		buf.writeFloat(this.strength);
		buf.writeInt(this.affectedBlockPositions.size());
		buf.writeUtf(this.sound.getLocation().toString());
		buf.writeId(BuiltInRegistries.PARTICLE_TYPE, this.emitter.getType());
		buf.writeId(BuiltInRegistries.PARTICLE_TYPE, this.particle.getType());

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
		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				LocalPlayer player = Minecraft.getInstance().player;
				CustomExplosion explosion = new CustomExplosion(player.getCommandSenderWorld(), null, message.posX, message.posY, message.posZ, message.strength, false, BlockInteraction.DESTROY, message.sound, message.emitter, message.particle);
				explosion.finalizeExplosion(true);
			});
		}
		context.setPacketHandled(true);
	}
}