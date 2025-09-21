package net.starelement;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityLevelChangeEvent;
import cn.nukkit.event.player.PlayerJoinEvent;

import java.util.Collection;

public class NPCListener implements Listener {

    private ElementNPC api = ElementNPC.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Collection<NPC> npcs = api.getSpawnedNPCs();
        for (NPC npc : npcs) {
            if (npc.getLevel().equals(event.getPlayer().getLevel().getName())) {
                npc.show(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onPlayerLevelChange(EntityLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            Collection<NPC> npcs = api.getSpawnedNPCs();
            for (NPC npc : npcs) {
                if (npc.getLevel().equals(event.getTarget().getName())) {
                    npc.show(player);
                } else {
                    npc.hide(player);
                }
            }
        }
    }

}
