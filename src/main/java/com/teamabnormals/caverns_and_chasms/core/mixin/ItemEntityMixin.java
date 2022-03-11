package com.teamabnormals.caverns_and_chasms.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.teamabnormals.caverns_and_chasms.common.block.CoilBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

	public ItemEntityMixin(EntityType<?> entity, Level level) {
		super(entity, level);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo ci) {
		boolean flag = false;
		float f = Float.MAX_VALUE;
		BlockPos pos = this.blockPosition();
		Vec3 vec3 = Vec3.ZERO;

		for (BlockPos blockpos : BlockPos.betweenClosed(pos.getX() - 8, pos.getY() - 8, pos.getZ() - 8, pos.getX() + 8, pos.getY() + 8, pos.getZ() + 8)) {
			BlockState blockstate = this.level.getBlockState(blockpos);

			if (blockstate.getBlock() instanceof CoilBlock && (blockstate.getValue(CoilBlock.POWERED) || blockstate.getValue(CoilBlock.INDUCTION_POWER) > 0)) {
				double x = blockpos.getX() + 0.5D - this.getX();
				double y = blockpos.getY() + 0.5D - this.getY();
				double z = blockpos.getZ() + 0.5D - this.getZ();
				Vec3 vec31 = new Vec3(x, y, z);
				double d0 = vec31.length();

				if (d0 < 8.0D) {
					double d1 = 1.0D - d0 / 8.0D;
					double d2 = blockstate.getValue(CoilBlock.POWERED) ? 1 : blockstate.getValue(CoilBlock.INDUCTION_POWER) / 5;
					double d3 = d1 * d1 * d2;
					double d4 = d3 * 0.075D;
					vec3 = vec3.add(new Vec3(vec31.x() * d4 / d0, vec31.y() * d4 / d0, vec31.z() * d4 / d0));
					
					if (Math.abs(x) <= 0.625D && Math.abs(y) <= 0.625D && Math.abs(z) <= 0.625D) {
						flag = true;
						
						float f1 = blockstate.getFriction(this.level, blockpos, this);
						if (f1 < f) {
							f = f1;
						}
					}
				}
			}
		}

		double d0 = vec3.length();
		if (d0 > 0.12D) {
			vec3 = new Vec3(vec3.x() * 0.12D / d0, vec3.y() * 0.12D / d0, vec3.z() * 0.12D / d0);
		}

		this.setDeltaMovement(this.getDeltaMovement().add(vec3));
		
		if (flag) {
			float f1 = this.onGround ? 1.0F : f;
			this.setDeltaMovement(this.getDeltaMovement().multiply(f1, f, f1));
		}
	}
}