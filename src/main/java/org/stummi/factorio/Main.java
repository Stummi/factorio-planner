package org.stummi.factorio;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import org.stummi.factorio.luaconf.GamePathFinder;
import org.stummi.factorio.luaconf.LuaEntityLoader;

public class Main {
	public static void main(String[] args) throws IOException {
		File file = GamePathFinder.findGameDir();
		if(file == null) {
			// no game path found :(
			return;
		}
		
		LuaEntityLoader loader = new LuaEntityLoader(file);

		Plan plan = new Plan();
		plan.addReceipe(loader.getRecipes().get("science-pack-1"), 4);
		plan.addReceipe(loader.getRecipes().get("science-pack-2"), 4);
		plan.addReceipe(loader.getRecipes().get("science-pack-3"), 8);
		plan.report().print(System.out);

		/*
		 * plan.addReceipe(loader.getReceipes().get("science-pack-2"), 4);
		 * plan.addReceipe(loader.getReceipes().get("science-pack-3"), 8);
		 * plan.addReceipe(loader.getReceipes().get("science-pack-4"), 1);
		 */
	}
}
