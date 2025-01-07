package com.teamabnormals.caverns_and_chasms.core.data.server.tags;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PaintingVariantTagsProvider;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.teamabnormals.caverns_and_chasms.core.registry.CCPaintingVariants.*;

public class CCPaintingVariantTagsProvider extends PaintingVariantTagsProvider {

	public CCPaintingVariantTagsProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		super(output, provider, CavernsAndChasms.MOD_ID, helper);
	}

	@Override
	public void addTags(Provider provider) {
		this.tag(PaintingVariantTags.PLACEABLE).add(
				ISOLATION.getKey(),
				EXSANGUINATED.getKey(),
				EMBEDDED.getKey(),
				NOIR.getKey(),
				STARRY_NIGHT.getKey(),
				KNIGHT.getKey(),
				PROTOTYPE_701.getKey(),
				SQUIRMY.getKey()
		);
	}
}