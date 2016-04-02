package org.stummi.factorio.gui;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import lombok.RequiredArgsConstructor;

import org.stummi.factorio.data.ResourceFactory;

@RequiredArgsConstructor
public class JFXImageFactory {
	public static final Image DEFAULT_IMAGE = new Image("/questionmark.png");
	private final Map<String, Image> cache = new HashMap<>();
	private final ResourceFactory factory;

	public Image getImage(String name) {
		if (name == null) {
			return DEFAULT_IMAGE;
		}

		Image ret = cache.get(name);
		if (ret == null) {
			ret = createImage(name);
			cache.put(name, ret);
		}
		
		return ret;
	}

	private Image createImage(String name) {
		try (InputStream in = factory.loadIcon(name)) {
			if(in == null) {
				System.err.println("image not found: " + name);
				return DEFAULT_IMAGE;
			}
			
			return new Image(in);
		} catch (Exception e) {
			System.err.println("Could not load image: " + name + " - " + e.getMessage());
			return DEFAULT_IMAGE;
		}
	}
}
