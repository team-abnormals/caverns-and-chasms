package com.teamabnormals.caverns_and_chasms.common.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.teamabnormals.blueprint.core.util.MathUtil;
import com.teamabnormals.caverns_and_chasms.common.level.CustomExplosion;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class SparklerWallBlock extends WallTorchBlock {
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, Block.box(5.5D, 3.0D, 11.0D, 10.5D, 15.0D, 16.0D),
			Direction.SOUTH, Block.box(5.5D, 3.0D, 0.0D, 10.5D, 15.0D, 5.0D),
			Direction.WEST, Block.box(11.0D, 3.0D, 5.5D, 16.0D, 15.0D, 10.5D),
			Direction.EAST, Block.box(0.0D, 3.0D, 5.5D, 5.0D, 15.0D, 10.5D)
	));

	public SparklerWallBlock(Properties properties) {
		super(properties, ParticleTypes.FLAME);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return AABBS.get(state.getValue(FACING));
	}


	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (player.getAbilities().mayBuild && player.getItemInHand(hand).isEmpty() && state.getValue(LIT)) {
			Vec3 vec3 = particlePos(state, pos, 0.0D, 0.0D);
			level.addParticle(ParticleTypes.SMOKE, vec3.x, vec3.y, vec3.z, 0.0D, 0.1F, 0.0D);
			level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
			if (!level.isClientSide) {
				if (level.random.nextFloat() > 0.25F) {
					level.setBlock(pos, state.setValue(LIT, false), 11);
				} else {
					CustomExplosion.spawnExplosion(level, null, vec3.x, vec3.y, vec3.z, 1.0F, false, BlockInteraction.KEEP, CCSoundEvents.SPARKLER_EXPLODE.get(), CCParticleTypes.SPARKLER_SPARK_EMITTER.get(), CCParticleTypes.SPARKLER_SPARK_EMITTER.get());
					level.destroyBlock(pos, false);
				}
			}
			level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
			return InteractionResult.sidedSuccess(level.isClientSide);
		} else {
			return InteractionResult.PASS;
		}
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (entity instanceof LivingEntity living && !level.isClientSide() && state.getValue(LIT) && (living.xOld != living.getX() || living.zOld != living.getZ()) && living.getRandom().nextFloat() < 0.1F) {
			Vec3 vec3 = particlePos(state, pos, 0.0D, 0.0D);
			CustomExplosion.spawnExplosion(level, null, vec3.x, vec3.y, vec3.z, 1.0F, false, BlockInteraction.KEEP, CCSoundEvents.SPARKLER_EXPLODE.get(), CCParticleTypes.SPARKLER_SPARK_EMITTER.get(), CCParticleTypes.SPARKLER_SPARK_EMITTER.get());
			level.destroyBlock(pos, false);
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (state.getValue(LIT)) {
			if (random.nextInt(24) == 0) {
				level.playLocalSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, CCSoundEvents.SPARKLER_SPARKLE.get(), SoundSource.BLOCKS, 0.4F, 1.0F, false);
			}

			Vec3 vec3 = particlePos(state, pos, 0.0D, 0.0D);
			for (int i = 0; i < 2; i++) {
				level.addParticle(CCParticleTypes.SPARKLER_SPARK.get(),
						vec3.x + MathUtil.makeNegativeRandomly(random.nextFloat() * 0.05D, random),
						vec3.y + MathUtil.makeNegativeRandomly(random.nextFloat() * 0.025D, random),
						vec3.z + MathUtil.makeNegativeRandomly(random.nextFloat() * 0.05D, random),
						0.0D, 0.0D, 0.0D);
			}
		}
	}

	public static Vec3 particlePos(BlockState state, BlockPos pos, double xzOffset, double yOffset) {
		Direction facing = state.getValue(FACING);
		Direction opposite = facing.getOpposite();
		double x = (double) pos.getX() + 0.5D;
		double y = (double) pos.getY() + 0.78D;
		double z = (double) pos.getZ() + 0.5D;

		double offsetY = 0.15D;
		double offsetXZ = 0.2D;

		return new Vec3(
				x + offsetXZ * (double) opposite.getStepX() + xzOffset,
				y + offsetY + yOffset,
				z + offsetXZ * (double) opposite.getStepZ() + xzOffset
		);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LIT);
	}
}