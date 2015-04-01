package com.kermekx.mcelo.data;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileManager {

	private final static String BASE_DIR = "plugins/MC_Elo/";
	private final static String YML = ".yml";

	public static String getString(String dir, String file, String field) throws Exception {

		if (getFileConfiguration(dir, file) == null)
			throw new Exception("file not found");

		FileConfiguration config = getFileConfiguration(dir, file);
		return config.getString(field);

	}
	
	public static int getInt(String dir, String file, String field) throws Exception {

		if (getFileConfiguration(dir, file) == null)
			throw new Exception("file not found");

		FileConfiguration config = getFileConfiguration(dir, file);
		return config.getInt(field);

	}

	public static void set(String dir, String file, String field, String data)
			throws IOException {

		if (getFileConfiguration(dir, file) == null)
			createFile(dir, file);

		FileConfiguration config = getFileConfiguration(dir, file);
		config.set(field, data);
		config.save(new File(BASE_DIR + dir + "/" + file + YML));
	}
	
	public static void set(String dir, String file, String field, int data)
			throws IOException {

		if (getFileConfiguration(dir, file) == null)
			createFile(dir, file);

		FileConfiguration config = getFileConfiguration(dir, file);
		config.set(field, data);
		config.save(new File(BASE_DIR + dir + "/" + file + YML));
	}

	private static FileConfiguration getFileConfiguration(String dirName,
			String fileName) {

		File dir = new File(BASE_DIR + dirName);
		File file = new File(BASE_DIR + dirName + "/" + fileName + YML);

		if (!dir.exists())
			return null;

		if (!file.exists())
			return null;

		return YamlConfiguration.loadConfiguration(file);

	}

	private static void createFile(String dirName, String fileName)
			throws IOException {

		File dir = new File(BASE_DIR + dirName);
		File file = new File(BASE_DIR + dirName + "/" + fileName + YML);

		if (!dir.exists())
			dir.mkdirs();

		if (!file.exists())
			file.createNewFile();

	}

}
