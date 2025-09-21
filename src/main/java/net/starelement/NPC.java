package net.starelement;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.EmotePacket;
import cn.nukkit.network.protocol.PlayerSkinPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;

import java.util.Collection;
import java.util.UUID;

public class NPC {

    private Skin skin;
    private String name;
    private UUID uuid;
    private long runtimeId;
    private Vector3f vector3f;
    private Server server = Server.getInstance();
    private String level;
    private boolean isSpawned;
    private float size = 1.0f;

    protected NPC(Skin skin, String name) {
        this.skin = skin;
        this.name = name;
        this.uuid = UUID.randomUUID();
        this.runtimeId = randomRuntimeId();
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

    public void setPosition(Vector3f vector3f) {
        this.vector3f = vector3f;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void show(Player player) {
        AddPlayerPacket packet = new AddPlayerPacket();
        packet.username = name;
        packet.uuid = uuid;
        packet.entityRuntimeId = runtimeId;
        packet.entityUniqueId = runtimeId;
        packet.yaw = 0;
        packet.pitch = 0;
        packet.x = vector3f.getX();
        packet.y = vector3f.getY();
        packet.z = vector3f.getZ();
        packet.metadata = new EntityMetadata()
                .putBoolean(Entity.DATA_ALWAYS_SHOW_NAMETAG, true)
                .putFloat(Entity.DATA_SCALE, size);
        player.dataPacket(packet);
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

    public void emote(Action action) {
        EmotePacket packet = new EmotePacket();
        packet.runtimeId = runtimeId;
        packet.emoteID = action.getId();
        packet.flags = EmotePacket.FLAG_MUTE_ANNOUNCEMENT;
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
