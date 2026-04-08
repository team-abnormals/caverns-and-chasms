package com.teamabnormals.caverns_and_chasms.common.entity.projectile;

import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class RicochetArrow extends AbstractArrow {

	public RicochetArrow(EntityType<? extends RicochetArrow> entityType, Level level) {
		super(entityType, level);
	}

	public RicochetArrow(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
		super(CCEntityTypes.RICOCHET_ARROW.get(), owner, level, pickupItemStack, firedFromWeapon);
	}

	public RicochetArrow(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
		super(CCEntityTypes.RICOCHET_ARROW.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
	}

	@Override
	protected ItemStack getDefaultPickupItem() {
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
}