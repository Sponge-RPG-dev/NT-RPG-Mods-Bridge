package ru.glassspirit.customnpcsbridge;

import com.google.inject.Inject;
import cz.neumimto.core.ioc.IoC;
import cz.neumimto.rpg.Log;
import cz.neumimto.rpg.configuration.DebugLevel;
import cz.neumimto.rpg.players.CharacterService;
import cz.neumimto.rpg.players.ExperienceSources;
import cz.neumimto.rpg.players.IActiveCharacter;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.constants.EntityType;
import noppes.npcs.api.event.NpcEvent;
import noppes.npcs.api.event.QuestEvent;
import org.slf4j.Logger;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import java.util.UUID;

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

    private CharacterService characterService;

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
            if (NpcAPI.IsAvailable()) {
                NpcAPI.Instance().events().register(this);
                characterService = IoC.get().build(CharacterService.class);
                logger.info("Registering CustomNPCs event listener");
            }
        } catch (ClassNotFoundException e) {
            logger.error("CustomNPCs not found!", e);
        }
    }

    @SubscribeEvent
    public void onNpcQuestCompletion(QuestEvent.QuestTurnedInEvent event) {
        IActiveCharacter character = characterService.getCharacter(UUID.fromString(event.player.getUUID()));
        if (character != null) {
            characterService.addExperiences(character, event.expReward, ExperienceSources.QUESTING);
            Log.info(String.format("Adding %s experience to %s for completing quest \"%s\"",
                    event.expReward,
                    character.getPlayer().getName(),
                    event.quest.getName()),
                    DebugLevel.BALANCE);
        }
    }

    @SubscribeEvent
    public void onNpcDeath(NpcEvent.DiedEvent event) {
        if (event.damageSource.getTrueSource().typeOf(EntityType.PLAYER)) {
            IActiveCharacter character = characterService.getCharacter(UUID.fromString(event.damageSource.getTrueSource().getUUID()));
            if (character != null) {
                int experience = event.npc.getInventory().getExpRNG();
                characterService.addExperiences(character, experience, ExperienceSources.PVE);
                Log.info(String.format("Adding %s experience to %s for killing npc \"%s\"",
                        experience,
                        character.getPlayer().getName(),
                        event.npc.getName()),
                        DebugLevel.BALANCE);
            }
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
