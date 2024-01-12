package hu.nvl.nvlblocks.blocks.ac;

import hu.nvl.nvlblocks.Setup.NVLBlockMenuRegistry;
import hu.nvl.nvlblocks.Setup.NVLBlockRegistry;
import hu.nvl.nvlblocks.components.base_classes.NVLContainerMenu;
import hu.nvl.nvlblocks.components.base_classes.NVLSlotItemHandler;
import hu.nvl.nvlblocks.components.gui.NVLMultiRestrictedSlot;
import hu.nvl.nvlblocks.components.gui.NVLReadOnlySlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;

public class NVLACMenu extends NVLContainerMenu {
    private static final int SHIFTX = 1;
    private static final int SHIFTY = 1;

    public NVLACMenu(int pContainerId, Inventory inv, BlockEntity entity) {
        super(NVLBlockMenuRegistry.Menu_AC.get(), pContainerId, inv, entity);
    }
    public NVLACMenu(int pContainerId, Inventory inv, FriendlyByteBuf extra) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extra.readBlockPos()));
    }
    @Override
    protected void addSlots() {
        ((NVLACBlockEntity)be).fillRecipes();
        this.be.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            // Source slots
            for (int y = 0; y < 2; y++)
                for (int x = 0; x < 9; x++)
                    addSlot(new NVLMultiRestrictedSlot(handler, NVLACBlockEntity.SRCSTART + x + y * 9, SHIFTX+48 + x * 18, SHIFTY + 10 + y * 18, NVLACBlockEntity.SRCLIST,true));
            // Destination slots
            for (int y = 0; y < 2; y++)
                for (int x = 0; x < 9; x++)
                    addSlot(new NVLReadOnlySlot(handler, NVLACBlockEntity.DSTSTART + x + y * 9, SHIFTX+48 + x * 18, SHIFTY + 70 + y * 18, true));
        });
    }
    @Override
    protected void addPlayerInventory(Inventory playerInventory) {
        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 9; ++c) {
                addSlot(new Slot(playerInventory, c + r * 9 + 9, 48 + SHIFTX + c * 18, SHIFTY + 110 + r * 18));
            }
        }
    }
    @Override
    protected void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            addSlot(new Slot(playerInventory, i, 48 + SHIFTX + i * 18, SHIFTY + 170));
        }
    }
    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, be.getBlockPos()), player, NVLBlockRegistry.NVL_Block_AC.get());
    }
}