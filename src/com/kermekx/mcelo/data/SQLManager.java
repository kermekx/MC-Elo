package com.kermekx.mcelo.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLManager {

	private static SQLManager INSTANCE;

	public static void makeInstance(String url, String login, String pass)
			throws ClassNotFoundException, SQLException {
		INSTANCE = new SQLManager(url, login, pass);
	}

	private final String url, login, pass;

	private Connection cn;
	private static Statement st;
	private static ResultSet rs;

	public SQLManager(String url, String login, String pass)
			throws ClassNotFoundException, SQLException {
		this.url = url;
		this.login = login;
		this.pass = pass;

		Class.forName("com.mysql.jdbc.Driver");
		cn = DriverManager.getConnection(url, login, pass);
		st = cn.createStatement();
	}

	public static boolean firstConnection(String uuid) throws SQLException {
		String req = "SELECT * FROM a222015050415245206332450.Players WHERE UUID = '"
				+ uuid + "';";

		rs = st.executeQuery(req);

		return rs.next();
	}

	public static int getInt(String uuid, String field) throws SQLException {
		String req = "SELECT * FROM a222015050415245206332450.Players WHERE UUID = '"
				+ uuid + "';";

		rs = st.executeQuery(req);

		if (rs.next()) {
			return rs.getInt(field);
		}

		return 0;

	}

	public static void create(String uuid, String name) throws SQLException {

		String req = "INSERT INTO a222015050415245206332450.Players (UUID, Username) VALUES ('"
				+ uuid + "', '" + name + "');";

		st.executeUpdate(req);

	}

	public static void save(String uuid, String name, int elo, int kills,
			int death) throws SQLException {

		String req = "UPDATE a222015050415245206332450.Players SET Elo = '"
				+ elo + "', Kills = '" + kills + "', Death = '" + death
				+ "', Username = '" + name + "' WHERE UUID = '" + uuid + "';";

		st.executeUpdate(req);

	}

}
