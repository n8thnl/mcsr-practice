package com.n8thnl.mcsr_practice.mixin;

import com.n8thnl.mcsr_practice.MCSRPractice;
import com.n8thnl.mcsr_practice.TeleportHelper;
import com.n8thnl.mcsr_practice.Stronghold;
import com.n8thnl.mcsr_practice.InventoryManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class PlayerJoinMixin {
    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void onJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        if (MCSRPractice.shouldTeleportToStronghold) {
            MCSRPractice.shouldTeleportToStronghold = false;
            // Your teleport logic here
            System.out.println("New world joined! Triggering TP...");

            MinecraftClient client = MinecraftClient.getInstance();
            MinecraftServer server = client.getServer();

            if (server != null) {
                server.execute(() -> {
                    ServerPlayerEntity player = server
                        .getPlayerManager()
                        .getPlayerList()
                        .get(0);
                    if (player != null) {
                        ServerWorld world = player.getServerWorld();
                        BlockPos structurePos = Stronghold.getPos(player, world);
                        BlockPos starterStaircasePos = Stronghold.getStarterStaircasePos(world, structurePos);

                        if (starterStaircasePos != null) {
                            TeleportHelper.goToPos(starterStaircasePos, server);

                            server.execute(() -> {
                                InventoryManager.applyInventoryFromFile(player, "stronghold-1.json");
                            });
                        }
                    }
                });
            }
        }
    }
}
