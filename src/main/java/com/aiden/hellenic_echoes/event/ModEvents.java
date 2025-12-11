package com.aiden.hellenic_echoes.event;

import com.aiden.hellenic_echoes.HellenicEchoes;
import com.aiden.hellenic_echoes.util.ModTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HellenicEchoes.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    @SubscribeEvent
    public static void RightClickHoldingItemEvent(PlayerInteractEvent.RightClickItem event) {
        var player = event.getEntity();
        var item = event.getItemStack();
        if (item.is(ModTags.Items.DASH_PROPERTY) && !player.getCooldowns().isOnCooldown(item.getItem())) {
            player.setDeltaMovement(player.getLookAngle().scale(2));
            player.getCooldowns().addCooldown(item.getItem(), 40);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;

        // Only run on the server
        if (player.level().isClientSide) return;

        // Check the item in the main hand (adjust if you want offhand or either hand)
        ItemStack item = player.getMainHandItem();
        if (!item.is(ModTags.Items.DASH_PROPERTY)) return;
        if (!player.getCooldowns().isOnCooldown(item.getItem())) return;

        // Find living entities intersecting the player's bounding box
        for (Entity e : player.level().getEntities(player, player.getBoundingBox().inflate(1), ent -> ent != player && ent instanceof LivingEntity)) {
            LivingEntity target = (LivingEntity) e;

            // Compute damage using player's attack attribute (fallback to 2.0)
            double base = 2.0;
            if (player.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
                base = player.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
            }
            float damage = (float) (base * 1.25);

            target.hurt(player.damageSources().playerAttack(player), damage);

            player.getCooldowns().removeCooldown(item.getItem());

            // Only hit one entity per touch; remove `break` to hit all overlapping entities
            break;
        }
    }
}
