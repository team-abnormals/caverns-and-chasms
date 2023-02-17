package com.teamabnormals.caverns_and_chasms.common.entity.item;

import com.teamabnormals.caverns_and_chasms.common.level.SpinelBoom;
import com.teamabnormals.caverns_and_chasms.common.network.S2CSpinelBoomMessage;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nullable;

public class PrimedTmt extends PrimedTnt {
	@Nullable
	private LivingEntity tmtPlacedBy;

	public PrimedTmt(EntityType<? extends PrimedTmt> type, Level level) {
		super(type, level);
	}

	public PrimedTmt(Level level, double x, double y, double z, @Nullable LivingEntity igniter) {
		this(CCEntityTypes.TMT.get(), level);
		this.setPos(x, y, z);
		double d0 = level.random.nextDouble() * (double) ((float) Math.PI * 2F);
		this.setDeltaMovement(-Math.sin(d0) * 0.02D, 0.2F, -Math.cos(d0) * 0.02D);
		this.setFuse(30);
		this.xo = x;
		this.yo = y;
		this.zo = z;
		this.tmtPlacedBy = igniter;
	}

	@Override
	protected void explode() {
		this.getExplosion();
	}

	protected SpinelBoom getExplosion() {
		SpinelBoom boom = new SpinelBoom(this.level, this, this.getX(), this.getY(0.0625D), this.getZ(), 4.0F);
		if (ForgeEventFactory.onExplosionStart(this.level, boom))
			return boom;
		boom.explode();
		boom.finalizeExplosion(true);
		CavernsAndChasms.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new S2CSpinelBoomMessage((float) this.getX(), (float) this.getY(0.0625D), (float) this.getZ(), 4.0F, boom.getToBlow()));
		return boom;
	}

	@Override
	@Nullable
	public LivingEntity getOwner() {
		return this.tmtPlacedBy;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}
}