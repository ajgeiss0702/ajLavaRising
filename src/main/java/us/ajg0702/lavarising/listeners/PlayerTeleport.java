package us.ajg0702.lavarising.listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import us.ajg0702.lavarising.LavaPlugin;

public class PlayerTeleport implements Listener {
    private final LavaPlugin plugin;

    public PlayerTeleport(LavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {

        assert e.getTo() != null;

        World from = e.getFrom().getWorld();
        World to = e.getTo().getWorld();

        assert from != null;
        assert to != null;

        if(from.equals(plugin.getLavaManager().getWorld()) && !to.equals(plugin.getLavaManager().getWorld())) {
            plugin.getLavaManager().removePlayer(e.getPlayer());
        } else
        if(to.equals(plugin.getLavaManager().getWorld()) && !from.equals(plugin.getLavaManager().getWorld())) {
            if(plugin.getLavaManager().isSetup()) {
                plugin.getLavaManager().addPlayer(e.getPlayer());
            }
        }
    }
}
