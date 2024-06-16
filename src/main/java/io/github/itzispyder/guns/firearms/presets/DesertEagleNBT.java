package io.github.itzispyder.guns.firearms.presets;

import io.github.itzispyder.guns.firearms.GunNBT;
import org.bukkit.Sound;

public class DesertEagleNBT extends GunNBT {

    public DesertEagleNBT() {
        super();

        shootSound = Sound.ENTITY_FIREWORK_ROCKET_BLAST;
        shootSoundPitch = 0.1F;

        maxUncertainty = 0.25;
        distance = 16;
        damage = 6;
        sneakUncertaintyMultiplier = 0.5;

        cooldownTicks = 10;
        reloadTicks = 10;
        ammo = maxAmmo = 15;
        repetitionIterations = 3;
        repetitionPeriod = 2;
    }
}
