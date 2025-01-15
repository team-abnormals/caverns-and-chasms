package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.level.SpinelBoom;
import com.teamabnormals.caverns_and_chasms.common.network.S2CSpinelBoomMessage;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {

	@Shadow
	private boolean hurtEntities;

	@Shadow
	private BlockState blockState;

	@Shadow
	private boolean cancelDrop;

	@Shadow
	private float fallDamagePerDistance;

	@Shadow
	private int fallDamageMax;

	public FallingBlockEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
		super(p_19870_, p_19871_);
	}

	@Inject(method = "causeFallDamage", at = @At("TAIL"))
	private void onPlace(float p_149643_, float p_149644_, DamageSource p_149645_, CallbackInfoReturnable<Boolean> cir) {
		if (this.hurtEntities) {
			int i = Mth.ceil(p_149643_ - 1.0F);
			if (i >= 0) {
				float fallDamage = (float) Math.min(Mth.floor((float) i * this.fallDamagePerDistance), this.fallDamageMax);
				boolean flag = this.blockState.is(CCBlocks.BEJEWELED_ANVIL.get());
				if (flag && fallDamage > 0.0F) {
					this.cancelDrop = true;

					Level level = this.level();
					BlockPos pos = this.blockPosition();

					if (!level.isClientSide()) {
						SpinelBoom boom = new SpinelBoom(level, null, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 2.0F);
						if (!ForgeEventFactory.onExplosionStart(level, boom)) {
							boom.explode();
							boom.finalizeExplosion(true);
							CavernsAndChasms.CHANNEL.send(PacketDistributor.DIMENSION.with(level::dimension), new S2CSpinelBoomMessage(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, 2.0F, boom.getToBlow()));
						}
					}

				}
			}
		}
	}
}