package org.stummi.factorio.gui;

import org.stummi.factorio.data.NameAndIcon;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleNameAndIcon implements NameAndIcon {

	private final String name;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getIconName() {
		return "/" + name.toLowerCase() + ".png";
	}

}
