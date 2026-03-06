package com.n8thnl.mcsr_practice;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class TeleportHelper {

    public static void goToPos(BlockPos pos, MinecraftServer mcServer) {

        ServerPlayerEntity player = mcServer
            .getPlayerManager()
            .getPlayerList()
            .get(0);

        if (player != null) {
            ServerWorld world = player.getServerWorld();

            // --- TELEPORT LOGIC ---
            player.setGameMode(
                net.minecraft.world.GameMode.SPECTATOR
            );

            world
                .getChunkManager()
                .addTicket(
                    net.minecraft.server.world.ChunkTicketType.START,
                    new net.minecraft.util.math.ChunkPos(
                        pos
                    ),
                    1,
                    net.minecraft.util.Unit.INSTANCE
                );

            double tpX = pos.getX() + 0.5;
            double tpY = pos.getY() + 0.1;
            double tpZ = pos.getZ() + 0.5;

            player.teleport(
                world,
                tpX,
                tpY,
                tpZ,
                player.yaw,
                player.pitch
            );
            player.refreshPositionAfterTeleport(tpX, tpY, tpZ);
            player.setSpawnPoint(
                world.getRegistryKey(),
                pos,
                true,
                false
            );

            // Delayed Survival switch
            new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        mcServer.execute(() -> {
                            player.setGameMode(
                                net.minecraft.world.GameMode.SURVIVAL
                            );
                            player.addStatusEffect(
                                new net.minecraft.entity.effect.StatusEffectInstance(
                                    net.minecraft.entity.effect.StatusEffects.NIGHT_VISION,
                                    12000,
                                    0,
                                    false,
                                    false
                                )
                            );
                        });
                    }
                },
                1500
            );
        }
    }
}
