package com.kermekx.mcelo.elo;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.kermekx.mcelo.MCElo;
import com.kermekx.mcelo.data.SQLManager;
import com.kermekx.mcelo.rank.Rank;

public class Elo {

	private final static String ELO = "Elo";
	private final static String KILLS = "Kills";
	private final static String DEATH = "Death";
	private UUID player;
	private int elo;
	private int kills;
	private int death;
	private double exp;

	public Elo(UUID player) {
		this.player = player;
		loadData();
		setRank();
		getPlayer().sendMessage(
				ChatColor.GOLD + "Votre rang actuel est " + getRank());
	}

	private void loadData() {

		try {
			if (SQLManager.firstConnection(player.toString())) {
				elo = SQLManager.getInt(player.toString(), ELO);
				kills = SQLManager.getInt(player.toString(), KILLS);
				death = SQLManager.getInt(player.toString(), DEATH);
				getPlayer().sendMessage(
						ChatColor.GOLD + "Re-bonjour "
								+ getPlayer().getName() + " !");
			} else {
				getPlayer().sendMessage(
						ChatColor.GOLD + "Bienvenue sur MC Elo "
								+ getPlayer().getName() + " !");
				SQLManager.create(player.toString(), getPlayer().getName());
				elo = 2000;
				kills = 0;
				death = 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void kill(Elo target) {
		int boost = ((elo < 1000) ? 80 : ((elo < 2000) ? 50
				: ((elo < 2400) ? 30 : 20)));
		double winRate = getWinRate(target);
		target.death(1.0 - winRate);
		kills++;
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
		death++;
		loseElo((int) (boost * winRate));
	}

	public void death() {
		death++;
		loseElo((int) (elo * 0.02));
	}

	private void winElo(int amount) {
		elo += amount;
		setRank();
		getPlayer().sendMessage(
				ChatColor.GREEN + "Vous avez obtenu " + amount + " elo (" + elo + " "
						+ getRank() + ChatColor.GREEN + ")");
		save();
	}

	private void loseElo(int amount) {
		elo -= amount;
		elo = (elo < 0) ? 0 : elo;
		setRank();
		getPlayer().sendMessage(
				ChatColor.RED + "Vous avez perdu " + amount + " elo (" + elo + " "
						+ getRank() + ChatColor.RED + ")");
		save();
	}

	private double getWinRate(Elo target) {
		return 1.0 / (1.0 + Math.pow(10.0, (target.elo - elo) / 400.0));
	}

	private void save() {
		try {
			SQLManager.save(player.toString(), getPlayer().getName(), elo,
					kills, death);
		} catch (SQLException e) {
			e.printStackTrace();
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
