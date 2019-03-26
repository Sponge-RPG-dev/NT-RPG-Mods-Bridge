package cz.neumimto.rpg.itemizerbridge.nbt;

import com.onaple.itemizer.data.beans.ItemNbtFactory;
import cz.neumimto.rpg.inventory.data.NKeys;
import cz.neumimto.rpg.inventory.data.manipulators.ItemLevelData;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;

@ConfigSerializable
public class ItemLevel implements ItemNbtFactory {

    @Setting("level")
    private int level;

    @Override
    public Key getKey() {
        return NKeys.ITEM_LEVEL;
    }

    @Override
    public DataManipulator<?, ?> constructDataManipulator() {
        return new ItemLevelData(level);
    }

}
