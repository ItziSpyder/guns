package io.github.itzispyder.guns.firearms.presets;

import io.github.itzispyder.guns.firearms.GunNBT;
import org.bukkit.Sound;

public class PistolNBT extends GunNBT {

    public PistolNBT() {
        super();

        shootSound = Sound.ENTITY_FIREWORK_ROCKET_BLAST;
        shootSoundPitch = 2F;

        maxUncertainty = 0.15;
        distance = 16;
        damage = 4;
        sneakUncertaintyMultiplier = 0.5;

        cooldownTicks = 5;
        reloadTicks = 10;
        ammo = maxAmmo = 12;
    }
}
