package us.ajg0702.lavarising;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import us.ajg0702.lavarising.utils.workload.PlaceBlockWorkload;

import static us.ajg0702.lavarising.LavaPlugin.legacyColor;

public class LavaManager {
    private final int LAVA_RADIUS = 100;
    private final LavaPlugin plugin;
    private final BossBar bar = Bukkit.createBossBar(legacyColor("&dLava at y = -50"), BarColor.PURPLE, BarStyle.SOLID);
    private final BossBar debugBar = Bukkit.createBossBar(legacyColor("&70 remaining"), BarColor.WHITE, BarStyle.SEGMENTED_20);

    private LavaState state = LavaState.PAUSED;
    private boolean setup = false;

    private World world = null;

    public LavaManager(LavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void start(Location center) {
        world = center.getWorld();
        if(world == null) throw new IllegalArgumentException("Must be in a world!");

        state = LavaState.RISING;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(!player.getWorld().equals(world)) continue;
            addPlayer(player);
        }

        if(setup) {
            world.getWorldBorder().setSize(5.5, secsUntilMaxHeight(world));
            return;
        }
        setup = true;

        world.getWorldBorder().setCenter(center);
        world.getWorldBorder().setSize(Math.floor(LAVA_RADIUS * 2d));
        world.getWorldBorder().setSize(5.5, secsUntilMaxHeight(world));
    }

    public void pause(World world) {
        world.getWorldBorder().setSize(world.getWorldBorder().getSize());
        state = LavaState.PAUSED;
    }

    public int secsUntilMaxHeight(World world) {
        return (int) Math.ceil((world.getMaxHeight() - currentLevel) * (ticksPerRise/20d));
    }

    private int currentLevel = -50;


    private final int ticksPerRise = 80;
    private int ticksUntilRise = ticksPerRise;
    public void tick() {
        if(state == LavaState.PAUSED) return;
        if(world == null) throw new IllegalStateException("Cannot tick while world is null!");
        if(currentLevel >= world.getMaxHeight()) return;

        int remaining = plugin.getWorkloadRunnable().getNumberOfTasksRemaining();

        ticksUntilRise--;
        if(ticksUntilRise <= 0) {
            currentLevel++;
            ticksUntilRise = ticksPerRise;
            bar.setTitle(legacyColor("&dLava at y = "+currentLevel));
            bar.setProgress(1);

            Location center = world.getWorldBorder().getCenter();
            center.setY(currentLevel);

            if(remaining > 0) {
                plugin.getLogger().warning("Still have " + remaining + " blocks from previous level!");
            }

            long start = System.currentTimeMillis();
            for (int x = center.getBlockX()-LAVA_RADIUS; x < center.getBlockX()+LAVA_RADIUS; x++) {
                for (int z = center.getBlockZ()-LAVA_RADIUS; z < center.getBlockZ()+LAVA_RADIUS; z++) {
                    Location location = center.clone();
                    location.setX(x);
                    location.setZ(z);

                    plugin.getWorkloadRunnable().addWorkload(
                            new PlaceBlockWorkload(location, Material.LAVA)
                    );
                }
            }
            long time = System.currentTimeMillis() - start;
            if(time > 10) {
                plugin.getLogger().warning("Lava scheduling took " + time + "ms");
            }
        } else {
            bar.setProgress(((double) ticksUntilRise) / ((double) ticksPerRise));
        }

        debugBar.setProgress(remaining/40000d);
        debugBar.setTitle(legacyColor("&7" + remaining + " remaining"));
    }

    public void addPlayer(Player player) {
        bar.addPlayer(player);

        if(player.getName().equals("ajgeiss0702")) {
            debugBar.addPlayer(player);
        }
    }

    public void removePlayer(Player player) {
        bar.removePlayer(player);
    }

    public LavaState getState() {
        return state;
    }

    public boolean isSetup() {
        return setup;
    }

    public World getWorld() {
        return world;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}
