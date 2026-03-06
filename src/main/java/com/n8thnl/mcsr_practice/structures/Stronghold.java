package com.n8thnl.mcsr_practice;

import net.minecraft.util.math.BlockPos;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.gen.feature.StructureFeature;

public class Stronghold {

    public static BlockPos getPos(ServerPlayerEntity player, ServerWorld world) {
        return world
            .getChunkManager()
            .getChunkGenerator()
            .locateStructure(
                world,
                net.minecraft.world.gen.feature.StructureFeature.STRONGHOLD,
                player.getBlockPos(),
                100,
                false
            );
    }

    public static BlockPos getStarterStaircasePos(ServerWorld world, BlockPos structurePos) {

        // 1. Get the chunk section position
        net.minecraft.util.math.ChunkSectionPos sectionPos =
            net.minecraft.util.math.ChunkSectionPos.from(
                structurePos
            );

        // 2. Get the actual Chunk object (which acts as the StructureHolder in 1.16.1)
        net.minecraft.world.chunk.Chunk chunk = world.getChunk(
            sectionPos.getSectionX(),
            sectionPos.getSectionZ()
        );

        // 3. Request the structure from that chunk specifically
        net.minecraft.structure.StructureStart start = world
            .getStructureAccessor()
            .getStructureStart(
                sectionPos,
                net.minecraft.world.gen.feature.StructureFeature.STRONGHOLD,
                chunk // Passing the chunk instead of the world fixes the error
            );

        if (start != null && start.hasChildren()) {
            // Cast to StructurePiece as before
            net.minecraft.structure.StructurePiece starterPiece =
                (net.minecraft.structure.StructurePiece) start
                    .getChildren()
                    .get(0);
            net.minecraft.util.math.BlockBox box =
                starterPiece.getBoundingBox();

            // Standard center calculation
            int centerX = (box.minX + box.maxX) / 2;
            int centerZ = (box.minZ + box.maxZ) / 2;
            int centerY = box.minY + 1;

            return new BlockPos(
                centerX,
                centerY,
                centerZ
            );
        } else {
            return null;
        }
    }
}
