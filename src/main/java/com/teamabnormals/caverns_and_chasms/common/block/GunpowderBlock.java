package com.teamabnormals.caverns_and_chasms.common.block;

import com.teamabnormals.caverns_and_chasms.common.level.CustomExplosion;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class GunpowderBlock extends FallingBlock {
	public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;

	public GunpowderBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(UNSTABLE, false));
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		ItemStack stack = player.getItemInHand(hand);
		if (!stack.is(Items.FLINT_AND_STEEL) && !stack.is(Items.FIRE_CHARGE)) {
			return super.use(state, level, pos, player, hand, result);
		} else {
			this.onCaughtFire(state, level, pos, result.getDirection(), player);
			level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
			Item item = stack.getItem();
			if (!player.isCreative()) {
				if (stack.is(Items.FLINT_AND_STEEL)) {
					stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
				} else {
					stack.shrink(1);
				}
			}

			player.awardStat(Stats.ITEM_USED.get(item));
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
	}

	@Override
	public void onProjectileHit(Level level, BlockState state, BlockHitResult result, Projectile projectile) {
		if (!level.isClientSide) {
			BlockPos pos = result.getBlockPos();
			Entity entity = projectile.getOwner();
			if (projectile.isOnFire() && projectile.mayInteract(level, pos)) {
				this.onCaughtFire(state, level, pos, null, entity instanceof LivingEntity living ? living : null);
				level.removeBlock(pos, false);
			}
		}
	}

	@Override
	public boolean dropFromExplosion(Explosion explosion) {
		return false;
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState otherState, boolean b) {
		if (!otherState.is(state.getBlock())) {
			if (level.hasNeighborSignal(pos)) {
				this.onCaughtFire(state, level, pos, null, null);
				level.removeBlock(pos, false);
			}
		}
		super.onPlace(state, level, pos, otherState, b);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos otherPos, boolean b) {
		if (level.hasNeighborSignal(pos)) {
			this.onCaughtFire(state, level, pos, null, null);
			level.removeBlock(pos, false);
		}
	}

	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player p_57448_) {
		if (!level.isClientSide() && !p_57448_.isCreative() && state.getValue(UNSTABLE)) {
			this.onCaughtFire(state, level, pos, null, null);
		}

		super.playerWillDestroy(level, pos, state, p_57448_);
	}

	@Override
	public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
		explode(level, pos, explosion.getIndirectSourceEntity());
	}

	@Override
	public void onCaughtFire(BlockState state, Level world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
		explode(world, pos, igniter);
	}

	public static void explode(Level level, BlockPos pos) {
		explode(level, pos, null);
	}

	public static void explode(Level level, BlockPos pos, @Nullable LivingEntity igniter) {
		if (!level.isClientSide) {
			CustomExplosion.spawnExplosion(level, null, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, 4.0F, false, BlockInteraction.DESTROY, CCSoundEvents.GUNPOWDER_EXPLODE.get(), CCParticleTypes.LARGE_SMOKE_EMITTER.get(), ParticleTypes.LARGE_SMOKE);
		}
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		if (level.isClientSide() && (entity.getDeltaMovement().x > 0 || entity.getDeltaMovement().z > 0)) {
			Supplier<Vec3> supplier = () -> new Vec3(Mth.nextDouble(level.getRandom(), -0.005F, 0.005F), Mth.nextDouble(level.getRandom(), -0.005F, 0.005F), Mth.nextDouble(level.getRandom(), -0.005F, 0.005F));
			ParticleUtils.spawnParticlesOnBlockFace(level, pos, ParticleTypes.SMOKE, ConstantInt.of(1), Direction.UP, supplier, 0.5D);
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (random.nextInt(5) == 0) {
			BlockPos belowPos = pos.below();
			if (FallingBlock.isFree(level.getBlockState(belowPos))) {
				ParticleUtils.spawnParticleBelow(level, pos, random, ParticleTypes.ASH);
			}
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(UNSTABLE);
	}
}
