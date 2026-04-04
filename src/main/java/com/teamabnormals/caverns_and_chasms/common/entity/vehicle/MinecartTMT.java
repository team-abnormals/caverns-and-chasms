package com.teamabnormals.caverns_and_chasms.common.entity.vehicle;

import com.teamabnormals.caverns_and_chasms.common.level.SpinelBoom;
import com.teamabnormals.caverns_and_chasms.common.network.S2CSpinelBoomMessage;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages.SpawnEntity;

import javax.annotation.Nullable;

public class MinecartTMT extends AbstractMinecart {
	private static final byte EVENT_PRIME = 10;
	private int fuse = -1;

	public MinecartTMT(EntityType<? extends MinecartTMT> p_38649_, Level p_38650_) {
		super(p_38649_, p_38650_);
	}

	public MinecartTMT(Level p_38652_, double p_38653_, double p_38654_, double p_38655_) {
		super(CCEntityTypes.TMT_MINECART.get(), p_38652_, p_38653_, p_38654_, p_38655_);
	}

	public MinecartTMT(SpawnEntity spawnEntity, Level level) {
		this(CCEntityTypes.TMT_MINECART.get(), level);
	}

	@Override
	public AbstractMinecart.Type getMinecartType() {
		return AbstractMinecart.Type.TNT;
	}

	@Override
	public BlockState getDefaultDisplayBlockState() {
		return CCBlocks.TMT.get().defaultBlockState();
	}

	public void tick() {
		super.tick();
		if (this.fuse > 0) {
			--this.fuse;
			this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
		} else if (this.fuse == 0) {
			this.explode(this.getDeltaMovement().horizontalDistanceSqr());
		}

		if (this.horizontalCollision) {
			double d0 = this.getDeltaMovement().horizontalDistanceSqr();
			if (d0 >= (double) 0.01F) {
				this.explode(d0);
			}
		}

	}

	@Override
	public boolean hurt(DamageSource source, float damage) {
		Entity entity = source.getDirectEntity();
		if (entity instanceof AbstractArrow arrow) {
			if (arrow.isOnFire()) {
				DamageSource damagesource = this.damageSources().explosion(this, source.getEntity());
				this.explode(damagesource, arrow.getDeltaMovement().lengthSqr());
			}
		}

		return super.hurt(source, damage);
	}

	@Override
	public void destroy(DamageSource p_38664_) {
		double d0 = this.getDeltaMovement().horizontalDistanceSqr();
		if (!p_38664_.is(DamageTypeTags.IS_FIRE) && !p_38664_.is(DamageTypeTags.IS_EXPLOSION) && !(d0 >= (double) 0.01F)) {
			super.destroy(p_38664_);
		} else {
			if (this.fuse < 0) {
				this.primeFuse();
				this.fuse = this.random.nextInt(20) + this.random.nextInt(20);
			}

		}
	}

	@Override
	protected Item getDropItem() {
		return CCItems.TMT_MINECART.get();
	}

	protected void explode(double p_38689_) {
		this.explode(null, p_38689_);
	}

	protected void explode(@Nullable DamageSource p_259539_, double p_260287_) {
		if (!this.level().isClientSide) {
			double d0 = Math.sqrt(p_260287_);
			if (d0 > 5.0D) {
				d0 = 5.0D;
			}

			SpinelBoom boom = new SpinelBoom(this.level(), this, this.getX(), this.getY(), this.getZ(), (float) (4.0D + this.random.nextDouble() * 1.5D * d0));
			if (ForgeEventFactory.onExplosionStart(this.level(), boom)) return;
			boom.explode();
			boom.finalizeExplosion(true);
			CavernsAndChasms.CHANNEL.send(PacketDistributor.DIMENSION.with(() -> this.level().dimension()), new S2CSpinelBoomMessage((float) this.getX(), (float) this.getY(0.0625D), (float) this.getZ(), 4.0F, boom.getToBlow()));
			this.discard();
		}

	}

	@Override
	public boolean causeFallDamage(float p_150347_, float p_150348_, DamageSource p_150349_) {
		if (p_150347_ >= 3.0F) {
			float f = p_150347_ / 10.0F;
			this.explode(f * f);
		}

		return super.causeFallDamage(p_150347_, p_150348_, p_150349_);
	}

	@Override
	public void activateMinecart(int p_38659_, int p_38660_, int p_38661_, boolean p_38662_) {
		if (p_38662_ && this.fuse < 0) {
			this.primeFuse();
		}

	}

	@Override
	public void handleEntityEvent(byte p_38657_) {
		if (p_38657_ == 10) {
			this.primeFuse();
		} else {
			super.handleEntityEvent(p_38657_);
		}

	}

	public void primeFuse() {
		this.fuse = 80;
		if (!this.level().isClientSide) {
			this.level().broadcastEntityEvent(this, (byte) 10);
			if (!this.isSilent()) {
				this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
			}
		}

	}

	public int getFuse() {
		return this.fuse;
	}

	public boolean isPrimed() {
		return this.fuse > -1;
	}

	@Override
	public float getBlockExplosionResistance(Explosion p_38675_, BlockGetter p_38676_, BlockPos p_38677_, BlockState p_38678_, FluidState p_38679_, float p_38680_) {
		return !this.isPrimed() || !p_38678_.is(BlockTags.RAILS) && !p_38676_.getBlockState(p_38677_.above()).is(BlockTags.RAILS) ? super.getBlockExplosionResistance(p_38675_, p_38676_, p_38677_, p_38678_, p_38679_, p_38680_) : 0.0F;
	}

	@Override
	public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, float p_38673_) {
		return (!this.isPrimed() || !state.is(BlockTags.RAILS) && !level.getBlockState(pos.above()).is(BlockTags.RAILS)) && super.shouldBlockExplode(explosion, level, pos, state, p_38673_);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("TNTFuse", 99)) {
			this.fuse = tag.getInt("TNTFuse");
		}

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("TNTFuse", this.fuse);
	}

	@Override
	public ItemStack getPickResult() {
		return new ItemStack(CCBlocks.TMT.get());
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}