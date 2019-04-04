package ru.glassspirit.customnpcsbridge;

import cz.neumimto.rpg.Log;
import cz.neumimto.rpg.NtRpgPlugin;
import cz.neumimto.rpg.configuration.DebugLevel;
import cz.neumimto.rpg.players.CharacterService;
import cz.neumimto.rpg.players.ExperienceSources;
import cz.neumimto.rpg.players.IActiveCharacter;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.constants.EntityType;
import noppes.npcs.api.event.NpcEvent;
import noppes.npcs.api.event.QuestEvent;

import java.util.UUID;

public class CustomNPCsEventListener {

    private CharacterService characterService;

    public CustomNPCsEventListener() {
        if (NpcAPI.IsAvailable()) {
            NpcAPI.Instance().events().register(this);
            characterService = NtRpgPlugin.GlobalScope.characterService;
        }
    }

    @SubscribeEvent
    public void onNpcQuestCompletion(QuestEvent.QuestTurnedInEvent event) {
        if (CustomNPCsBridge.configuration.QUESTS_EXP_RPG) {
            IActiveCharacter character = characterService.getCharacter(UUID.fromString(event.player.getUUID()));
            if (character != null && !character.isStub()) {
                characterService.addExperiences(character, event.expReward, ExperienceSources.QUESTING);
                Log.info(String.format("Adding %s experience to %s for completing quest \"%s\"",
                        event.expReward,
                        character.getPlayer().getName(),
                        event.quest.getName()),
                        DebugLevel.BALANCE);
            }
        }
        if (!CustomNPCsBridge.configuration.QUESTS_EXP_MINECRAFT) {
            event.expReward = 0;
        }
    }

    @SubscribeEvent
    public void onNpcDeath(NpcEvent.DiedEvent event) {
        if (CustomNPCsBridge.configuration.NPC_KILLS_EXP_RPG) {
            if (event.damageSource.getTrueSource().typeOf(EntityType.PLAYER)) {
                IActiveCharacter character = characterService.getCharacter(UUID.fromString(event.damageSource.getTrueSource().getUUID()));
                if (character != null && !character.isStub()) {
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
    }


}
