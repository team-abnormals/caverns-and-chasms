package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.common.entity.vehicle.MinecartTMT;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;

public class TmtMinecartItem extends MinecartItem {
	private static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
		private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

		@Override
		public ItemStack execute(BlockSource source, ItemStack stack) {
			Direction dir = source.getBlockState().getValue(DispenserBlock.FACING);
			Level level = source.getLevel();
			double d0 = source.x() + (double) dir.getStepX() * 1.125D;
			double d1 = Math.floor(source.y()) + (double) dir.getStepY();
			double d2 = source.z() + (double) dir.getStepZ() * 1.125D;
			BlockPos pos = source.getPos().relative(dir);
			BlockState state = level.getBlockState(pos);
			RailShape shape = state.getBlock() instanceof BaseRailBlock railBlock ? railBlock.getRailDirection(state, level, pos, null) : RailShape.NORTH_SOUTH;
			double d3;
			if (state.is(BlockTags.RAILS)) {
				if (shape.isAscending()) {
					d3 = 0.6D;
				} else {
					d3 = 0.1D;
				}
			} else {
				if (!state.isAir() || !level.getBlockState(pos.below()).is(BlockTags.RAILS)) {
					return this.defaultDispenseItemBehavior.dispense(source, stack);
				}

				BlockState belowState = level.getBlockState(pos.below());
				RailShape railshape1 = belowState.getBlock() instanceof BaseRailBlock ? belowState.getValue(((BaseRailBlock) belowState.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
				if (dir != Direction.DOWN && railshape1.isAscending()) {
					d3 = -0.4D;
				} else {
					d3 = -0.9D;
				}
			}

			AbstractMinecart cart = ((TmtMinecartItem) stack.getItem()).createMinecart(level, d0, d1 + d3, d2);
			if (stack.hasCustomHoverName()) {
				cart.setCustomName(stack.getHoverName());
			}

			level.addFreshEntity(cart);
			stack.shrink(1);
			return stack;
		}

		protected void playSound(BlockSource p_42947_) {
			p_42947_.getLevel().levelEvent(1000, p_42947_.getPos(), 0);
		}
	};

	public TmtMinecartItem(Item.Properties properties) {
		super(AbstractMinecart.Type.TNT, properties);
		DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = level.getBlockState(pos);
		if (!state.is(BlockTags.RAILS)) {
			return InteractionResult.FAIL;
		} else {
			ItemStack itemstack = context.getItemInHand();
			if (!level.isClientSide) {
				RailShape railshape = state.getBlock() instanceof BaseRailBlock rail ? rail.getRailDirection(state, level, pos, null) : RailShape.NORTH_SOUTH;
				double d0 = 0.0D;
				if (railshape.isAscending()) {
					d0 = 0.5D;
				}

				AbstractMinecart cart = this.createMinecart(level, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.0625D + d0, (double) pos.getZ() + 0.5D);
				if (itemstack.hasCustomHoverName()) {
					cart.setCustomName(itemstack.getHoverName());
				}

				level.addFreshEntity(cart);
				level.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of(context.getPlayer(), level.getBlockState(pos.below())));
			}

			itemstack.shrink(1);
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
	}

	public AbstractMinecart createMinecart(Level level, double x, double y, double z) {
		return new MinecartTMT(level, x, y, z);
	}
}