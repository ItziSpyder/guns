package io.github.itzispyder.guns.firearms.nbt.presets;

import io.github.itzispyder.guns.firearms.nbt.GunNBT;
import org.bukkit.Sound;

public class DesertEagleNBT extends GunNBT {

    public DesertEagleNBT() {
        super();

        fire.sounds.get(0).sound = Sound.ENTITY_FIREWORK_ROCKET_BLAST;
        fire.sounds.get(0).pitch = 0.1F;

        maxUncertainty = 0.25;
        hitscan.distance = 16;
        damage = 6;
        sneakUncertaintyMultiplier = 0.5;

        cooldownTicks = 10;
        reloadTicks = 10;
        ammo = maxAmmo = 15;
        repetitionIterations = 3;
        repetitionPeriod = 2;
    }
}
