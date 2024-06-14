package io.github.itzispyder.guns.firearms.presets;

import io.github.itzispyder.guns.firearms.GunNBT;
import org.bukkit.Sound;

public class SmgNBT extends GunNBT {

    public SmgNBT() {
        super();

        shootSound = Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR;
        shootSoundPitch = 0.1F;

        maxUncertainty = 0.1;
        distance = 32;
        damage = 1;
        sneakUncertaintyMultiplier = 0.5;

        cooldownTicks = 0;
        reloadTicks = 20;
        ammo = maxAmmo = 32;
    }
}
