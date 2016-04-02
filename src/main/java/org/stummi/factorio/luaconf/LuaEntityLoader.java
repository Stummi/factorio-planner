package org.stummi.factorio.luaconf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.stummi.factorio.data.AssemblingMachine;
import org.stummi.factorio.data.AssemblingMachine.AssemblingMachineBuilder;
import org.stummi.factorio.data.EntityLoader;
import org.stummi.factorio.data.GroupableEntity;
import org.stummi.factorio.data.Grouped;
import org.stummi.factorio.data.Item;
import org.stummi.factorio.data.ItemAmount;
import org.stummi.factorio.data.ItemGroup;
import org.stummi.factorio.data.ItemSubgroup;
import org.stummi.factorio.data.Named;
import org.stummi.factorio.data.Order;
import org.stummi.factorio.data.Ordered;
import org.stummi.factorio.data.Recipe;
import org.stummi.factorio.data.Recipe.RecipeBuilder;
import org.stummi.factorio.data.ResourceFactory;
import org.stummi.factorio.data.Subgrouped;
import org.stummi.factorio.data.Ticks;

public class LuaEntityLoader implements EntityLoader {

	private final File baseDir;
	private final LuaTable rawData;

	private LuaConfResourceFactory resourceFactory;

	private Map<String, Item> items;
	private Map<String, AssemblingMachine> assemblingMachines;
	private Map<String, Recipe> recipes;

	private Map<String, ItemGroup> itemGroups;
	private Map<String, ItemSubgroup> itemSubgroups;

	private List<Grouped<Item>> groupedItems;
	private List<Grouped<Recipe>> groupedRecipes;

	public LuaEntityLoader(File baseDir) throws IOException {
		this.baseDir = baseDir;
		File luaLib = new File(baseDir, "data/core/lualib/?.lua");
		File baseData = new File(baseDir, "data/base/?.lua");
		String packagePath = luaLib.getAbsolutePath() + ";"
				+ baseData.getAbsolutePath();

		Globals context = JsePlatform.standardGlobals();
		try (InputStream in = LuaEntityLoader.class
				.getResourceAsStream("/compat.lua")) {
			context.load(in, "compat", "t", context).call();
		}

		((LuaTable) context.get("package")).set("path", packagePath);
		context.load("require 'dataloader'").call();
		context.load("require 'data'").call();
		LuaTable data = (LuaTable) context.get("data");
		rawData = (LuaTable) data.get("raw");
	}

	@Override
	public Map<String, Item> getItems() {
		if (items == null) {
			items = getEntityMap("item", this::toItem);
			items.putAll(getEntityMap("fluid", this::toItem));
			items.putAll(getEntityMap("capsule", this::toItem));
			items.putAll(getEntityMap("module", this::toItem));
			items.putAll(getEntityMap("ammo", this::toItem));
			items.putAll(getEntityMap("armor", this::toItem));
			items.putAll(getEntityMap("mining-tool", this::toItem));
			items.putAll(getEntityMap("gun", this::toItem));
			items.putAll(getEntityMap("blueprint", this::toItem));
			items.putAll(getEntityMap("deconstruction-item", this::toItem));
			items.putAll(getEntityMap("repair-tool", this::toItem));
			items.putAll(getEntityMap("tool", this::toItem));
		}
		return items;
	}

	@Override
	public Map<String, AssemblingMachine> getAssemblingMachines() {
		if (assemblingMachines == null) {
			assemblingMachines = getEntityMap("assembling-machine",
					LuaEntityLoader::toAssemblingMachine);
			assemblingMachines.putAll(getEntityMap("furnace",
					LuaEntityLoader::toAssemblingMachine));
			assemblingMachines.putAll(getEntityMap("rocket-silo",
					LuaEntityLoader::toAssemblingMachine));
			assemblingMachines.put(AssemblingMachine.NONE.getName(),
					AssemblingMachine.NONE);
		}
		return assemblingMachines;
	}

	@Override
	public Map<String, Recipe> getRecipes() {
		if (recipes == null) {
			recipes = getEntityMap("recipe", this::toRecipe);
			recipes.put(Recipe.NONE.getName(), Recipe.NONE);
		}
		return recipes;
	}

	public Map<String, ItemGroup> getItemGroups() {
		if (itemGroups == null) {
			itemGroups = getEntityMap("item-group",
					LuaEntityLoader::toItemGroup);
		}
		return itemGroups;
	}

	public Map<String, ItemSubgroup> getItemSubgroups() {
		if (itemSubgroups == null) {
			itemSubgroups = getEntityMap("item-subgroup", this::toItemSubGroup);
		}
		return itemSubgroups;
	}

	@Override
	public List<Grouped<Item>> getGroupedItems() {
		if (groupedItems == null) {
			groupedItems = getGrouped(getItems());
		}
		return groupedItems;
	}

	@Override
	public List<Grouped<Recipe>> getGroupedRecipes() {
		if (groupedRecipes == null) {
			groupedRecipes = getGrouped(getRecipes());
		}
		return groupedRecipes;
	}

	@Override
	public ResourceFactory getResourceFactory() {
		if (resourceFactory == null) {
			resourceFactory = new LuaConfResourceFactory(new File(baseDir,
					"data/base"));
		}
		return resourceFactory;
	}

	private <T extends Named> Map<String, T> getEntityMap(String name,
			Function<LuaTable, T> converter) {
		LuaTable entities = (LuaTable) rawData.get(name);
		Map<String, T> retMap = new HashMap<>();

		for (LuaValue val : entities.keys()) {
			LuaTable e = (LuaTable) entities.get(val);
			T entity = converter.apply(e);
			retMap.put(entity.getName(), entity);
		}
		return retMap;
	}

	private <T extends GroupableEntity> List<Grouped<T>> getGrouped(
			Map<String, T> entities) {
		Map<ItemGroup, Map<ItemSubgroup, List<T>>> groupMap = new HashMap<>();

		for (T entity : entities.values()) {
			ItemSubgroup subgroup = entity.getSubgroup();
			if (subgroup == null) {
				// System.out.println("whoot? No subgroup for " + entity);
				continue;
			}
			ItemGroup group = subgroup.getGroup();

			Map<ItemSubgroup, List<T>> subgroupMap = groupMap.computeIfAbsent(
					group, g -> new HashMap<>());

			List<T> entityList = subgroupMap.computeIfAbsent(subgroup,
					sg -> new ArrayList<>());

			entityList.add(entity);
		}

		List<Grouped<T>> groupedList = new ArrayList<>();
		for (Entry<ItemGroup, Map<ItemSubgroup, List<T>>> group : groupMap
				.entrySet()) {
			List<Subgrouped<T>> subgrouped = new ArrayList<>();
			for (Entry<ItemSubgroup, List<T>> subgroup : group.getValue()
					.entrySet()) {
				List<T> list = subgroup.getValue();
				Collections.sort(list, Ordered.orderedComparator());
				subgrouped.add(new Subgrouped<>(subgroup.getKey(), list));
			}

			Collections.sort(
					subgrouped,
					Comparator.comparing(Subgrouped::getSubgroup,
							Ordered.orderedComparator()));

			groupedList.add(new Grouped<>(group.getKey(), subgrouped));
		}

		Collections.sort(
				groupedList,
				Comparator.comparing(Grouped::getGroup,
						Ordered.orderedComparator()));

		return groupedList;

	}

	private Item toItem(LuaTable table) {
		String name = table.get("name").toString();
		String icon = table.get("icon").toString();
		String order = table.get("order").toString();
		
		LuaValue subgroupVal = table.get("subgroup");
		if(subgroupVal.isnil()) {
			// Not sure if this is correct behavior. The only items without subgroup so far are fluids, which have the type "fluid" and also belong in this groups
			subgroupVal = table.get("type");
		}
		ItemSubgroup subgroup = getItemSubgroups().get(subgroupVal.toString());
		
		if(subgroup == null) {
			
		}
		return new Item(name, icon, new Order(order), subgroup);
	}

	private static AssemblingMachine toAssemblingMachine(LuaTable table) {
		AssemblingMachineBuilder builder = AssemblingMachine.builder();
		builder.name(table.get("name").toString());
		builder.iconName(table.get("icon").toString());

		LuaValue craftingCategories = table.get("crafting_categories");
		IntStream.rangeClosed(1, craftingCategories.length())
				.mapToObj(craftingCategories::get).map(LuaValue::toString)
				.forEach(builder::craftingCategory);

		LuaValue modSpec = table.get("module_specification");
		int moduleSlots;
		if (!modSpec.istable()) {
			moduleSlots = 0;
		} else {
			moduleSlots = ((LuaTable) modSpec).get("module_slots").toint();
		}

		builder.moduleSlots(moduleSlots);
		builder.craftingSpeed(table.get("crafting_speed").todouble());

		int ingredientCount = table.get("ingedient_count").toint();
		builder.ingredientCount(ingredientCount);

		return builder.build();
	}

	private Recipe toRecipe(LuaTable table) {
		String name = table.get("name").toString();
		int seconds = table.get("energy_required").toint();
		Ticks ticks = seconds == 0 ? new Ticks(30) : Ticks.forSeconds(seconds);

		LuaTable ingreds = (LuaTable) table.get("ingredients");
		LuaValue results = table.get("results");
		LuaValue result = table.get("result");
		LuaValue resultAmount = table.get("result_count");
		LuaValue category = table.get("category");

		RecipeBuilder builder = Recipe.builder().name(name)
				.cycleTime(ticks.getTicks());
		builder.category(category.isnil() ? "crafting" : category.toString());
		Stream.of(ingreds.keys()).map(t -> (LuaTable) ingreds.get(t))
				.map(this::toItemAmount).forEach(builder::resource);

		ItemAmount[] products;
		if (!result.isnil()) {
			int amount = resultAmount.isnil() ? 1 : resultAmount.toint();
			String itemName = result.toString();
			Item item = getNonNullItem(itemName);
			products = new ItemAmount[] { item.amount(amount) };
			builder.iconName(item.getIconName());
		} else {
			LuaTable tbl = (LuaTable) results;
			products = Stream.of(tbl.keys()).map(t -> (LuaTable) tbl.get(t))
					.map(this::toItemAmount).toArray(i -> new ItemAmount[i]);
		}

		builder.products(Arrays.asList(products));
		String icon;
		LuaValue iconVal = table.get("icon");
		if (iconVal.isnil()) {
			icon = products[0].getItem().getIconName();
		} else {
			icon = iconVal.toString();
		}
		builder.iconName(icon);

		LuaValue orderVal = table.get("order");
		Order order;
		if (orderVal.isnil()) {
			order = products[0].getItem().getOrder();
		} else {
			order = new Order(orderVal.toString());
		}
		builder.order(order);

		LuaValue subgroupVal = table.get("subgroup");
		ItemSubgroup subgroup;
		if (subgroupVal.isnil()) {
			subgroup = products[0].getItem().getSubgroup();
		} else {
			subgroup = getItemSubgroups().get(subgroupVal.toString());
		}
		builder.subgroup(subgroup);

		return builder.build();
	}

	private ItemAmount toItemAmount(LuaTable table) {
		LuaValue nameVal = table.get("name");
		String name;
		double amount;
		if (nameVal.isnil()) {
			name = table.get(1).toString();
			amount = table.get(2).toint();
		} else {
			name = nameVal.toString();
			amount = table.get("amount").todouble();
		}

		Item item = getNonNullItem(name);
		return new ItemAmount(item, amount);
	}

	private static ItemGroup toItemGroup(LuaTable table) {
		String name = table.get("name").toString();
		String order = table.get("order").toString();
		String iconName = table.get("icon").toString();
		return new ItemGroup(name, new Order(order), iconName);
	}

	private ItemSubgroup toItemSubGroup(LuaTable table) {
		String name = table.get("name").toString();
		String order = table.get("order").toString();
		String groupName = table.get("group").toString();
		return new ItemSubgroup(name, new Order(order), getItemGroups().get(
				groupName));
	}

	private Item getNonNullItem(String name) {
		Item item = getItems().get(name);
		Objects.requireNonNull(item, "no such item: " + name);
		return item;
	}
}
