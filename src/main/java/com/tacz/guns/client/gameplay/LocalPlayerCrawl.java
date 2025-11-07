package com.tacz.guns.client.gameplay;

import com.tacz.guns.network.NetworkHandler;
import com.tacz.guns.network.message.ClientMessagePlayerCrawl;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Pose;

public class LocalPlayerCrawl {
    /**
     * 冷却时间为 10 tick
     */
    private static final int COOLDOWN_TICKS = 4;
    private final LocalPlayer player;
    private boolean isCrawling = false;
    private int crawCooldownTicks = 0;

    public LocalPlayerCrawl(LocalPlayer player) {
        this.player = player;
    }

    public void crawl(boolean isCrawl) {
        if (crawCooldownTicks > 0) {
            return;
        }
        if (player.isSpectator() || player.isPassenger() || !player.onGround()) {
            return;
        }
        this.isCrawling = isCrawl;
        this.crawCooldownTicks = COOLDOWN_TICKS;
        NetworkHandler.CHANNEL.sendToServer(new ClientMessagePlayerCrawl(isCrawl));
    }

    public void tickCrawl() {
        if (crawCooldownTicks > 0) {
            crawCooldownTicks--;
        }
        if (player.isSpectator() || player.isPassenger() || player.isSwimming() || (player.fallDistance>=0.6)) {
            isCrawling = false;
            this.setCrawlPose();
            return;
        }
        this.setCrawlPose();
    }

    public boolean isCrawling() {
        return isCrawling;
    }

    private void setCrawlPose() {
        if (isCrawling) {
            player.setForcedPose(Pose.SWIMMING);
        } else {
            player.setForcedPose(null);
        }
    }
}
