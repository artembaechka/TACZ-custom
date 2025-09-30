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
        WATER,
        WOOD,
        STONE,
        GLASS,
        SAND,
        SNOW,
        GRASS,
        DIRT,
        GRAVEL,
        CONCRETE
    }
    

    public static void playBulletHitSound(Level level, Vec3 hitPos, BlockState blockState, boolean isUnderwater) {
        if (!(level instanceof ServerLevel)) {
            return;
        }
        
        ServerLevel serverLevel = (ServerLevel) level;
        float volumeMultiplier = 1.0f;
        
        MaterialType materialType = getMaterialType(blockState);
        
        if (isUnderwater && materialType != MaterialType.WATER) {
            float pitch = 0.6f + serverLevel.getRandom().nextFloat() * 0.3f;
            float volume = (0.4f + serverLevel.getRandom().nextFloat() * 0.2f) * volumeMultiplier;
            serverLevel.playSound(null, hitPos.x, hitPos.y, hitPos.z, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, volume, pitch);
            return;
        }
        
        RegistryObject<SoundEvent> soundEventRegistry = MATERIAL_SOUND_EVENTS.get(materialType);
        if (soundEventRegistry != null) {
            SoundEvent customSound = soundEventRegistry.get();
            if (customSound != null) {
                float pitch = 0.8f + serverLevel.getRandom().nextFloat() * 0.4f;
                float volume = (0.7f + serverLevel.getRandom().nextFloat() * 0.3f) * volumeMultiplier;
                
                serverLevel.playSound(null, hitPos.x, hitPos.y, hitPos.z, customSound, SoundSource.BLOCKS, volume, pitch);
                return;
            }
        }
        
        SoundEvent fallbackSound = getFallbackSound(materialType);
        
        float pitch = 0.8f + serverLevel.getRandom().nextFloat() * 0.4f;
        float volume = (0.6f + serverLevel.getRandom().nextFloat() * 0.4f) * volumeMultiplier;
        serverLevel.playSound(null, hitPos.x, hitPos.y, hitPos.z, fallbackSound, SoundSource.BLOCKS, volume, pitch);
    }
    
    public static void playBulletHitSound(Level level, Vec3 hitPos, BlockState blockState) {
        playBulletHitSound(level, hitPos, blockState, false);
    }
    

    private static SoundEvent getFallbackSound(MaterialType materialType) {
        switch (materialType) {
            case METAL:
                return SoundEvents.ANVIL_HIT;
            case WATER:
                return SoundEvents.GENERIC_SPLASH;
            case WOOD:
                return SoundEvents.WOOD_HIT;
            case STONE:
                return SoundEvents.STONE_HIT;
            case GLASS:
                return SoundEvents.GLASS_BREAK;
            case SAND:
                return SoundEvents.SAND_HIT;
            case SNOW:
                return SoundEvents.SNOW_HIT;
            case GRASS:
                return SoundEvents.GRASS_HIT;
            case DIRT:
                return SoundEvents.GRAVEL_HIT;
            case GRAVEL:
                return SoundEvents.GRAVEL_HIT;
            case CONCRETE:
                return SoundEvents.STONE_HIT;
            default:
                return SoundEvents.STONE_HIT;
        }
    }

    private static MaterialType getMaterialType(BlockState blockState) {
        Block block = blockState.getBlock();
        
        if (blockState.is(Blocks.WATER) || blockState.is(BlockTags.CAULDRONS)) {
            return MaterialType.WATER;
        }
        
        if (isMetalBlock(block)) {
            return MaterialType.METAL;
        }
        
        if (isWoodBlock(block)) {
            return MaterialType.WOOD;
        }
        
        if (isStoneBlock(block)) {
            return MaterialType.STONE;
        }
        
        if (isGlassBlock(block)) {
            return MaterialType.GLASS;
        }
        
        if (isSandBlock(block)) {
            return MaterialType.SAND;
        }
        
        if (isSnowBlock(block)) {
            return MaterialType.SNOW;
        }
        
        if (isGrassBlock(block)) {
            return MaterialType.GRASS;
        }
        
        if (isDirtBlock(block)) {
            return MaterialType.DIRT;
        }
        
        if (isGravelBlock(block)) {
            return MaterialType.GRAVEL;
        }
        
        if (isConcreteBlock(block)) {
            return MaterialType.CONCRETE;
        }
        
        return MaterialType.GENERIC;
    }
    

    private static boolean isMetalBlock(Block block) {
        if (block == Blocks.IRON_BLOCK || block == Blocks.IRON_DOOR ||
            block == Blocks.IRON_TRAPDOOR || block == Blocks.IRON_BARS) {
            return true;
        }
        
        if (block == Blocks.GOLD_BLOCK || block == Blocks.NETHERITE_BLOCK) {
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
        
        if (block == Blocks.CAULDRON || block == Blocks.CHAIN || block == Blocks.LANTERN) {
            return true;
        }
        
        return false;
    }
    
    private static boolean isWoodBlock(Block block) {
        String blockName = block.toString().toLowerCase();
        if (blockName.contains("log") || blockName.contains("plank") || blockName.contains("wood")) {
            return true;
        }
        
        if (block == Blocks.OAK_DOOR || block == Blocks.BIRCH_DOOR || block == Blocks.SPRUCE_DOOR ||
            block == Blocks.JUNGLE_DOOR || block == Blocks.ACACIA_DOOR || block == Blocks.DARK_OAK_DOOR ||
            block == Blocks.CRIMSON_DOOR || block == Blocks.WARPED_DOOR) {
            return true;
        }
        
        if (block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST || block == Blocks.BARREL ||
            block == Blocks.CRAFTING_TABLE || block == Blocks.BOOKSHELF) {
            return true;
        }
        
        return false;
    }
    
    private static boolean isStoneBlock(Block block) {
        String blockName = block.toString().toLowerCase();
        if (blockName.contains("stone") || blockName.contains("cobble")) {
            return true;
        }
        
        if (block == Blocks.COBBLESTONE || block == Blocks.STONE_BRICKS || block == Blocks.DEEPSLATE ||
            block == Blocks.BLACKSTONE || block == Blocks.BASALT || block == Blocks.OBSIDIAN) {
            return true;
        }
        
        return false;
    }
    
    private static boolean isGlassBlock(Block block) {
        String blockName = block.toString().toLowerCase();
        if (blockName.contains("glass")) {
            return true;
        }
        
        if (block == Blocks.GLASS || block == Blocks.TINTED_GLASS) {
            return true;
        }
        
        return false;
    }
    
    private static boolean isSandBlock(Block block) {
        String blockName = block.toString().toLowerCase();
        if (blockName.contains("sand")) {
            return true;
        }
        
        if (block == Blocks.SANDSTONE || block == Blocks.RED_SANDSTONE) {
            return true;
        }
        
        return false;
    }
    
    private static boolean isSnowBlock(Block block) {
        if (block == Blocks.SNOW || block == Blocks.SNOW_BLOCK || block == Blocks.POWDER_SNOW) {
            return true;
        }
        
        return false;
    }
    
    private static boolean isGrassBlock(Block block) {
        if (block == Blocks.GRASS_BLOCK || block == Blocks.TALL_GRASS || block == Blocks.GRASS) {
            return true;
        }
        
        return false;
    }
    
    private static boolean isDirtBlock(Block block) {
        if (block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL ||
            block == Blocks.MYCELIUM || block == Blocks.FARMLAND) {
            return true;
        }
        
        return false;
    }
    
    private static boolean isGravelBlock(Block block) {
        if (block == Blocks.GRAVEL) {
            return true;
        }
        
        return false;
    }
    
    private static boolean isConcreteBlock(Block block) {
        return block.toString().contains("concrete");
    }
    

}