package com.minecraftabnormals.caverns_and_chasms.common.entity;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEffects;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntities;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class KunaiEntity extends AbstractArrowEntity implements IRendersAsItem {

	public KunaiEntity(EntityType<? extends KunaiEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public KunaiEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		this(CCEntities.THROWING_KNIFE.get(), world);
	}

	public KunaiEntity(World worldIn, LivingEntity shooter) {
		super(CCEntities.THROWING_KNIFE.get(), shooter, worldIn);
	}

	@Override
	protected void arrowHit(LivingEntity living) {
		super.arrowHit(living);
		if (living.isEntityUndead()) living.addPotionEffect(new EffectInstance(CCEffects.AFFLICTION.get(), 60));
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
		return 3.0D;
	}
}