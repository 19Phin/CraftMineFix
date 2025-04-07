package net.dialingspoon.craftminefix;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CraftMineFix implements ModInitializer {
	public static final String MOD_ID = "craftminefix";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Config.load();
	}
}