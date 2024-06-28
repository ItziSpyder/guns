package io.github.itzispyder.guns.firearms.nbt.presets;

import io.github.itzispyder.guns.firearms.nbt.GunNBT;
import io.github.itzispyder.guns.firearms.scopes.ScopeType;
import org.bukkit.Sound;

public class DoubleBarrelNBT extends GunNBT {

    public DoubleBarrelNBT() {
        super();

        fire.sounds.get(0).sound = Sound.ENTITY_IRON_GOLEM_REPAIR;
        fire.sounds.get(0).pitch = 2F;

        scopeType = ScopeType.SPREAD;
        scopeSlownessAmplifier = 5;

        maxUncertainty = 0.3;
        hitscan.distance = 16;
        damage = 2;
        sneakUncertaintyMultiplier = 0.5;

        roundsPerShot = 15;
        cooldownTicks = 20;
        reloadTicks = 20;
        ammo = maxAmmo = 4;
        repetitionIterations = 2;
    }
}
