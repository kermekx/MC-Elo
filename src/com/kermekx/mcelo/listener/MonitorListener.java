package com.kermekx.mcelo.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.kermekx.mcelo.MCElo;
import com.kermekx.mcelo.elo.Elo;

public class MonitorListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
		MCElo.getPlugin().addPlayer(playerJoinEvent.getPlayer().getUniqueId());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
		MCElo.getPlugin().removePlayer(
				playerQuitEvent.getPlayer().getUniqueId());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerKick(PlayerQuitEvent playerKickEvent) {
		MCElo.getPlugin().removePlayer(
				playerKickEvent.getPlayer().getUniqueId());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void OnPlayerDeath(PlayerDeathEvent playerDeathEvent) {
		Elo player = MCElo.getPlugin().getElo(
				playerDeathEvent.getEntity().getUniqueId());
		try {
			Elo killer = MCElo.getPlugin().getElo(
					playerDeathEvent.getEntity().getKiller().getUniqueId());
			killer.kill(player);
		} catch (Exception ex) {
			player.death();
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerExpChange(PlayerExpChangeEvent playerExpChangeEvent) {
		Elo player = MCElo.getPlugin().getElo(
				playerExpChangeEvent.getPlayer().getUniqueId());
		player.expElo(playerExpChangeEvent.getAmount());
	}

}
