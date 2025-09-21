package net.starelement;

import cn.nukkit.Player;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.plugin.PluginBase;

import java.util.Collection;
import java.util.HashMap;

public class ElementNPC extends PluginBase {

    private static ElementNPC instance;
    private HashMap<Long, NPC> npcs = new HashMap<>();

    public static ElementNPC getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }


    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new NPCListener(), this);
    }

    public NPC createNPC(Player player) {
        return this.createNPC(player.getDisplayName(), player.getSkin());
    }

    public NPC createNPC(String name, Skin skin) {
        NPC npc = new NPC(skin, name);
        npcs.put(npc.getRuntimeId(), npc);
        return npc;
    }

    public NPC createNPC(String name) {
        return this.createNPC(name, new Skin());
    }

    public Collection<NPC> getNPCs() {
        return npcs.values();
    }

    public Collection<NPC> getSpawnedNPCs() {
        return npcs.values().stream().filter(NPC::isSpawned).toList();
    }

    public void removeNPC(NPC npc) {
        npcs.remove(npc.getRuntimeId());
    }

    public void removeNPC(long id) {
        npcs.remove(id);
    }
}
