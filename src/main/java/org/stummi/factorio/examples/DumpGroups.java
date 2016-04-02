package org.stummi.factorio.examples;

import java.util.List;

import org.stummi.factorio.data.EntityLoader;
import org.stummi.factorio.data.Grouped;
import org.stummi.factorio.data.ItemGroup;
import org.stummi.factorio.data.Named;
import org.stummi.factorio.data.Subgrouped;

public class DumpGroups extends AbstractExample {
	public static void main(String[] args) throws Exception {
		new DumpGroups().run(args);
	}

	@Override
	protected void runExample(EntityLoader loader, String[] args)
			throws Exception {
		System.out.println("--- ITEMS ---");
		dumpGroups(loader.getGroupedItems());
		System.out.println();
		System.out.println("--- RECIPES --");
		dumpGroups(loader.getGroupedRecipes());
	}

	private void dumpGroups(List<? extends Grouped<?>> groupedItems) {
		for (Grouped<?> grouped : groupedItems) {
			ItemGroup group = grouped.getGroup();
			System.out.println(group.getName() + " [" + group.getIconName()
					+ "]");
			
			for(Subgrouped<?> subgrouped : grouped.getSubgroups()) {
				System.out.println("\t"+subgrouped.getSubgroup().getName());
				
				for(Named item : subgrouped.getEntities()) {
					System.out.println("\t\t" + item.getName());
				}
			}
		}
	}

}
