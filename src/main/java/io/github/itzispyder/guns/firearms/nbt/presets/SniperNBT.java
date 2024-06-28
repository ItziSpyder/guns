package io.github.itzispyder.guns.firearms.nbt.presets;

import io.github.itzispyder.guns.firearms.nbt.GunNBT;
import io.github.itzispyder.guns.firearms.scopes.ScopeType;
import org.bukkit.Sound;

public class SniperNBT extends GunNBT {

    public SniperNBT() {
        super();

        fire.sounds.get(0).sound = Sound.ENTITY_IRON_GOLEM_REPAIR;
        fire.sounds.get(0).pitch = 0.7F;

        scopeType = ScopeType.SNIPER;
        scopeSlownessAmplifier = 255;

        maxUncertainty = 0.15;
        hitscan.distance = 128;
        damage = 18;
        sneakUncertaintyMultiplier = 0.04628;

        cooldownTicks = 20;
        reloadTicks = 60;
        ammo = maxAmmo = 10;
    }
}
