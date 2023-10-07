package com.teamabnormals.caverns_and_chasms.common.entity.projectile;

import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class BluntArrow extends AbstractArrow {

	public BluntArrow(EntityType<? extends BluntArrow> type, Level worldIn) {
		super(type, worldIn);
	}

	public BluntArrow(Level worldIn, double x, double y, double z) {
		super(CCEntityTypes.BLUNT_ARROW.get(), x, y, z, worldIn);
	}

	public BluntArrow(PlayMessages.SpawnEntity spawnEntity, Level world) {
		this(CCEntityTypes.BLUNT_ARROW.get(), world);
	}

	public BluntArrow(Level worldIn, LivingEntity shooter) {
		super(CCEntityTypes.BLUNT_ARROW.get(), shooter, worldIn);
	}

	@Override
	protected ItemStack getPickupItem() {
		return new ItemStack(CCItems.BLUNT_ARROW.get());
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}