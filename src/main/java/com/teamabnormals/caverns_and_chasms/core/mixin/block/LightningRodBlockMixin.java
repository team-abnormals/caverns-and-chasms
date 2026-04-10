package com.teamabnormals.caverns_and_chasms.core.mixin.block;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.CopperGolem;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.RodBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningRodBlock.class)
public abstract class LightningRodBlockMixin extends RodBlock implements WeatheringCopper {

	public LightningRodBlockMixin(Properties properties) {
		super(properties);
	}

	@Inject(method = "onPlace", at = @At("TAIL"), cancellable = true)
	private void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean p_51391_, CallbackInfo ci) {
		if (!oldState.is(state.getBlock()) && state.is(CCBlockTags.COPPER_GOLEM_SUMMON_BLOCKS)) {
			BlockPos belowpos = pos.below();
			BlockState belowstate = level.getBlockState(belowpos);
			if (belowstate.getBlock() instanceof CarvedPumpkinBlock && state.getValue(LightningRodBlock.FACING) == Direction.UP) {
				CopperGolem.createGolem(level, belowpos, belowstate);
				ci.cancel();
			}
		}
	}

	@Override
	public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility action, boolean simulate) {
		return action == ItemAbilities.AXE_SCRAPE ? WeatheringCopper.getPrevious(state).orElse(null) : super.getToolModifiedState(state, context, action, simulate);
	}

	@Override
	public WeatherState getAge() {
		return WeatherState.UNAFFECTED;
	}
}