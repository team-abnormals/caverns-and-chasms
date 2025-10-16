package com.teamabnormals.caverns_and_chasms.common.item;

import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
public class UnicornHornItem extends Item implements DyeableLeatherItem {

	public UnicornHornItem(Properties properties) {
		super(properties);
	}

	@SubscribeEvent
	public static void interactLivingEntity(EntityInteract event) {
		Entity entity = event.getTarget();
		Player player = event.getEntity();
		ItemStack stack = event.getItemStack();
		if (entity instanceof AbstractHorse horse && entity.isAlive() && horse instanceof IDataManager dataManager) {
			if (dataManager.getValue(CCDataProcessors.UNICORN_HORN).isEmpty()) {
				if (stack.is(CCItems.UNICORN_HORN.get())) {
					horse.level().playSound(null, horse, SoundEvents.LARGE_AMETHYST_BUD_PLACE, SoundSource.PLAYERS, 1.0F, 1.0F);
					dataManager.setValue(CCDataProcessors.UNICORN_HORN, stack.copy());
					if (!player.level().isClientSide) {
						entity.level().gameEvent(entity, GameEvent.EQUIP, entity.position());
						if (!player.getAbilities().instabuild) {
							stack.shrink(1);
						}
					}

					event.setCancellationResult(InteractionResult.sidedSuccess(player.level().isClientSide));
					event.setCanceled(true);
				}
			} else if (stack.is(Tags.Items.SHEARS)) {
				horse.level().playSound(null, horse, SoundEvents.MOOSHROOM_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);
				if (!player.level().isClientSide) {
					entity.level().gameEvent(entity, GameEvent.SHEAR, entity.position());
					entity.spawnAtLocation(dataManager.getValue(CCDataProcessors.UNICORN_HORN), 1.0F);
					stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(event.getHand()));
				}
				dataManager.setValue(CCDataProcessors.UNICORN_HORN, ItemStack.EMPTY);

				event.setCancellationResult(InteractionResult.sidedSuccess(player.level().isClientSide));
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof Horse horse && !((IDataManager) horse).getValue(CCDataProcessors.UNICORN_HORN).isEmpty()) {
			horse.spawnAtLocation(((IDataManager) horse).getValue(CCDataProcessors.UNICORN_HORN), 1.0F);
		}
	}

	@Override
	public int getColor(ItemStack stack) {
		CompoundTag tag = stack.getTagElement("display");
		return tag != null && tag.contains("color", 99) ? tag.getInt("color") : -1;
	}
}