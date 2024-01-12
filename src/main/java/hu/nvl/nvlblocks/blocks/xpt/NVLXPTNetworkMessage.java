package hu.nvl.nvlblocks.blocks.xpt;

import hu.nvl.nvlblocks.NVLBlocks;
import io.netty.buffer.ByteBufUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class NVLXPTNetworkMessage
{
    private BlockPos pos;
    private int privateAmount;
    private int commonAmount;
    public NVLXPTNetworkMessage(BlockPos p, int pxp, int cxp) {
        this.pos = p;
        this.privateAmount = pxp;
        this.commonAmount = cxp;
    }
    public static NVLXPTNetworkMessage decode (FriendlyByteBuf buf) {
//        NVLBlocks.nvlLogger.logLine("NVLXPTMessageHandler","decode");
        try {
            int x = buf.readInt();
            int y = buf.readInt();
            int z = buf.readInt();
            int pxp = buf.readInt();
            int cxp = buf.readInt();
            return new NVLXPTNetworkMessage(new BlockPos(x, y, z), pxp, cxp);
        }
        catch (IndexOutOfBoundsException e) {
            NVLBlocks.nvlLogger.logLine("PacketHandler","CountUpdateMessage: Unexpected end of packet.\nMessage: " + ByteBufUtil.hexDump(buf, 0, buf.writerIndex()));
            return null;
        }
    }
    public static void encode (NVLXPTNetworkMessage msg, FriendlyByteBuf buf) {
//        NVLBlocks.nvlLogger.logLine("NVLXPTMessageHandler","encode");
        buf.writeInt(msg.pos.getX());
        buf.writeInt(msg.pos.getY());
        buf.writeInt(msg.pos.getZ());
        buf.writeInt(msg.privateAmount);
        buf.writeInt(msg.commonAmount);
    }
    public static void handle(NVLXPTNetworkMessage msg, Supplier<NetworkEvent.Context> ctx) {
        handleServer(msg, ctx.get());
    }
    private static void handleServer(NVLXPTNetworkMessage msg, NetworkEvent.Context ctx) {
        if (ctx.getSender() != null) {
            Level world = ctx.getSender().level();
            if (world != null) {
                if (!world.isClientSide) {
                    AbstractContainerMenu c = ctx.getSender().containerMenu;
                    if (c instanceof NVLXPTMenu) {
                        ((NVLXPTBlockEntity) ((NVLXPTMenu) c).be).setFromClient(ctx.getSender(), msg.privateAmount, msg.commonAmount);
                        ctx.setPacketHandled(true);
                    } else NVLBlocks.nvlLogger.logLine("NVLXPTMessageHandler", "Menu is not the correct instance type or NULL");
                } else NVLBlocks.nvlLogger.logLine("NVLXPTMessageHandler","Level is NOT Server side");
            } else NVLBlocks.nvlLogger.logLine("NVLXPTMessageHandler","World is NULL");
        } else NVLBlocks.nvlLogger.logLine("NVLXPTMessageHandler","Sender is NULL");
    }
}
