package com.teamabnormals.caverns_and_chasms.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TrailPotionItem extends TetherPotionItem {
	public TrailPotionItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		PotionUtils.addPotionTooltip(stack, tooltip, 0.25F);
	}

	public static void makeAreaOfEffectCloud(ItemStack p_37538_, Potion p_37539_, Entity entity, Level level, boolean shatter) {
		AreaEffectCloud areaeffectcloud = new AreaEffectCloud(level, entity.getX(), entity.getY(), entity.getZ());
		if (entity instanceof LivingEntity) {
			areaeffectcloud.setOwner((LivingEntity) entity);
		}

		areaeffectcloud.setRadius(shatter ? 3F : 1F);
		areaeffectcloud.setRadiusOnUse(-0.25F);
		areaeffectcloud.setWaitTime(5);
		areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) (areaeffectcloud.getDuration() / (shatter ? 1 : 2)));
		areaeffectcloud.setPotion(p_37539_);

		for (MobEffectInstance mobeffectinstance : PotionUtils.getCustomEffects(p_37538_)) {
			areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
		}

		CompoundTag compoundtag = p_37538_.getTag();
		if (compoundtag != null && compoundtag.contains("CustomPotionColor", 99)) {
			areaeffectcloud.setFixedColor(compoundtag.getInt("CustomPotionColor"));
		}

		level.addFreshEntity(areaeffectcloud);
	}
}
