package com.teamabnormals.caverns_and_chasms.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet.Named;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class CopperHornItem extends Item {
	private static final String TAG_HARMONY_INSTRUMENT = "harmony_instrument";
	private static final String TAG_MELODY_INSTRUMENT = "melody_instrument";
	private static final String TAG_BASS_INSTRUMENT = "bass_instrument";

	private final TagKey<Instrument> harmonyInstruments;
	private final TagKey<Instrument> melodyInstruments;
	private final TagKey<Instrument> bassInstruments;

	public CopperHornItem(Properties properties, TagKey<Instrument> harmonyInstruments, TagKey<Instrument> melodyInstruments, TagKey<Instrument> bassInstruments) {
		super(properties);
		this.harmonyInstruments = harmonyInstruments;
		this.melodyInstruments = melodyInstruments;
		this.bassInstruments = bassInstruments;
	}

	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		Optional<ResourceKey<Instrument>> harmony = getInstrument(stack, "harmony", this.harmonyInstruments).flatMap(Holder::unwrapKey);
		Optional<ResourceKey<Instrument>> melody = getInstrument(stack, "melody", this.melodyInstruments).flatMap(Holder::unwrapKey);
		Optional<ResourceKey<Instrument>> bass = getInstrument(stack, "bass", this.bassInstruments).flatMap(Holder::unwrapKey);
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
		setSoundVariantId(stack, harmony, melody, bass);
		return stack;
	}

	public static void setRandom(ItemStack stack, TagKey<Instrument> harmony, TagKey<Instrument> melody, TagKey<Instrument> bass, RandomSource random) {
		Optional<Named<Instrument>> harmonyOptional = BuiltInRegistries.INSTRUMENT.getTag(harmony);
		Optional<Named<Instrument>> melodyOptional = BuiltInRegistries.INSTRUMENT.getTag(melody);
		Optional<Named<Instrument>> bassOptional = BuiltInRegistries.INSTRUMENT.getTag(bass);

		if (harmonyOptional.isPresent() && melodyOptional.isPresent() && bassOptional.isPresent()) {
			int index = random.nextInt(10);
			setSoundVariantId(stack, harmonyOptional.get().get(index), melodyOptional.get().get(index), bassOptional.get().get(index));
		}
	}

	private static void setSoundVariantId(ItemStack stack, Holder<Instrument> harmony, Holder<Instrument> melody, Holder<Instrument> bass) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putString(TAG_HARMONY_INSTRUMENT, harmony.unwrapKey().orElseThrow(() -> new IllegalStateException("Invalid instrument")).location().toString());
		tag.putString(TAG_MELODY_INSTRUMENT, melody.unwrapKey().orElseThrow(() -> new IllegalStateException("Invalid instrument")).location().toString());
		tag.putString(TAG_BASS_INSTRUMENT, bass.unwrapKey().orElseThrow(() -> new IllegalStateException("Invalid instrument")).location().toString());
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
	public int getUseDuration(ItemStack stack) {
		Optional<? extends Holder<Instrument>> optional = this.getInstrument(stack, null);
		return optional.map(instrument -> instrument.value().useDuration()).orElse(0);
	}

	private Optional<? extends Holder<Instrument>> getInstrument(ItemStack stack, @Nullable Player player) {
		if (player != null && player.isCrouching()) {
			return getInstrument(stack, "bass", this.bassInstruments);
		} else if (player != null && player.getXRot() <= -60.0F) {
			return getInstrument(stack, "harmony", this.harmonyInstruments);
		} else {
			return getInstrument(stack, "melody", this.melodyInstruments);
		}
	}

	private static Optional<? extends Holder<Instrument>> getInstrument(ItemStack stack, String type, TagKey<Instrument> tagKey) {
		String instrument = type + "_instrument";

		CompoundTag tag = stack.getTag();
		if (tag != null && tag.contains(instrument, 8)) {
			ResourceLocation instrumentKey = ResourceLocation.tryParse(tag.getString(instrument));
			if (instrumentKey != null) {
				return BuiltInRegistries.INSTRUMENT.getHolder(ResourceKey.create(Registries.INSTRUMENT, instrumentKey));
			}
		}

		Iterator<Holder<Instrument>> iterator = BuiltInRegistries.INSTRUMENT.getTagOrEmpty(tagKey).iterator();
		return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.empty();
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