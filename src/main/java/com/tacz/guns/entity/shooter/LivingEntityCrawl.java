package com.tacz.guns.entity.shooter;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;

public class LivingEntityCrawl {
    private final LivingEntity shooter;
    private final ShooterDataHolder data;

    public LivingEntityCrawl(LivingEntity shooter, ShooterDataHolder data) {
        this.shooter = shooter;
        this.data = data;
    }

    public void crawl(boolean isCrawl) {
        data.isCrawling = isCrawl;
    }

    public void tickCrawling() {
        if (shooter.isSpectator() || shooter.isPassenger() || shooter.isSwimming() || (shooter.fallDistance>0.75)) {
            data.isCrawling = false;
            this.setCrawlPose();
            return;
        }
        this.setCrawlPose();
    }

    private void setCrawlPose() {
        if (data.isCrawling) {
            if (shooter instanceof Player player) {
                player.setForcedPose(Pose.SWIMMING);
            } else {
                this.shooter.setPose(Pose.SWIMMING);
            }
        } else {
            if (shooter instanceof Player player) {
                player.setForcedPose(null);
            }
        }
    }
}
