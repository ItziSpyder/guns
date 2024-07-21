package io.github.itzispyder.guns.events;

import io.github.itzispyder.pdk.events.CustomListener;
import io.github.itzispyder.pdk.utils.ServerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ServerEventListener implements CustomListener {

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onDeath(PlayerDeathEvent e) {
        if (!getConfig().getBoolean("chat-to-action-bar.kill-messages"))
            return;
        if (e.isCancelled())
            return;

        String msg = e.getDeathMessage();
        e.setDeathMessage(null);

        if (msg != null)
            ServerUtils.sendActionBar(msg);
    }

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onLeave(PlayerQuitEvent e) {
        if (!getConfig().getBoolean("game.quit-messages")) {
            e.setQuitMessage(null);
            return;
        }
        if (!getConfig().getBoolean("chat-to-action-bar.quit-messages"))
            return;

        String msg = e.getQuitMessage();
        e.setQuitMessage(null);

        if (msg != null)
            ServerUtils.sendActionBar(msg);
    }

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onJoin(PlayerJoinEvent e) {
        if (!getConfig().getBoolean("chat-to-action-bar.join-messages"))
            return;

        String msg = e.getJoinMessage();
        e.setJoinMessage(null);

        if (msg != null)
            ServerUtils.sendActionBar(msg);
    }
}
