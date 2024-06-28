package io.github.itzispyder.guns.firearms.nbt.presets;

import io.github.itzispyder.guns.firearms.nbt.GunNBT;
import io.github.itzispyder.guns.firearms.scopes.ScopeType;
import org.bukkit.Sound;

public class SmgNBT extends GunNBT {

    public SmgNBT() {
        super();

        fire.sounds.get(0).sound = Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR;
        fire.sounds.get(0).pitch = 0.1F;

        scopeType = ScopeType.ASSAULT;
        scopeSlownessAmplifier = 5;

        maxUncertainty = 0.1;
        hitscan.distance = 32;
        damage = 1;
        sneakUncertaintyMultiplier = 0.5;

        cooldownTicks = 0;
        reloadTicks = 20;
        repetitionIterations = 3;
        ammo = maxAmmo = 32;
    }
}
