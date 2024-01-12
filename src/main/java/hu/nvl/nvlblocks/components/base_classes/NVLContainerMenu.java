package hu.nvl.nvlblocks.components.base_classes;
import hu.nvl.nvlblocks.components.interfaces.NVLInterfaceLogger;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;

public class NVLContainerMenu extends AbstractContainerMenu implements NVLInterfaceLogger {
	public final NVLBlockEntityInventory be;
	protected final Level level;
	public NVLContainerMenu(@Nullable MenuType<?> menuType, int pContainerId, Inventory inv, BlockEntity entity) {
		super(menuType, pContainerId);
		be = (NVLBlockEntityInventory) entity;
		level = inv.player.level();
		addSlotsInternal();
		addPlayerHotbarInternal(inv);
		addPlayerInventoryInternal(inv);
	}
	protected void logSided(String msg) {
		if (level == null) logLine(msg);
		else logLine((level.isClientSide?"C":"S")+" "+msg);
	}
	public int PLAYER_START = -1;
	public int PLAYER_END = -1;
	public int PLAYER_HOTBAR_START = -1;
	public int PLAYER_HOTBAR_END = -1;
	public int CONTAINER_START = -1;
	public int CONTAINER_END = -1;
	@Override
	public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
//		logLine("player:["+PLAYER_HOTBAR_START+":"+PLAYER_END+"], container:["+CONTAINER_START+":"+CONTAINER_END+"]");
		Slot sourceSlot = slots.get(index);
		if (!sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
		ItemStack sourceStack = sourceSlot.getItem();
		ItemStack copyOfSourceStack = sourceStack.copy();

		// Check if the slot clicked is one of the vanilla container slots
		if (isPlayerSlot(index)) {
			// This is a vanilla container slot so merge the stack into the tile inventory
			if (!transferToContainer(sourceStack,false)) return ItemStack.EMPTY;  // EMPTY_ITEM
		} else if (isContainerSlot(index)) {
			// This is a TE slot so merge the stack into the players inventory
			if (!transferToPlayerInventory(sourceStack,false)) return ItemStack.EMPTY;
		} else {
			logLine("Invalid slotIndex:" + index);
			return ItemStack.EMPTY;
		}
		// If stack size == 0 (the entire stack was moved) set slot contents to null
		if (sourceStack.getCount() == 0) sourceSlot.set(ItemStack.EMPTY);
		else sourceSlot.setChanged();
		sourceSlot.onTake(playerIn, sourceStack);
		return copyOfSourceStack;
	}
	// ---- Helpers
	protected boolean transferToContainer(ItemStack stack, boolean reverse) {
		return moveItemStackTo(stack, CONTAINER_START, CONTAINER_END, reverse);
	}
	protected boolean transferToPlayerInventory(ItemStack stack, boolean reverse) {
		return moveItemStackTo(stack, PLAYER_HOTBAR_START, PLAYER_END, reverse);
	}
	protected boolean isPlayerSlot(int index) {
		return index >= PLAYER_HOTBAR_START && index <= PLAYER_END;
	}
	protected boolean isContainerSlot(int index) {
		return index >= CONTAINER_START && index <= CONTAINER_END;
	}
	@Override
	protected boolean moveItemStackTo(@NotNull ItemStack pStack, int pStartIndex, int pEndIndex, boolean pReverseDirection) {
		boolean flag = false;
		int i = pStartIndex;
		if (pReverseDirection) i = pEndIndex - 1;
		// find if the inventory has the same item and has free space left
		if (pStack.isStackable()) {
			while(!pStack.isEmpty()) {
				if (pReverseDirection) {
					if (i < pStartIndex) break;
				} else if (i >= pEndIndex) break;

				Slot slot = this.slots.get(i);
				// skip inactive stacks and the ones which cannot accept the item
				if (slot.isActive() && slot.mayPlace(pStack)) {
					ItemStack itemstack = slot.getItem();
					if (!itemstack.isEmpty() && ItemStack.isSameItemSameTags(pStack, itemstack)) {
						int j = itemstack.getCount() + pStack.getCount();
						int maxSize = Math.min(slot.getMaxStackSize(), pStack.getMaxStackSize());
						if (j <= maxSize) {
							pStack.setCount(0);
							itemstack.setCount(j);
							slot.setChanged();
							flag = true;
						} else if (itemstack.getCount() < maxSize) {
							pStack.shrink(maxSize - itemstack.getCount());
							itemstack.setCount(maxSize);
							slot.setChanged();
							flag = true;
						}
					}
				}
				if (pReverseDirection) --i;	else ++i;
			}
		}
		// find an empty slot
		if (!pStack.isEmpty()) {
			if (pReverseDirection) i = pEndIndex - 1;
			else i = pStartIndex;

			while(true) {
				if (pReverseDirection) {
					if (i < pStartIndex) break;
				} else if (i >= pEndIndex) break;

				Slot slot1 = this.slots.get(i);
				ItemStack itemstack1 = slot1.getItem();
				if (itemstack1.isEmpty() && slot1.mayPlace(pStack) && slot1.isActive()) {
					if (pStack.getCount() > slot1.getMaxStackSize()) slot1.setByPlayer(pStack.split(slot1.getMaxStackSize()));
					else slot1.setByPlayer(pStack.split(pStack.getCount()));
					slot1.setChanged();
					flag = true;
					break;
				}
				if (pReverseDirection) --i;	else ++i;
			}
		}
		return flag;
	}
	// Wrappers
	private void addSlotsInternal() {
		CONTAINER_START = slots.size();
		addSlots();
		CONTAINER_END = slots.size()-1;
	}
	protected void addSlots() {	}
	private void addPlayerInventoryInternal(Inventory inv) {
		PLAYER_START = slots.size();
		addPlayerInventory(inv);
		PLAYER_END = slots.size()-1;
	}
	protected void addPlayerInventory(Inventory playerInventory) { }
	private void addPlayerHotbarInternal(Inventory inv) {
		PLAYER_HOTBAR_START = slots.size();
		addPlayerHotbar(inv);
		PLAYER_HOTBAR_END = slots.size()-1;
	}
	protected void addPlayerHotbar(Inventory playerInventory) {	}
	@Override
	public boolean stillValid(@NotNull Player player) { return false; }
}
