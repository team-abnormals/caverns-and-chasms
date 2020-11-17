package com.minecraftabnormals.caverns_and_chasms.common.entity;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntities;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class RottenEggEntity extends ProjectileItemEntity implements IRendersAsItem {

	public RottenEggEntity(EntityType<? extends RottenEggEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public RottenEggEntity(World worldIn, LivingEntity throwerIn) {
		super(CCEntities.ROTTEN_EGG.get(), throwerIn, worldIn);
	}

	public RottenEggEntity(World worldIn, double x, double y, double z) {
		super(CCEntities.ROTTEN_EGG.get(), x, y, z, worldIn);
	}

	public RottenEggEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		this(CCEntities.ROTTEN_EGG.get(), world);
	}

	@OnlyIn(Dist.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; ++i) {
				this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, this.getItem()), this.getPosX(), this.getPosY(), this.getPosZ(), ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D);
			}
		}
	}

	@Override
	protected void onEntityHit(EntityRayTraceResult result) {
		super.onEntityHit(result);
		result.getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_234616_v_()), 0.0F);
		if (result.getEntity() instanceof LivingEntity) {
			((LivingEntity) result.getEntity()).addPotionEffect(new EffectInstance(Effects.SLOWNESS, 20));
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		super.onImpact(result);
		if (!this.world.isRemote) {
			if (this.rand.nextInt(8) == 0) {
				int i = 1;
				if (this.rand.nextInt(32) == 0) {
					i = 4;
				}

				for (int j = 0; j < i; ++j) {
					ZombieChickenEntity chickenentity = CCEntities.ZOMBIE_CHICKEN.get().create(this.world);
					chickenentity.setGrowingAge(-24000);
					chickenentity.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, 0.0F);
					this.world.addEntity(chickenentity);
				}
			}
			this.world.setEntityState(this, (byte) 3);
			this.remove();
		}
	}

	@Override
	protected Item getDefaultItem() {
		return CCItems.ROTTEN_EGG.get();
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(CCItems.ROTTEN_EGG.get());
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}