package com.minecraftabnormals.caverns_and_chasms.core.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin extends MonsterEntity {

	protected AbstractSkeletonEntityMixin(EntityType<? extends MonsterEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Inject(method = "populateDefaultEquipmentSlots", at = @At("TAIL"))
	private void populateDefaultEquipmentSlots(DifficultyInstance difficulty, CallbackInfo info) {
		Random random = this.level.getRandom();
		int difficultyChance = difficulty.getDifficulty().getId() + 1;

		if (this.level.dimension() != World.NETHER && random.nextInt(difficultyChance) == 0) {
			this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(random.nextInt(difficultyChance) == 0 ? Items.WOODEN_SWORD : Items.WOODEN_AXE));
		}
	}
}
