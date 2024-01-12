package hu.nvl.nvlblocks.blocks.xpt;
// ------------------------------------------------------------------------------------------------
// Network packet for full data sync from server to all clients
// ------------------------------------------------------------------------------------------------
import hu.nvl.nvlblocks.NVLBlocks;
import io.netty.buffer.ByteBufUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class NVLXPTNetworkMessageSyncAll {
    private BlockPos pos;
    private String privateAmount;
    private long commonAmount;
    public NVLXPTNetworkMessageSyncAll(BlockPos p, String pxp, long cxp) {
        this.pos = p;
        this.privateAmount = pxp;
        this.commonAmount = cxp;
    }
    public static NVLXPTNetworkMessageSyncAll decode (FriendlyByteBuf buf) {
//        NVLBlocks.nvlLogger.logLine("NVLXPTMessageHandler","decode");
        try {
            int x = buf.readInt();
            int y = buf.readInt();
            int z = buf.readInt();
            long cxp = buf.readLong();
            String pxp = buf.readUtf();
            return new NVLXPTNetworkMessageSyncAll(new BlockPos(x, y, z), pxp, cxp);
        }
        catch (IndexOutOfBoundsException e) {
            NVLBlocks.nvlLogger.logLine("PacketHandler","CountUpdateMessage: Unexpected end of packet.\nMessage: " + ByteBufUtil.hexDump(buf, 0, buf.writerIndex()));
            return null;
        }
    }
    public static void encode (NVLXPTNetworkMessageSyncAll msg, FriendlyByteBuf buf) {
//        NVLBlocks.nvlLogger.logLine("NVLXPTMessageHandler","encode");
        buf.writeInt(msg.pos.getX());
        buf.writeInt(msg.pos.getY());
        buf.writeInt(msg.pos.getZ());
        buf.writeLong(msg.commonAmount);
        buf.writeUtf(msg.privateAmount);
    }
    public static void handle(NVLXPTNetworkMessageSyncAll msg, Supplier<NetworkEvent.Context> ctx) {
///        NVLBlocks.nvlLogger.logLine("NVLXPTMessageHandler","handle:"+ctx.get().getDirection().name());
//        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> handleClient(msg, ctx.get()));
//        DistExecutor.runWhenOn(Dist.DEDICATED_SERVER, () -> () -> handleServer(msg, ctx.get()));
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(msg, ctx.get()));
//        handleClient(msg, ctx.get());
    }
    @OnlyIn(Dist.CLIENT)
    private static void handleClient(NVLXPTNetworkMessageSyncAll msg, NetworkEvent.Context ctx) {
        Level world = Minecraft.getInstance().level;
        if (world != null) {
            if (world.isClientSide) {
                BlockEntity be = world.getBlockEntity(msg.pos);
                if (be instanceof NVLXPTBlockEntity) {
                    ((NVLXPTBlockEntity)be).setFromServer(msg.privateAmount, msg.commonAmount);
                    ctx.setPacketHandled(true);
                } else NVLBlocks.nvlLogger.logLine("NVLXPTNetworkMessageSyncAll", "Menu is NULL or not the correct instance");
            } else NVLBlocks.nvlLogger.logLine("NVLXPTNetworkMessageSyncAll","Level is NOT Client side");
        } else NVLBlocks.nvlLogger.logLine("NVLXPTNetworkMessageSyncAll","World is NULL");
    }
}
