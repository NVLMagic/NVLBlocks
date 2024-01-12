package hu.nvl.nvlblocks.blocks.xpt;

import hu.nvl.nvlblocks.Setup.NVLBlockRegistry;
import hu.nvl.nvlblocks.Setup.NVLBlockMenuRegistry;
import hu.nvl.nvlblocks.components.base_classes.NVLMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.entity.BlockEntity;

public class NVLXPTMenu extends NVLMenu {
    public NVLXPTMenu(int pContainerId, Inventory inv, FriendlyByteBuf extra) {
        this(pContainerId, inv.player.level().getBlockEntity(extra.readBlockPos()));
    }
    public NVLXPTMenu(int pContainerId, BlockEntity entity) {
        super(NVLBlockMenuRegistry.Menu_XPT.get(), pContainerId, entity, entity.getLevel());
    }
    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, be.getBlockPos()), player, NVLBlockRegistry.NVL_Block_XPT.get());
    }
}
