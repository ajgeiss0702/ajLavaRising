package us.ajg0702.lavarising.listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import us.ajg0702.lavarising.LavaPlugin;
import us.ajg0702.lavarising.LavaState;

public class PlayerJoin implements Listener {
    private final LavaPlugin plugin;

    public PlayerJoin(LavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        World playerWorld = e.getPlayer().getLocation().getWorld();
        assert playerWorld != null;
        if(plugin.getLavaManager().getState() == LavaState.RISING && playerWorld.equals(plugin.getLavaManager().getWorld())) {
            plugin.getLavaManager().addPlayer(e.getPlayer());
        }
    }
}
