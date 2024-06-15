package io.github.itzispyder.guns.firearms.presets;

import io.github.itzispyder.guns.firearms.GunNBT;
import io.github.itzispyder.guns.firearms.scopes.ScopeType;
import org.bukkit.Sound;

public class SmgNBT extends GunNBT {

    public SmgNBT() {
        super();

        shootSound = Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR;
        shootSoundPitch = 0.1F;

        scopeType = ScopeType.ASSAULT;
        scopeSlownessAmplifier = 5;

        maxUncertainty = 0.1;
        distance = 32;
        damage = 1;
        sneakUncertaintyMultiplier = 0.5;

        cooldownTicks = 0;
        reloadTicks = 20;
        repetitionIterations = 3;
        ammo = maxAmmo = 32;
    }
}
