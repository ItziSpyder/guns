package io.github.itzispyder.guns.firearms.nbt.presets;

import io.github.itzispyder.guns.firearms.nbt.GunNBT;
import io.github.itzispyder.guns.firearms.nbt.ParticleInstance;
import io.github.itzispyder.guns.firearms.nbt.RaycastMode;
import io.github.itzispyder.guns.firearms.nbt.SoundInstance;
import io.github.itzispyder.guns.firearms.scopes.ScopeType;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class BlasterRifleNBT extends GunNBT {

    public BlasterRifleNBT() {
        super();

        fire.sounds.clear();
        fire.sounds.add(new SoundInstance(Sound.ENTITY_BEE_HURT, 0.5F, 1.5F, 15));
        fire.sounds.add(new SoundInstance(Sound.BLOCK_BEACON_DEACTIVATE, 10F, 2F, 15));
        impact.particles.add(new ParticleInstance(Particle.DRIP_LAVA, 10, 0.0625, 0.0625, 0.0625, 0));
        impact.particles.add(new ParticleInstance(Particle.FLAME, 10, 0.0625, 0.0625, 0.0625, 0));

        raycastMode = RaycastMode.BALLISTICS;

        ballistics.bulletDropPerTick = 0.3;

        scopeType = ScopeType.SNIPER;
        scopeSlownessAmplifier = 255;

        maxUncertainty = 0;
        hitscan.distance = 128;
        damage = 10;
        sneakUncertaintyMultiplier = 0;

        cooldownTicks = 10;
        reloadTicks = 20;
        ammo = maxAmmo = 10;
    }
}
