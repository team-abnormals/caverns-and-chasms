package com.minecraftabnormals.caverns_and_chasms.common.entity.zombie;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntities;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.UUID;

public class ZombieParrotEntity extends ParrotEntity {
	private static final DataParameter<Boolean> CONVERTING = EntityDataManager.createKey(ZombieParrotEntity.class, DataSerializers.BOOLEAN);
	private int conversionTime;
	private UUID converstionStarter;

	public ZombieParrotEntity(EntityType<? extends ZombieParrotEntity> type, World worldIn) {
		super(type, worldIn);
	}

	protected void registerData() {
		super.registerData();
		this.dataManager.register(CONVERTING, false);
	}

	@Override
	public ZombieParrotEntity func_241840_a(ServerWorld world, AgeableEntity entity) {
		ZombieParrotEntity parrot = CCEntities.ZOMBIE_PARROT.get().create(world);
		if (this.rand.nextBoolean()) {
			parrot.setVariant(this.getVariant());
		} else {
			parrot.setVariant(parrot.getVariant());
		}

		if (this.isTamed()) {
			parrot.setOwnerId(this.getOwnerId());
			parrot.setTamed(true);
		}

		return parrot;
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putInt("ConversionTime", this.isConverting() ? this.conversionTime : -1);
		if (this.converstionStarter != null) {
			compound.putUniqueId("ConversionPlayer", this.converstionStarter);
		}
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		if (compound.contains("ConversionTime", 99) && compound.getInt("ConversionTime") > -1) {
			this.startConverting(compound.hasUniqueId("ConversionPlayer") ? compound.getUniqueId("ConversionPlayer") : null, compound.getInt("ConversionTime"));
		}
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.UNDEAD;
	}

	@Override
	public void tick() {
		if (!this.world.isRemote && this.isAlive() && this.isConverting()) {
			int i = this.getConversionProgress();
			this.conversionTime -= i;
			if (this.conversionTime <= 0 && ForgeEventFactory.canLivingConvert(this, EntityType.PARROT, (timer) -> this.conversionTime = timer)) {
				this.cureZombie((ServerWorld) this.world);
			}
		}

		super.tick();
	}

	@Override
	public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		if (itemstack.getItem() == Items.GOLDEN_APPLE) {
			if (this.isPotionActive(Effects.WEAKNESS)) {
				if (!player.abilities.isCreativeMode) {
					itemstack.shrink(1);
				}
				if (!this.world.isRemote) {
					this.startConverting(player.getUniqueID(), this.rand.nextInt(2401) + 3600);
				}
				return ActionResultType.SUCCESS;
			} else {
				return ActionResultType.CONSUME;
			}
		} else {
			return super.func_230254_b_(player, hand);
		}
	}

	public boolean isConverting() {
		return this.getDataManager().get(CONVERTING);
	}

	private void startConverting(@Nullable UUID conversionStarterIn, int conversionTimeIn) {
		this.converstionStarter = conversionStarterIn;
		this.conversionTime = conversionTimeIn;
		this.getDataManager().set(CONVERTING, true);
		this.removePotionEffect(Effects.WEAKNESS);
		this.addPotionEffect(new EffectInstance(Effects.STRENGTH, conversionTimeIn, Math.min(this.world.getDifficulty().getId() - 1, 0)));
		this.world.setEntityState(this, (byte) 16);
	}

	@OnlyIn(Dist.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 16) {
			if (!this.isSilent()) {
				this.world.playSound(this.getPosX(), this.getPosYEye(), this.getPosZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, this.getSoundCategory(), 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
			}

		} else {
			super.handleStatusUpdate(id);
		}
	}

	private void cureZombie(ServerWorld world) {
		ParrotEntity parrotEntity = this.copyEntityData();
		parrotEntity.onInitialSpawn(world, world.getDifficultyForLocation(parrotEntity.getPosition()), SpawnReason.CONVERSION, null, null);
		parrotEntity.setVariant(this.getVariant());
		parrotEntity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 200, 0));
		if (!this.isSilent()) {
			world.playEvent(null, 1027, this.getPosition(), 0);
		}

		ForgeEventFactory.onLivingConvert(this, parrotEntity);
	}

	public ParrotEntity copyEntityData() {
		ParrotEntity parrot = this.func_233656_b_(EntityType.PARROT, false);
		parrot.setTamed(this.isTamed());
		parrot.func_233687_w_(this.isSitting());
		if (this.getOwner() != null)
			parrot.setOwnerId(this.getOwner().getUniqueID());
		return parrot;
	}

	private int getConversionProgress() {
		int i = 1;
		if (this.rand.nextFloat() < 0.01F) {
			int j = 0;
			BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

			for (int k = (int) this.getPosX() - 4; k < (int) this.getPosX() + 4 && j < 14; ++k) {
				for (int l = (int) this.getPosY() - 4; l < (int) this.getPosY() + 4 && j < 14; ++l) {
					for (int i1 = (int) this.getPosZ() - 4; i1 < (int) this.getPosZ() + 4 && j < 14; ++i1) {
						Block block = this.world.getBlockState(blockpos$mutable.setPos(k, l, i1)).getBlock();
						if (block.isIn(BlockTags.CARPETS) || block instanceof BedBlock) {
							if (this.rand.nextFloat() < 0.3F) {
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