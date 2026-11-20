package com.enderdevelopment.plugin;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TotemOfUndying implements Listener {

    private final JavaPlugin plugin;
    private final NamespacedKey totemKey;

    public TotemOfUndying(JavaPlugin plugin) {
        this.plugin = plugin;
        this.totemKey = new NamespacedKey(plugin, "totem_of_undying");
    }

    public ItemStack createTotem() {
        ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta meta = totem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6Totem of Undying");
            meta.getPersistentDataContainer().set(totemKey, PersistentDataType.BYTE, (byte) 1);
            totem.setItemMeta(meta);
        }
        return totem;
    }

    @EventHandler
    public void onPlayerHoldTotem(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());

        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.getPersistentDataContainer().has(totemKey, PersistentDataType.BYTE)) {
                // Apply beneficial effects when holding the totem
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 60, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 20 * 60, 0));
            }
        }
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            ItemStack mainHand = attacker.getInventory().getItemInMainHand();

            if (mainHand != null && mainHand.hasItemMeta()) {
                ItemMeta meta = mainHand.getItemMeta();
                if (meta != null && meta.getPersistentDataContainer().has(totemKey, PersistentDataType.BYTE)) {
                    // Apply negative effects to the attacked entity
                    if (event.getEntity() instanceof Player) {
                        Player attacked = (Player) event.getEntity();
                        attacked.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 1));
                        attacked.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 0));
                        attacked.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 5, 0));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ItemStack offHand = player.getInventory().getItemInOffHand();

            if (offHand != null && offHand.hasItemMeta()) {
                ItemMeta meta = offHand.getItemMeta();
                if (meta != null && meta.getPersistentDataContainer().has(totemKey, PersistentDataType.BYTE)) {
                    // Protect from fall and explosion damage
                    if (event.getCause() == EntityDamageEvent.DamageCause.FALL ||
                        event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION ||
                        event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}