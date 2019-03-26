package cz.neumimto.rpg.itemizerbridge.nbt;

import com.onaple.itemizer.data.beans.ItemNbtFactory;
import cz.neumimto.config.blackjack.and.hookers.annotations.CustomAdapter;
import cz.neumimto.rpg.effects.EffectParams;
import cz.neumimto.rpg.inventory.data.NKeys;
import cz.neumimto.rpg.inventory.data.manipulators.EffectsData;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;

import java.util.Map;

@ConfigSerializable
public class CustomEnchantment implements ItemNbtFactory {

    @Setting("effects")
    private Map<String, EffectParams> effects;

    @Override
    public Key getKey() {
        return NKeys.ITEM_EFFECTS;
    }

    @Override
    public DataManipulator<?, ?> constructDataManipulator() {
        return new EffectsData(effects);
    }
}
