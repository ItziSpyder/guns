package io.github.itzispyder.guns.firearms.presets;

import io.github.itzispyder.guns.firearms.GunNBT;
import org.bukkit.Sound;

public class SniperNBT extends GunNBT {

    public SniperNBT() {
        super();

        shootSound = Sound.ENTITY_IRON_GOLEM_REPAIR;
        shootSoundPitch = 0.1F;

        maxUncertainty = 0.15;
        distance = 128;
        damage = 18;
        sneakUncertaintyMultiplier = 0.04628;

        cooldownTicks = 20;
        reloadTicks = 60;
        ammo = maxAmmo = 10;
    }
}
