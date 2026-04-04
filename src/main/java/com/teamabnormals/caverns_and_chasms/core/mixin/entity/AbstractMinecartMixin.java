package com.teamabnormals.caverns_and_chasms.core.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.caverns_and_chasms.common.block.HaltRailBlock;
import com.teamabnormals.caverns_and_chasms.common.block.SpikedRailBlock;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin extends Entity {
	@Unique
	private boolean caverns_and_chasms$isHalted = false;

	public AbstractMinecartMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;activateMinecart(IIIZ)V"))
	private void tick(AbstractMinecart cart, int x, int y, int z, boolean powered, Operation<Void> original) {
		BlockState state = cart.level().getBlockState(new BlockPos(x, y, z));
		if (!(state.getBlock() instanceof SpikedRailBlock)) {
			original.call(cart, x, y, z, powered);
		}
	}

	@Inject(method = "getBlockSpeedFactor", at = @At("RETURN"), cancellable = true)
	private void getBlockSpeedFactor(CallbackInfoReturnable<Float> cir) {
		BlockState blockstate = this.level().getBlockState(this.blockPosition());
		if (blockstate.is(CCBlockTags.COPPER_RAILS)) {
			cir.setReturnValue(0.985F);
		}
	}

	@Inject(method = "moveAlongTrack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;moveMinecartOnRail(Lnet/minecraft/core/BlockPos;)V", shift = At.Shift.BEFORE))
	private void moveAlongTrack(BlockPos pos, BlockState state, CallbackInfo ci) {
		if (state.getBlock() instanceof HaltRailBlock) {
			boolean top = state.getValue(HaltRailBlock.TOP_POWERED);
			boolean bottom = state.getValue(HaltRailBlock.BOTTOM_POWERED);

			Direction direction = switch (state.getValue(HaltRailBlock.SHAPE)) {
				case EAST_WEST, ASCENDING_WEST, ASCENDING_EAST -> Direction.WEST;
				default -> Direction.SOUTH;
			};

			Vec3 movement = this.getDeltaMovement();
			if (movement.horizontalDistanceSqr() > 0.0D) {
				Direction motionDir = Direction.getNearest(movement.x, 0.0D, movement.z);
				if (top && motionDir.equals(direction) || bottom && motionDir.equals(direction.getOpposite())) {
					boolean halt = false;

					if (motionDir.getAxis() == Axis.X) {
						double deltaX = pos.getX() + 0.5D - this.getX();
						int stepX = motionDir.getStepX();
						if (deltaX * stepX < movement.x * stepX) {
							this.setDeltaMovement(deltaX * stepX < 0.0D ? 0.0D : deltaX, movement.y, movement.z);
							halt = true;
						}
					} else if (motionDir.getAxis() == Axis.Z) {
						double deltaZ = pos.getZ() + 0.5D - this.getZ();
						int stepZ = motionDir.getStepZ();
						if (deltaZ * stepZ < movement.z * stepZ) {
							this.setDeltaMovement(movement.x, movement.y, deltaZ * stepZ < 0.0D ? 0.0D : deltaZ);
							halt = true;
						}
					}

					if (halt) {
						this.halt(pos);
						return;
					}
				}

				this.caverns_and_chasms$isHalted = false;
			} else if ((top || bottom) && this.position().x == pos.getX() + 0.5D && this.position().z == pos.getZ() + 0.5D) {
				this.halt(pos);
			}
		} else {
			this.caverns_and_chasms$isHalted = false;
		}
	}

	private void halt(BlockPos pos) {
		if (!this.caverns_and_chasms$isHalted) {
			if (!this.firstTick) {
				this.level().playSound(null, pos, CCSoundEvents.HALT_RAIL_HALT.get(), SoundSource.BLOCKS);
			}
			this.caverns_and_chasms$isHalted = true;
		}
	}
}
