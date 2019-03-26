package cz.neumimto.rpg.itemizerbridge;

import org.slf4j.Logger;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import javax.inject.Inject;

@Plugin(
        id = "itemizerbridge",
        name = "Itemizer-NtRpg-Bridge",
        description = "Itemizer <=> NT-Rpg bridge",
        authors = {"NeumimTo"},
        version = "0.1",
        dependencies = {
                @Dependency(id = "itemizer"),
                @Dependency(id = "nt-rpg"),
        }
)
public class ItemizerBridge {

    @Inject
    private Logger logger;


}
