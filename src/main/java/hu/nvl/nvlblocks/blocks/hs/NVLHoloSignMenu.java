package hu.nvl.nvlblocks.blocks.hs;

import hu.nvl.nvlblocks.Setup.NVLBlockMenuRegistry;
import hu.nvl.nvlblocks.Setup.NVLBlockRegistry;
import hu.nvl.nvlblocks.components.base_classes.NVLContainerMenu;
import hu.nvl.nvlblocks.components.base_classes.NVLSlotItemHandler;
import hu.nvl.nvlblocks.components.gui.NVLReadOnlySlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;

public class NVLHoloSignMenu extends NVLContainerMenu {
    private static final int SHIFTY = 12;
    private static final int SHIFTX = 11;

    public NVLHoloSignMenu(int pContainerId, Inventory inv, BlockEntity entity) {
        super(NVLBlockMenuRegistry.Menu_HS.get(), pContainerId, inv, entity);
    }
    public NVLHoloSignMenu (int pContainerId, Inventory inv, FriendlyByteBuf extra) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extra.readBlockPos()));
    }
    @Override
    protected void addSlots() {
        this.be.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            // Source slot
            addSlot(new NVLSlotItemHandler(handler, 0, SHIFTX + 164, SHIFTY, true));
            // NEWS
            addSlot(new NVLReadOnlySlot(handler, 1, SHIFTX + 182, SHIFTY + 18, true));
            addSlot(new NVLReadOnlySlot(handler, 2, SHIFTX + 164, SHIFTY + 36, true));
            addSlot(new NVLReadOnlySlot(handler, 3, SHIFTX + 200, SHIFTY + 36, true));
            addSlot(new NVLReadOnlySlot(handler, 4, SHIFTX + 182, SHIFTY + 54, true));
            // TB
            addSlot(new NVLReadOnlySlot(handler, 5, SHIFTX + 218, SHIFTY + 18, true));
            addSlot(new NVLReadOnlySlot(handler, 6, SHIFTX + 218, SHIFTY + 54, true));
            // Display slot
            addSlot(new NVLReadOnlySlot(handler, 7, SHIFTX + 200, SHIFTY, true));
        });
    }
    @Override
    protected void addPlayerInventory(Inventory playerInventory) {
        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 9; ++c) {
                addSlot(new Slot(playerInventory, c + r * 9 + 9, SHIFTX + c * 18, SHIFTY + r * 18));
            }
        }
    }
    @Override
    protected void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            addSlot(new Slot(playerInventory, i,  SHIFTX + i * 18, SHIFTY + 54));
        }
    }
    @Override
    public void clicked(int slot, int button, @NotNull ClickType clickType, @NotNull Player player) {
//		logSided("slotClick:"+pSlotId+","+pButton+","+pClickType+","+pPlayer);
        if (slot == 0) {
            super.clicked(slot, button, clickType, player);
            ((NVLHoloSignBlockEntity)be).setDisplay();
        }
        else if (slot > 0 && slot < 5) ((NVLHoloSignBlockEntity)be).toggleTrans(slot);
        else if (slot == 5 || slot == 6) ((NVLHoloSignBlockEntity)be).toggleTriState(slot);
        else if (slot != 7) super.clicked(slot, button, clickType, player);
    }
    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, be.getBlockPos()), player, NVLBlockRegistry.NVL_Block_HS.get());
    }
}
