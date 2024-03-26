package com.teamabnormals.caverns_and_chasms.common.entity;

import com.teamabnormals.blueprint.core.util.MathUtil;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCInstrumentTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class LostGoat extends Mob {
	public static final TargetingConditions TARGETING_CONDITIONS = TargetingConditions.forNonCombat().selector(entity -> entity instanceof Player);

	public LostGoat(EntityType<? extends LostGoat> entity, Level level) {
		super(entity, level);
	}

	@Nullable
	@Override
	public SoundEvent getAmbientSound() {
		return null;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return null;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return null;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected void doPush(Entity entity) {
	}

	@Override
	protected void pushEntities() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.2F).add(Attributes.ATTACK_DAMAGE, 2.0D);
	}

	@Override
	public void aiStep() {
		if (!this.level.getNearbyPlayers(TARGETING_CONDITIONS, this, this.getBoundingBox().inflate(3.0D, 2.0D, 3.0D)).isEmpty()) {
			this.turnIntoSilverfish();
		}
		super.aiStep();
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else {
			this.turnIntoSilverfish();
		}
		return super.hurt(source, amount);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_29835_, DifficultyInstance p_29836_, MobSpawnType p_29837_, @Nullable SpawnGroupData p_29838_, @Nullable CompoundTag p_29839_) {
		this.setYBodyRot(this.random.nextFloat() * 360.0F);
		return super.finalizeSpawn(p_29835_, p_29836_, p_29837_, p_29838_, p_29839_);
	}

	public void turnIntoSilverfish() {
		if (this.level.isClientSide()) {
			this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.AMBIENT_CAVE, SoundSource.NEUTRAL, 20.0F, 20.0F + this.random.nextFloat() * 2.0F, false);
		} else if (this.level instanceof ServerLevel serverLevel) {
			MobEffectInstance darkness = new MobEffectInstance(MobEffects.DARKNESS, 260, 0, false, false);
			MobEffectUtil.addEffectToPlayersAround(serverLevel, this, this.position(), 8, darkness, 200);
		}

		int count = 12 + random.nextInt(4) + random.nextInt(4) + random.nextInt(4) + random.nextInt(4);
		for (int i = 0; i < count; i++) {
			Silverfish silverfish = EntityType.SILVERFISH.create(level);
			silverfish.moveTo(this.getX() + MathUtil.makeNegativeRandomly(random.nextDouble() * 0.5D, random), this.getY(), this.getZ() + MathUtil.makeNegativeRandomly(random.nextDouble() * 0.5D, random), 0.0F, 0.0F);
			level.addFreshEntity(silverfish);
			silverfish.spawnAnim();
		}
		this.convertTo(EntityType.SILVERFISH, true);

		this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(1.0D), this.getZ(), this.createHorn()));
	}

	public ItemStack createHorn() {
		RandomSource random = RandomSource.create(this.getUUID().hashCode());
		HolderSet<Instrument> holderSet = Registry.INSTRUMENT.getOrCreateTag(CCInstrumentTags.LOST_GOAT_HORNS);
		return InstrumentItem.create(CCItems.LOST_GOAT_HORN.get(), holderSet.getRandomElement(random).get());
	}

	@Override
	public MobType getMobType() {
		return MobType.ARTHROPOD;
	}

	public static boolean checkLostGoatSpawnRules(EntityType<? extends LostGoat> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
		return pos.getY() > 80 && level.getDifficulty() != Difficulty.PEACEFUL && !level.canSeeSky(pos) && level.getRawBrightness(pos, 0) == 0 && random.nextInt(5) == 0;
	}
}