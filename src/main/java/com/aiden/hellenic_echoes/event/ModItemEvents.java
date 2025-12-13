package com.aiden.hellenic_echoes.event;

import com.aiden.hellenic_echoes.HellenicEchoes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = HellenicEchoes.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModItemEvents {

    private static final UUID CUSTOM_MOON_DAMAGE_UUID = UUID.fromString("e1a1f3c4-5b6d-4f7e-9a2b-3c4d5e6f7a8b");

    private static boolean isNight(Level level) {
        long time = level.getDayTime() % 24000L;
        // Night roughly between 13000 (sunset) and 23000 (sunrise-ish). Adjust if you want different thresholds.
        return time >= 13000L && time < 23000L;
    }

    @SubscribeEvent
    public static void RightClickHoldingItemEvent(PlayerInteractEvent.RightClickItem event) {
        var player = event.getEntity();
        var item = event.getItemStack();
        if (!item.hasTag()) return;
        if (item.getTag().getString("AetherTag").equals("aether_dash") && !player.getCooldowns().isOnCooldown(item.getItem())) {
            player.setDeltaMovement(player.getLookAngle().scale(2));
            player.getCooldowns().addCooldown(item.getItem(), 30);

            player.getPersistentData().putInt("HellenicDashTicks", 10);
        }
    }

    @SubscribeEvent
    public static void LeftClickOnMob(AttackEntityEvent event) {
        var player = event.getEntity();
        var target = event.getTarget();
        ItemStack item = player.getMainHandItem();
        if (!(target instanceof LivingEntity)) return;
        if (!item.hasTag()) return;
        if (item.getTag().getString("AetherTag").equals("greed") && !player.getCooldowns().isOnCooldown(item.getItem())) {
            double playerHealth = player.getHealth();
            double maxPlayerHealth = player.getMaxHealth();
            double targetHealth = ((LivingEntity) target).getHealth();
            if (playerHealth < maxPlayerHealth){
                player.setHealth(Math.min((float)(playerHealth + targetHealth * 0.1f), (float)maxPlayerHealth));
                if (player.level().isClientSide) {
                    ModClientOverlays.triggerOverlay(10);
                }
            }
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

        handleDash(player);
        handleMoonsPower(player, item);

    }

    private static void handleMoonsPower(Player player, ItemStack item) {
        if (item == null || !item.hasTag()) return;
        if (!"moons_power".equals(item.getTag().getString("AetherTag"))) return;

        double desired = isNight(player.level()) ? 3.0 : 0.0;
        double current = item.getTag().contains("MoonDamageBoost") ? item.getTag().getDouble("MoonDamageBoost") : Double.NaN;

        if (Double.compare(current, desired) != 0) {
            item.getOrCreateTag().putDouble("MoonDamageBoost", desired);
        }
    }

    @SubscribeEvent
    public static void onAttributeModifiers(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();

        if (stack != null && stack.hasTag() && stack.getTag().contains("MoonDamageBoost")
                && event.getSlotType() == EquipmentSlot.MAINHAND) {

            double bonus = stack.getTag().getDouble("MoonDamageBoost");

            AttributeModifier typeModifier = new AttributeModifier(
                    CUSTOM_MOON_DAMAGE_UUID,
                    "Custom Attack Damage", // Name for the modifier
                    bonus, // Amount to add (e.g., +5 attack damage)
                    AttributeModifier.Operation.ADDITION // Operation (ADD_VALUE, MULTIPLY_BASE, etc.)
            );

            event.addModifier(Attributes.ATTACK_DAMAGE, typeModifier);

            AttributeModifier modifier = new AttributeModifier(
                    CUSTOM_MOON_DAMAGE_UUID,
                    "Custom Attack Damage", // Name for the modifier
                    bonus, // Amount to add (e.g., +5 attack damage)
                    AttributeModifier.Operation.ADDITION // Operation (ADD_VALUE, MULTIPLY_BASE, etc.)
            );

            event.addModifier(Attributes.ATTACK_DAMAGE, modifier);
        }
    }

    private static void handleDash(Player player) {
        int dashTicks = player.getPersistentData().getInt("HellenicDashTicks");

        if (dashTicks > 0) {
            player.getPersistentData().putInt("HellenicDashTicks", dashTicks - 1);

            // Only check damage during active dash window
            handleDashDamage(player);
        }
    }

    private static void handleDashDamage(Player player) {
        // Still in active dash state?
        int dashTicks = player.getPersistentData().getInt("HellenicDashTicks");
        if (dashTicks <= 0) return;

        ItemStack item = player.getMainHandItem();
        if (!item.hasTag()) return;
        if (!"aether_dash".equals(item.getTag().getString("AetherTag"))) return;

        // Check collision with mobs
        for (Entity e : player.level().getEntities(player, player.getBoundingBox().inflate(1),
                ent -> ent instanceof LivingEntity && ent != player)) {

            LivingEntity target = (LivingEntity) e;

            double base = player.getAttribute(Attributes.ATTACK_DAMAGE) != null
                    ? player.getAttribute(Attributes.ATTACK_DAMAGE).getValue()
                    : 2.0;
            float damage = (float) (base * 1.25);

            target.hurt(player.damageSources().playerAttack(player), damage);

            // Dash window ends after hitting a mob
            player.getPersistentData().putInt("HellenicDashTicks", 0);

            break;
        }
    }

}
