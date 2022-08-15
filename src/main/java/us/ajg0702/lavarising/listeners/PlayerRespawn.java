package us.ajg0702.lavarising.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import us.ajg0702.lavarising.LavaPlugin;

import static us.ajg0702.lavarising.LavaPlugin.legacyColor;

public class PlayerRespawn implements Listener {
    private final LavaPlugin plugin;

    public PlayerRespawn(LavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        World world = e.getRespawnLocation().getWorld();
        assert world != null;
        if(!world.equals(plugin.getLavaManager().getWorld())) return;
        if(e.getRespawnLocation().getBlockY()-1 <= plugin.getLavaManager().getCurrentLevel()) {
            Bukkit.getScheduler().runTask(plugin, () -> e.getPlayer().setGameMode(GameMode.SPECTATOR));
            e.getPlayer().sendMessage(legacyColor("&cYou respawned below the lava. You are now a spectator."));
        }
    }
}
