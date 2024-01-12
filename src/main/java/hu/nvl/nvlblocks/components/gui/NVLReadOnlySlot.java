package hu.nvl.nvlblocks.components.gui;

import hu.nvl.nvlblocks.components.base_classes.NVLSlotItemHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class NVLReadOnlySlot extends NVLSlotItemHandler {
	public NVLReadOnlySlot(IItemHandler itemHandler, int idx, int x, int y, boolean isActive) {
		super(itemHandler, idx, x, y, isActive);
	}
	@Override
	public boolean mayPlace(@NotNull ItemStack stack) {
		return false;
	}
}
