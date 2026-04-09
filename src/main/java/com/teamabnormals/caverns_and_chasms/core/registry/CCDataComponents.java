package com.teamabnormals.caverns_and_chasms.core.registry;

import com.teamabnormals.caverns_and_chasms.common.item.component.PackingContainerContents;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponentType.Builder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.component.BundleContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class CCDataComponents {
	public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, CavernsAndChasms.MOD_ID);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> SUBTLE = register("subtle", builder -> builder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> EMISSIVE_TRIM = register("emissive_trim", builder -> builder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> FADED_TRIM = register("faded_trim", builder -> builder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> PULSE_TRIM = register("pulse_trim", builder -> builder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> NOTE = register("note", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> LIFE = register("life", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> FLUID_LEVEL = register("fluid_level", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TETHER_COOLDOWN = register("tether_cooldown", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<PackingContainerContents>> PACKING_CONTAINER_CONTENTS = register("packing_container_contents", builder -> builder.persistent(PackingContainerContents.CODEC).networkSynchronized(PackingContainerContents.STREAM_CODEC).cacheEncoding());

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Holder<Instrument>>> HARMONY_INSTRUMENT = register("harmony_instrument", builder -> builder.persistent(Instrument.CODEC).networkSynchronized(Instrument.STREAM_CODEC).cacheEncoding());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Holder<Instrument>>> MELODY_INSTRUMENT = register("melody_instrument", builder -> builder.persistent(Instrument.CODEC).networkSynchronized(Instrument.STREAM_CODEC).cacheEncoding());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Holder<Instrument>>> BASS_INSTRUMENT = register("bass_instrument", builder -> builder.persistent(Instrument.CODEC).networkSynchronized(Instrument.STREAM_CODEC).cacheEncoding());

	private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<Builder<T>> builder) {
		return DATA_COMPONENTS.register(name, () -> builder.apply(DataComponentType.builder()).build());
	}
}