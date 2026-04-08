package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.caverns_and_chasms.common.entity.vehicle.MinecartTMT;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class TmtMinecartItem extends MinecartItem {
	private static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
		private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

		@Override
		public ItemStack execute(BlockSource source, ItemStack stack) {
			Direction dir = source.state().getValue(DispenserBlock.FACING);
			ServerLevel level = source.level();
			Vec3 vec3 = source.center();
			double x = vec3.x() + (double) dir.getStepX() * 1.125;
			double y = Math.floor(vec3.y()) + (double) dir.getStepY();
			double z = vec3.z() + (double) dir.getStepZ() * 1.125;
			BlockPos pos = source.pos().relative(dir);
			BlockState state = level.getBlockState(pos);
			RailShape shape = state.getBlock() instanceof BaseRailBlock baseRailBlock ? baseRailBlock.getRailDirection(state, level, pos, null) : RailShape.NORTH_SOUTH;
			double d3;
			if (state.is(BlockTags.RAILS)) {
				if (shape.isAscending()) {
					d3 = 0.6;
				} else {
					d3 = 0.1;
				}
			} else {
				if (!state.isAir() || !level.getBlockState(pos.below()).is(BlockTags.RAILS)) {
					return this.defaultDispenseItemBehavior.dispense(source, stack);
				}

				BlockState belowPos = level.getBlockState(pos.below());
				RailShape belowShape = belowPos.getBlock() instanceof BaseRailBlock baseRailBlock ? baseRailBlock.getRailDirection(belowPos, level, pos.below(), null) : RailShape.NORTH_SOUTH;
				if (dir != Direction.DOWN && belowShape.isAscending()) {
					d3 = -0.4;
				} else {
					d3 = -0.9;
				}
			}

			AbstractMinecart cart = ((TmtMinecartItem) stack.getItem()).createMinecart(level, x, y + d3, z, stack, null);
			level.addFreshEntity(cart);
			stack.shrink(1);
			return stack;
		}

		@Override
		protected void playSound(BlockSource source) {
			source.level().levelEvent(1000, source.pos(), 0);
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
			ItemStack stack = context.getItemInHand();
			if (level instanceof ServerLevel serverLevel) {
				RailShape railshape = state.getBlock() instanceof BaseRailBlock rail ? rail.getRailDirection(state, level, pos, null) : RailShape.NORTH_SOUTH;
				double d0 = 0.0D;
				if (railshape.isAscending()) {
					d0 = 0.5D;
				}

				AbstractMinecart cart = this.createMinecart(serverLevel, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.0625D + d0, (double) pos.getZ() + 0.5D, stack, context.getPlayer());
				level.addFreshEntity(cart);
				level.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of(context.getPlayer(), level.getBlockState(pos.below())));
			}

			stack.shrink(1);
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
	}

	public AbstractMinecart createMinecart(ServerLevel level, double x, double y, double z, ItemStack stack, @Nullable Player player) {
		AbstractMinecart cart = new MinecartTMT(level, x, y, z);
		EntityType.<AbstractMinecart>createDefaultStackConfig(level, stack, player).accept(cart);
		return new MinecartTMT(level, x, y, z);
	}
}