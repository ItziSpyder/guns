package io.github.itzispyder.guns.firearms.nbt;

import org.bukkit.Color;

public class BallisticsMode {

    public double bulletDropPerTick, blocksPerTick;
    public boolean glowing;
    public Color glowColor;

    public BallisticsMode(double bulletDropPerTick, double blocksPerTick, boolean glowing, Color glowColor) {
        this.bulletDropPerTick = bulletDropPerTick;
        this.blocksPerTick = blocksPerTick;
        this.glowing = glowing;
        this.glowColor = glowColor;
    }
}