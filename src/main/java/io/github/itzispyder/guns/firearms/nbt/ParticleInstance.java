package io.github.itzispyder.guns.firearms.nbt;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class ParticleInstance {

    public Particle particle;
    public int count;
    public double deltaX, deltaY, deltaZ;
    public double extra;
    public Particle.DustOptions dust;

    public ParticleInstance(Particle particle, int count, double deltaX, double deltaY, double deltaZ, double extra, Particle.DustOptions dust) {
        this.particle = particle;
        this.count = count;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
        this.extra = extra;
        this.dust = dust;
    }

    public ParticleInstance(Particle particle, int count, double deltaX, double deltaY, double deltaZ, double extra) {
        this(particle, count, deltaX, deltaY, deltaZ, extra, null);
    }

    public void spawn(Location loc) {
        World world = loc.getWorld();
        if (world == null)
            return;

        if (dust == null)
            world.spawnParticle(particle, loc, count, deltaX, deltaY, deltaZ, extra);
        else
            world.spawnParticle(particle, loc, count, deltaX, deltaY, deltaZ, extra, dust);
    }
}
