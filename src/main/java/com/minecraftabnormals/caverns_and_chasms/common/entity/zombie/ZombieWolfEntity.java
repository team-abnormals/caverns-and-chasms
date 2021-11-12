package com.minecraftabnormals.caverns_and_chasms.common.entity.zombie;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.UUID;

public class ZombieWolfEntity extends Wolf {
	private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(ZombieWolfEntity.class, EntityDataSerializers.BOOLEAN);
	private int conversionTime;
	private UUID converstionStarter;

	public ZombieWolfEntity(EntityType<? extends ZombieWolfEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(CONVERTING, false);
	}

	@Override
	public ZombieWolfEntity getBreedOffspring(ServerLevel world, AgeableMob entity) {
		ZombieWolfEntity wolf = CCEntityTypes.ZOMBIE_WOLF.get().create(world);
		UUID uuid = this.getOwnerUUID();
		if (uuid != null) {
			wolf.setOwnerUUID(uuid);
			wolf.setTame(true);
		}

		return wolf;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("ConversionTime", this.isConverting() ? this.conversionTime : -1);
		if (this.converstionStarter != null) {
			compound.putUUID("ConversionPlayer", this.converstionStarter);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("ConversionTime", 99) && compound.getInt("ConversionTime") > -1) {
			this.startConverting(compound.hasUUID("ConversionPlayer") ? compound.getUUID("ConversionPlayer") : null, compound.getInt("ConversionTime"));
		}
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}

	@Override
	public void tick() {
		if (!this.level.isClientSide && this.isAlive() && this.isConverting()) {
			int i = this.getConversionProgress();
			this.conversionTime -= i;
			if (this.conversionTime <= 0 && ForgeEventFactory.canLivingConvert(this, EntityType.WOLF, (timer) -> this.conversionTime = timer)) {
				this.cureZombie((ServerLevel) this.level);
			}
		}

		super.tick();
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.getItem() == Items.GOLDEN_APPLE) {
			if (this.hasEffect(MobEffects.WEAKNESS)) {
				if (!player.getAbilities().instabuild) {
					itemstack.shrink(1);
				}

				if (!this.level.isClientSide) {
					this.startConverting(player.getUUID(), this.random.nextInt(2401) + 3600);
				}

				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.CONSUME;
			}
		} else {
			return super.mobInteract(player, hand);
		}
	}

	public boolean isConverting() {
		return this.getEntityData().get(CONVERTING);
	}

	private void startConverting(@Nullable UUID conversionStarterIn, int conversionTimeIn) {
		this.converstionStarter = conversionStarterIn;
		this.conversionTime = conversionTimeIn;
		this.getEntityData().set(CONVERTING, true);
		this.removeEffect(MobEffects.WEAKNESS);
		this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, conversionTimeIn, Math.min(this.level.getDifficulty().getId() - 1, 0)));
		this.level.broadcastEntityEvent(this, (byte) 16);
	}

	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte id) {
		if (id == 16) {
			if (!this.isSilent()) {
				this.level.playLocalSound(this.getX(), this.getEyeY(), this.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, this.getSoundSource(), 1.0F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
			}

		} else {
			super.handleEntityEvent(id);
		}
	}

	private void cureZombie(ServerLevel world) {
		Wolf wolfEntity = this.copyEntityData();
		wolfEntity.finalizeSpawn(world, world.getCurrentDifficultyAt(wolfEntity.blockPosition()), MobSpawnType.CONVERSION, null, null);
		wolfEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
		if (!this.isSilent()) {
			world.levelEvent(null, 1027, this.blockPosition(), 0);
		}

		ForgeEventFactory.onLivingConvert(this, wolfEntity);
	}

	public Wolf copyEntityData() {
		Wolf wolf = this.convertTo(EntityType.WOLF, false);
		wolf.setCollarColor(this.getCollarColor());
		wolf.setTame(this.isTame());
		wolf.setOrderedToSit(this.isOrderedToSit());
		if (this.getOwner() != null)
			wolf.setOwnerUUID(this.getOwner().getUUID());
		return wolf;
	}

	private int getConversionProgress() {
		int i = 1;
		if (this.random.nextFloat() < 0.01F) {
			int j = 0;
			BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

			for (int k = (int) this.getX() - 4; k < (int) this.getX() + 4 && j < 14; ++k) {
				for (int l = (int) this.getY() - 4; l < (int) this.getY() + 4 && j < 14; ++l) {
					for (int i1 = (int) this.getZ() - 4; i1 < (int) this.getZ() + 4 && j < 14; ++i1) {
						BlockState state = this.level.getBlockState(mutableBlockPos.set(k, l, i1));
						if (state.is(BlockTags.CARPETS) || state.getBlock() instanceof BedBlock) {
							if (this.random.nextFloat() < 0.3F) {
								++i;
							}
							++j;
						}
					}
				}
			}
		}
		return i;
	}
}