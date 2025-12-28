package com.FREEMOD.freemod.world.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

import java.util.Optional;

public class OblivionMineshaft extends StructureFeature<JigsawConfiguration> {

    public OblivionMineshaft() {
        super(JigsawConfiguration.CODEC, OblivionMineshaft::createPiecesGenerator, PostPlacementProcessor.NONE);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }

    private static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        // 1. 乱数生成器を作成する (1.18.2の作法)
        // context.seed() と チャンク座標を使って、場所ごとに固定のランダムを作る
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenrandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);

        // 2. Y座標をランダムに決定 (例: Y=10 ～ Y=50 の間)
        int minHeight = 10; // 生成される最低の高さ
        int maxHeight = 50; // 生成される最高の高さ

        // context.random() の代わりに作成した worldgenrandom を使う
        int y = worldgenrandom.nextInt(maxHeight - minHeight) + minHeight;

        BlockPos blockpos = new BlockPos(context.chunkPos().getMinBlockX(), y, context.chunkPos().getMinBlockZ());

        return JigsawPlacement.addPieces(
                context,
                PoolElementStructurePiece::new,
                blockpos,
                false, // false = 地形の高さに合わせない（地中埋め込み）
                false   // true = 空洞を作る
        );
    }
}