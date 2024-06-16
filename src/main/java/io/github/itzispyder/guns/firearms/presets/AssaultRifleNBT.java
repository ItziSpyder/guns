package io.github.itzispyder.guns.firearms.presets;

import io.github.itzispyder.guns.firearms.GunNBT;
import io.github.itzispyder.guns.firearms.scopes.ScopeType;
import org.bukkit.Sound;

public class AssaultRifleNBT extends GunNBT {

    public AssaultRifleNBT() {
        super();

        shootSound = Sound.ENTITY_FIREWORK_ROCKET_BLAST;
        shootSoundPitch = 2F;

        scopeType = ScopeType.ASSAULT;
        scopeSlownessAmplifier = 5;

        maxUncertainty = 0.1;
        distance = 32;
        damage = 6;
        sneakUncertaintyMultiplier = 0.5;

        cooldownTicks = 3;
        reloadTicks = 30;
        ammo = maxAmmo = 24;
    }
}
