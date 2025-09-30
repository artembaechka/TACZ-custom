package com.tacz.guns.init;

import com.tacz.guns.GunMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, GunMod.MOD_ID);

    public static final RegistryObject<SoundEvent> GUN = SOUNDS.register("gun", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(GunMod.MOD_ID, "gun")));
    public static final RegistryObject<SoundEvent> TARGET_HIT = SOUNDS.register("target_block_hit", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(GunMod.MOD_ID, "target_block_hit")));
    
    public static final RegistryObject<SoundEvent> BULLET_HIT_GENERIC = SOUNDS.register("bullet_hit_generic", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(GunMod.MOD_ID, "bullet_hit_generic")));
    public static final RegistryObject<SoundEvent> BULLET_HIT_METAL = SOUNDS.register("bullet_hit_metal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(GunMod.MOD_ID, "bullet_hit_metal")));
    public static final RegistryObject<SoundEvent> BULLET_HIT_WATER = SOUNDS.register("bullet_hit_water", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(GunMod.MOD_ID, "bullet_hit_water")));
}
