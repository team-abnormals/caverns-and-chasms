package com.teamabnormals.caverns_and_chasms.common.dispenser;

import com.teamabnormals.caverns_and_chasms.common.entity.projectile.BluntArrow;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BluntArrowDispenseBehavior extends AbstractProjectileDispenseBehavior {

	@Override
	protected Projectile getProjectile(Level level, Position pos, ItemStack stack) {
		BluntArrow entity = new BluntArrow(level, pos.x(), pos.y(), pos.z());
		entity.pickup = AbstractArrow.Pickup.ALLOWED;
		return entity;
	}
}