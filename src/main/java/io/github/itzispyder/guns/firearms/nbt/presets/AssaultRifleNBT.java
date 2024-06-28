package io.github.itzispyder.guns.firearms.nbt.presets;

import io.github.itzispyder.guns.firearms.nbt.GunNBT;
import io.github.itzispyder.guns.firearms.scopes.ScopeType;
import org.bukkit.Sound;

public class AssaultRifleNBT extends GunNBT {

    public AssaultRifleNBT() {
        super();

        fire.sounds.get(0).sound = Sound.ENTITY_FIREWORK_ROCKET_BLAST;
        fire.sounds.get(0).pitch = 2F;

        scopeType = ScopeType.ASSAULT;
        scopeSlownessAmplifier = 5;

        maxUncertainty = 0.1;
        hitscan.distance = 32;
        damage = 6;
        sneakUncertaintyMultiplier = 0.5;

        cooldownTicks = 3;
        reloadTicks = 30;
        ammo = maxAmmo = 24;
    }
}
