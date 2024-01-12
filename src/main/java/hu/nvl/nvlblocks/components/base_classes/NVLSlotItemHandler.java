package hu.nvl.nvlblocks.components.base_classes;

import hu.nvl.nvlblocks.components.interfaces.NVLInterfaceLogger;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class NVLSlotItemHandler extends SlotItemHandler implements NVLInterfaceLogger {
    private boolean active = true;
    public NVLSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition, boolean isActive) {
        super(itemHandler, index, xPosition, yPosition);
        active = isActive;
    }
    @Override
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean newActive) {
        active = newActive;
    }
}
