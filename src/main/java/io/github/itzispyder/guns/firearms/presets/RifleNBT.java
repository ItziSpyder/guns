package io.github.itzispyder.guns.firearms.presets;

import io.github.itzispyder.guns.firearms.GunNBT;
import io.github.itzispyder.guns.firearms.scopes.ScopeType;
import org.bukkit.Sound;

public class RifleNBT extends GunNBT {

    public RifleNBT() {
        super();

        shootSound = Sound.ENTITY_IRON_GOLEM_REPAIR;
        shootSoundPitch = 0.7F;

        scopeType = ScopeType.SNIPER;
        scopeSlownessAmplifier = 255;

        maxUncertainty = 0.15;
        distance = 128;
        damage = 8;
        sneakUncertaintyMultiplier = 0.04628;

        cooldownTicks = 15;
        reloadTicks = 40;
        ammo = maxAmmo = 6;
        repetitionIterations = 2;
    }
}
