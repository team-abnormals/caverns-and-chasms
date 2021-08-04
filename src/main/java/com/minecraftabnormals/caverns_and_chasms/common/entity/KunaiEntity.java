package com.minecraftabnormals.caverns_and_chasms.common.entity;

import com.minecraftabnormals.caverns_and_chasms.core.other.CCDamageSources;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEffects;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntities;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class KunaiEntity extends AbstractArrowEntity implements IRendersAsItem {

	public KunaiEntity(EntityType<? extends KunaiEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public KunaiEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		this(CCEntities.KUNAI.get(), world);
	}

	public KunaiEntity(World worldIn, LivingEntity shooter) {
		super(CCEntities.KUNAI.get(), shooter, worldIn);
	}

	@Override
	protected void doPostHurtEffects(LivingEntity living) {
		super.doPostHurtEffects(living);
		if (living.isInvertedHealAndHarm())
			living.addEffect(new EffectInstance(CCEffects.AFFLICTION.get(), 60));
	}

	@Override
	protected void onHitEntity(EntityRayTraceResult result) {
		Entity target = result.getEntity();
		Entity shooter = this.getOwner();

		float motion = (float) this.getDeltaMovement().length();
		int damage = MathHelper.ceil(MathHelper.clamp((double) motion * 0.5F * this.getBaseDamage(), 0.0D, 2.147483647E9D));

		DamageSource damagesource;
		if (shooter == null) {
			damagesource = CCDamageSources.causeKunaiDamage(this, this);
		} else {
			damagesource = CCDamageSources.causeKunaiDamage(this, shooter);
			if (shooter instanceof LivingEntity) {
				((LivingEntity) shooter).setLastHurtMob(target);
			}
		}

		boolean isEnderman = target.getType() == EntityType.ENDERMAN;
		if (this.isOnFire() && !isEnderman) {
			target.setSecondsOnFire(5);
		}

		if (target.hurt(damagesource, (float) damage)) {
			if (isEnderman) return;

			if (target instanceof LivingEntity) {
				LivingEntity livingTarget = (LivingEntity) target;

				if (!this.level.isClientSide() && shooter instanceof LivingEntity) {
					EnchantmentHelper.doPostHurtEffects(livingTarget, shooter);
					EnchantmentHelper.doPostDamageEffects((LivingEntity) shooter, livingTarget);
				}

				this.doPostHurtEffects(livingTarget);
				if (livingTarget != shooter && livingTarget instanceof PlayerEntity && shooter instanceof ServerPlayerEntity && !this.isSilent()) {
					((ServerPlayerEntity) shooter).connection.send(new SChangeGameStatePacket(SChangeGameStatePacket.ARROW_HIT_PLAYER, 0.0F));
				}

				if (!target.isAlive() && this.piercedAndKilledEntities != null) {
					this.piercedAndKilledEntities.add(livingTarget);
				}
			}

			this.playSound(this.getDefaultHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
		} else {
			target.setRemainingFireTicks(target.getRemainingFireTicks());
			this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
			this.yRot += 180.0F;
			this.yRotO += 180.0F;
			if (!this.level.isClientSide() && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
				if (this.pickup == AbstractArrowEntity.PickupStatus.ALLOWED) {
					this.spawnAtLocation(this.getPickupItem(), 0.1F);
				}

				this.remove();
			}
		}
	}

	protected ItemStack getPickupItem() {
		return new ItemStack(CCItems.KUNAI.get());
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public ItemStack getItem() {
		return new ItemStack(CCItems.KUNAI.get());
	}

	@Override
	protected void tickDespawn() {
		++this.life;
		if (this.life >= 6000) {
			this.remove();
		}
	}

	@Override
	protected SoundEvent getDefaultHitGroundSoundEvent() {
		return SoundEvents.WOOD_BREAK;
	}

	@Override
	public double getBaseDamage() {
		return 2.0D;
	}
}