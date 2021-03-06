package com.gmail.gogobebe2.shiftpath.path;

import com.gmail.gogobebe2.shiftpath.LocationData;
import com.gmail.gogobebe2.shiftpath.Platform;
import com.gmail.gogobebe2.shiftpath.ShiftPath;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

public class ActivePath extends Path {
    private static Set<ActivePath> activePaths = new HashSet<>();
    private Platform platform;
    private int currentGoalIndex = 0;

    public ActivePath(int pathID, ShiftPath plugin) {
        super(plugin);
        LocationData selection1 = new LocationData("Paths." + pathID + ".selection1", getPlugin());
        LocationData selection2 = new LocationData("Paths." + pathID + ".selection2", getPlugin());
        platform = new Platform(selection1.getLocation(), selection2.getLocation());
        for (String pointKey : getPlugin().getConfig().getConfigurationSection("Paths." + pathID + ".path").getKeys(false)) {
            LocationData point = new LocationData("Paths." + pathID + ".path." + pointKey, getPlugin());
            getPath().add(point.getLocation());
        }
        activePaths.add(this);
    }

    public void approachNextPoint() {
        if (getPath().size() != 1) {
            Location center = platform.getCenter().getBlock().getLocation();

            if (center.distance(getPath().get(currentGoalIndex).getBlock().getLocation()) == 0) {
                // The goal as been reached.
                if (currentGoalIndex == getPath().size() - 1) {
                    // If I want to to just reverse:
//                    Collections.reverse(getPath());
//                    currentGoalIndex = 1;
                    currentGoalIndex = 0;
                } else {
                    currentGoalIndex++;
                }
            }
            Location currentGoal = getPath().get(currentGoalIndex).getBlock().getLocation();
            int xFactor = 0;
            int yFactor = 0;
            int zFactor = 0;

            if (center.getBlockX() < currentGoal.getBlockX()) {
                xFactor = 1;
            } else if (center.getBlockX() > currentGoal.getBlockX()) {
                xFactor = -1;
            }

            if (center.getBlockY() < currentGoal.getBlockY()) {
                yFactor = 1;
            } else if (center.getBlockY() > currentGoal.getBlockY()) {
                yFactor = -1;
            }

            if (center.getBlockZ() < currentGoal.getBlockZ()) {
                zFactor = 1;
            } else if (center.getBlockZ() > currentGoal.getBlockZ()) {
                zFactor = -1;
            }

            platform.move(center.clone().add(xFactor, yFactor, zFactor));
        }
    }

    public static Set<ActivePath> getActivePaths() {
        return activePaths;
    }
}