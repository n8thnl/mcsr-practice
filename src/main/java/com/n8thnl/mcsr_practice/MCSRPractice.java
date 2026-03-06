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

    private SpeedrunSocketServer server;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

        // This event fires when the integrated server (singleplayer) starts
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STARTED.register(clientServer -> {
            server = new SpeedrunSocketServer(8887, clientServer);
            server.start();
        });

        // Clean up when the world closes
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPING.register(clientServer -> {
            if (server != null) {
                try {
                    server.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
	}
}
