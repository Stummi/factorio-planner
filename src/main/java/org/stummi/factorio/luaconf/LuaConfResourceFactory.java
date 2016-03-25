package org.stummi.factorio.luaconf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import lombok.RequiredArgsConstructor;

import org.stummi.factorio.data.ResourceFactory;

@RequiredArgsConstructor
public class LuaConfResourceFactory implements ResourceFactory {


	private final File baseDir;

	@Override
	public InputStream loadIcon(String iconName) {
		if(iconName == null) {
			return null;
		}
		
		String fileName = iconName.replace("__base__",
				baseDir.getAbsolutePath());
		File file = new File(fileName);
		if(!file.isFile()) {
			System.err.println("File does not exist: " + file + " ( for icon path: " + iconName + ")");
			return null;
		}

		try {
			return new FileInputStream(file);
		} catch (IOException ioe) {
			System.err.println("Could not read file: "+ file);
			return null;
		}

	}

}
