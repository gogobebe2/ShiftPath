package com.gmail.gogobebe2.shiftpath;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class PathInProgress {
    private static Set<PathInProgress> pathsInProgress = new HashSet<>();

    private List<Location> path = new ArrayList<>();
    private Location selection1 = null;
    private Location selection2 = null;
    private Player owner;
    private ShiftPath plugin;

    private PathInProgress(Player owner, ShiftPath plugin) {
        this.owner = owner;
        this.plugin = plugin;
        pathsInProgress.add(this);
    }

    public Player getOwner() {
        return this.owner;
    }

    public void save() {
        Set<String> paths = plugin.getConfig().getConfigurationSection("Paths").getKeys(false);
        int id = 0;
        if (!paths.isEmpty()) {
            Set<Integer> ids = new HashSet<>(paths.size());
            for (String pID : paths) ids.add(Integer.parseInt(pID));
            id = Collections.max(ids) + 1;
        }
        plugin.getConfig().set("Paths." + id + ".sel1", selection1);
        plugin.getConfig().set("Paths." + id + ".sel2", selection2);
        plugin.saveConfig();
        pathsInProgress.remove(this);
    }

    public List<Location> getPath() {
        return this.path;
    }

    public Location getSelection1() {
        return selection1;
    }

    public Location getSelection2() {
        return selection2;
    }

    public void createSelection(Location selection) {
        if (selection1 == null) {
            selection1 = selection;
        } else if (selection2 == null) {
            selection2 = selection;
        } else {
            selection2 = null;
            selection1 = selection;
        }
    }

    public static PathInProgress getPathInProgress(Player owner, ShiftPath plugin) {
        for (PathInProgress path : pathsInProgress) {
            if (path.getOwner().getUniqueId().equals(owner.getUniqueId())) {
                return path;
            }
        }
        PathInProgress path = new PathInProgress(owner, plugin);
        pathsInProgress.add(path);
        return path;
    }

}
