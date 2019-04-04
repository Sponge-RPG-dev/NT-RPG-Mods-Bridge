package ru.glassspirit.customnpcsbridge;

import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
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
        version = "@VERSION@",
        authors = {"GlassSpirit"},
        dependencies = {
                @Dependency(id = "nt-rpg"),
                @Dependency(id = "customnpcs")
        }
)
public class CustomNPCsBridge {

    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> config;

    public static Configuration configuration;

    @Listener
    public void onGameInitialization(GameInitializationEvent event) {
        configuration = new Configuration();
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
            new CustomNPCsEventListener();
            logger.info("CustomNPCs found! Event listener registered.");
        } catch (ClassNotFoundException e) {
            logger.error("CustomNPCs not found!", e);
        }
    }

    private void loadConfig() {
        try {
            ObjectMapper.BoundInstance configMapper = ObjectMapper.forObject(configuration);
            CommentedConfigurationNode node = config.load();
            configMapper.serialize(node);
            config.save(node);
        } catch (Exception e) {
            logger.error("Could not load config", e);
        }
    }

}
