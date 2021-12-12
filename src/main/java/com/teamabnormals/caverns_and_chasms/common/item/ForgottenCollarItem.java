package com.teamabnormals.caverns_and_chasms.common.item;

import com.google.common.collect.Maps;
import com.teamabnormals.caverns_and_chasms.common.block.GravestoneBlock;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.other.tags.CCEntityTypeTags;
import com.teamabnormals.caverns_and_chasms.core.registry.*;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nullable;
import java.util.*;

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

		if (type.is(CCEntityTypeTags.COLLAR_DROP_MOBS)) {
			ItemStack collar = new ItemStack(CCItems.FORGOTTEN_COLLAR.get());
			CompoundTag tag = collar.getOrCreateTag();

			tag.putString(ForgottenCollarItem.PET_ID, type.getRegistryName().toString());
			tag.putBoolean(ForgottenCollarItem.IS_CHILD, entity.isBaby());
			if (entity.hasCustomName()) {
				tag.putString(ForgottenCollarItem.PET_NAME, entity.getCustomName().getString());
			}

			if (entity instanceof TamableAnimal) {
				TamableAnimal pet = (TamableAnimal) entity;
				if (pet.isTame()) {
					tag.putString(ForgottenCollarItem.OWNER_ID, pet.getOwnerUUID().toString());

					if (entity instanceof Wolf) {
						Wolf wolf = (Wolf) entity;
						tag.putInt(ForgottenCollarItem.COLLAR_COLOR, wolf.getCollarColor().getId());
					}

					if (entity instanceof Cat) {
						Cat cat = (Cat) entity;
						tag.putInt(ForgottenCollarItem.PET_VARIANT, cat.getCatType());
						tag.putInt(ForgottenCollarItem.COLLAR_COLOR, cat.getCollarColor().getId());
					}

					if (entity instanceof Parrot) {
						Parrot parrot = (Parrot) entity;
						tag.putInt(ForgottenCollarItem.PET_VARIANT, parrot.getVariant());
					}

					entity.spawnAtLocation(collar);
				}
			} else if (entity instanceof AbstractHorse) {
				AbstractHorse horse = (AbstractHorse) entity;
				if (horse.isTamed()) {
					tag.putString(ForgottenCollarItem.OWNER_ID, horse.getOwnerUUID().toString());
					tag.putDouble(ForgottenCollarItem.HORSE_SPEED, horse.getAttributeBaseValue(Attributes.MOVEMENT_SPEED));
					tag.putDouble(ForgottenCollarItem.HORSE_HEALTH, horse.getAttributeBaseValue(Attributes.MAX_HEALTH));
					tag.putDouble(ForgottenCollarItem.HORSE_STRENGTH, horse.getAttributeBaseValue(Attributes.JUMP_STRENGTH));
					if (entity instanceof Horse)
						tag.putInt(ForgottenCollarItem.PET_VARIANT, ((Horse) entity).getTypeVariant());

					entity.spawnAtLocation(collar);
				}
			}
		}
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = world.getBlockState(pos);
		ItemStack stack = context.getItemInHand();

		if (state.is(CCBlocks.GRAVESTONE.get()) && state.getValue(GravestoneBlock.CHARGE) == 10 && world.canSeeSky(pos.above()) && world.isNight()) {
			BlockPos offsetPos = pos.relative(state.getValue(GravestoneBlock.FACING));
			if (!world.getBlockState(offsetPos).getCollisionShape(world, offsetPos).isEmpty())
				return InteractionResult.FAIL;

			Player player = context.getPlayer();
			CompoundTag tag = stack.getOrCreateTag();

			if (tag.contains(PET_ID)) {
				Map<EntityType<?>, EntityType<?>> UNDEAD_MAP = Util.make(Maps.newHashMap(), (map) -> {
					map.put(EntityType.WOLF, CCEntityTypes.ZOMBIE_WOLF.get());
					map.put(EntityType.CAT, CCEntityTypes.ZOMBIE_CAT.get());
					map.put(EntityType.HORSE, EntityType.ZOMBIE_HORSE);
					map.put(EntityType.PARROT, CCEntityTypes.ZOMBIE_PARROT.get());
					map.put(CCEntityTypes.ZOMBIE_WOLF.get(), CCEntityTypes.SKELETON_WOLF.get());
					map.put(CCEntityTypes.ZOMBIE_CAT.get(), CCEntityTypes.SKELETON_CAT.get());
					map.put(EntityType.ZOMBIE_HORSE, EntityType.SKELETON_HORSE);
					map.put(CCEntityTypes.ZOMBIE_PARROT.get(), CCEntityTypes.SKELETON_PARROT.get());
				});

				EntityType<?> entityType = UNDEAD_MAP.get(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(tag.getString(PET_ID))));
				System.out.println(entityType);

				if (entityType != null) {
					Animal entity = (Animal) entityType.create(world);
					UUID owner = tag.contains(OWNER_ID) ? UUID.fromString(tag.getString(OWNER_ID)) : player.getUUID();
					DyeColor collarColor = DyeColor.byId(tag.getInt(COLLAR_COLOR));
					int variant = tag.getInt(PET_VARIANT);
					LivingEntity returnEntity = null;

					entity.setBaby(tag.getBoolean(IS_CHILD));
					entity.setPos(offsetPos.getX() + 0.5F, offsetPos.getY(), offsetPos.getZ() + 0.5F);
					if (tag.contains(PET_NAME))
						entity.setCustomName(new TextComponent(tag.getString(PET_NAME)));

					if (entity instanceof TamableAnimal) {
						TamableAnimal tameableEntity = (TamableAnimal) entity;
						tameableEntity.setTame(true);
						tameableEntity.setOwnerUUID(owner);

						if (tameableEntity instanceof Wolf) {
							Wolf wolf = (Wolf) tameableEntity;
							wolf.setCollarColor(collarColor);
							returnEntity = wolf;
						}

						if (tameableEntity instanceof Cat) {
							Cat cat = (Cat) tameableEntity;
							cat.setCatType(variant);
							cat.setCollarColor(collarColor);
							returnEntity = cat;
						}

						if (tameableEntity instanceof Parrot) {
							Parrot parrot = (Parrot) tameableEntity;
							parrot.setVariant(variant);
							returnEntity = parrot;
						}

					} else if (entity instanceof AbstractHorse) {
						AbstractHorse horseEntity = (AbstractHorse) entity;

						horseEntity.setTamed(true);
						horseEntity.setOwnerUUID(owner);

						horseEntity.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(tag.getDouble(HORSE_STRENGTH));
						horseEntity.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(tag.getDouble(HORSE_SPEED));
						horseEntity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(tag.getDouble(HORSE_HEALTH));

						returnEntity = horseEntity;
					}

					if (returnEntity != null) {
						world.setBlockAndUpdate(pos, state.setValue(GravestoneBlock.CHARGE, 0));

						LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(world);
						bolt.moveTo(Vec3.atBottomCenterOf(entity.blockPosition()));
						bolt.setCause(player instanceof ServerPlayer ? (ServerPlayer) player : null);
						bolt.setVisualOnly(true);
						world.addFreshEntity(bolt);

						world.playSound(player, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
						world.addFreshEntity(returnEntity);
						if (!player.getAbilities().instabuild)
							stack.shrink(1);
					}
				}
			}

			return InteractionResult.sidedSuccess(world.isClientSide);
		}
		return InteractionResult.PASS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		CompoundTag tag = stack.getOrCreateTag();

		if (tag.contains(PET_NAME)) {
			String name = tag.getString(PET_NAME);
			tooltip.add(new TextComponent(name).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
		}

		if (tag.contains(PET_ID)) {
			String petID = tag.getString(PET_ID);
			EntityType<?> pet = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(petID));

			Component petType = new TranslatableComponent(pet.getDescriptionId()).withStyle(ChatFormatting.GRAY);
			if (tag.contains(PET_VARIANT)) {
				int type = tag.getInt(PET_VARIANT);
				String texture = "";

				if (pet == EntityType.CAT || pet == CCEntityTypes.ZOMBIE_CAT.get()) {
					texture = Cat.TEXTURE_BY_TYPE.get(type).toString().replace("minecraft:textures/entity/cat/", "");
					texture = texture.replace(".png", "").replace("all_", "").replace("_", " ").concat(" ");
				}

				if (pet == EntityType.PARROT || pet == CCEntityTypes.ZOMBIE_PARROT.get()) {
					texture = ParrotRenderer.PARROT_LOCATIONS[type].toString().replace("minecraft:textures/entity/parrot/parrot_", "");
					texture = texture.replace(".png", "").replace("_", "-").concat(" ");
				}

				petType = new TextComponent(WordUtils.capitalize(texture)).withStyle(ChatFormatting.GRAY).append(petType);
			}

			if (tag.getBoolean(IS_CHILD))
				petType = new TranslatableComponent("tooltip.caverns_and_chasms.baby").withStyle(ChatFormatting.GRAY).append(" ").append(petType);

			tooltip.add(petType);
		}

		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}

	public int getColor(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		return tag.contains(COLLAR_COLOR) ? DyeColor.byId(tag.getInt(COLLAR_COLOR)).getTextColor() : 10511680;
	}
}
