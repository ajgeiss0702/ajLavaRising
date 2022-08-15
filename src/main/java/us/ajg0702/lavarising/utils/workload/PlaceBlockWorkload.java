package us.ajg0702.lavarising.utils.workload;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;

import java.util.Arrays;
import java.util.List;

public class PlaceBlockWorkload implements Workload {

    private final Location location;
    private final Material to;

    public PlaceBlockWorkload(Location location, Material to) {
        this.location = location;
        this.to = to;
    }

    @Override
    public void compute() {
        Block block = location.getBlock();
        if(!block.getType().isAir() && !waterLogged(block)) return;
        block.setType(to, false);
    }

    private static List<Material> waterBlocks = Arrays.asList(
            Material.WATER,
            Material.KELP_PLANT,
            Material.KELP,
            Material.SEAGRASS,
            Material.TALL_SEAGRASS
    );

    private static boolean waterLogged(Block block) {
        if(waterBlocks.contains(block.getType())) return true;
        if(!(block.getBlockData() instanceof Waterlogged)) return false;
        Waterlogged waterlogged = (Waterlogged) block.getBlockData();
        return waterlogged.isWaterlogged();
    }
}
