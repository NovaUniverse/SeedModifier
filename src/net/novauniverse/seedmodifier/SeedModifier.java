package net.novauniverse.seedmodifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;

public class SeedModifier {
	private Properties minecraftProperties;
	private File propertiesFile;
	private File seedsFile;

	public static void main(String[] args) {
		new SeedModifier();
	}

	public SeedModifier() {
		propertiesFile = new File("./server.properties");
		seedsFile = new File("./seeds.json");
		minecraftProperties = new Properties();

		if (!seedsFile.exists()) {
			System.err.println("[SeedModifier] Cant find seeds.json");
			return;
		}

		String seed = "";

		try {
			JSONArray seedsJson = new JSONArray(FileUtils.readFileToString(seedsFile, StandardCharsets.UTF_8));

			if (seedsJson.length() == 0) {
				System.err.println("[SeedModifier] No seeds defined in seeds.json");
				return;
			}

			seed = seedsJson.getString(new Random().nextInt(seedsJson.length()));

			System.out.println("[SeedModifier] Using seed: " + seed);
		} catch (Exception e) {
			System.err.println("[SeedModifier] Failed to read seeds.json");
			e.printStackTrace();
			return;
		}

		try {
			FileInputStream istream = new FileInputStream(propertiesFile);
			System.out.println("[SeedModifier] Reading file");
			minecraftProperties.load(istream);
			istream.close();

			System.out.println("[SeedModifier] Changing seed");
			minecraftProperties.setProperty("seed", new Random().nextLong() + "");

			System.out.println("[SeedModifier] Writing changes");
			FileOutputStream ostream = new FileOutputStream(propertiesFile);
			minecraftProperties.store(ostream, "Seed modified by seed modifier");
			ostream.close();
		} catch (IOException e) {
			System.err.println("[SeedModifier] An error occurred");
			e.printStackTrace();
		}
	}
}