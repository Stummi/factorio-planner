package org.stummi.factorio;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import org.stummi.factorio.luaconf.LuaEntityLoader;

public class Main {
	public static void main(String[] args) throws IOException {
		LuaEntityLoader loader = new LuaEntityLoader(new File(System.getenv("HOME"), ".steam/steam/SteamApps/common/Factorio/"));
		
		Plan plan = new Plan();
		plan.addReceipe(loader.getRecipes().get("science-pack-1"), 4);
		plan.addReceipe(loader.getRecipes().get("science-pack-2"), 4);
		plan.addReceipe(loader.getRecipes().get("science-pack-3"), 8);
		plan.report().print(System.out);
		
		
		/*
		 * plan.addReceipe(loader.getReceipes().get("science-pack-2"), 4);
		 * plan.addReceipe(loader.getReceipes().get("science-pack-3"), 8);
		 * plan.addReceipe(loader.getReceipes().get("science-pack-4"), 1);
		 * 
		 */
	}
}
