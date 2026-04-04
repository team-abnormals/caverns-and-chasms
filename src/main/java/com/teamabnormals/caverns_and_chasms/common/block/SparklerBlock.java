package com.teamabnormals.caverns_and_chasms.common.block;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SparklerBlock extends TorchBlock implements Sparkler {
	protected static final VoxelShape AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 12.0D, 10.0D);
	protected final Pair<RegistryObject<SimpleParticleType>, RegistryObject<SimpleParticleType>> particle;

	public SparklerBlock(Properties properties, Pair<RegistryObject<SimpleParticleType>, RegistryObject<SimpleParticleType>> particle) {
		super(properties, ParticleTypes.FLAME);
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
		this.particle = particle;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return AABB;
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