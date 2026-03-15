package com.n8thnl.mcsr_practice;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MCSRPractice implements ModInitializer {
	public static final String MOD_ID = "mcsr_practice";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static SpeedrunSocketServer globalServer;

    public static boolean shouldTeleportToStronghold = false;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

        globalServer = new SpeedrunSocketServer(8887);
        new Thread(globalServer).start();

        AdminServer admin = new AdminServer();
        admin.start();
	}
}
