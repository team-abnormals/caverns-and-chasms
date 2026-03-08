package com.teamabnormals.caverns_and_chasms.common.entity.monster.deeper;

import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Evendeeper extends Deeper {
	public Evendeeper(EntityType<? extends Evendeeper> type, Level level) {
		super(type, level);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 25.0D).add(Attributes.MOVEMENT_SPEED, 0.24D);
	}

	@Override
	protected ItemStack getSkull() {
		return new ItemStack(CCItems.EVENDEEPER_HEAD.get());
	}
}