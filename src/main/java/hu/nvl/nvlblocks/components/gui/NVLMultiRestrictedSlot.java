package hu.nvl.nvlblocks.components.gui;

import hu.nvl.nvlblocks.components.base_classes.NVLSlotItemHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;

public class NVLMultiRestrictedSlot extends NVLSlotItemHandler {
	private final ArrayList<ItemStack> restrictors = new ArrayList<>();
	public NVLMultiRestrictedSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, ItemStack[] filter, boolean isActive)
	{
		super (itemHandler, index, xPosition, yPosition, isActive);
        restrictors.addAll(Arrays.asList(filter));
	}
	public NVLMultiRestrictedSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, ArrayList<ItemStack> filter, boolean isActive)
	{
		super (itemHandler, index, xPosition, yPosition, isActive);
		restrictors.addAll(filter);
		//logLine("res:"+restrictors.size());
	}
	@Override
	public boolean mayPlace(@NotNull ItemStack stack) {
		boolean r = false;
		for (int i = 0; i < restrictors.size() && !r; i++) r = stack.getItem().equals(restrictors.get(i).getItem());
		return r;
	}
}
