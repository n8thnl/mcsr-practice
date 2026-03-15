package com.n8thnl.mcsr_practice;

import java.net.InetSocketAddress;
import net.minecraft.server.MinecraftServer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.feature.StructureFeature;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class SpeedrunSocketServer extends WebSocketServer {

    public SpeedrunSocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("External tool connected!");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        if (message.equalsIgnoreCase("tpsr:stronghold")) {
            // We must execute on the main server thread to avoid crashes
            MinecraftClient client = MinecraftClient.getInstance();
            MinecraftServer mcServer = client.getServer();

            if (mcServer != null) {
                mcServer.execute(() -> {
                    ServerPlayerEntity player = mcServer
                        .getPlayerManager()
                        .getPlayerList()
                        .get(0);
                    if (player != null) {
                        ServerWorld world = player.getServerWorld();
                        BlockPos structurePos = Stronghold.getPos(player, world);
                        BlockPos starterStaircasePos = Stronghold.getStarterStaircasePos(world, structurePos);

                        if (starterStaircasePos != null) {
                            TeleportHelper.goToPos(starterStaircasePos, mcServer);

                            conn.send(
                                "Teleport successful to structure piece index 0."
                            );
                        }
                    }
                });
            }
        }

        if (message.equalsIgnoreCase("tpsr:reset:stronghold")) {

            MCSRPractice.shouldTeleportToStronghold = true;

            MinecraftClient.getInstance().execute(() -> {
                try {
                    me.voidxwalker.autoreset.Atum.scheduleReset();
                } catch (Exception e) {
                    conn.send("Error: Could not call Atum. Is it installed?");
                }
            });
        }
    }

    @Override
    public void onClose(
        WebSocket conn,
        int code,
        String reason,
        boolean remote
    ) {}

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket Server started on port " + getPort());
    }

    // Ensure this helper checks for enough height
    private boolean is3x3Air(ServerWorld world, BlockPos pos) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = 0; y < 3; y++) {
                    // 3 blocks high is enough for a player
                    if (!world.getBlockState(pos.add(x, y, z)).isAir()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // Update your helper to be more "forgiving"
    private boolean isSafeVolume(ServerWorld world, BlockPos pos) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = 0; y < 3; y++) {
                    net.minecraft.block.BlockState state = world.getBlockState(
                        pos.add(x, y, z)
                    );
                    // If it's a full solid block (Stone, Dirt, etc), it's NOT safe.
                    // This allows torches, ladders, and water to count as "air".
                    if (
                        state.getMaterial().isSolid() &&
                        state.isFullCube(world, pos.add(x, y, z))
                    ) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
