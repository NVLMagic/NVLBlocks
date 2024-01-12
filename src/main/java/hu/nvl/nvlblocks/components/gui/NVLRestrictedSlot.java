package hu.nvl.nvlblocks.components.gui;

import hu.nvl.nvlblocks.components.base_classes.NVLSlotItemHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class NVLRestrictedSlot extends NVLSlotItemHandler {
	private ItemStack restrictor = null;
	public NVLRestrictedSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, ItemStack filter, boolean isActive)
	{
		super(itemHandler, index, xPosition, yPosition, isActive);
		restrictor = filter;
	}
	@Override
	public boolean mayPlace(@NotNull ItemStack stack)
	{
		boolean r = false;
		if (restrictor != null) {
			r = stack.getItem().equals(restrictor.getItem());
	//		logLine("mayPlace:" + (restrictor.isEmpty() ? "empty" : restrictor.toString()) + "?" + (stack.isEmpty() ? "empty" : stack.toString()) + ":" + r);
		}
		return r;
	}
}
