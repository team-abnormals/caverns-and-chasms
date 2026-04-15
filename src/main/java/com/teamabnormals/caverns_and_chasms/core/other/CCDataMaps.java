package com.teamabnormals.caverns_and_chasms.core.other;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
public class CCDataMaps {

	public static final DataMapType<Item, TrialToken> TRIAL_TOKENS = DataMapType.builder(CavernsAndChasms.location("trial_tokens"), Registries.ITEM, TrialToken.CODEC).synced(TrialToken.CODEC, false).build();

	@SubscribeEvent
	public static void registerDataMaps(RegisterDataMapTypesEvent event) {
		event.register(TRIAL_TOKENS);
	}

	public record TrialToken(ItemStack keyItem, ResourceKey<LootTable> tokenLootTable, ResourceKey<LootTable> replaceLootTable, ResourceKey<LootTable> vaultLootTable) {
		public static final Codec<TrialToken> CODEC = RecordCodecBuilder.create(in -> in.group(
				ItemStack.CODEC.fieldOf("key_item").forGetter(TrialToken::keyItem),
				ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("token_loot_table").forGetter(TrialToken::tokenLootTable),
				ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("replace_loot_table").forGetter(TrialToken::replaceLootTable),
				ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("vault_loot_table").forGetter(TrialToken::vaultLootTable)
		).apply(in, TrialToken::new));
	}
}
