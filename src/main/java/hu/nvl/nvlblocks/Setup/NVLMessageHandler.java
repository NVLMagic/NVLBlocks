package hu.nvl.nvlblocks.Setup;

import hu.nvl.nvlblocks.NVLBlocks;
import hu.nvl.nvlblocks.blocks.xpt.NVLXPTNetworkMessage;
import hu.nvl.nvlblocks.blocks.xpt.NVLXPTNetworkMessageSyncAll;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class NVLMessageHandler
{
    // https://minecraft.fandom.com/wiki/Protocol_version
    private static final String PROTOCOL_VERSION = "763";
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
        .named(new ResourceLocation(NVLBlocks.MODID,"main_channel"))
        .networkProtocolVersion(() -> PROTOCOL_VERSION)
        .clientAcceptedVersions(PROTOCOL_VERSION::equals)
        .serverAcceptedVersions(PROTOCOL_VERSION::equals)
        .simpleChannel();

    public static void init() {
        INSTANCE.registerMessage(0, NVLXPTNetworkMessage.class, NVLXPTNetworkMessage::encode, NVLXPTNetworkMessage::decode, NVLXPTNetworkMessage::handle);
        INSTANCE.registerMessage(1, NVLXPTNetworkMessageSyncAll.class, NVLXPTNetworkMessageSyncAll::encode, NVLXPTNetworkMessageSyncAll::decode, NVLXPTNetworkMessageSyncAll::handle);
    }

    public static void sendToServer(Object msg) {
        INSTANCE.sendToServer(msg);
    }
    public static void sendToAllClients(Object msg) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }

}
