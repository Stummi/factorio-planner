package org.stummi.factorio.examples;

import org.stummi.factorio.Plan;
import org.stummi.factorio.data.EntityLoader;

public class BalancingExample extends AbstractExample{
	public static void main(String[] args) throws Exception {
		new BalancingExample().run(args);
	}

	@Override
	protected void runExample(EntityLoader loader, String[] args)
			throws Exception {		
		Plan plan = new Plan();
		plan.addGoal(loader.getItem("science-pack-1").amount(30).perMinute());
		plan.addGoal(loader.getItem("science-pack-2").amount(30).perMinute());
		plan.addGoal(loader.getItem("science-pack-3").amount(30).perMinute());
		plan.addGoal(loader.getItem("alien-science-pack").amount(30).perMinute());

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
