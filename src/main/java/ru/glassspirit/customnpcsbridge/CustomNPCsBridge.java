package ru.glassspirit.customnpcsbridge;

import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "nt-rpg-customnpcs-bridge",
        name = "NT-RPG CustomNPCs Bridge",
        description = "Bridge plugin that connects NT-RPG plugin and CustomNPCs mod",
        version = "0.1",
        authors = {"GlassSpirit"},
        dependencies = {
                @Dependency(id = "nt-rpg")
        }
)
public class CustomNPCsBridge {

    public static final String QUESTS_EXP_NODE = "quests_exp";
    public static final String NPC_KILLS_EXP_NODE = "npc_kills_exp";

    public static boolean questsGiveExp;
    public static boolean npcKillsGiveExp;

    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> config;

    @Listener
    public void onGameInitialization(GameInitializationEvent event) {
        loadConfig();
    }

    @Listener
    public void onGameReload(GameReloadEvent event) {
        loadConfig();
    }

    @Listener
    public void onGameAboutToStartServer(GameAboutToStartServerEvent event) {
        try {
            Class.forName("noppes.npcs.api.NpcAPI");
            new CustomNPCsEventListener().setup();
            logger.info("CustomNPCs event listener registered!");
        } catch (ClassNotFoundException e) {
            logger.error("CustomNPCs not found!", e);
        }
    }

    private void loadConfig() {
        try {
            CommentedConfigurationNode node = config.load();
            if (node.getNode(QUESTS_EXP_NODE).isVirtual()) {
                node.getNode(QUESTS_EXP_NODE).setComment("Should players get experience for completing quests?");
                node.getNode(QUESTS_EXP_NODE).setValue(true);
            }
            if (node.getNode(NPC_KILLS_EXP_NODE).isVirtual()) {
                node.getNode(NPC_KILLS_EXP_NODE).setComment("Should players get experience for killing NPCs?");
                node.getNode(NPC_KILLS_EXP_NODE).setValue(true);
            }
            config.save(node);

            questsGiveExp = node.getNode(QUESTS_EXP_NODE).getBoolean(true);
            npcKillsGiveExp = node.getNode(NPC_KILLS_EXP_NODE).getBoolean(true);
        } catch (Exception e) {
            logger.error("Could not load config", e);
        }
    }

}
