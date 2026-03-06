package com.n8thnl.mcsr_practice.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MCSRPracticeMixin {
	@Inject(at = @At("HEAD"), method = "createWorlds")
	private void init(CallbackInfo info) {
		// This code is injected into the start of MinecraftServer.loadLevel()V
	}
}
