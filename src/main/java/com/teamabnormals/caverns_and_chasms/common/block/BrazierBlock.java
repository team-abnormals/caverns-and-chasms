package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;

public class BrazierBlock extends Block implements SimpleWaterloggedBlock {
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	protected static final VoxelShape GROUNDED_SHAPE = Shapes.or(Block.box(1.0D, 4.0D, 1.0D, 15.0D, 9.0D, 15.0D), Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D));
	protected static final VoxelShape HANGING_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 5.0D, 15.0D);

	private final float fireDamage;

	public BrazierBlock(float fireDamage, BlockBehaviour.Properties properties) {
		super(properties);
		this.fireDamage = fireDamage;
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, true).setValue(HANGING, false).setValue(WATERLOGGED, false));
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn) {
		if (!entityIn.fireImmune() && state.getValue(LIT) && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entityIn)) {
			entityIn.hurt(DamageSource.IN_FIRE, this.fireDamage);
		}

		super.entityInside(state, level, pos, entityIn);
	}

	@Nullable
	@Override
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

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.getValue(WATERLOGGED)) {
			level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return getBlockConnected(stateIn).getOpposite() == facing && !stateIn.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
	}

	protected static Direction getBlockConnected(BlockState state) {
		return state.getValue(HANGING) ? Direction.DOWN : Direction.UP;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(HANGING) ? HANGING_SHAPE : GROUNDED_SHAPE;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	public static BlockState extinguish(Entity entity, LevelAccessor level, BlockPos pos, BlockState state) {
		BlockState extinguishedsstate = state.setValue(LIT, false);
		if (level.isClientSide()) {
			for (int i = 0; i < 20; ++i) {
				spawnSmokeParticles((Level) level, pos.offset(0.5D, 0, 0.5D));
			}
		}

		level.gameEvent(entity, GameEvent.BLOCK_CHANGE, pos);

		return extinguishedsstate;
	}

	@Override
	public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidStateIn) {
		if (!state.getValue(BlockStateProperties.WATERLOGGED) && fluidStateIn.getType() == Fluids.WATER) {
			if (state.getValue(LIT)) {
				level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
				extinguish(null, level, pos, state);
			}
			level.setBlock(pos, state.setValue(WATERLOGGED, true).setValue(LIT, false), 3);
			level.scheduleTick(pos, fluidStateIn.getType(), fluidStateIn.getType().getTickDelay(level));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (random.nextInt(10) == 0) {
			level.playLocalSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);
		}
	}

	@Override
	public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
		if (!level.isClientSide && projectile.isOnFire()) {
			Entity entity = projectile.getOwner();
			boolean flag = entity == null || entity instanceof Player || ForgeEventFactory.getMobGriefingEvent(level, entity);
			if (flag && !state.getValue(LIT) && !state.getValue(WATERLOGGED)) {
				BlockPos blockpos = hit.getBlockPos();
				level.setBlock(blockpos, state.setValue(BlockStateProperties.LIT, true), 11);
			}
		}

	}

	public static void spawnSmokeParticles(Level level, BlockPos pos) {
		RandomSource random = level.getRandom();
		level.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), pos.getY() + 0.4D, pos.getZ() + 0.5D + random.nextDouble() / 2.0D * (random.nextBoolean() ? 1 : -1), 0.0D, 0.005D, 0.0D);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		Direction direction = getBlockConnected(state).getOpposite();
		return Block.canSupportCenter(level, pos.relative(direction), direction.getOpposite());
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.DESTROY;
	}

	public static boolean isLit(BlockState state) {
		return state.hasProperty(LIT) && state.is(CCBlockTags.BRAZIERS) && state.getValue(LIT);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LIT, HANGING, WATERLOGGED);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
		return false;
	}

	public static boolean canBeLit(BlockState state) {
		return state.is(CCBlockTags.BRAZIERS, (stateIn) -> stateIn.hasProperty(BlockStateProperties.WATERLOGGED) && stateIn.hasProperty(BlockStateProperties.LIT)) && !state.getValue(BlockStateProperties.WATERLOGGED) && !state.getValue(BlockStateProperties.LIT);
	}

	@Nullable
	@Override
	public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob entity) {
		return isLit(state) ? BlockPathTypes.DAMAGE_FIRE : super.getBlockPathType(state, level, pos, entity);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return state.getValue(LIT) ? 15 : 0;
	}
}
