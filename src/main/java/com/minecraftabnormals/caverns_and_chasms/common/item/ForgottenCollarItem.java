package com.minecraftabnormals.caverns_and_chasms.common.item;

import com.google.common.collect.Maps;
import com.minecraftabnormals.caverns_and_chasms.common.block.GravestoneBlock;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntities;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
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
import java.util.Map;
import java.util.UUID;

public class ForgottenCollarItem extends Item {
	public static final String PET_ID = "PetID";
	public static final String PET_NAME = "PetName";
	public static final String PET_VARIANT = "PetVariant";

	public static final String COLLAR_COLOR = "CollarColor";
	public static final String IS_CHILD = "IsChild";
	public static final String OWNER_ID = "OwnerID";

	public static final String HORSE_STRENGTH = "HorseStrength";
	public static final String HORSE_SPEED = "HorseSpeed";
	public static final String HORSE_HEALTH = "HorseHealth";

	public ForgottenCollarItem(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		BlockState state = world.getBlockState(pos);
		ItemStack stack = context.getItem();

		if (state.isIn(CCBlocks.GRAVESTONE.get()) && state.get(GravestoneBlock.CHARGE) == 10) {
			BlockPos offsetPos = pos.offset(state.get(GravestoneBlock.HORIZONTAL_FACING));
			if (!world.getBlockState(offsetPos).getCollisionShape(world, offsetPos).isEmpty())
				return ActionResultType.FAIL;

			PlayerEntity player = context.getPlayer();
			CompoundNBT tag = stack.getOrCreateTag();

			if (tag.contains(PET_ID)) {
				Map<EntityType<?>, EntityType<?>> UNDEAD_MAP = Util.make(Maps.newHashMap(), (map) -> {
					map.put(EntityType.WOLF, CCEntities.ZOMBIE_WOLF.get());
					map.put(EntityType.CAT, CCEntities.ZOMBIE_CAT.get());
					map.put(EntityType.HORSE, EntityType.ZOMBIE_HORSE);
					map.put(CCEntities.ZOMBIE_WOLF.get(), CCEntities.SKELETON_WOLF.get());
					map.put(CCEntities.ZOMBIE_CAT.get(), CCEntities.SKELETON_CAT.get());
					map.put(EntityType.ZOMBIE_HORSE, EntityType.SKELETON_HORSE);
				});

				EntityType<?> entityType = UNDEAD_MAP.get(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(tag.getString(PET_ID))));
				System.out.println(entityType);

				if (entityType != null) {
					AnimalEntity entity = (AnimalEntity) entityType.create(world);
					UUID owner = tag.contains(OWNER_ID) ? UUID.fromString(tag.getString(OWNER_ID)) : player.getUniqueID();
					DyeColor collarColor = DyeColor.byId(tag.getInt(COLLAR_COLOR));
					int variant = tag.getInt(PET_VARIANT);

					entity.setChild(tag.getBoolean(IS_CHILD));
					entity.setPosition(offsetPos.getX() + 0.5F, offsetPos.getY(), offsetPos.getZ() + 0.5F);
					if (tag.contains(PET_NAME))
						entity.setCustomName(new StringTextComponent(tag.getString(PET_NAME)));

					if (entity instanceof TameableEntity) {
						TameableEntity tameableEntity = (TameableEntity) entity;
						tameableEntity.setTamed(true);
						tameableEntity.setOwnerId(owner);

						if (tameableEntity instanceof WolfEntity) {
							WolfEntity wolf = (WolfEntity) tameableEntity;
							wolf.setCollarColor(collarColor);
							world.addEntity(wolf);
						}

						if (tameableEntity instanceof CatEntity) {
							CatEntity cat = (CatEntity) tameableEntity;
							cat.setCatType(variant);
							cat.setCollarColor(collarColor);
							world.addEntity(cat);
						}

					} else if (entity instanceof AbstractHorseEntity) {
						AbstractHorseEntity horseEntity = (AbstractHorseEntity) entity;

						horseEntity.setHorseTamed(true);
						horseEntity.setOwnerUniqueId(owner);

						horseEntity.getAttribute(Attributes.HORSE_JUMP_STRENGTH).setBaseValue(tag.getDouble(HORSE_STRENGTH));
						horseEntity.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(tag.getDouble(HORSE_SPEED));
						horseEntity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(tag.getDouble(HORSE_HEALTH));

						world.addEntity(horseEntity);
					}

					world.playSound(player, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
					world.setBlockState(pos, state.with(GravestoneBlock.CHARGE, 0));
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
			if (tag.contains(PET_VARIANT)) {
				int type = tag.getInt(PET_VARIANT);
				String texture = CatEntity.TEXTURE_BY_ID.get(type).toString().replace("minecraft:textures/entity/cat/", "");
				texture = texture.replace(".png", "").replace("all_", "").replace("_", " ").concat(" ");
				petType = new StringTextComponent(WordUtils.capitalize(texture)).mergeStyle(TextFormatting.GRAY).append(petType);
			}

			if (tag.getBoolean(IS_CHILD))
				petType = new TranslationTextComponent("tooltip.caverns_and_chasms.baby").mergeStyle(TextFormatting.GRAY).appendString(" ").append(petType);

			tooltip.add(petType);
		}


		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	public int getColor(ItemStack stack) {
		CompoundNBT tag = stack.getOrCreateTag();
		return tag.contains(COLLAR_COLOR) ? DyeColor.byId(tag.getInt(COLLAR_COLOR)).getColorValue() : 10511680;
	}
}
