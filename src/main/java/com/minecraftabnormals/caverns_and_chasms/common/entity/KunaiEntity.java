package com.minecraftabnormals.caverns_and_chasms.common.entity;

import com.minecraftabnormals.caverns_and_chasms.core.other.CCDamageSources;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEffects;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntities;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
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

import javax.annotation.Nullable;

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
	protected void arrowHit(LivingEntity living) {
		super.arrowHit(living);
		if (living.isEntityUndead())
			living.addPotionEffect(new EffectInstance(CCEffects.AFFLICTION.get(), 60));
	}

	@Override
	protected void onEntityHit(EntityRayTraceResult result) {
		Entity target = result.getEntity();
		Entity shooter = this.func_234616_v_();

		float motion = (float) this.getMotion().length();
		int damage = MathHelper.ceil(MathHelper.clamp((double) motion * 0.5F * this.getDamage(), 0.0D, 2.147483647E9D));

		DamageSource damagesource;
		if (shooter == null) {
			damagesource = CCDamageSources.causeKunaiDamage(this, this);
		} else {
			damagesource = CCDamageSources.causeKunaiDamage(this, shooter);
			if (shooter instanceof LivingEntity) {
				((LivingEntity) shooter).setLastAttackedEntity(target);
			}
		}

		boolean isEnderman = target.getType() == EntityType.ENDERMAN;
		if (this.isBurning() && !isEnderman) {
			target.setFire(5);
		}

		if (target.attackEntityFrom(damagesource, (float) damage)) {
			if (isEnderman) return;

			if (target instanceof LivingEntity) {
				LivingEntity livingTarget = (LivingEntity) target;

				if (!this.world.isRemote() && shooter instanceof LivingEntity) {
					EnchantmentHelper.applyThornEnchantments(livingTarget, shooter);
					EnchantmentHelper.applyArthropodEnchantments((LivingEntity) shooter, livingTarget);
				}

				this.arrowHit(livingTarget);
				if (livingTarget != shooter && livingTarget instanceof PlayerEntity && shooter instanceof ServerPlayerEntity && !this.isSilent()) {
					((ServerPlayerEntity) shooter).connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.field_241770_g_, 0.0F));
				}

				if (!target.isAlive() && this.hitEntities != null) {
					this.hitEntities.add(livingTarget);
				}
			}

			this.playSound(this.getHitEntitySound(), 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
		} else {
			target.forceFireTicks(target.getFireTimer());
			this.setMotion(this.getMotion().scale(-0.1D));
			this.rotationYaw += 180.0F;
			this.prevRotationYaw += 180.0F;
			if (!this.world.isRemote() && this.getMotion().lengthSquared() < 1.0E-7D) {
				if (this.pickupStatus == AbstractArrowEntity.PickupStatus.ALLOWED) {
					this.entityDropItem(this.getArrowStack(), 0.1F);
				}

				this.remove();
			}
		}
	}

	protected ItemStack getArrowStack() {
		return new ItemStack(CCItems.KUNAI.get());
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public ItemStack getItem() {
		return new ItemStack(CCItems.KUNAI.get());
	}

	@Override
	protected void func_225516_i_() {
		++this.ticksInGround;
		if (this.ticksInGround >= 6000) {
			this.remove();
		}
	}

	@Override
	protected SoundEvent getHitEntitySound() {
		return SoundEvents.BLOCK_WOOD_BREAK;
	}

	@Override
	public double getDamage() {
		return 2.0D;
	}
}