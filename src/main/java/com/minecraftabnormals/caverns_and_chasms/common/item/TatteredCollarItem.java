package com.minecraftabnormals.caverns_and_chasms.common.item;

import com.minecraftabnormals.caverns_and_chasms.common.block.GravestoneBlock;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntities;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nullable;
import java.util.List;

public class TatteredCollarItem extends Item {
	public static final String PET_NAME = "PetName";
	public static final String COLLAR_COLOR = "CollarColor";
	public static final String CAT_TYPE = "CatType";
	public static final String IS_CHILD = "IsChild";
	public static final String PET_ID = "PetID";

	public TatteredCollarItem(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		BlockState state = world.getBlockState(pos);
		ItemStack stack = context.getItem();

		if (state.isIn(CCBlocks.GRAVESTONE.get()) && state.get(GravestoneBlock.ACTIVATED)) {
			PlayerEntity player = context.getPlayer();
			CompoundNBT tag = stack.getOrCreateTag();


			if (tag.contains(PET_ID)) {
				EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(tag.getString(PET_ID)));

				if (entityType == CCEntities.ZOMBIE_WOLF.get()) entityType = CCEntities.SKELETON_WOLF.get();
				else if (entityType == EntityType.WOLF) entityType = CCEntities.ZOMBIE_WOLF.get();
				else if (entityType == EntityType.CAT) entityType = CCEntities.ZOMBIE_CAT.get();
				else if (entityType == CCEntities.ZOMBIE_CAT.get()) entityType = CCEntities.SKELETON_CAT.get();

				if (entityType != null && entityType.create(world) instanceof TameableEntity) {
					TameableEntity entity = (TameableEntity) entityType.create(world);

					entity.setTamedBy(player);
					entity.setChild(tag.getBoolean(IS_CHILD));
					entity.setPosition(pos.getX() + 0.5F, pos.getY() + 1.5F, pos.getZ() + 0.5F);
					if (tag.contains(PET_NAME))
						entity.setCustomName(new StringTextComponent(tag.getString(PET_NAME)));

					if (entity instanceof WolfEntity) {
						WolfEntity wolf = (WolfEntity) entity;
						wolf.setCollarColor(DyeColor.byId(tag.getInt(COLLAR_COLOR)));
						world.addEntity(wolf);
					}

					if (entity instanceof CatEntity) {
						CatEntity cat = (CatEntity) entity;
						cat.setCatType(tag.getInt(CAT_TYPE));
						cat.setCollarColor(DyeColor.byId(tag.getInt(COLLAR_COLOR)));
						world.addEntity(cat);
					}

					world.playSound(player, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
					if (!player.abilities.isCreativeMode)
						stack.shrink(1);
				}
			}

			return ActionResultType.func_233537_a_(world.isRemote);
		}

		return ActionResultType.PASS;

	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		CompoundNBT tag = stack.getOrCreateTag();

		if (tag.contains(PET_NAME)) {
			String name = tag.getString(PET_NAME);
			tooltip.add(new StringTextComponent(name).mergeStyle(TextFormatting.GRAY, TextFormatting.ITALIC));
		}

		if (tag.contains(PET_ID)) {
			String petID = tag.getString(PET_ID);
			EntityType<?> pet = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(petID));

			ITextComponent petType = new TranslationTextComponent(pet.getTranslationKey()).mergeStyle(TextFormatting.GRAY);
			if (tag.contains(CAT_TYPE)) {
				int catType = tag.getInt(CAT_TYPE);
				String texture = WordUtils.capitalize(CatEntity.TEXTURE_BY_ID.get(catType).toString().replace("minecraft:textures/entity/cat/", "").replace(".png", "").replace("all_", "").replace("_", " ").concat(" "));
				petType = new StringTextComponent(texture).mergeStyle(TextFormatting.GRAY).append(petType);
			}

			if (tag.getBoolean(IS_CHILD))
				petType = new TranslationTextComponent("tooltip.caverns_and_chasms.baby").mergeStyle(TextFormatting.GRAY).appendString(" ").append(petType);

			tooltip.add(petType);
		}


		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	public int getColor(ItemStack stack) {
		CompoundNBT tag = stack.getOrCreateTag();
		return tag.contains(COLLAR_COLOR) ? DyeColor.byId(tag.getInt(COLLAR_COLOR)).getColorValue() : DyeColor.RED.getColorValue();
	}
}
