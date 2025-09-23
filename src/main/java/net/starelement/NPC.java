package net.starelement;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.network.protocol.*;

import java.util.Collection;
import java.util.UUID;

public class NPC {

    private Skin skin;
    private String name;
    private UUID uuid;
    private long runtimeId;
    private Position position;
    private Server server = Server.getInstance();
    private String level;
    private boolean isSpawned;
    private float size = 1.0f;
    private EntityMetadata metadata;

    protected NPC(Skin skin, String name) {
        this.skin = skin;
        this.name = name;
        this.uuid = UUID.randomUUID();
        this.runtimeId = randomRuntimeId();
        this.metadata = new EntityMetadata();
        this.metadata.putBoolean(Entity.DATA_ALWAYS_SHOW_NAMETAG, true);
        this.metadata.putFloat(Entity.DATA_SCALE, size);
    }

    public void setMetadata(EntityMetadata metadata) {
        this.metadata = metadata;
    }

    public long getRuntimeId() {
        return runtimeId;
    }

    public String getName() {
        return name;
    }

    public Skin getSkin() {
        return skin;
    }

    public String getLevel() {
        return level;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
        if (isSpawned) {
            PlayerSkinPacket packet = new PlayerSkinPacket();
            packet.skin = skin;
            packet.uuid = uuid;
            packet.newSkinName = "Standard_Custom";
            packet.oldSkinName = "Standard_Custom";
            dataPacket(packet);
        }
    }

    public void show(Player player) {
        AddPlayerPacket packet = new AddPlayerPacket();
        packet.username = name;
        packet.uuid = uuid;
        packet.entityRuntimeId = runtimeId;
        packet.entityUniqueId = runtimeId;
        packet.yaw = 0;
        packet.pitch = 0;
        packet.x = (float) position.getX();
        packet.y = (float) position.getY();
        packet.z = (float) position.getZ();
        packet.metadata = metadata;
        player.dataPacket(packet);
        if (skin != null) {
            PlayerSkinPacket sp = new PlayerSkinPacket();
            sp.skin = skin;
            sp.uuid = uuid;
            sp.newSkinName = "Standard_Custom";
            sp.oldSkinName = "Standard_Custom";
            player.dataPacket(sp);
        }
    }

    public void hide(Player player) {
        RemoveEntityPacket packet = new RemoveEntityPacket();
        packet.eid = runtimeId;
        Collection<Player> players = server.getOnlinePlayers().values();
        player.dataPacket(packet);
    }

    public boolean isSpawned() {
        return isSpawned;
    }

    public void spawn(Level level) {
        this.level = level.getName();
        isSpawned = true;
        for (Player player : server.getOnlinePlayers().values()) {
            if (player.getLevel().getName().equals(level.getName())) {
                show(player);
            }
        }
    }

    public void destroy() {
        isSpawned = false;
        for (Player player : server.getOnlinePlayers().values()) {
            hide(player);
        }
    }

    public void emote(Emote emote, Player player) {
        EmotePacket packet = new EmotePacket();
        packet.runtimeId = runtimeId;
        packet.emoteID = emote.getId();
        packet.flags = EmotePacket.FLAG_MUTE_ANNOUNCEMENT;
        if (player == null) {
            dataPacket(packet);
        } else {
            player.dataPacket(packet);
        }
    }

    public void emote(Emote emote) {
        emote(emote, null);
    }

    public void action(AnimatePacket.Action action) {
        AnimatePacket packet = new AnimatePacket();
        packet.action = action;
        packet.eid = runtimeId;
        dataPacket(packet);
    }

    public void dataPacket(DataPacket packet) {
        for (Player player : getLevelPlayers()) {
            player.dataPacket(packet);
        }
    }

    public Collection<Player> getLevelPlayers() {
        return server.getOnlinePlayers().values().stream().filter(player -> player.getLevel().getName().equals(level)).toList();
    }

    private long randomRuntimeId() {
        return (long) (Math.random() * 1000000000000000000L);
    }

}
