package com.FREEMOD.freemod.fluid;

import com.FREEMOD.freemod.main.FreeMod;
import com.FREEMOD.freemod.effect.ModEffects;
import com.FREEMOD.freemod.register.BlockRegister;
import com.FREEMOD.freemod.register.ItemRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AcidFluid {

    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
    public static final ResourceLocation WATER_OVERLAY_RL = new ResourceLocation("block/water_overlay");

    public static final DeferredRegister<Fluid> ACID_FLUIDS
            = DeferredRegister.create(ForgeRegistries.FLUIDS, FreeMod.MOD_ID);

    public static final RegistryObject<FlowingFluid> ACID_FLUID
            = ACID_FLUIDS.register("acid_fluid", () -> new ForgeFlowingFluid.Source(AcidFluid.ACID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> ACID_FLOWING
            = ACID_FLUIDS.register("acid_flowing", () -> new ForgeFlowingFluid.Flowing(AcidFluid.ACID_PROPERTIES));

    public static final ForgeFlowingFluid.Properties ACID_PROPERTIES = new ForgeFlowingFluid.Properties(
            () -> ACID_FLUID.get(), () -> ACID_FLOWING.get(), FluidAttributes.builder(WATER_STILL_RL, WATER_FLOWING_RL)
            .density(15).luminosity(2).viscosity(5).sound(SoundEvents.WATER_AMBIENT).overlay(WATER_OVERLAY_RL)
            .color(0xFF9ACD32)).slopeFindDistance(4).levelDecreasePerBlock(1).canMultiply()
            .block(() -> AcidFluid.ACID_BLOCK.get()).bucket(() -> ItemRegister.ACID_BUCKET.get());

    public static final RegistryObject<LiquidBlock> ACID_BLOCK = BlockRegister.BLOCKS.register("acid_water",
            () -> new LiquidBlock(() -> AcidFluid.ACID_FLUID.get(), BlockBehaviour.Properties.of(Material.WATER)
                    .noCollission().strength(100f).noDrops()));

    public static void register(IEventBus eventBus) {
        ACID_FLUIDS.register(eventBus);
        MinecraftForge.EVENT_BUS.register(new AcidFluid.AcidEventHandler());
    }

    public static class AcidEventHandler {
        private final Map<UUID, Integer> playerAcidTicks = new HashMap<>();

        @SubscribeEvent
        public void onPlayerTick(TickEvent.PlayerTickEvent event) {
            Player player = event.player;
            UUID playerUUID = player.getUUID();

            if (!player.level.isClientSide) {
                BlockPos playerPos = player.blockPosition();
                BlockState blockState = player.level.getBlockState(playerPos);

                if (blockState.getBlock() == AcidFluid.ACID_BLOCK.get()) {
                    // 液体に触れている場合、タイマーをリセットし、効果を適用
                    playerAcidTicks.put(playerUUID, 20 * 5); // 5秒間効果持続
                    player.addEffect(new MobEffectInstance(ModEffects.ACID_EFFECT.get(), 20, 0));
                } else if (playerAcidTicks.containsKey(playerUUID)) {
                    // 液体から離れた場合、タイマーを減らす
                    int ticks = playerAcidTicks.get(playerUUID);
                    ticks--;

                    if (ticks > 0) {
                        playerAcidTicks.put(playerUUID, ticks);
                        player.addEffect(new MobEffectInstance(ModEffects.ACID_EFFECT.get(), 20, 0));
                    } else {
                        playerAcidTicks.remove(playerUUID);
                    }
                }
            }
        }
    }
}
