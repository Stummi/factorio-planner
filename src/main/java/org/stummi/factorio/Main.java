package org.stummi.factorio;

import java.io.File;
import java.io.IOException;

import org.stummi.factorio.data.Throughput;
import org.stummi.factorio.luaconf.GamePathFinder;
import org.stummi.factorio.luaconf.LuaEntityLoader;

public class Main {
	public static void main(String[] args) throws IOException {
		File file = GamePathFinder.findGameDir();
		if (file == null) {
			// no game path found :(
			return;
		}

		LuaEntityLoader loader = new LuaEntityLoader(file);

		Plan plan = new Plan();
		plan.addGoal(loader.getItem("science-pack-1"), Throughput.perMinute(30));
		plan.addGoal(loader.getItem("science-pack-2"), Throughput.perMinute(30));
		plan.addGoal(loader.getItem("science-pack-3"), Throughput.perMinute(30));
		plan.addGoal(loader.getItem("alien-science-pack"), Throughput.perMinute(50));

		plan.addFactoryToBalance(loader.getFactory("assembling-machine-1", "science-pack-1"));
		plan.addFactoryToBalance(loader.getFactory("assembling-machine-1", "science-pack-2"));
		plan.addFactoryToBalance(loader.getFactory("assembling-machine-2", "science-pack-3"));

		plan.addFactoryToBalance(loader.getFactory("assembling-machine-1", "basic-transport-belt"));
		plan.addFactoryToBalance(loader.getFactory("assembling-machine-1", "smart-inserter"));
		plan.addFactoryToBalance(loader.getFactory("assembling-machine-1", "fast-inserter"));
		plan.addFactoryToBalance(loader.getFactory("chemical-plant", "plastic-bar"));
		plan.addFactoryToBalance(loader.getFactory("assembling-machine-2", "basic-inserter"));

		plan.addFactoryToBalance(loader.getFactory("assembling-machine-2", "advanced-circuit"));
		plan.addFactoryToBalance(loader.getFactory("assembling-machine-1", "electronic-circuit"));
		plan.addFactoryToBalance(loader.getFactory("assembling-machine-1", "copper-cable"));
		plan.addFactoryToBalance(loader.getFactory("assembling-machine-1", "iron-gear-wheel"));
		plan.addFactoryToBalance(loader.getFactory("chemical-plant", "sulfur"));
		plan.addFactoryToBalance(loader.getFactory("chemical-plant", "sulfuric-acid"));

		plan.addFactoryToBalance(loader.getFactory("electric-furnace", "copper-plate"));
		plan.addFactoryToBalance(loader.getFactory("electric-furnace", "iron-plate"));
		plan.addFactoryToBalance(loader.getFactory("electric-furnace", "steel-plate"));

		plan.addFactoryToBalance(loader.getFactory("chemical-plant", "battery"));

		plan.getBalancedReport().print(System.out);
	}
}
