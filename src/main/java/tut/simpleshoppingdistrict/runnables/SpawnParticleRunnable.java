package tut.simpleshoppingdistrict.runnables;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SpawnParticleRunnable extends BukkitRunnable {
    private static ConcurrentHashMap<Integer, HashSet<String>> playersViewingParticleRunnable;
    private static Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromBGR(0, 127, 255), .25f);
    private Player player;
    private Plugin plugin;
    private Location[] locations;
    private int timerCounter = 0;

    static {
        playersViewingParticleRunnable = new ConcurrentHashMap<>();
    }

    public SpawnParticleRunnable(final Player player, final Plugin plugin, final Location[] locations) {
        this.plugin = plugin;
        this.player = player;
        this.locations = locations;
    }

    public void start() {
        final Integer locationHash = Arrays.hashCode(this.locations);
        HashSet<String> currentPlayersRunning;

        if (playersViewingParticleRunnable.containsKey(locationHash)) {
            currentPlayersRunning = playersViewingParticleRunnable.get(locationHash);
        }
        else {
            currentPlayersRunning = new HashSet<>();
        }

        if (!currentPlayersRunning.contains(this.player.getUniqueId().toString())) {
            currentPlayersRunning.add(this.player.getUniqueId().toString());
            playersViewingParticleRunnable.put(locationHash, currentPlayersRunning);
            this.runTaskTimer(plugin, 0, 1);
        }
    }

    public void stop() {
        Integer locationHash = Arrays.hashCode(this.locations);
        final HashSet<String> currentPlayersRunning = playersViewingParticleRunnable.get(locationHash);
        currentPlayersRunning.remove(this.player.getUniqueId().toString());

        if (currentPlayersRunning.size() == 0) {
            playersViewingParticleRunnable.remove(locationHash);
        }
        else  {
            playersViewingParticleRunnable.put(locationHash, currentPlayersRunning);
        }

        this.cancel();
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        for (Location l : locations) {
            player.spawnParticle(Particle.REDSTONE, l, 1, dustOptions);
        }

        timerCounter++;

        if (timerCounter >= 200) {
            this.stop();
        }
    }
}
