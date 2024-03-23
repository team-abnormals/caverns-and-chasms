package com.teamabnormals.caverns_and_chasms.common.entity.projectile;

import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class LargeArrow extends AbstractArrow {

	public LargeArrow(EntityType<? extends LargeArrow> type, Level worldIn) {
		super(type, worldIn);
	}

	public LargeArrow(Level worldIn, double x, double y, double z) {
		super(CCEntityTypes.LARGE_ARROW.get(), x, y, z, worldIn);
	}

	public LargeArrow(PlayMessages.SpawnEntity spawnEntity, Level world) {
		this(CCEntityTypes.LARGE_ARROW.get(), world);
	}

	public LargeArrow(Level worldIn, LivingEntity shooter) {
		super(CCEntityTypes.LARGE_ARROW.get(), shooter, worldIn);
	}

	@Override
	protected ItemStack getPickupItem() {
		return new ItemStack(CCItems.LARGE_ARROW.get());
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public double getBaseDamage() {
		return super.getBaseDamage() * 3.0D;
	}

	@Override
	public void shoot(double p_37266_, double p_37267_, double p_37268_, float p_37269_, float p_37270_) {
		super.shoot(p_37266_, p_37267_, p_37268_, p_37269_ * 0.5F, p_37270_);
	}
}