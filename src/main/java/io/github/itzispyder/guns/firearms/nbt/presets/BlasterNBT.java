package io.github.itzispyder.guns.firearms.nbt.presets;

import io.github.itzispyder.guns.firearms.nbt.GunNBT;
import io.github.itzispyder.guns.firearms.nbt.ParticleInstance;
import io.github.itzispyder.guns.firearms.nbt.RaycastMode;
import io.github.itzispyder.guns.firearms.nbt.SoundInstance;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class BlasterNBT extends GunNBT {

    public BlasterNBT() {
        super();

        fire.sounds.clear();
        fire.sounds.add(new SoundInstance(Sound.ENTITY_BEE_HURT, 0.5F, 1.5F, 15));
        fire.sounds.add(new SoundInstance(Sound.BLOCK_BEACON_DEACTIVATE, 10F, 2F, 15));
        impact.particles.add(new ParticleInstance(Particle.DRIP_LAVA, 10, 0.0625, 0.0625, 0.0625, 0));
        impact.particles.add(new ParticleInstance(Particle.FLAME, 10, 0.0625, 0.0625, 0.0625, 0));

        raycastMode = RaycastMode.BALLISTICS;

        ballistics.bulletDropPerTick = 0.5;

        maxUncertainty = 0;
        hitscan.distance = 128;
        damage = 5;
        sneakUncertaintyMultiplier = 0;

        cooldownTicks = 0;
        reloadTicks = 20;
        ammo = maxAmmo = 500;

        repetitionIterations = 2;
    }
}
