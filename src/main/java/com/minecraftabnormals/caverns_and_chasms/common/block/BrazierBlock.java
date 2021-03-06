package com.minecraftabnormals.caverns_and_chasms.common.block;

import com.minecraftabnormals.abnormals_core.core.util.BlockUtil;
import com.minecraftabnormals.caverns_and_chasms.core.other.CCTags;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.PushReaction;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.Random;

public class BrazierBlock extends Block implements IWaterLoggable {
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	protected static final VoxelShape GROUNDED_SHAPE = VoxelShapes.or(Block.makeCuboidShape(0.0D, 4.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D));
	protected static final VoxelShape HANGING_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);

	private final int fireDamage;

	public BrazierBlock(int fireDamage, AbstractBlock.Properties properties) {
		super(properties);
		this.fireDamage = fireDamage;
		this.setDefaultState(this.stateContainer.getBaseState().with(LIT, true).with(HANGING, false).with(WATERLOGGED, false));
	}

	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if (!entityIn.isImmuneToFire() && state.get(LIT) && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entityIn)) {
			if (state.isIn(CCBlocks.CURSED_BRAZIER.get()) && !((LivingEntity) entityIn).isEntityUndead())
				return;
			entityIn.attackEntityFrom(DamageSource.IN_FIRE, (float) this.fireDamage);
		}

		super.onEntityCollision(state, worldIn, pos, entityIn);
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		FluidState fluidstate = context.getWorld().getFluidState(context.getPos());

		for (Direction direction : context.getNearestLookingDirections()) {
			if (direction.getAxis() == Direction.Axis.Y) {
				BlockState blockstate = this.getDefaultState().with(HANGING, direction == Direction.UP);
				if (blockstate.isValidPosition(context.getWorld(), context.getPos())) {
					boolean water = fluidstate.getFluid() == Fluids.WATER;
					return blockstate.with(WATERLOGGED, water).with(LIT, !water);
				}
			}
		}

		return null;
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}

		return getBlockConnected(stateIn).getOpposite() == facing && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	protected static Direction getBlockConnected(BlockState state) {
		return state.get(HANGING) ? Direction.DOWN : Direction.UP;
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return state.get(HANGING) ? HANGING_SHAPE : GROUNDED_SHAPE;
	}

	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	public static BlockState extinguish(IWorld world, BlockPos pos, BlockState state) {
		if (world.isRemote()) {
			for (int i = 0; i < 20; ++i) {
				spawnSmokeParticles((World) world, pos);
			}
		} else {
			world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
		return state.with(LIT, false);
	}

	public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
		if (!state.get(BlockStateProperties.WATERLOGGED) && fluidStateIn.getFluid() == Fluids.WATER) {
			if (state.get(LIT)) extinguish(worldIn, pos, state);
			worldIn.setBlockState(pos, state.with(WATERLOGGED, true).with(LIT, false), 3);
			worldIn.getPendingFluidTicks().scheduleTick(pos, fluidStateIn.getFluid(), fluidStateIn.getFluid().getTickRate(worldIn));
			return true;
		} else {
			return false;
		}
	}

	public void onProjectileCollision(World worldIn, BlockState state, BlockRayTraceResult hit, ProjectileEntity projectile) {
		if (!worldIn.isRemote && projectile.isBurning()) {
			Entity entity = projectile.func_234616_v_();
			boolean flag = entity == null || entity instanceof PlayerEntity || ForgeEventFactory.getMobGriefingEvent(worldIn, entity);
			if (flag && !state.get(LIT) && !state.get(WATERLOGGED)) {
				BlockPos blockpos = hit.getPos();
				worldIn.setBlockState(blockpos, state.with(BlockStateProperties.LIT, true), 11);
			}
		}

	}

	public static void spawnSmokeParticles(World worldIn, BlockPos pos) {
		Random random = worldIn.getRandom();
		worldIn.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.25D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), pos.getY() + 0.4D, pos.getZ() + 0.25D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), 0.0D, 0.005D, 0.0D);
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		Direction direction = getBlockConnected(state).getOpposite();
		return Block.hasEnoughSolidSide(worldIn, pos.offset(direction), direction.getOpposite());
	}

	public PushReaction getPushReaction(BlockState state) {
		return PushReaction.DESTROY;
	}

	public static boolean isLit(BlockState state) {
		return state.hasProperty(LIT) && state.isIn(CCTags.Blocks.BRAZIERS) && state.get(LIT);
	}

	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(LIT, HANGING, WATERLOGGED);
	}

	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}

	public static boolean canBeLit(BlockState state) {
		return state.isInAndMatches(CCTags.Blocks.BRAZIERS, (stateIn) -> stateIn.hasProperty(BlockStateProperties.WATERLOGGED) && stateIn.hasProperty(BlockStateProperties.LIT)) && !state.get(BlockStateProperties.WATERLOGGED) && !state.get(BlockStateProperties.LIT);
	}

	@Nullable
	@Override
	public PathNodeType getAiPathNodeType(BlockState state, IBlockReader world, BlockPos pos, @Nullable MobEntity entity) {
		return isLit(state) ? PathNodeType.DAMAGE_FIRE : super.getAiPathNodeType(state, world, pos, entity);
	}
}
