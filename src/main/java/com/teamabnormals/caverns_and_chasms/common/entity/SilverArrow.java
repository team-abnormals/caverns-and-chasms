package com.teamabnormals.caverns_and_chasms.common.entity;

import com.teamabnormals.caverns_and_chasms.core.registry.CCEffects;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.FMLPlayMessages;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

public class SilverArrow extends AbstractArrow {

	public SilverArrow(EntityType<? extends SilverArrow> type, Level worldIn) {
		super(type, worldIn);
	}

	public SilverArrow(Level worldIn, double x, double y, double z) {
		super(CCEntityTypes.SILVER_ARROW.get(), x, y, z, worldIn);
	}

	public SilverArrow(FMLPlayMessages.SpawnEntity spawnEntity, Level world) {
		this(CCEntityTypes.SILVER_ARROW.get(), world);
	}

	public SilverArrow(Level worldIn, LivingEntity shooter) {
		super(CCEntityTypes.SILVER_ARROW.get(), shooter, worldIn);
	}

	@Override
	protected void doPostHurtEffects(LivingEntity living) {
		super.doPostHurtEffects(living);
		if (living.isInvertedHealAndHarm()) living.addEffect(new MobEffectInstance(CCEffects.AFFLICTION.get(), 60));
	}

	protected ItemStack getPickupItem() {
		return new ItemStack(CCItems.SILVER_ARROW.get());
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}