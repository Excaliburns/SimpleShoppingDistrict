package tut.simpleshoppingdistrict.commands;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.RayTraceResult;
import tut.simpleshoppingdistrict.SimpleShoppingDistrict;
import tut.simpleshoppingdistrict.data.SSDRegion;
import tut.simpleshoppingdistrict.runnables.SpawnParticleRunnable;
import tut.simpleshoppingdistrict.utils.ParticleUtils;
import tut.simpleshoppingdistrict.utils.SSDCache;
import tut.simpleshoppingdistrict.utils.SSDUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class SSDDisplayRegionsCommand {
    public static boolean SSDRegionCommand(Player sender) {
        final String senderUUID = sender.getUniqueId().toString();

            if (!SSDCache.playerRegionCache.containsKey(senderUUID)) {
                sender.sendMessage(ChatColor.AQUA + "Player region cache is empty.");
                return true;
            }

            sender.sendMessage(ChatColor.AQUA + "Showing you your regions within this chunk and surrounding chunks!");

            HashSet<Chunk> chunks = new HashSet<>();
            chunks.add(sender.getLocation().getChunk());
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    chunks.add(sender.getWorld().getChunkAt(sender.getLocation().getChunk().getX() + x, sender.getLocation().getChunk().getZ() + z));
                }
            }

            List<Long> chunkHashes = chunks.stream().map(SSDUtils::getChunkHash).collect(Collectors.toList());
            List<SSDRegion> regions = new ArrayList<>(SSDCache.playerRegionCache.get(senderUUID));
            regions = regions.stream()
                             .filter(region -> chunkHashes.stream().anyMatch(new HashSet<>(region.getChunkContainerHash())::contains))
                             .collect(Collectors.toList());

            for (SSDRegion r : regions) {
                final Location[] regionCube = ParticleUtils.getCuboidPointsFromRegionBounds(r.getBound1(), r.getBound2(), sender.getWorld());

                final SpawnParticleRunnable runnable = new SpawnParticleRunnable(sender, SimpleShoppingDistrict.getInstance(), regionCube);
                runnable.start();
            }

            return true;
    }
}
