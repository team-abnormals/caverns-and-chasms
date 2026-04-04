package com.teamabnormals.caverns_and_chasms.client.resources.sounds;

import com.teamabnormals.caverns_and_chasms.common.block.entity.holdable.MovingDoorHeaderBlockEntity;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MovingDoorMoveSoundInstance extends AbstractTickableSoundInstance {
	private final MovingDoorHeaderBlockEntity headerEntity;
	private final SoundEvent startSound;
	private final SoundEvent stopSound;
	private final int startSoundTime;
	private boolean wasAudible;
	private float wantedVolume;
	private int startSoundTimer;

	public MovingDoorMoveSoundInstance(MovingDoorHeaderBlockEntity headerEntity, SoundEvent startSound, SoundEvent moveSound, SoundEvent stopSound, int startSoundTime) {
		super(moveSound, SoundSource.BLOCKS, SoundInstance.createUnseededRandom());
		this.headerEntity = headerEntity;
		this.startSound = startSound;
		this.stopSound = stopSound;
		this.startSoundTime = startSoundTime;
		BlockPos blockPos = headerEntity.getBlockPos();
		this.x = blockPos.getX() + 0.5D;
		this.y = blockPos.getY() + 0.5D;
		this.z = blockPos.getZ() + 0.5D;
		this.looping = true;
		this.attenuation = Attenuation.LINEAR;
		this.delay = 0;
		this.volume = 0.0F;
	}

	@Override
	public void tick() {
		if (!this.headerEntity.isRemoved()) {
			if (this.wantedVolume > 0.0F) {
				if (this.startSoundTimer > 0) {
					this.startSoundTimer--;
					this.volume = 0.0F;
				} else {
					this.volume = this.wantedVolume;
				}
			} else {
				this.volume = 0.0F;
			}
			this.wasAudible = this.wantedVolume > 0.0F;
		} else {
			this.stop();
		}
	}

	@Override
	public boolean canStartSilent() {
		return true;
	}

	public void setVolume(float volume) {
		float oldWantedVolume = this.wantedVolume;
		this.wantedVolume = volume;
		if (this.wantedVolume > 0.0F && !this.wasAudible) {
			this.startSoundTimer = this.startSoundTime;
			this.headerEntity.getLevel().playLocalSound(this.x, this.y, this.z, this.startSound, SoundSource.BLOCKS, volume, 1.0F, false);
		} else if (this.wantedVolume <= 0.0F && this.wasAudible && this.startSoundTimer <= 0) {
			this.headerEntity.getLevel().playLocalSound(this.x, this.y, this.z, this.stopSound, SoundSource.BLOCKS, oldWantedVolume, 1.0F, false);
		}
	}
}