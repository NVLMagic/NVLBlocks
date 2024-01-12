package hu.nvl.nvlblocks.blocks.ett;

import hu.nvl.nvlblocks.components.base_classes.NVLSlotItemHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class NVLETTSlotEnc extends NVLSlotItemHandler {
	public NVLETTSlotEnc(IItemHandler itemHandler, int index, int xPosition, int yPosition, boolean isActive) {
		super(itemHandler, index, xPosition, yPosition, isActive);
	}
	@Override
	public boolean mayPlace(@NotNull ItemStack stack) {
		return stack.isEnchantable() || stack.isEnchanted();
	}
}
