package com.teamabnormals.caverns_and_chasms.common.entity.projectile;

import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class RicochetArrow extends AbstractArrow {

	public RicochetArrow(EntityType<? extends RicochetArrow> type, Level worldIn) {
		super(type, worldIn);
	}

	public RicochetArrow(Level worldIn, double x, double y, double z) {
		super(CCEntityTypes.RICOCHET_ARROW.get(), x, y, z, worldIn);
	}

	public RicochetArrow(PlayMessages.SpawnEntity spawnEntity, Level world) {
		this(CCEntityTypes.RICOCHET_ARROW.get(), world);
	}

	public RicochetArrow(Level worldIn, LivingEntity shooter) {
		super(CCEntityTypes.RICOCHET_ARROW.get(), shooter, worldIn);
	}

	@Override
	protected ItemStack getPickupItem() {
		return new ItemStack(CCItems.RICOCHET_ARROW.get());
	}

	@Override
	protected void onHitBlock(BlockHitResult hitResult) {
		super.onHitBlock(hitResult);
		this.setSoundEvent(this.getDefaultHitGroundSoundEvent());
	}

	@Override
	protected SoundEvent getDefaultHitGroundSoundEvent() {
		return CCSoundEvents.RICOCHET_ARROW_HIT.get();
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}