package org.stummi.factorio.luaconf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.stummi.factorio.data.AssemblingMachine;
import org.stummi.factorio.data.Entity;
import org.stummi.factorio.data.EntityLoader;
import org.stummi.factorio.data.Item;
import org.stummi.factorio.data.ItemAmount;
import org.stummi.factorio.data.Receipe;
import org.stummi.factorio.data.Receipe.Builder;
import org.stummi.factorio.data.Ticks;
import org.stummi.factorio.gui.ImageFactory;

public class LuaEntityLoader implements EntityLoader {

	private final LuaTable rawData;
	private final LuaConfImageFactory imageFactory;

	private Map<String, Item> items;
	private Map<String, AssemblingMachine> assemblingMachines;
	private Map<String, Receipe> recipes;

	public LuaEntityLoader(File baseDir) throws IOException {
		File luaLib = new File(baseDir, "data/core/lualib/?.lua");
		File baseData = new File(baseDir, "data/base/?.lua");
		String packagePath = luaLib.getAbsolutePath() + ";" + baseData.getAbsolutePath();

		Globals context = JsePlatform.standardGlobals();
		try (InputStream in = LuaEntityLoader.class.getResourceAsStream("/compat.lua")) {
			context.load(in, "compat", "t", context).call();
		}

		context.load("package.path = '" + packagePath + "'").call();
		context.load("require 'dataloader'").call();
		context.load("require 'data'").call();
		LuaTable data = (LuaTable) context.get("data");
		rawData = (LuaTable) data.get("raw");

		this.imageFactory = new LuaConfImageFactory(new File(baseDir, "data/base"));
	}

	@Override
	public Map<String, Item> getItems() {
		if (items == null) {
			items = getEntityMap("item", LuaEntityLoader::toItem);
			items.putAll(getEntityMap("fluid", LuaEntityLoader::toItem));
			items.putAll(getEntityMap("capsule", LuaEntityLoader::toItem));
			items.putAll(getEntityMap("module", LuaEntityLoader::toItem));
			items.putAll(getEntityMap("ammo", LuaEntityLoader::toItem));
			items.putAll(getEntityMap("armor", LuaEntityLoader::toItem));
			items.putAll(getEntityMap("mining-tool", LuaEntityLoader::toItem));
			items.putAll(getEntityMap("gun", LuaEntityLoader::toItem));
			items.putAll(getEntityMap("blueprint", LuaEntityLoader::toItem));
			items.putAll(getEntityMap("deconstruction-item", LuaEntityLoader::toItem));
			items.putAll(getEntityMap("repair-tool", LuaEntityLoader::toItem));
			items.putAll(getEntityMap("tool", LuaEntityLoader::toItem));
		}
		return items;
	}

	@Override
	public Map<String, AssemblingMachine> getAssemblingMachines() {
		if (assemblingMachines == null) {
			assemblingMachines = getEntityMap("assembling-machine", LuaEntityLoader::toAssemblingMachine);
			assemblingMachines.putAll(getEntityMap("furnace", LuaEntityLoader::toAssemblingMachine));
			assemblingMachines.put(AssemblingMachine.NONE.getName(), AssemblingMachine.NONE);
		}
		return assemblingMachines;
	}

	@Override
	public Map<String, Receipe> getReceipes() {
		if (recipes == null) {
			recipes = getEntityMap("recipe", this::toRecipe);
			recipes.put(Receipe.NONE.getName(), Receipe.NONE);
		}
		return recipes;
	}

	@Override
	public ImageFactory getImageFactory() {
		return imageFactory;
	}

	private <T extends Entity> Map<String, T> getEntityMap(String name, Function<LuaTable, T> converter) {
		LuaTable entities = (LuaTable) rawData.get(name);
		Map<String, T> retMap = new HashMap<>();

		for (LuaValue val : entities.keys()) {
			LuaTable e = (LuaTable) entities.get(val);
			T entity = converter.apply(e);
			retMap.put(entity.getName(), entity);
		}
		return retMap;
	}

	private static Item toItem(LuaTable table) {
		String name = table.get("name").toString();
		String icon = table.get("icon").toString();
		return new Item(name, icon);
	}

	private static AssemblingMachine toAssemblingMachine(LuaTable table) {
		String name = table.get("name").toString();
		String icon = table.get("icon").toString();
		LuaValue modSpec = table.get("module_specification");
		int moduleSlots;
		if (!modSpec.istable()) {
			moduleSlots = 0;
		} else {
			moduleSlots = ((LuaTable) modSpec).get("module_slots").toint();
		}
		return new AssemblingMachine(name, icon, moduleSlots);
	}

	private Receipe toRecipe(LuaTable table) {
		String name = table.get("name").toString();
		int seconds = table.get("energy_required").toint();
		Ticks ticks = seconds == 0 ? new Ticks(30) : Ticks.forSeconds(seconds);

		LuaTable ingreds = (LuaTable) table.get("ingredients");
		LuaValue results = table.get("results");
		LuaValue result = table.get("result");
		LuaValue resultAmount = table.get("result_count");

		Builder builder = Receipe.builder(name, ticks.getTicks());
		Stream.of(ingreds.keys()).map(t -> (LuaTable) ingreds.get(t)).map(this::parseItemAmount).forEach(builder::resource);

		if (!result.isnil()) {
			int amount = resultAmount.isnil() ? 1 : resultAmount.toint();
			String itemName = result.toString();
			Item item = getNonNullItem(itemName);
			builder.icon(item.getIconName());
			builder.product(item, amount);
		} else {
			LuaTable tbl = (LuaTable) results;
			String icon = table.get("icon").toString();
			builder.icon(icon);
			Stream.of(tbl.keys()).map(t -> (LuaTable) tbl.get(t)).map(this::parseItemAmount).forEach(builder::product);
		}

		return builder.build();
	}

	private ItemAmount parseItemAmount(LuaTable table) {
		LuaValue nameVal = table.get("name");
		String name;
		int amount;
		if (nameVal.isnil()) {
			name = table.get(1).toString();
			amount = table.get(2).toint();
		} else {
			name = nameVal.toString();
			amount = table.get("amount").toint();
		}

		Item item = getNonNullItem(name);
		return new ItemAmount(item, amount);
	}

	private Item getNonNullItem(String name) {
		Item item = getItems().get(name);
		Objects.requireNonNull(item, "no such item: " + name);
		return item;
	}
}