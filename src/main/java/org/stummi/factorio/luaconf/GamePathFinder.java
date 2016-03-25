package org.stummi.factorio.luaconf;

import java.io.File;

/**
 * Class to help finding the installation path of the Factorio game
 */
public class GamePathFinder {

	public static File findGameDir() {
		String osName = System.getProperty("os.name");

		if (!osName.equals("Linux")) {
			// no support for other OSes so far. TODO
			return null;
		}

		String home = System.getenv("HOME");
		File[] candidates = {
				// seems to differ between two linux steam machines for me. No
				// Idea why
				new File(home, ".steam/steam/SteamApps/common/Factorio/"),
				new File(home, ".steam/steam/steamapps/common/Factorio/") };

		for (File f : candidates) {
			if (isSuitable(f)) {
				return f;
			}
		}

		return null;
	}

	/**
	 * Checks if a File points to a directory which is suitable as Game
	 * Directory. It just checks the existence of some files under these
	 * directories
	 */
	public static boolean isSuitable(File f) {
		if (!f.isDirectory()) {
			return false;
		}

		// if these files exists, just lets assume we are in Factorio Game directory
		String[] filesToCheck = { 
				"data/base/data.lua",
				"data/core/lualib/util.lua", "data/core/lualib/dataloader.lua" };
		
		for(String check:filesToCheck) {
			if (!(new File(f, check).exists())) {
				return false;
			}
		}
		return true;

	}
	
	public static void main(String[] args) {
		System.out.println(findGameDir());
	}
}
