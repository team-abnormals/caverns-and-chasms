package com.teamabnormals.caverns_and_chasms.core.mixin;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(WitherSkeleton.class)
public abstract class WitherSkeletonEntityMixin extends AbstractSkeleton {

	protected WitherSkeletonEntityMixin(EntityType<? extends AbstractSkeleton> type, Level worldIn) {
		super(type, worldIn);
	}

	@Inject(method = "populateDefaultEquipmentSlots", at = @At("TAIL"))
	private void populateDefaultEquipmentSlots(DifficultyInstance difficulty, CallbackInfo info) {
		Random random = this.level.getRandom();
		int difficultyChance = difficulty.getDifficulty().getId() + 1;

		if (random.nextInt(difficultyChance) != 0) {
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_AXE));
		}
	}
}
