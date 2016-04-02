package org.stummi.factorio.examples;

import java.io.File;

import org.stummi.factorio.data.EntityLoader;
import org.stummi.factorio.luaconf.GamePathFinder;
import org.stummi.factorio.luaconf.LuaEntityLoader;

public abstract class AbstractExample {
	protected void run(String... args) throws Exception {
		File file = GamePathFinder.findGameDir();
		if (file == null) {
			System.err.println("could not determine gamedir");
			return;
		}

		LuaEntityLoader loader = new LuaEntityLoader(file);
		
		runExample(loader, args);
	}

	protected abstract void runExample(EntityLoader loader, String[] args) throws Exception;
}
