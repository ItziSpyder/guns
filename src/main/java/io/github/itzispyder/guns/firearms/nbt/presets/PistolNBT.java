package io.github.itzispyder.guns.firearms.nbt.presets;

import io.github.itzispyder.guns.firearms.nbt.GunNBT;
import org.bukkit.Sound;

public class PistolNBT extends GunNBT {

    public PistolNBT() {
        super();

        fire.sounds.get(0).sound = Sound.ENTITY_FIREWORK_ROCKET_BLAST;
        fire.sounds.get(0).pitch = 2F;

        maxUncertainty = 0.15;
        hitscan.distance = 16;
        damage = 4;
        sneakUncertaintyMultiplier = 0.5;

        cooldownTicks = 5;
        reloadTicks = 10;
        ammo = maxAmmo = 12;
    }
}
