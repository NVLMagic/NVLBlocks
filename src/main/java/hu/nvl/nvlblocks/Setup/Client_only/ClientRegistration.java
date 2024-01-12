package hu.nvl.nvlblocks.Setup.Client_only;

import hu.nvl.nvlblocks.Setup.NVLBlockMenuRegistry;
import hu.nvl.nvlblocks.Setup.NVLBlockRegistry;
import hu.nvl.nvlblocks.Setup.NVLItemRegistry;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientRegistration {
    public static void init(final FMLClientSetupEvent event) {
        //Screens
        event.enqueueWork(NVLBlockMenuRegistry::registerScreens);
    }
    public static void AddCreative(BuildCreativeModeTabContentsEvent event) {
        NVLItemRegistry.AddCreative(event);
        NVLBlockRegistry.AddCreative(event);
    }
}
