package cz.neumimto.rpg.itemizerbridge.serializers;


import com.google.common.reflect.TypeToken;
import cz.neumimto.rpg.effects.EffectParams;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;

public class EffectMapSerializer implements TypeSerializer<Map<String, EffectParams>> {

    @Nullable
    @Override
    public Map<String, EffectParams> deserialize(@NonNull TypeToken type, @NonNull ConfigurationNode value) throws ObjectMappingException {
        Map<String, EffectParams> map = new HashMap<>();

        Map<Object, ? extends ConfigurationNode> childrenMap = value.getChildrenMap();

        for (Map.Entry<Object, ? extends ConfigurationNode> entry : childrenMap.entrySet()) {
            String effectName = (String) entry.getKey();
            ConfigurationNode val = entry.getValue();
            Map<String, String> value1 = (Map<String, String>) val.getValue();
            map.put(effectName, new EffectParams(value1));
        }

        return map;
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable Map<String, EffectParams> obj, @NonNull ConfigurationNode value) throws ObjectMappingException {

    }
}
