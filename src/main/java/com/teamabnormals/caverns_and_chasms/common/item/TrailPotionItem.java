package com.teamabnormals.caverns_and_chasms.common.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;

import java.util.List;

public class TrailPotionItem extends TetherPotionItem {
	public TrailPotionItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
		if (contents != null) {
			contents.addPotionTooltip(tooltip::add, 0.25F, context.tickRate());
		}
	}

	public static void makeAreaOfEffectCloud(PotionContents contents, Entity entity, Level level, boolean shatter) {
		AreaEffectCloud areaeffectcloud = new AreaEffectCloud(level, entity.getX(), entity.getY(), entity.getZ());
		if (entity instanceof LivingEntity) {
			areaeffectcloud.setOwner((LivingEntity) entity);
		}

		areaeffectcloud.setRadius(shatter ? 3F : 1F);
		areaeffectcloud.setRadiusOnUse(-0.25F);
		areaeffectcloud.setWaitTime(5);
		areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) (areaeffectcloud.getDuration() / (shatter ? 1 : 2)));
		areaeffectcloud.setPotionContents(contents);

		level.addFreshEntity(areaeffectcloud);
	}
}
