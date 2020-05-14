package es.meriland.core.modules.bees;

import es.meriland.core.MeriCore;
import es.meriland.core.api.user.MeriUser;
import es.meriland.core.api.user.UserRank;
import es.meriland.core.modules.item.ItemMethods;
import es.meriland.core.util.Cooldown;
import es.meriland.core.util.Metodos;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BeesListener implements Listener {

    private static Cooldown cd = new Cooldown(30);
    private static final String _tag = Metodos.colorizar("&7[&5Dama Abeja&7]&r");

    @EventHandler
    public void onBeeClick(PlayerInteractEntityEvent event) {
        MeriUser user = MeriCore.get().getUserManager().getUser(event.getPlayer());

        if (!user.getGroup().equals(UserRank.Invitado)) {

            if(event.getHand().equals(EquipmentSlot.HAND)) {

                if (event.getRightClicked().getScoreboardTags().contains("bees")) {
                    event.setCancelled(true);

                    if(cd.isCoolingDown(user.getPlayer())) {
                        switch (user.get(BeesModule.TIMES).orElse(7)) {
                            case 4:
                                user.sendTagMessage(_tag, "*beeRewards.aggro.4");
                                break;
                            case 3:
                                user.sendTagMessage(_tag, "*beeRewards.aggro.3");
                                break;
                            case 2:
                                user.sendTagMessage(_tag, "*beeRewards.aggro.2");
                                break;
                            case 1:
                                user.sendTagMessage(_tag, "*beeRewards.aggro.1");
                                user.getPlayer().damage(2, event.getRightClicked());
                                break;
                            case 0:
                                user.sendTagMessage(_tag, "*beeRewards.aggro.0");
                                user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, 450, 1));
                                break;
                            default:
                                user.sendTagMessage(_tag, "*beeRewards.cooldown");
                        }
                        user.offer(BeesModule.TIMES, user.get(BeesModule.TIMES).orElse(7) - 1);
                    } else {
                        Inventory inv = user.getPlayer().getInventory();
                        int contained = ItemMethods.getRegularContainedAmount(inv, Material.HONEY_BLOCK);
                        if (contained < 64) {
                            user.sendTagMessage(_tag, "*beeRewards.notEnough");
                            cd.setOnCooldown(user.getPlayer());
                            user.offer(BeesModule.TIMES, 7);
                        } else {
                            user.sendTagMessage(_tag, "*beeRewards.success");
                            ItemMethods.removeRegular(inv, Material.HONEY_BLOCK, 64);
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "loot give " + user.getName() + " loot meriland:entities/abeja");
                        }
                    }
                }
            }
        }
    }
}
