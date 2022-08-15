package us.ajg0702.lavarising;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.ajg0702.commands.platforms.bukkit.BukkitCommand;
import us.ajg0702.commands.platforms.bukkit.BukkitSender;
import us.ajg0702.lavarising.commands.LavaCommand;
import us.ajg0702.lavarising.listeners.PlayerJoin;
import us.ajg0702.lavarising.listeners.PlayerRespawn;
import us.ajg0702.lavarising.listeners.PlayerTeleport;
import us.ajg0702.lavarising.utils.workload.WorkloadRunnable;
import us.ajg0702.utils.common.Messages;

public class LavaPlugin extends JavaPlugin {

    private WorkloadRunnable workloadRunnable;

    private LavaManager lavaManager;

    @Override
    public void onEnable() {

        workloadRunnable = new WorkloadRunnable();
        Bukkit.getScheduler().runTaskTimer(this, workloadRunnable, 20, 1);

        lavaManager = new LavaManager(this);
        Bukkit.getScheduler().runTaskTimer(this, lavaManager::tick, 10, 1);

        registerListeners(new PlayerJoin(this), new PlayerTeleport(this), new PlayerRespawn(this));

        BukkitCommand bukkitMainCommand = new BukkitCommand(new LavaCommand(this));
        bukkitMainCommand.register(this);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if(!player.getWorld().equals(getLavaManager().getWorld())) continue;
                if(player.getLocation().getY() < getLavaManager().getCurrentLevel()) {
                    player.setFireTicks(100);
                }
            }
        }, 200, 80);

        BukkitSender.setAdventure(this);

        getLogger().info("ajLavaRising v" + getDescription().getVersion() + " enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("ajLavaRising v" + getDescription().getVersion() + " disabled");
    }

    public void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

    private static MiniMessage miniMessage;
    public static MiniMessage getMiniMessage() {
        if(miniMessage == null) {
            miniMessage = MiniMessage.miniMessage();
        }
        return miniMessage;
    }

    public WorkloadRunnable getWorkloadRunnable() {
        return workloadRunnable;
    }

    public LavaManager getLavaManager() {
        return lavaManager;
    }

    public static Component message(String miniMessage) {
        return getMiniMessage().deserialize(Messages.color(miniMessage));
    }

    public static String legacyColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
