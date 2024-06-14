package io.github.itzispyder.guns.firearms.presets;

import io.github.itzispyder.guns.firearms.GunNBT;
import org.bukkit.Sound;

public class ShotgunNBT extends GunNBT {

    public ShotgunNBT() {
        super();

        shootSound = Sound.ENTITY_IRON_GOLEM_REPAIR;
        shootSoundPitch = 2F;

        maxUncertainty = 0.3;
        distance = 16;
        damage = 1;
        sneakUncertaintyMultiplier = 0.5;

        roundsPerShot = 15;
        cooldownTicks = 20;
        reloadTicks = 60;
        ammo = maxAmmo = 8;
    }
}
