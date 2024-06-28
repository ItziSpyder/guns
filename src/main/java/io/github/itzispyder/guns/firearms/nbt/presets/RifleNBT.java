package io.github.itzispyder.guns.firearms.nbt.presets;

import io.github.itzispyder.guns.firearms.nbt.GunNBT;
import io.github.itzispyder.guns.firearms.scopes.ScopeType;
import org.bukkit.Sound;

public class RifleNBT extends GunNBT {

    public RifleNBT() {
        super();

        fire.sounds.get(0).sound = Sound.ENTITY_IRON_GOLEM_REPAIR;
        fire.sounds.get(0).pitch = 0.7F;

        scopeType = ScopeType.SNIPER;
        scopeSlownessAmplifier = 255;

        maxUncertainty = 0.15;
        hitscan.distance = 128;
        damage = 8;
        sneakUncertaintyMultiplier = 0.04628;

        cooldownTicks = 15;
        reloadTicks = 40;
        ammo = maxAmmo = 6;
        repetitionIterations = 2;
    }
}
