package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.block.CaveGrowthsBlock;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.deeper.Deeper;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.deeper.DeeperHat;
import com.teamabnormals.caverns_and_chasms.common.level.CustomSoundExplosion;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {
	@Final
	@Shadow
	private RandomSource random;

	@Final
	@Shadow
	private Level level;

	@Final
	@Shadow
	private double x;

	@Final
	@Shadow
	private double y;

	@Final
	@Shadow
	private double z;

	@Final
	@Shadow
	private ObjectArrayList<BlockPos> toBlow;

	@Shadow
	public abstract Entity getExploder();

	@Inject(method = "finalizeExplosion", at = @At("TAIL"))
	public void finalizeExplosion(boolean p_46076_, CallbackInfo ci) {
		if (this.getExploder() instanceof Deeper deeper && deeper.getHat() != DeeperHat.NONE) {
			DeeperHat hat = deeper.getHat();
			boolean moschatel = hat == DeeperHat.MOSCHATEL;
			BlockState blockstate = hat.getBlock().defaultBlockState();
			for (BlockPos blockpos : this.toBlow) {
				if (this.random.nextInt(10) == 0 && this.level.getBlockState(blockpos).isAir()) {
					blockstate = moschatel ? blockstate : blockstate.setValue(CaveGrowthsBlock.FACING, Direction.getNearest(this.x - blockpos.getX() - 0.5D, this.y - blockpos.getY() - 0.5D, this.z - blockpos.getZ() - 0.5D));
					if (blockstate.canSurvive(this.level, blockpos))
						this.level.setBlockAndUpdate(blockpos, blockstate);
				}
			}
		}
	}

	@ModifyArg(method = "finalizeExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"), index = 3)
	public SoundEvent setCustomExplosionSound(SoundEvent soundEvent) {
		return ((Object) this) instanceof CustomSoundExplosion customSoundExplosion ? customSoundExplosion.getSound() : soundEvent;
	}
}