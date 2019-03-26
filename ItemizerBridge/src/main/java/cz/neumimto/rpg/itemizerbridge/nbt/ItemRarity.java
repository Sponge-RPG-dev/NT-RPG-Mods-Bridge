package cz.neumimto.rpg.itemizerbridge.nbt;

import com.onaple.itemizer.data.beans.ItemNbtFactory;
import cz.neumimto.rpg.inventory.data.NKeys;
import cz.neumimto.rpg.inventory.data.manipulators.ItemRarityData;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;

@ConfigSerializable
public class ItemRarity implements ItemNbtFactory {

    @Override
    public Key getKey() {
        return NKeys.ITEM_RARITY;
    }

    @Override
    public DataManipulator<?, ?> constructDataManipulator() {

        return new ItemRarityData();
    }
}
