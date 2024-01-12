package hu.nvl.nvlblocks.Setup;

import hu.nvl.nvlblocks.components.NVLToolsTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class Registration {
    public static void Init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        NVLItemRegistry.register(bus);
        NVLBlockRegistry.register(bus);
        NVLBlockEntityRegistry.register(bus);
        NVLBlockMenuRegistry.register(bus);
        NVLSoundRegistry.register(bus);
        NVLToolsTab.register(bus);
    }
}
