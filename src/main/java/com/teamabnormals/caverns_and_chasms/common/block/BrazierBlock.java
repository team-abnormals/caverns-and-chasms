package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.Random;

public class BrazierBlock extends Block implements SimpleWaterloggedBlock {
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	protected static final VoxelShape GROUNDED_SHAPE = Shapes.or(Block.box(0.0D, 4.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D));
	protected static final VoxelShape HANGING_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);

	private final int fireDamage;

	public BrazierBlock(int fireDamage, BlockBehaviour.Properties properties) {
		super(properties);
		this.fireDamage = fireDamage;
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, true).setValue(HANGING, false).setValue(WATERLOGGED, false));
	}

	public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
		if (!entityIn.fireImmune() && state.getValue(LIT) && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entityIn)) {
			if (state.is(CCBlocks.CURSED_BRAZIER.get()) && !((LivingEntity) entityIn).isInvertedHealAndHarm())
				return;
			entityIn.hurt(DamageSource.IN_FIRE, (float) this.fireDamage);
		}

		super.entityInside(state, worldIn, pos, entityIn);
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		for (Direction direction : context.getNearestLookingDirections()) {
			if (direction.getAxis() == Direction.Axis.Y) {
				BlockState blockstate = this.defaultBlockState().setValue(HANGING, direction == Direction.UP);
				if (blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
					boolean water = fluidstate.getType() == Fluids.WATER;
					return blockstate.setValue(WATERLOGGED, water).setValue(LIT, !water);
				}
			}
		}

		return null;
	}

	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.getValue(WATERLOGGED)) {
			worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
		}

		return getBlockConnected(stateIn).getOpposite() == facing && !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	protected static Direction getBlockConnected(BlockState state) {
		return state.getValue(HANGING) ? Direction.DOWN : Direction.UP;
	}

	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return state.getValue(HANGING) ? HANGING_SHAPE : GROUNDED_SHAPE;
	}

	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	public static BlockState extinguish(LevelAccessor world, BlockPos pos, BlockState state) {
		if (world.isClientSide()) {
			for (int i = 0; i < 20; ++i) {
				spawnSmokeParticles((Level) world, pos);
			}
		} else {
			world.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
		return state.setValue(LIT, false);
	}

	public boolean placeLiquid(LevelAccessor worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
		if (!state.getValue(BlockStateProperties.WATERLOGGED) && fluidStateIn.getType() == Fluids.WATER) {
			if (state.getValue(LIT)) extinguish(worldIn, pos, state);
			worldIn.setBlock(pos, state.setValue(WATERLOGGED, true).setValue(LIT, false), 3);
			worldIn.scheduleTick(pos, fluidStateIn.getType(), fluidStateIn.getType().getTickDelay(worldIn));
			return true;
		} else {
			return false;
		}
	}

	public void onProjectileHit(Level worldIn, BlockState state, BlockHitResult hit, Projectile projectile) {
		if (!worldIn.isClientSide && projectile.isOnFire()) {
			Entity entity = projectile.getOwner();
			boolean flag = entity == null || entity instanceof Player || ForgeEventFactory.getMobGriefingEvent(worldIn, entity);
			if (flag && !state.getValue(LIT) && !state.getValue(WATERLOGGED)) {
				BlockPos blockpos = hit.getBlockPos();
				worldIn.setBlock(blockpos, state.setValue(BlockStateProperties.LIT, true), 11);
			}
		}

	}

	public static void spawnSmokeParticles(Level worldIn, BlockPos pos) {
		Random random = worldIn.getRandom();
		worldIn.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.25D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), pos.getY() + 0.4D, pos.getZ() + 0.25D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), 0.0D, 0.005D, 0.0D);
	}

	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		Direction direction = getBlockConnected(state).getOpposite();
		return Block.canSupportCenter(worldIn, pos.relative(direction), direction.getOpposite());
	}

	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.DESTROY;
	}

	public static boolean isLit(BlockState state) {
		return state.hasProperty(LIT) && state.is(CCBlockTags.BRAZIERS) && state.getValue(LIT);
	}

	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LIT, HANGING, WATERLOGGED);
	}

	public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
		return false;
	}

	public static boolean canBeLit(BlockState state) {
		return state.is(CCBlockTags.BRAZIERS, (stateIn) -> stateIn.hasProperty(BlockStateProperties.WATERLOGGED) && stateIn.hasProperty(BlockStateProperties.LIT)) && !state.getValue(BlockStateProperties.WATERLOGGED) && !state.getValue(BlockStateProperties.LIT);
	}

	@Nullable
	@Override
	public BlockPathTypes getAiPathNodeType(BlockState state, BlockGetter world, BlockPos pos, @Nullable Mob entity) {
		return isLit(state) ? BlockPathTypes.DAMAGE_FIRE : super.getAiPathNodeType(state, world, pos, entity);
	}
}
