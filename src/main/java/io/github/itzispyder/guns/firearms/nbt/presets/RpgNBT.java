package io.github.itzispyder.guns.firearms.nbt.presets;

import io.github.itzispyder.guns.firearms.nbt.GunNBT;
import io.github.itzispyder.guns.firearms.nbt.ParticleInstance;
import io.github.itzispyder.guns.firearms.nbt.RaycastMode;
import io.github.itzispyder.guns.firearms.nbt.SoundInstance;
import io.github.itzispyder.guns.firearms.scopes.ScopeType;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class RpgNBT extends GunNBT {

    public RpgNBT() {
        super();

        fire.sounds.clear();
        fire.sounds.add(new SoundInstance(Sound.ENTITY_GENERIC_EXPLODE, 10F, 0.69F, 20));
        fire.sounds.add(new SoundInstance(Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 10F, 0.1F, 20));

        impact.sounds.clear();
        impact.sounds.add(new SoundInstance(Sound.ENTITY_GENERIC_EXPLODE, 10F, 1.2F, 20));
        impact.particles.clear();
        impact.particles.add(new ParticleInstance(Particle.EXPLOSION_NORMAL, 1, 1, 1, 1, 1));
        impact.particles.add(new ParticleInstance(Particle.EXPLOSION_HUGE, 1, 1, 1, 1, 1));
        impact.particles.add(new ParticleInstance(Particle.EXPLOSION_LARGE, 1, 1, 1, 1, 1));

        raycastMode = RaycastMode.BALLISTICS;

        ballistics.bulletDropPerTick = 0;
        ballistics.glowColor = Color.ORANGE;
        ballistics.blocksPerTick = 2;

        damage = 20;
        cooldownTicks = 20;
        maxAmmo = 1;
        ammo = 1;
        reloadTicks = 20;
        easterEgg = true;
        maxUncertainty = 0;
        sneakUncertaintyMultiplier = 0;
        scopeType = ScopeType.SPREAD;
    }
}
