package com.kermekx.mcelo.elo;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.kermekx.mcelo.MCElo;
import com.kermekx.mcelo.data.FileManager;
import com.kermekx.mcelo.rank.Rank;

public class Elo {

	private final static String DIR = "players";
	private final static String ELO = "elo";
	private UUID player;
	private int elo;
	private double exp;

	public Elo(UUID player) {
		this.player = player;
		loadData();
		setRank();
		getPlayer().sendMessage(
				ChatColor.GOLD + "Your rank is currently " + getRank());
	}

	private void loadData() {
		try {
			elo = FileManager.getInt(DIR, player.toString(), ELO);
			getPlayer().sendMessage(
					ChatColor.GOLD + "Welcome back " + getPlayer().getName()
							+ " !");
		} catch (Exception ex) {
			getPlayer().sendMessage(
					ChatColor.GOLD + "Welcome to the server "
							+ getPlayer().getName() + " !");
			try {
				FileManager.set(DIR, player.toString(), ELO, 2000);
			} catch (IOException e) {
				getPlayer().sendMessage(
						ChatColor.RED + "Cannot create your data !");
			}
			elo = 2000;
		}
	}

	public void kill(Elo target) {
		int boost = ((elo < 1000) ? 80 : ((elo < 2000) ? 50
				: ((elo < 2400) ? 30 : 20)));
		double winRate = getWinRate(target);
		target.death(1.0 - winRate);
		winElo((int) (boost * (1.0 - winRate)));
	}

	public void expElo(int exp) {
		int boost = ((elo < 1000) ? 8 : ((elo < 2000) ? 5 : ((elo < 2400) ? 3
				: ((elo < 3000) ? 2 : ((elo < 3500) ? 1 : 0)))));
		if (exp * boost / 16 > 0)
			winElo(exp * boost / 16);
		else {
			this.exp += (double) (exp * boost) / 16d;
			if ((int) this.exp > 0)
				winElo((int) this.exp);
			this.exp -= (int) this.exp;
		}
	}

	public void death(double winRate) {
		int boost = ((elo < 1000) ? 80 : ((elo < 2000) ? 50
				: ((elo < 2400) ? 30 : 20)));
		loseElo((int) (boost * winRate));
	}

	public void death() {
		loseElo((int) (elo * 0.02));
	}

	private void winElo(int amount) {
		elo += amount;
		setRank();
		getPlayer().sendMessage(
				ChatColor.GREEN + "You winned " + amount + " elo (" + elo + " "
						+ getRank() + ChatColor.GREEN + ")");
		save();
	}

	private void loseElo(int amount) {
		elo -= amount;
		elo = (elo < 0) ? 0 : elo;
		setRank();
		getPlayer().sendMessage(
				ChatColor.RED + "You losed " + amount + " elo (" + elo + " "
						+ getRank() + ChatColor.RED + ")");
		save();
	}

	private double getWinRate(Elo target) {
		return 1.0 / (1.0 + Math.pow(10.0, (target.elo - elo) / 400.0));
	}

	private void save() {
		try {
			FileManager.set(DIR, player.toString(), ELO, elo);
		} catch (IOException e) {
			getPlayer().sendMessage(ChatColor.RED + "Cannot save your data !");
		}
	}

	private void setRank() {
		getPlayer().setDisplayName(
				"[" + getRank() + ChatColor.WHITE + "] "
						+ getPlayer().getName());
	}

	public Rank getRank() {
		return Rank.getRank(elo);
	}

	public Player getPlayer() {
		return MCElo.getPlugin().getPlayer(player);
	}
}
