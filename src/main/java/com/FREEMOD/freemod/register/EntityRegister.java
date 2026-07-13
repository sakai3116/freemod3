package com.FREEMOD.freemod.register;


import com.FREEMOD.freemod.entity.CameraEntity;
import com.FREEMOD.freemod.entity.DroneEntity;
import com.FREEMOD.freemod.entity.FastShotSkeleton;
import com.FREEMOD.freemod.main.FreeMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegister {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, FreeMod.MOD_ID);

    // 即射スケルトンの登録
    public static final RegistryObject<EntityType<FastShotSkeleton>> FAST_SHOT_SKELETON =
            ENTITY_TYPES.register("fast_shot_skeleton",
                    () -> EntityType.Builder.of(FastShotSkeleton::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.99F) // スケルトンの標準サイズ
                            .build("fast_shot_skeleton")
            );

    // 💡 ドローンエンティティの登録を追加
    public static final RegistryObject<EntityType<DroneEntity>> DRONE =
            ENTITY_TYPES.register("drone",
                    () -> EntityType.Builder.<DroneEntity>of(DroneEntity::new, MobCategory.MISC)
                            .sized(1.375F, 0.5625F) // ボート基準のサイズ（当たり判定）
                            .build("drone")
            );

    public static final RegistryObject<EntityType<CameraEntity>> CAMERA =
            ENTITY_TYPES.register("camera",
                    () -> EntityType.Builder.<CameraEntity>of(CameraEntity::new, MobCategory.MISC)
                            .sized(1.375F, 0.5625F)
                            .build("camera")
            );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }



}
