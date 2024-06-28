package io.github.itzispyder.guns.firearms.nbt;

public enum RaycastMode {

    HITSCAN,
    BALLISTICS;

    public boolean isInstant() {
        return this == HITSCAN;
    }
}
