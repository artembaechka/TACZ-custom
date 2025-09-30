package com.tacz.guns.sound;

import com.tacz.guns.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class BulletHitSoundManager {
    
    private static final Map<MaterialType, RegistryObject<SoundEvent>> MATERIAL_SOUND_EVENTS = new HashMap<>();
    
    static {
        MATERIAL_SOUND_EVENTS.put(MaterialType.GENERIC, ModSounds.BULLET_HIT_GENERIC);
        
        MATERIAL_SOUND_EVENTS.put(MaterialType.METAL, ModSounds.BULLET_HIT_METAL);
        
        MATERIAL_SOUND_EVENTS.put(MaterialType.WATER, ModSounds.BULLET_HIT_WATER);
    }

    public enum MaterialType {
        GENERIC,
        METAL,
        WATER
    }
    

    public static void playBulletHitSound(Level level, Vec3 hitPos, BlockState blockState) {
        if (!(level instanceof ServerLevel)) {
            return;
        }
        
        ServerLevel serverLevel = (ServerLevel) level;
        
        MaterialType materialType = getMaterialType(blockState);
        
        RegistryObject<SoundEvent> soundEventRegistry = MATERIAL_SOUND_EVENTS.get(materialType);
        if (soundEventRegistry != null) {
            SoundEvent customSound = soundEventRegistry.get();
            if (customSound != null) {
                float pitch = 0.8f + serverLevel.getRandom().nextFloat() * 0.4f;
                float volume = 0.7f + serverLevel.getRandom().nextFloat() * 0.3f;
                
                serverLevel.playSound(null, hitPos.x, hitPos.y, hitPos.z, customSound, SoundSource.BLOCKS, volume, pitch);
                return;
            }
        }
        
        SoundEvent fallbackSound;
        switch (materialType) {
            case METAL:
                fallbackSound = SoundEvents.ANVIL_HIT;
                break;
            case WATER:
                fallbackSound = SoundEvents.GENERIC_SPLASH;
                break;
            default:
                fallbackSound = SoundEvents.STONE_HIT;
                break;
        }
        
        float pitch = 0.8f + serverLevel.getRandom().nextFloat() * 0.4f;
        float volume = 0.6f + serverLevel.getRandom().nextFloat() * 0.4f;
        serverLevel.playSound(null, hitPos.x, hitPos.y, hitPos.z, fallbackSound, SoundSource.BLOCKS, volume, pitch);
    }
    

    private static MaterialType getMaterialType(BlockState blockState) {
        Block block = blockState.getBlock();
        
        if (blockState.is(Blocks.WATER) || blockState.is(BlockTags.CAULDRONS)) {
            return MaterialType.WATER;
        }
        
        if (isMetalBlock(block)) {
            return MaterialType.METAL;
        }
        
        return MaterialType.GENERIC;
    }
    

    private static boolean isMetalBlock(Block block) {
        if (block == Blocks.IRON_BLOCK || block == Blocks.IRON_DOOR ||
            block == Blocks.IRON_TRAPDOOR || block == Blocks.IRON_BARS) {
            return true;
        }
        
        if (block == Blocks.GOLD_BLOCK) {
            return true;
        }
        
        if (block == Blocks.COPPER_BLOCK || block == Blocks.EXPOSED_COPPER ||
            block == Blocks.WEATHERED_COPPER || block == Blocks.OXIDIZED_COPPER) {
            return true;
        }
        
        if (block == Blocks.ANVIL || block == Blocks.CHIPPED_ANVIL || block == Blocks.DAMAGED_ANVIL) {
            return true;
        }
        
        if (block == Blocks.RAIL || block == Blocks.POWERED_RAIL ||
            block == Blocks.DETECTOR_RAIL || block == Blocks.ACTIVATOR_RAIL) {
            return true;
        }
        
        if (block == Blocks.CAULDRON) {
            return true;
        }
        
        if (block == Blocks.CHAIN) {
            return true;
        }
        
        return false;
    }
    

}