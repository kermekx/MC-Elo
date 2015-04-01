package com.kermekx.mcelo.rank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.kermekx.mcelo.MCElo;

public class Rank implements Comparable<Rank> {
	
	private static List<Rank> ranks;
	
	public static void loadRanks() {

		MCElo plugin = MCElo.getPlugin();

		ranks = new ArrayList<Rank>();
		for (String rank : MCElo.getPlugin().getConfig()
				.getConfigurationSection("ranks").getKeys(false))
			ranks.add(new Rank(plugin.getConfig().getString(
					"ranks." + rank + ".name"), plugin.getConfig().getInt(
					"ranks." + rank + ".elo")));

		Collections.sort(ranks);
	}

	public static void unloadRanks() {
		ranks = null;
	}

	public static Rank getRank(int elo) {
		for (Rank rank : ranks)
			if (elo >= rank.elo)
				return rank;

		return ranks.get(0);
	}

	private final String name;
	private final int elo;

	public Rank(String name, int elo) {
		this.name = name;
		this.elo = elo;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(Rank rank) {
		return (rank.elo - elo);
	}

	public int getElo() {
		return elo;
	}

}
