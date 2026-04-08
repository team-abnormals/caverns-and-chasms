package com.teamabnormals.caverns_and_chasms.common.item.copper;

import com.teamabnormals.caverns_and_chasms.core.registry.CCDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class CopperHornItem extends Item {
	private final TagKey<Instrument> harmonyInstruments;
	private final TagKey<Instrument> melodyInstruments;
	private final TagKey<Instrument> bassInstruments;

	public CopperHornItem(Properties properties, TagKey<Instrument> harmonyInstruments, TagKey<Instrument> melodyInstruments, TagKey<Instrument> bassInstruments) {
		super(properties);
		this.harmonyInstruments = harmonyInstruments;
		this.melodyInstruments = melodyInstruments;
		this.bassInstruments = bassInstruments;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		Optional<ResourceKey<Instrument>> harmony = getInstrument(stack, CCDataComponents.HARMONY_INSTRUMENT.get(), this.harmonyInstruments).flatMap(Holder::unwrapKey);
		Optional<ResourceKey<Instrument>> melody = getInstrument(stack, CCDataComponents.MELODY_INSTRUMENT.get(), this.melodyInstruments).flatMap(Holder::unwrapKey);
		Optional<ResourceKey<Instrument>> bass = getInstrument(stack, CCDataComponents.BASS_INSTRUMENT.get(), this.bassInstruments).flatMap(Holder::unwrapKey);
		if (harmony.isPresent()) {
			MutableComponent harmonyTag = Component.translatable(Util.makeDescriptionId("instrument", harmony.get().location()));
			MutableComponent melodyTag = Component.translatable(Util.makeDescriptionId("instrument", melody.get().location()));
			MutableComponent bassTag = Component.translatable(Util.makeDescriptionId("instrument", bass.get().location()));
			MutableComponent component = harmonyTag.append(" ").append(melodyTag).append(" ").append(bassTag);
			tooltip.add(component.withStyle(ChatFormatting.GRAY));
		}
	}

	public static ItemStack create(Item item, Holder<Instrument> harmony, Holder<Instrument> melody, Holder<Instrument> bass) {
		ItemStack stack = new ItemStack(item);
		stack.set(CCDataComponents.HARMONY_INSTRUMENT, harmony);
		stack.set(CCDataComponents.MELODY_INSTRUMENT, melody);
		stack.set(CCDataComponents.BASS_INSTRUMENT, bass);
		return stack;
	}

	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		Optional<? extends Holder<Instrument>> optional = this.getInstrument(stack, player);
		if (optional.isPresent()) {
			Instrument instrument = optional.get().value();
			player.startUsingItem(hand);
			play(level, player, instrument);
			player.getCooldowns().addCooldown(this, instrument.useDuration());
			player.awardStat(Stats.ITEM_USED.get(this));
			return InteractionResultHolder.consume(stack);
		} else {
			return InteractionResultHolder.fail(stack);
		}
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		Optional<? extends Holder<Instrument>> optional = this.getInstrument(stack, null);
		return optional.map(instrument -> instrument.value().useDuration()).orElse(0);
	}

	private Optional<? extends Holder<Instrument>> getInstrument(ItemStack stack, @Nullable Player player) {
		if (player != null && player.isCrouching()) {
			return getInstrument(stack, CCDataComponents.BASS_INSTRUMENT.get(), this.bassInstruments);
		} else if (player != null && player.getXRot() < -15.0F) {
			return getInstrument(stack, CCDataComponents.HARMONY_INSTRUMENT.get(), this.harmonyInstruments);
		} else {
			return getInstrument(stack, CCDataComponents.MELODY_INSTRUMENT.get(), this.melodyInstruments);
		}
	}

	private static Optional<? extends Holder<Instrument>> getInstrument(ItemStack stack, DataComponentType<Holder<Instrument>> type, TagKey<Instrument> tagKey) {
		Holder<Instrument> holder = stack.get(type);
		if (holder != null) {
			return Optional.of(holder);
		} else {
			Iterator<Holder<Instrument>> iterator = BuiltInRegistries.INSTRUMENT.getTagOrEmpty(tagKey).iterator();
			return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.empty();
		}
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.TOOT_HORN;
	}

	private static void play(Level level, Player player, Instrument instrument) {
		SoundEvent soundevent = instrument.soundEvent().value();
		float f = instrument.range() / 16.0F;
		level.playSound(player, player, soundevent, SoundSource.RECORDS, f, 1.0F);
		level.gameEvent(GameEvent.INSTRUMENT_PLAY, player.position(), GameEvent.Context.of(player));
	}
}