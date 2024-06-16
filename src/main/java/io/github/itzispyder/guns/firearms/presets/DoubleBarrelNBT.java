package io.github.itzispyder.guns.firearms.presets;

import io.github.itzispyder.guns.firearms.GunNBT;
import io.github.itzispyder.guns.firearms.scopes.ScopeType;
import org.bukkit.Sound;

public class DoubleBarrelNBT extends GunNBT {

    public DoubleBarrelNBT() {
        super();

        shootSound = Sound.ENTITY_IRON_GOLEM_REPAIR;
        shootSoundPitch = 2F;

        scopeType = ScopeType.SPREAD;
        scopeSlownessAmplifier = 5;

        maxUncertainty = 0.3;
        distance = 16;
        damage = 2;
        sneakUncertaintyMultiplier = 0.5;

        roundsPerShot = 15;
        cooldownTicks = 20;
        reloadTicks = 20;
        ammo = maxAmmo = 4;
        repetitionIterations = 2;
    }
}
