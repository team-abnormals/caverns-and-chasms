package com.teamabnormals.caverns_and_chasms.common.entity.animal;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.PlayMessages;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiConsumer;

public class Cavefish extends AbstractSchoolingFish implements VibrationSystem {
	private static final Logger LOGGER = LogUtils.getLogger();

	private final DynamicGameEventListener<Listener> dynamicGameEventListener;
	private final VibrationSystem.User vibrationUser;
	private VibrationSystem.Data vibrationData;

	public Cavefish(EntityType<? extends Cavefish> p_30015_, Level p_30016_) {
		super(p_30015_, p_30016_);
		this.vibrationUser = new Cavefish.VibrationUser();
		this.vibrationData = new VibrationSystem.Data();
		this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationSystem.Listener(this));
	}

	public Cavefish(PlayMessages.SpawnEntity message, Level level) {
		this(CCEntityTypes.CAVEFISH.get(), level);
	}

	@Override
	public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> consumer) {
		Level level = this.level();
		if (level instanceof ServerLevel serverlevel) {
			consumer.accept(this.dynamicGameEventListener, serverlevel);
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		VibrationSystem.Data.CODEC.encodeStart(NbtOps.INSTANCE, this.vibrationData).resultOrPartial(LOGGER::error).ifPresent((t) -> tag.put("listener", t));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("listener", 10)) {
			VibrationSystem.Data.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, tag.getCompound("listener"))).resultOrPartial(LOGGER::error).ifPresent((d) -> this.vibrationData = d);
		}
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(CCItems.CAVEFISH_BUCKET.get());
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return CCSoundEvents.CAVEFISH_AMBIENT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CCSoundEvents.CAVEFISH_DEATH.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_30039_) {
		return CCSoundEvents.CAVEFISH_HURT.get();
	}

	@Override
	protected SoundEvent getFlopSound() {
		return CCSoundEvents.CAVEFISH_FLOP.get();
	}

	@Override
	public int getMaxSchoolSize() {
		return 8;
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 8;
	}

	public int getMinSchoolSize() {
		return 6;
	}

	@Override
	public void tick() {
		Level level = this.level();
		if (level instanceof ServerLevel serverlevel) {
			VibrationSystem.Ticker.tick(serverlevel, this.vibrationData, this.vibrationUser);
		}

		super.tick();

		if (this.hasFollowers() && this.isFollower()) {
			if (!tryMergeSchools(this, this.leader)) {
				this.stopFollowing();
			}
		}

		if (!this.isSchoolFull() && this.random.nextInt(50) == 0) {
			List<? extends Cavefish> list = level.getEntitiesOfClass(this.getClass(), this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D),
					cavefish -> cavefish != this &&
							((cavefish.canBeFollowed() && cavefish != this.leader) ||
									(!this.isFollower() && !this.hasFollowers() && cavefish.schoolSize < cavefish.getMaxSchoolSize() && !cavefish.isFollower())));

			for (Cavefish cavefish : list) {
				if ((this.hasFollowers() || this.isFollower()) && tryMergeSchools(this.isFollower() ? this.leader : this, cavefish)) {
					break;
				} else if (!this.isFollower() && !this.hasFollowers()) {
					this.startFollowing(cavefish);
					break;
				}

			}
		}
	}

	public boolean isSchoolFull() {
		return this.hasFollowers() ? this.schoolSize <= this.getMinSchoolSize() : this.isFollower() && this.leader.schoolSize <= this.getMinSchoolSize();
	}

	public static boolean tryMergeSchools(AbstractSchoolingFish from, AbstractSchoolingFish to) {
		if (from.schoolSize + to.schoolSize <= to.getMaxSchoolSize()) {
			List<? extends Cavefish> fromFollowers = from.level().getEntitiesOfClass(Cavefish.class, from.getBoundingBox().inflate(8.0D, 8.0D, 8.0D),
					t -> t.isFollower() && t.leader == from);

			fromFollowers.forEach(follower -> {
				follower.stopFollowing();
				follower.startFollowing(to);
			});

			from.startFollowing(to);
			return true;
		}
		return false;
	}

	@Override
	public void pathToLeader() {
		if (this.isFollower()) {
			this.getNavigation().moveTo(this.leader, 1.25D);
		}
	}

	public static AttributeSupplier.Builder createAttributes() {
		return AbstractFish.createAttributes().add(Attributes.MOVEMENT_SPEED, 2.0D);
	}

	public static boolean checkCavefishSpawnRules(EntityType<Cavefish> cavefish, LevelAccessor level, MobSpawnType type, BlockPos pos, RandomSource random) {
		return pos.getY() <= level.getSeaLevel() - 33 && level.getRawBrightness(pos, 0) == 0 && level.getFluidState(pos.below()).is(FluidTags.WATER) && level.getBlockState(pos.above()).is(Blocks.WATER);
	}

	@Override
	public Data getVibrationData() {
		return this.vibrationData;
	}

	@Override
	public User getVibrationUser() {
		return this.vibrationUser;
	}

	class VibrationUser implements VibrationSystem.User {
		private final PositionSource positionSource = new EntityPositionSource(Cavefish.this, Cavefish.this.getEyeHeight());

		@Override
		public int getListenerRadius() {
			return 16;
		}

		@Override
		public PositionSource getPositionSource() {
			return this.positionSource;
		}

		@Override
		public TagKey<GameEvent> getListenableEvents() {
			return GameEventTags.VIBRATIONS;
		}

		@Override
		public boolean canTriggerAvoidVibration() {
			return true;
		}

		@Override
		public boolean canReceiveVibration(ServerLevel level, BlockPos pos, GameEvent event, GameEvent.Context context) {
			if (!Cavefish.this.isNoAi() && !Cavefish.this.isDeadOrDying() && level.getWorldBorder().isWithinBounds(pos) && !Cavefish.this.isFollower()) {
				Entity entity = context.sourceEntity();
				if (entity instanceof LivingEntity living) {
					if (living instanceof WaterAnimal) {
						return false;
					}
				}

				return true;
			} else {
				return false;
			}
		}

		@Override
		public void onReceiveVibration(ServerLevel level, BlockPos pos, GameEvent event, @Nullable Entity entity, @Nullable Entity source, float f) {
			if (!Cavefish.this.isDeadOrDying()) {
				if (source != null) {
					if (Cavefish.this.closerThan(source, 30.0D)) {
						int freq = VibrationSystem.getGameEventFrequency(event);
						Vec3 newPos = DefaultRandomPos.getPos(Cavefish.this, freq, 4);
						if (newPos != null) {
							Cavefish.this.getNavigation().moveTo(newPos.x, newPos.y, newPos.z, 1.0D + 0.02D * freq);
						}
					}
				}
			}
		}
	}
}