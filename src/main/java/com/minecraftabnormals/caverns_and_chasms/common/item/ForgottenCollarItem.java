package com.minecraftabnormals.caverns_and_chasms.common.item;

import com.google.common.collect.Maps;
import com.minecraftabnormals.caverns_and_chasms.common.block.GravestoneBlock;
import com.minecraftabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.minecraftabnormals.caverns_and_chasms.core.other.CCTags;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCEntities;
import com.minecraftabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = CavernsAndChasms.MOD_ID)
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

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		LivingEntity entity = event.getEntityLiving();
		EntityType<?> type = entity.getType();

		if (type.is(CCTags.EntityTypes.COLLAR_DROP_MOBS)) {
			ItemStack collar = new ItemStack(CCItems.FORGOTTEN_COLLAR.get());
			CompoundNBT tag = collar.getOrCreateTag();

			tag.putString(ForgottenCollarItem.PET_ID, type.getRegistryName().toString());
			tag.putBoolean(ForgottenCollarItem.IS_CHILD, entity.isBaby());
			if (entity.hasCustomName()) {
				tag.putString(ForgottenCollarItem.PET_NAME, entity.getCustomName().getString());
			}

			if (entity instanceof TameableEntity) {
				TameableEntity pet = (TameableEntity) entity;
				if (pet.isTame()) {
					tag.putString(ForgottenCollarItem.OWNER_ID, pet.getOwnerUUID().toString());

					if (entity instanceof WolfEntity) {
						WolfEntity wolf = (WolfEntity) entity;
						tag.putInt(ForgottenCollarItem.COLLAR_COLOR, wolf.getCollarColor().getId());
					}

					if (entity instanceof CatEntity) {
						CatEntity cat = (CatEntity) entity;
						tag.putInt(ForgottenCollarItem.PET_VARIANT, cat.getCatType());
						tag.putInt(ForgottenCollarItem.COLLAR_COLOR, cat.getCollarColor().getId());
					}

					if (entity instanceof ParrotEntity) {
						ParrotEntity parrot = (ParrotEntity) entity;
						tag.putInt(ForgottenCollarItem.PET_VARIANT, parrot.getVariant());
					}

					entity.spawnAtLocation(collar);
				}
			} else if (entity instanceof AbstractHorseEntity) {
				AbstractHorseEntity horse = (AbstractHorseEntity) entity;
				if (horse.isTamed()) {
					tag.putString(ForgottenCollarItem.OWNER_ID, horse.getOwnerUUID().toString());
					tag.putDouble(ForgottenCollarItem.HORSE_SPEED, horse.getAttributeBaseValue(Attributes.MOVEMENT_SPEED));
					tag.putDouble(ForgottenCollarItem.HORSE_HEALTH, horse.getAttributeBaseValue(Attributes.MAX_HEALTH));
					tag.putDouble(ForgottenCollarItem.HORSE_STRENGTH, horse.getAttributeBaseValue(Attributes.JUMP_STRENGTH));
					if (entity instanceof HorseEntity)
						tag.putInt(ForgottenCollarItem.PET_VARIANT, ((HorseEntity) entity).getTypeVariant());

					entity.spawnAtLocation(collar);
				}
			}
		}
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		World world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = world.getBlockState(pos);
		ItemStack stack = context.getItemInHand();

		if (state.is(CCBlocks.GRAVESTONE.get()) && state.getValue(GravestoneBlock.CHARGE) == 10 && world.canSeeSky(pos.above()) && world.isNight()) {
			BlockPos offsetPos = pos.relative(state.getValue(GravestoneBlock.FACING));
			if (!world.getBlockState(offsetPos).getCollisionShape(world, offsetPos).isEmpty())
				return ActionResultType.FAIL;

			PlayerEntity player = context.getPlayer();
			CompoundNBT tag = stack.getOrCreateTag();

			if (tag.contains(PET_ID)) {
				Map<EntityType<?>, EntityType<?>> UNDEAD_MAP = Util.make(Maps.newHashMap(), (map) -> {
					map.put(EntityType.WOLF, CCEntities.ZOMBIE_WOLF.get());
					map.put(EntityType.CAT, CCEntities.ZOMBIE_CAT.get());
					map.put(EntityType.HORSE, EntityType.ZOMBIE_HORSE);
					map.put(EntityType.PARROT, CCEntities.ZOMBIE_PARROT.get());
					map.put(CCEntities.ZOMBIE_WOLF.get(), CCEntities.SKELETON_WOLF.get());
					map.put(CCEntities.ZOMBIE_CAT.get(), CCEntities.SKELETON_CAT.get());
					map.put(EntityType.ZOMBIE_HORSE, EntityType.SKELETON_HORSE);
					map.put(CCEntities.ZOMBIE_PARROT.get(), CCEntities.SKELETON_PARROT.get());
				});

				EntityType<?> entityType = UNDEAD_MAP.get(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(tag.getString(PET_ID))));
				System.out.println(entityType);

				if (entityType != null) {
					AnimalEntity entity = (AnimalEntity) entityType.create(world);
					UUID owner = tag.contains(OWNER_ID) ? UUID.fromString(tag.getString(OWNER_ID)) : player.getUUID();
					DyeColor collarColor = DyeColor.byId(tag.getInt(COLLAR_COLOR));
					int variant = tag.getInt(PET_VARIANT);
					LivingEntity returnEntity = null;

					entity.setBaby(tag.getBoolean(IS_CHILD));
					entity.setPos(offsetPos.getX() + 0.5F, offsetPos.getY(), offsetPos.getZ() + 0.5F);
					if (tag.contains(PET_NAME))
						entity.setCustomName(new StringTextComponent(tag.getString(PET_NAME)));

					if (entity instanceof TameableEntity) {
						TameableEntity tameableEntity = (TameableEntity) entity;
						tameableEntity.setTame(true);
						tameableEntity.setOwnerUUID(owner);

						if (tameableEntity instanceof WolfEntity) {
							WolfEntity wolf = (WolfEntity) tameableEntity;
							wolf.setCollarColor(collarColor);
							returnEntity = wolf;
						}

						if (tameableEntity instanceof CatEntity) {
							CatEntity cat = (CatEntity) tameableEntity;
							cat.setCatType(variant);
							cat.setCollarColor(collarColor);
							returnEntity = cat;
						}

						if (tameableEntity instanceof ParrotEntity) {
							ParrotEntity parrot = (ParrotEntity) tameableEntity;
							parrot.setVariant(variant);
							returnEntity = parrot;
						}

					} else if (entity instanceof AbstractHorseEntity) {
						AbstractHorseEntity horseEntity = (AbstractHorseEntity) entity;

						horseEntity.setTamed(true);
						horseEntity.setOwnerUUID(owner);

						horseEntity.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(tag.getDouble(HORSE_STRENGTH));
						horseEntity.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(tag.getDouble(HORSE_SPEED));
						horseEntity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(tag.getDouble(HORSE_HEALTH));

						returnEntity = horseEntity;
					}

					if (returnEntity != null) {
						world.setBlockAndUpdate(pos, state.setValue(GravestoneBlock.CHARGE, 0));

						LightningBoltEntity bolt = EntityType.LIGHTNING_BOLT.create(world);
						bolt.moveTo(Vector3d.atBottomCenterOf(entity.blockPosition()));
						bolt.setCause(player instanceof ServerPlayerEntity ? (ServerPlayerEntity) player : null);
						bolt.setVisualOnly(true);
						world.addFreshEntity(bolt);

						world.playSound(player, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
						world.addFreshEntity(returnEntity);
						if (!player.abilities.instabuild)
							stack.shrink(1);
					}
				}
			}

			return ActionResultType.sidedSuccess(world.isClientSide);
		}
		return ActionResultType.PASS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		CompoundNBT tag = stack.getOrCreateTag();

		if (tag.contains(PET_NAME)) {
			String name = tag.getString(PET_NAME);
			tooltip.add(new StringTextComponent(name).withStyle(TextFormatting.GRAY, TextFormatting.ITALIC));
		}

		if (tag.contains(PET_ID)) {
			String petID = tag.getString(PET_ID);
			EntityType<?> pet = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(petID));

			ITextComponent petType = new TranslationTextComponent(pet.getDescriptionId()).withStyle(TextFormatting.GRAY);
			if (tag.contains(PET_VARIANT)) {
				int type = tag.getInt(PET_VARIANT);
				String texture = "";

				if (pet == EntityType.CAT || pet == CCEntities.ZOMBIE_CAT.get()) {
					texture = CatEntity.TEXTURE_BY_TYPE.get(type).toString().replace("minecraft:textures/entity/cat/", "");
					texture = texture.replace(".png", "").replace("all_", "").replace("_", " ").concat(" ");
				}

				if (pet == EntityType.PARROT || pet == CCEntities.ZOMBIE_PARROT.get()) {
					texture = ParrotRenderer.PARROT_LOCATIONS[type].toString().replace("minecraft:textures/entity/parrot/parrot_", "");
					texture = texture.replace(".png", "").replace("_", "-").concat(" ");
				}

				petType = new StringTextComponent(WordUtils.capitalize(texture)).withStyle(TextFormatting.GRAY).append(petType);
			}

			if (tag.getBoolean(IS_CHILD))
				petType = new TranslationTextComponent("tooltip.caverns_and_chasms.baby").withStyle(TextFormatting.GRAY).append(" ").append(petType);

			tooltip.add(petType);
		}

		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}

	public int getColor(ItemStack stack) {
		CompoundNBT tag = stack.getOrCreateTag();
		return tag.contains(COLLAR_COLOR) ? DyeColor.byId(tag.getInt(COLLAR_COLOR)).getColorValue() : 10511680;
	}
}
