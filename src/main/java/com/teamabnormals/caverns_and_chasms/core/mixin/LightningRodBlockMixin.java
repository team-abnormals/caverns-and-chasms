package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.block.CCWeatheringCopper;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.CopperGolem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.RodBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningRodBlock.class)
public class LightningRodBlockMixin extends RodBlock implements CCWeatheringCopper {

	public LightningRodBlockMixin(Properties properties) {
		super(properties);
	}

	@Inject(method = "onPlace", at = @At("TAIL"), cancellable = true)
	private void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean p_51391_, CallbackInfo ci) {
		if (!oldState.is(state.getBlock())) {
			BlockPos belowpos = pos.below();
			BlockState belowstate = level.getBlockState(belowpos);
			if (belowstate.getBlock() instanceof CarvedPumpkinBlock && state.getValue(LightningRodBlock.FACING) == Direction.UP) {
				CopperGolem.createGolem(level, belowpos, belowstate);
				ci.cancel();
			}
		}
	}

	@Override
	public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction action, boolean simulate) {
		return action == ToolActions.AXE_SCRAPE ? CCWeatheringCopper.getPrevious(state).orElse(null) : super.getToolModifiedState(state, context, action, simulate);
	}

	@Override
	public WeatherState getAge() {
		return WeatherState.UNAFFECTED;
	}
}