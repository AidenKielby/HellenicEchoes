package com.aiden.hellenic_echoes.event;

import com.aiden.hellenic_echoes.HellenicEchoes;
import com.aiden.hellenic_echoes.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = HellenicEchoes.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModArmorEvents {

    private static final UUID CUSTOM_MOON_DAMAGE_UUID = UUID.fromString("e1a1f3c4-5b6d-4f7e-9a2b-3c4d5e6f7a8b");
    private static boolean hadeffect = false;

    private static boolean isNight(Level level) {
        long time = level.getDayTime() % 24000L;
        // Night roughly between 13000 (sunset) and 23000 (sunrise-ish). Adjust if you want different thresholds.
        return time >= 13000L && time < 23000L;
    }

    private static boolean lightLevelIsLow(Level level, Entity entity) {

        BlockPos feetPos = entity.blockPosition();
        Vec3 eyeVec = entity.getEyePosition(1.0F);
        BlockPos eyePos = BlockPos.containing(eyeVec);
        // Check the light level at the entity's position
        int blockLightFeet = level.getBrightness(LightLayer.BLOCK, feetPos);
        int skyLightFeet   = level.getBrightness(LightLayer.SKY, feetPos);
        int blockLightEye  = level.getBrightness(LightLayer.BLOCK, eyePos);
        int skyLightEye    = level.getBrightness(LightLayer.SKY, eyePos);

        int maxLight = Math.max(Math.max(blockLightFeet, skyLightFeet),
                Math.max(blockLightEye,  skyLightEye));
        return maxLight <= 12;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;

        // Only run on the server
        if (player.level().isClientSide) return;

        // Check the item in the main hand (adjust if you want offhand or either hand)
        ItemStack item = player.getMainHandItem();
        handleApolloSight(item, player);

    }

    private static void handleApolloSight(ItemStack item, Player player) {
        if (lightLevelIsLow(player.level(), player)) {
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            if (!helmet.hasTag()) return;
            if (helmet != null && helmet.getTag().getString("AetherTag").equals("apollos_sight")) {
                player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                        net.minecraft.world.effect.MobEffects.NIGHT_VISION,
                        500, // Duration in ticks (10 seconds)
                        0,   // Amplifier level (0 = level 1)
                        false, // Ambient
                        false  // Show particles
                ));
                hadeffect = true;
            }
        }
        else{
            if (hadeffect) {
                player.removeEffect(net.minecraft.world.effect.MobEffects.NIGHT_VISION);
                hadeffect = false;
            }
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

}
