package es.meriland.core.modules.bees;

import es.meriland.core.MeriCore;
import es.meriland.core.api.data.Key;
import es.meriland.core.api.module.Module;
import es.meriland.core.api.module.ModuleInfo;

@ModuleInfo(name = "bees", description = "Bee rewards module")
public class BeesModule implements Module {

    public static Key.User<Integer> TIMES = new Key.User<>("bee_times");

    public void onInit() {
        MeriCore.get().getServer().getPluginManager().registerEvents(new BeesListener(), MeriCore.get());
    }
}
