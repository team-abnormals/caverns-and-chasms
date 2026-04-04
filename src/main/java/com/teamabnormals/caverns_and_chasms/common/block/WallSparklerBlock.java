package com.teamabnormals.caverns_and_chasms.common.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.function.Supplier;

public class WallSparklerBlock extends WallTorchBlock implements Sparkler {
	private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, Block.box(5.5D, 3.0D, 11.0D, 10.5D, 15.0D, 16.0D),
			Direction.SOUTH, Block.box(5.5D, 3.0D, 0.0D, 10.5D, 15.0D, 5.0D),
			Direction.WEST, Block.box(11.0D, 3.0D, 5.5D, 16.0D, 15.0D, 10.5D),
			Direction.EAST, Block.box(0.0D, 3.0D, 5.5D, 5.0D, 15.0D, 10.5D)
	));
	protected final Pair<RegistryObject<SimpleParticleType>, RegistryObject<SimpleParticleType>> particle;

	public WallSparklerBlock(Properties properties, Pair<RegistryObject<SimpleParticleType>, RegistryObject<SimpleParticleType>> particle) {
		super(properties, ParticleTypes.FLAME);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
		this.particle = particle;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return AABBS.get(state.getValue(FACING));
	}

	@Override
	public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
		this.onProjectileHitSparkler(level, state, hit.getBlockPos(), projectile);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		return this.useSparkler(state, level, pos, player, hand);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		this.entityInsideSparkler(state, level, pos, entity);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		this.animateTickSparkler(state, level, pos, random);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LIT);
	}

	@Override
	public Supplier<? extends ParticleOptions> getParticle() {
		return this.particle.getFirst();
	}

	@Override
	public Supplier<? extends ParticleOptions> getParticleEmitter() {
		return this.particle.getSecond();
	}
}