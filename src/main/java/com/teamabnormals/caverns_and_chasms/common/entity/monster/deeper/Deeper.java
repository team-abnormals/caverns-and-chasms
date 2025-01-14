package com.teamabnormals.caverns_and_chasms.common.entity.monster.deeper;

import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCBiomeTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.ToolActions;
import org.apache.commons.compress.utils.Lists;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class Deeper extends Creeper implements Shearable, IForgeShearable {
	private static final EntityDataAccessor<Integer> HAT = SynchedEntityData.defineId(Deeper.class, EntityDataSerializers.INT);

	public Deeper(EntityType<? extends Deeper> type, Level worldIn) {
		super(type, worldIn);
		this.explosionRadius = 4;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(HAT, 0);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 25.0D).add(Attributes.MOVEMENT_SPEED, 0.24D);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return CCSoundEvents.ENTITY_DEEPER_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CCSoundEvents.ENTITY_DEEPER_DEATH.get();
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putString("Hat", this.getHat().getSerializedName());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setHat(DeeperHat.byName(compound.getString("Hat")));
	}

	public DeeperHat getHat() {
		return DeeperHat.byId(this.entityData.get(HAT));
	}

	public void setHat(DeeperHat hat) {
		this.entityData.set(HAT, hat.getId());
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (player.isCreative() && this.getHat() == DeeperHat.NONE) {
			ItemStack itemstack = player.getItemInHand(hand);
			DeeperHat hat = DeeperHat.byItem(itemstack.getItem());

			if (hat != DeeperHat.NONE) {
				this.level().playSound(null, this, SoundEvents.GRASS_PLACE, SoundSource.PLAYERS, 1.0F, 1.0F);
				if (!this.level().isClientSide)
					this.setHat(hat);
				return InteractionResult.sidedSuccess(this.level().isClientSide);
			}
		}

		return super.mobInteract(player, hand);
	}

	@Override
	public void shear(SoundSource soundSource) {
		// TODO: Replace SoundEvent
		this.level().playSound(null, this, SoundEvents.SNOW_GOLEM_SHEAR, soundSource, 1.0F, 1.0F);
		if (!this.level().isClientSide()) {
			this.spawnAtLocation(new ItemStack(this.getHat().getItem()), 1.7F);
			this.setHat(DeeperHat.NONE);
		}
	}

	@Override
	public List<ItemStack> onSheared(Player player, ItemStack item, Level world, BlockPos pos, int fortune) {
		world.playSound(null, this, SoundEvents.SNOW_GOLEM_SHEAR, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0F, 1.0F);
		this.gameEvent(GameEvent.SHEAR, player);
		if (!world.isClientSide()) {
			ItemStack itemstack = new ItemStack(this.getHat().getItem());
			this.setHat(DeeperHat.NONE);
			return Collections.singletonList(itemstack);
		}
		return Collections.emptyList();
	}

	@Override
	public boolean readyForShearing() {
		return this.isAlive() && this.getHat() != DeeperHat.NONE;
	}

	@Override
	public boolean isShearable(ItemStack stack, Level level, BlockPos pos) {
		return this.readyForShearing();
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.getEntity() instanceof LivingEntity entity) {
			if (entity.getMainHandItem().canPerformAction(ToolActions.PICKAXE_DIG))
				amount *= 3.0F;
		}
		return super.hurt(source, amount);
	}

	@Override
	public void explodeCreeper() {
		if (!this.level().isClientSide) {
			float f = this.isPowered() ? 2.0F : 1.0F;
			this.dead = true;
			this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float) this.explosionRadius * f, this.isOnFire(), CCConfig.COMMON.deepersDropAllBlocks.get() ? ExplosionInteraction.MOB : ExplosionInteraction.NONE);
			this.discard();
			this.spawnLingeringCloud();
		}
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource source, int p_34292_, boolean p_34293_) {
		for (EquipmentSlot equipmentslot : EquipmentSlot.values()) {
			ItemStack itemstack = this.getItemBySlot(equipmentslot);
			float f = this.getEquipmentDropChance(equipmentslot);
			boolean flag = f > 1.0F;
			if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack) && (p_34293_ || flag) && Math.max(this.random.nextFloat() - (float) p_34292_ * 0.01F, 0.0F) < f) {
				if (!flag && itemstack.isDamageableItem()) {
					itemstack.setDamageValue(itemstack.getMaxDamage() - this.random.nextInt(1 + this.random.nextInt(Math.max(itemstack.getMaxDamage() - 3, 1))));
				}

				this.spawnAtLocation(itemstack);
				this.setItemSlot(equipmentslot, ItemStack.EMPTY);
			}
		}

		Entity entity = source.getEntity();
		if (entity instanceof Creeper creeper) {
			if (creeper.canDropMobsSkull()) {
				ItemStack itemstack = this.getSkull();
				if (!itemstack.isEmpty()) {
					creeper.increaseDroppedSkulls();
					this.spawnAtLocation(itemstack);
				}
			}
		}
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag compound) {
		if (level.getRandom().nextFloat() < 0.05F + level.getMoonBrightness() * 0.05F) {
			if (level.getRandom().nextFloat() < 0.1F) {
				this.setHat(DeeperHat.MOSCHATEL);
			} else if (level.getRandom().nextFloat() < 0.7F) {
				this.setHat(DeeperHat.STANDARD);
			} else {
				List<DeeperHat> possibleHats = Lists.newArrayList();
				BlockPos blockpos = this.blockPosition();
				Holder<Biome> biome = level.getBiome(blockpos);

				if (blockpos.getY() < 0 || (biome.is(CCBiomeTags.HAS_GRAINY_CAVE_GROWTHS) && !biome.is(CCBiomeTags.WITHOUT_GRAINY_CAVE_GROWTHS)))
					possibleHats.add(DeeperHat.GRAINY);

				if (biome.is(CCBiomeTags.HAS_ZESTY_CAVE_GROWTHS) && !biome.is(CCBiomeTags.WITHOUT_ZESTY_CAVE_GROWTHS))
					possibleHats.add(DeeperHat.ZESTY);

				if (biome.is(CCBiomeTags.HAS_WISPY_CAVE_GROWTHS) && !biome.is(CCBiomeTags.WITHOUT_WISPY_CAVE_GROWTHS))
					possibleHats.add(DeeperHat.WISPY);

				if (biome.is(CCBiomeTags.HAS_LURID_CAVE_GROWTHS) && !biome.is(CCBiomeTags.WITHOUT_LURID_CAVE_GROWTHS))
					possibleHats.add(DeeperHat.LURID);

				if (!possibleHats.isEmpty())
					this.setHat(possibleHats.get(level.getRandom().nextInt(possibleHats.size())));
				else
					this.setHat(DeeperHat.STANDARD);
			}
		}

		return super.finalizeSpawn(level, difficulty, spawnType, groupData, compound);
	}

	protected ItemStack getSkull() {
		return new ItemStack(CCItems.DEEPER_HEAD.get());
	}
}