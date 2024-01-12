package hu.nvl.nvlblocks.blocks.hs;

import hu.nvl.nvlblocks.Setup.NVLBlockEntityRegistry;
import hu.nvl.nvlblocks.Setup.NVLItemRegistry;
import hu.nvl.nvlblocks.components.base_classes.NVLBlockEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;

public class NVLHoloSignBlockEntity extends NVLBlockEntityInventory {
	private final ItemStack OFF = new ItemStack(Blocks.GLASS_PANE,1);
	private final ItemStack EAST = new ItemStack(NVLItemRegistry.Item_dir_east.get(),1);
	private final ItemStack NORTH = new ItemStack(NVLItemRegistry.Item_dir_north.get(),1);
	private final ItemStack WEST = new ItemStack(NVLItemRegistry.Item_dir_west.get(),1);
	private final ItemStack SOUTH = new ItemStack(NVLItemRegistry.Item_dir_south.get(),1);
	private final ItemStack EASTWEST = new ItemStack(NVLItemRegistry.Item_dir_eastwest.get(),1);
	private final ItemStack NORTHSOUTH = new ItemStack(NVLItemRegistry.Item_dir_northsouth.get(),1);
	private final ItemStack[] TRISTATE = new ItemStack[] { OFF,EASTWEST,NORTHSOUTH };
	
	public NVLHoloSignBlockEntity(BlockPos pos, BlockState state) {
		super(NVLBlockEntityRegistry.BE_HS.get(),pos,state);
		createInventory(8);
		fillStacks();
		dropList.add(0);
//		syncTE = true;
	}
	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
		return new NVLHoloSignMenu(pContainerId, pPlayerInventory, this);
	}
	private void fillStacks() {
		inventory.setStackInSlot(0,ItemStack.EMPTY); // Item
		inventory.setStackInSlot(1,OFF.copy().setHoverName(getLocalTextNoClass("item_dir_off"))); // North
		inventory.setStackInSlot(2,OFF.copy().setHoverName(getLocalTextNoClass("item_dir_off"))); // West
		inventory.setStackInSlot(3,OFF.copy().setHoverName(getLocalTextNoClass("item_dir_off"))); // East
		inventory.setStackInSlot(4,OFF.copy().setHoverName(getLocalTextNoClass("item_dir_off"))); // South
		inventory.setStackInSlot(5,OFF.copy().setHoverName(getLocalTextNoClass("item_dir_off"))); // Top
		inventory.setStackInSlot(6,OFF.copy().setHoverName(getLocalTextNoClass("item_dir_off"))); // Bottom
		inventory.setStackInSlot(7,ItemStack.EMPTY); // Display
	}
	public boolean isProjecting() {
//		logSided("isProjecting:");
		boolean r = false;
		for(int i = 1;i < inventory.getSlots()-1 && !r;i++) r = !isMatch(inventory.getStackInSlot(i),OFF);
		return r;
	}
	
	// ------------------ Particle called functions -------------------------------
	public boolean showNorth() { return show(1); }
	public boolean showWest() { return show(2); }
	public boolean showEast() { return show(3); }
	public boolean showSouth() { return show(4); }
	public int showTop() { return get3state(5); }
	public int showBottom() { return get3state(6); }
	private boolean show(int side) { return !isMatch(inventory.getStackInSlot(side), OFF); }
	private int get3state(int slot) {
		int r = -1;
		for(int i = 0;i < TRISTATE.length && r == -1;i++) if (isMatch(inventory.getStackInSlot(slot),TRISTATE[i])) r = i;
		return r;
	}
	public void toggleTrans(int slot) {
		ItemStack stack = inventory.getStackInSlot(slot);
		switch(slot) {
			case 1: inventory.setStackInSlot(slot,isMatch(stack,OFF)?NORTH.copy().setHoverName(getLocalTextNoClass("item_dir_north")):
				OFF.copy().setHoverName(getLocalTextNoClass("item_dir_off"))); break;
			case 2: inventory.setStackInSlot(slot,isMatch(stack,OFF)?WEST.copy().setHoverName(getLocalTextNoClass("item_dir_west")):
				OFF.copy().setHoverName(getLocalTextNoClass("item_dir_off"))); break;
			case 3: inventory.setStackInSlot(slot,isMatch(stack,OFF)?EAST.copy().setHoverName(getLocalTextNoClass("item_dir_east")):
				OFF.copy().setHoverName(getLocalTextNoClass("item_dir_off"))); break;
			case 4: inventory.setStackInSlot(slot,isMatch(stack,OFF)?SOUTH.copy().setHoverName(getLocalTextNoClass("item_dir_south")):
				OFF.copy().setHoverName(getLocalTextNoClass("item_dir_off"))); break;
		}
		if (level != null && level.isClientSide) level.getLightEngine().checkBlock(worldPosition);
	}
	public void toggleTriState(int slot) {
		if (slot > 4 && slot < 7) {
			ItemStack stack = inventory.getStackInSlot(slot);
			inventory.setStackInSlot(slot,isMatch(stack,OFF)?EASTWEST.copy().setHoverName(getLocalTextNoClass("item_dir_eastwest")):
					(isMatch(stack,EASTWEST)?NORTHSOUTH.copy().setHoverName(getLocalTextNoClass("item_dir_northsouth")):
					OFF.copy().setHoverName(getLocalTextNoClass("item_dir_off"))));
		}
		if (level != null && level.isClientSide) level.getLightEngine().checkBlock(worldPosition);
	}
	public void setDisplay() {
		ItemStack stack = inventory.getStackInSlot(0).copy();
		if (!stack.isEmpty()) {
			stack.setCount(1);
			inventory.setStackInSlot(7,stack.setHoverName(getLocalTextNoClass("item_hs_display")));
		}
	}
	public void drops() {
		SimpleContainer inventory = new SimpleContainer(this.inventory.getSlots());
		for (int i = 0; i < this.dropList.size(); i++) {
			inventory.setItem(i, this.inventory.getStackInSlot(dropList.get(i)));
		}
		if (this.level != null) Containers.dropContents(this.level, this.worldPosition, inventory);
	}
    public ItemStack getDisplay() {
//		logSided("getDisplay:"+inventory.getStackInSlot(7));
		return inventory.getStackInSlot(7);
    }
	// -------------------------- Container overrides -------------------------------------
	// Do not allow any automation to put anything else just the requirements
	@Override
	public boolean canPlaceItem(int pIndex, ItemStack pStack) {
		return false;
	}
	// Do not allow anything to be taken
	@Override
	public boolean canTakeItem(Container pTarget, int pIndex, ItemStack pStack) {
		return false;
	}
}
