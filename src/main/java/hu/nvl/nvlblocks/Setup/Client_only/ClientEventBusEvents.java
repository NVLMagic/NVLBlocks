package hu.nvl.nvlblocks.Setup.Client_only;

import hu.nvl.nvlblocks.NVLBlocks;
import hu.nvl.nvlblocks.Setup.NVLBlockEntityRegistry;
import hu.nvl.nvlblocks.blocks.hs.NVLHoloSignBlockEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NVLBlocks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusEvents {
    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(NVLBlockEntityRegistry.BE_HS.get(), NVLHoloSignBlockEntityRenderer::new);
    }

}
