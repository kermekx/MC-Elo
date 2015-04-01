package com.kermekx.mcelo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.kermekx.mcelo.elo.Elo;
import com.kermekx.mcelo.listener.MonitorListener;
import com.kermekx.mcelo.rank.Rank;

public class MCElo extends JavaPlugin {
	
	private static MCElo plugin;

	private Map<UUID, Elo> elos;
	
	@Override
	public void onEnable() {
		
		plugin = this;

		saveDefaultConfig();

		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new MonitorListener(), this);
		
		Rank.loadRanks();
		
		elos = new HashMap<UUID, Elo>();
		for(Player player : this.getServer().getOnlinePlayers())
			elos.put(player.getUniqueId(), new Elo(player.getUniqueId()));
		
		getLogger().info("Enable !");
		
	}
	
	@Override
	public void onDisable() {
		
		HandlerList.unregisterAll(this);
		
		elos = null;
		
		Rank.unloadRanks();
		plugin = null;
		
		getLogger().info("Disable !");
		
	}
	
	public void addPlayer(UUID player) {
		elos.put(player, new Elo(player));
	}
	
	public void removePlayer(UUID player) {
		elos.remove(player);
	}
	
	public Elo getElo(UUID player) {
		return elos.get(player);
	}
	
	public Player getPlayer(UUID player) {
		return getServer().getPlayer(player);
	}
	
	public static MCElo getPlugin() {
		return plugin;
	}

}
