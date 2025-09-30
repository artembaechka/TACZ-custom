package com.tacz.guns.util;

import com.tacz.guns.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BulletHitEffects {
    
    public static void playHitSound(Level level, Vec3 hitPos, BlockState blockState, BlockPos blockPos) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        
        float basePitch = 0.8f + serverLevel.getRandom().nextFloat() * 0.4f;
        float baseVolume = 0.8f + serverLevel.getRandom().nextFloat() * 0.2f;
        
        boolean hasWaterAbove = hasWaterAbove(level, blockPos);
        if (hasWaterAbove) {
            basePitch *= 0.6f;
            baseVolume *= 0.4f;
        }
        
        SoundEvent blockSound = getBlockSound(blockState.getBlock());
        if (blockSound != null) {
            serverLevel.playSound(null, hitPos.x, hitPos.y, hitPos.z, blockSound, SoundSource.BLOCKS, baseVolume * 0.7f, basePitch);
        }
        
        SoundEvent bulletSound = getBulletSound(blockState.getBlock());
        serverLevel.playSound(null, hitPos.x, hitPos.y, hitPos.z, bulletSound, SoundSource.BLOCKS, baseVolume, basePitch);
    }
    
    public static void playHitSound(Level level, Vec3 hitPos, BlockState blockState, boolean isUnderwater) {
        BlockPos blockPos = new BlockPos((int) hitPos.x, (int) hitPos.y, (int) hitPos.z);
        playHitSound(level, hitPos, blockState, blockPos);
    }
    
    public static void spawnHitParticles(Level level, Vec3 hitPos, BlockState blockState, Direction hitDirection) {
        if (!(level instanceof ServerLevel serverLevel) || blockState.isAir()) {
            return;
        }
        
        BlockParticleOption particleData = new BlockParticleOption(ParticleTypes.BLOCK, blockState);
        Vec3 dir = new Vec3(hitDirection.getStepX(), hitDirection.getStepY(), hitDirection.getStepZ());
        
        for (int i = 0; i < 4; i++) {
            Vec3 velocity = randomVec(dir, 25);
            Vec3 spawnPos = hitPos.add(dir.scale(0.03 * i));
            
            serverLevel.sendParticles(particleData,
                    spawnPos.x, spawnPos.y, spawnPos.z,
                    1, velocity.x * 0.3, velocity.y * 0.3, velocity.z * 0.3, 0.1);
        }
    }
    
    public static void spawnWaterHitEffects(Level level, Vec3 hitPos) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        
        for (int i = 0; i < 8; i++) {
            Vec3 velocity = randomVec(new Vec3(0, 1, 0), 30);
            serverLevel.sendParticles(ParticleTypes.SPLASH,
                    hitPos.x + (serverLevel.getRandom().nextDouble() - 0.5) * 0.4,
                    hitPos.y,
                    hitPos.z + (serverLevel.getRandom().nextDouble() - 0.5) * 0.4,
                    1, velocity.x * 0.3, velocity.y * 0.4, velocity.z * 0.3, 0.1);
        }
        
        for (int i = 0; i < 4; i++) {
            Vec3 velocity = randomVec(new Vec3(0, 1, 0), 15);
            serverLevel.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP,
                    hitPos.x + (serverLevel.getRandom().nextDouble() - 0.5) * 0.2,
                    hitPos.y,
                    hitPos.z + (serverLevel.getRandom().nextDouble() - 0.5) * 0.2,
                    1, velocity.x * 0.1, velocity.y * 0.2, velocity.z * 0.1, 0.05);
        }
        
        float pitch = 0.9f + serverLevel.getRandom().nextFloat() * 0.2f;
        float volume = 0.8f + serverLevel.getRandom().nextFloat() * 0.2f;
        serverLevel.playSound(null, hitPos.x, hitPos.y, hitPos.z, ModSounds.BULLET_HIT_WATER.get(), SoundSource.BLOCKS, volume, pitch);
    }
    
    private static SoundEvent getBlockSound(Block block) {
        if (block == Blocks.WATER) {
            return null;
        } else if (isMetalBlock(block)) {
            return SoundEvents.ANVIL_HIT;
        } else if (block.toString().toLowerCase().contains("glass")) {
            return SoundEvents.GLASS_BREAK;
        } else if (block.toString().toLowerCase().contains("wood") || 
                   block.toString().toLowerCase().contains("plank") ||
                   block.toString().toLowerCase().contains("log")) {
            return SoundEvents.WOOD_HIT;
        } else if (block.toString().toLowerCase().contains("stone") ||
                   block.toString().toLowerCase().contains("cobble")) {
            return SoundEvents.STONE_HIT;
        } else {
            return SoundEvents.STONE_HIT;
        }
    }
    
    private static SoundEvent getBulletSound(Block block) {
        if (block == Blocks.WATER) {
            return ModSounds.BULLET_HIT_WATER.get();
        } else if (isMetalBlock(block)) {
            return ModSounds.BULLET_HIT_METAL.get();
        } else {
            return ModSounds.BULLET_HIT_GENERIC.get();
        }
    }
    
    private static boolean isMetalBlock(Block block) {
        return block == Blocks.IRON_BLOCK || block == Blocks.IRON_DOOR ||
               block == Blocks.IRON_TRAPDOOR || block == Blocks.IRON_BARS ||
               block == Blocks.GOLD_BLOCK || block == Blocks.COPPER_BLOCK ||
               block == Blocks.ANVIL || block == Blocks.CHAIN ||
               block.toString().toLowerCase().contains("iron") ||
               block.toString().toLowerCase().contains("metal");
    }
    
    private static boolean hasWaterAbove(Level level, BlockPos blockPos) {
        for (int y = 1; y <= 1; y++) {
            BlockPos checkPos = blockPos.above(y);
            BlockState checkState = level.getBlockState(checkPos);
            if (checkState.getBlock() == Blocks.WATER) {
                return true;
            }
            if (!checkState.isAir()) {
                break;
            }
        }
        return false;
    }
    
    private static Vec3 randomVec(Vec3 baseVec, double spread) {
        double spreadRad = Math.toRadians(spread);
        return baseVec.normalize().add(
                (Math.random() - 0.5) * spreadRad,
                (Math.random() - 0.5) * spreadRad,
                (Math.random() - 0.5) * spreadRad
        );
    }
}