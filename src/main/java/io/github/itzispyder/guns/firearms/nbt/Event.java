package io.github.itzispyder.guns.firearms.nbt;

import io.github.itzispyder.pdk.utils.misc.SoundPlayer;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Event {

    public List<SoundInstance> sounds = new ArrayList<>();
    public List<ParticleInstance> particles = new ArrayList<>();

    public void trigger(Location loc) {
        for (ParticleInstance particle : particles)
            particle.spawn(loc);

        SoundPlayer player = new SoundPlayer(loc, null, 0, 0);
        for (SoundInstance sound : sounds)
            sound.play(player);
    }

    public void trigger(SoundPlayer player) {
        for (ParticleInstance particle : particles)
            particle.spawn(player.getLocation());

        for (SoundInstance sound : sounds)
            sound.play(player);
    }

    public void trigger(SoundPlayer player, Location loc) {
        for (ParticleInstance particle : particles)
            particle.spawn(loc);

        for (SoundInstance sound : sounds)
            sound.play(player);
    }
}
