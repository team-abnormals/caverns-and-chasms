package com.teamabnormals.caverns_and_chasms.common.entity.decoration;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.CopperGolem;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.CopperGolem.Oxidation;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolActions;

import java.util.Collections;
import java.util.List;

public class OxidizedCopperGolem extends LivingEntity {
	private static final List<ItemStack> EMPTY_LIST = Collections.emptyList();

	private static final EntityDataAccessor<Boolean> DAMAGED = SynchedEntityData.defineId(OxidizedCopperGolem.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> WAXED = SynchedEntityData.defineId(OxidizedCopperGolem.class, EntityDataSerializers.BOOLEAN);

	private boolean isNoAi;
	private boolean persistenceRequired;

	public long lastHit;

	public OxidizedCopperGolem(EntityType<? extends OxidizedCopperGolem> entity, Level level) {
		super(entity, level);
		this.setMaxUpStep(0.0F);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DAMAGED, false);
		this.entityData.define(WAXED, false);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return CCSoundEvents.ENTITY_COPPER_GOLEM_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CCSoundEvents.ENTITY_COPPER_GOLEM_DEATH.get();
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("NoAI", this.isNoAi());
		compound.putBoolean("PersistenceRequired", this.isPersistenceRequired());
		compound.putBoolean("Damaged", this.isDamaged());
		compound.putBoolean("Waxed", this.isWaxed());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setNoAi(compound.getBoolean("NoAI"));
		this.setPersistenceRequired(compound.getBoolean("PersistenceRequired"));
		this.setDamaged(compound.getBoolean("Damaged"));
		this.setWaxed(compound.getBoolean("Waxed"));
	}

	@Override
	public InteractionResult interactAt(Player player, Vec3 pos, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		Item item = itemstack.getItem();
		boolean success = false;
		if (item == Items.HONEYCOMB) {
			if (!this.isWaxed()) {
				this.setWaxed(true);
				this.spawnSparkParticles(ParticleTypes.WAX_ON);
				this.level().playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.HONEYCOMB_WAX_ON, SoundSource.NEUTRAL, 1.0F, 1.0F);
				if (!player.getAbilities().instabuild) {
					itemstack.shrink(1);
				}
				success = true;
			}
		} else if (itemstack.getItem() instanceof AxeItem) {
			if (this.isWaxed()) {
				this.setWaxed(false);
				this.spawnSparkParticles(ParticleTypes.WAX_OFF);
				this.level().playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.AXE_WAX_OFF, SoundSource.NEUTRAL, 1.0F, 1.0F);
				success = true;
			} else {
				this.deoxidize(Oxidation.WEATHERED);
				this.spawnSparkParticles(ParticleTypes.SCRAPE);
				this.level().playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.AXE_SCRAPE, SoundSource.NEUTRAL, 1.0F, 1.0F);
				this.gameEvent(GameEvent.ENTITY_INTERACT);
				success = true;
			}

			if (success && !this.level().isClientSide) {
				itemstack.hurtAndBreak(1, player, (entity) -> {
					entity.broadcastBreakEvent(hand);
				});
			}
		}

		return success ? InteractionResult.sidedSuccess(this.level().isClientSide) : InteractionResult.PASS;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (!this.level().isClientSide && !this.isRemoved()) {
			Entity directentity = source.getDirectEntity();
			if (this.damageSources().fellOutOfWorld().equals(source)) {
				this.removeStatue();
				return false;
			} else if (!this.isInvulnerableTo(source)) {
				if (source.is(DamageTypeTags.IS_EXPLOSION)) {
					this.breakStatue(source, true, false);
					return false;
				} else if ("player".equals(source.getMsgId()) && directentity instanceof Player && ((Player) directentity).getAbilities().mayBuild) {
					if (source.isCreativePlayer()) {
						this.breakStatue(source, false, false);
						return true;
					} else if (((Player) directentity).getMainHandItem().canPerformAction(ToolActions.PICKAXE_DIG)) {
						if (this.isDamaged()) {
							this.breakStatue(source, true, true);
						} else {
							this.setDamaged(true);
							this.gameEvent(GameEvent.ENTITY_DAMAGE, directentity);
							this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.COPPER_BREAK, this.getSoundSource(), 1.0F, 1.0F);
						}
						return true;
					}
				}
			}
		}

		return false;
	}

	@Override
	public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
		CopperGolem coppergolem = this.deoxidize(Oxidation.UNAFFECTED);
		if (coppergolem != null) {
			coppergolem.spinHead();
			this.level().broadcastEntityEvent(coppergolem, (byte) 5);
		}
	}

	private void breakStatue(DamageSource source, boolean dropLoot, boolean brokenWithPickaxe) {
		if (dropLoot) {
			if (brokenWithPickaxe) {
				ItemStack itemstack = this.isWaxed() ? new ItemStack(CCItems.WAXED_OXIDIZED_COPPER_GOLEM.get()) : new ItemStack(CCItems.OXIDIZED_COPPER_GOLEM.get());

				CompoundTag compound = itemstack.getOrCreateTag();
				if (this.hasCustomName())
					itemstack.setHoverName(this.getCustomName());
				if (this.isNoAi())
					compound.putBoolean("NoAI", true);
				if (this.isSilent())
					compound.putBoolean("Silent", true);
				if (this.isNoGravity())
					compound.putBoolean("NoGravity", true);
				if (this.hasGlowingTag())
					compound.putBoolean("Glowing", true);
				if (this.isInvulnerable())
					compound.putBoolean("Invulnerable", true);
				if (this.isPersistenceRequired())
					compound.putBoolean("PersistenceRequired", true);

				Block.popResource(this.level(), this.blockPosition(), itemstack);
			} else {
				this.dropAllDeathLoot(source);
			}
		}

		((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OXIDIZED_COPPER.defaultBlockState()), this.getX(), this.getY(0.6666666666666666D), this.getZ(), 10, (this.getBbWidth() / 4.0F), (this.getBbHeight() / 4.0F), (this.getBbWidth() / 4.0F), 0.05D);
		this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.COPPER_BREAK, this.getSoundSource(), 1.0F, 1.0F);
		this.gameEvent(GameEvent.ENTITY_DIE);
		this.removeStatue();
	}

	private void removeStatue() {
		this.remove(RemovalReason.KILLED);
	}

	public CopperGolem deoxidize(Oxidation oxidation) {
		if (!this.isRemoved()) {
			CopperGolem coppergolem = CCEntityTypes.COPPER_GOLEM.get().create(this.level());
			coppergolem.copyPosition(this);
			coppergolem.setYHeadRot(this.getYRot());
			coppergolem.setYBodyRot(this.getYRot());

			coppergolem.setNoAi(this.isNoAi());
			coppergolem.setSilent(this.isSilent());
			coppergolem.setNoGravity(this.isNoGravity());
			coppergolem.setGlowingTag(this.hasGlowingTag());
			coppergolem.setInvulnerable(this.isInvulnerable());
			coppergolem.setOxidation(oxidation);
			coppergolem.setWaxed(this.isWaxed());

			if (this.isDamaged())
				coppergolem.setHealth(coppergolem.getMaxHealth() * 0.5F - 1.0F);

			if (this.isPersistenceRequired())
				coppergolem.setPersistenceRequired();

			if (this.hasCustomName()) {
				coppergolem.setCustomName(this.getCustomName());
				coppergolem.setCustomNameVisible(this.isCustomNameVisible());
			}

			this.level().addFreshEntity(coppergolem);

			if (this.isPassenger()) {
				Entity entity = this.getVehicle();
				this.stopRiding();
				coppergolem.startRiding(entity, true);
			}

			this.discard();
			return coppergolem;
		}

		return null;
	}

	public boolean isDamaged() {
		return this.entityData.get(DAMAGED);
	}

	public void setDamaged(boolean damaged) {
		this.entityData.set(DAMAGED, damaged);
	}

	public boolean isWaxed() {
		return this.entityData.get(WAXED);
	}

	public void setWaxed(boolean waxed) {
		this.entityData.set(WAXED, waxed);
	}

	private void spawnSparkParticles(ParticleOptions particle) {
		if (this.level().isClientSide) {
			for (int i = 0; i < 7; ++i) {
				double d0 = Mth.nextDouble(this.getRandom(), -1.0D, 1.0D);
				double d1 = Mth.nextDouble(this.getRandom(), -1.0D, 1.0D);
				double d2 = Mth.nextDouble(this.getRandom(), -1.0D, 1.0D);
				this.level().addParticle(particle, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
			}
		}
	}

	public boolean isNoAi() {
		return this.isNoAi;
	}

	public void setNoAi(boolean noAi) {
		this.isNoAi = noAi;
	}

	public boolean isPersistenceRequired() {
		return this.persistenceRequired;
	}

	public void setPersistenceRequired(boolean persistenceRequired) {
		this.persistenceRequired = persistenceRequired;
	}

	@Override
	public Iterable<ItemStack> getArmorSlots() {
		return EMPTY_LIST;
	}

	@Override
	public ItemStack getItemBySlot(EquipmentSlot slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
	}

	@Override
	public HumanoidArm getMainArm() {
		return HumanoidArm.RIGHT;
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
		return size.height * 0.8F;
	}

	@Override
	public boolean canBeCollidedWith() {
		return this.isAlive();
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected void doPush(Entity entity) {
	}

	@Override
	protected void pushEntities() {
	}

	@Override
	public boolean isAffectedByPotions() {
		return false;
	}

	@Override
	public boolean attackable() {
		return false;
	}

	@Override
	protected float tickHeadTurn(float yRot, float xRot) {
		this.yBodyRotO = this.yRotO;
		this.yBodyRot = this.getYRot();
		return 0.0F;
	}

	@Override
	public void setYBodyRot(float rot) {
		this.yBodyRotO = this.yRotO = rot;
		this.yHeadRotO = this.yHeadRot = rot;
	}

	@Override
	public void setYHeadRot(float rot) {
		this.yBodyRotO = this.yRotO = rot;
		this.yHeadRotO = this.yHeadRot = rot;
	}

	@Override
	public ItemStack getPickResult() {
		return new ItemStack(CCItems.OXIDIZED_COPPER_GOLEM.get());
	}
}
