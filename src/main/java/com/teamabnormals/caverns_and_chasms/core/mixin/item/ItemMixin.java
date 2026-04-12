package com.teamabnormals.caverns_and_chasms.core.mixin.item;

import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCItemTags;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEnchantmentEffects;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Item.class)
public abstract class ItemMixin {

	@Shadow
	public abstract InteractionResult useOn(UseOnContext context);

	@Inject(at = @At("RETURN"), method = "isValidRepairItem", cancellable = true)
	private void isValidRepairItem(ItemStack item, ItemStack repairIngredient, CallbackInfoReturnable<Boolean> cir) {
		if (repairIngredient.is(CCItems.ZIRCONIA.get()) && !item.is(CCItemTags.UNREPAIRABLE_BY_ZIRCONIA) && CCConfig.COMMON.zirconiaUniversalRepairing.get()) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void use(Level level, Player player, InteractionHand usedHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
		if (EnchantmentHelper.has(player.getItemBySlot(EquipmentSlot.LEGS), CCEnchantmentEffects.CAN_PLACE_MIDAIR.get())) {
			double reach = player.blockInteractionRange() - 2.0D;
			Vec3 eyeLoc = player.getEyePosition();
			Vec3 scaled = eyeLoc.add(player.calculateViewVector(player.getXRot(), player.getYRot()).scale(player.isSecondaryUseActive() ? reach / 2.0D : reach));
			BlockHitResult blockResult = level.clip(new ClipContext(eyeLoc, scaled, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, player));
			cir.setReturnValue(new InteractionResultHolder<>(this.useOn(new UseOnContext(player, usedHand, blockResult)), player.getItemInHand(usedHand)));
		}
	}

	@Inject(method = "useOn", at = @At("TAIL"), cancellable = true)
	private void useItem(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
		ItemStack stack = context.getItemInHand();
		if (stack.is(CCItemTags.PLACEABLE_ITEMS)) {
			Optional<Registry<Item>> registry = context.getLevel().registryAccess().registry(Registries.ITEM);
			if (registry.isPresent()) {
				for (Item item1 : registry.get()) {
					if (item1 instanceof BlockItem blockItem && stack.is(blockItem.getBlock().asItem())) {
						cir.setReturnValue(item1.useOn(context));
						break;
					}
				}
			}
		}
	}
}