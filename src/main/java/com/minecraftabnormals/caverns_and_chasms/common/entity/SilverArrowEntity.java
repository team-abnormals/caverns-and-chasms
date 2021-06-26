package com.minecraftabnormals.caverns_and_chasms.common.entity;

import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEffects;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntities;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class SilverArrowEntity extends AbstractArrowEntity {

	public SilverArrowEntity(EntityType<? extends SilverArrowEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public SilverArrowEntity(World worldIn, double x, double y, double z) {
		super(CCEntities.SILVER_ARROW.get(), x, y, z, worldIn);
	}

	public SilverArrowEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		this(CCEntities.SILVER_ARROW.get(), world);
	}

	public SilverArrowEntity(World worldIn, LivingEntity shooter) {
		super(CCEntities.SILVER_ARROW.get(), shooter, worldIn);
	}

	@Override
	protected void doPostHurtEffects(LivingEntity living) {
		super.doPostHurtEffects(living);
		if (living.isInvertedHealAndHarm()) living.addEffect(new EffectInstance(CCEffects.AFFLICTION.get(), 60));
	}

	protected ItemStack getPickupItem() {
		return new ItemStack(CCItems.SILVER_ARROW.get());
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}