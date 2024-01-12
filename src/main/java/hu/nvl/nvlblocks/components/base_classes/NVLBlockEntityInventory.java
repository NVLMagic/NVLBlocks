package hu.nvl.nvlblocks.components.base_classes;

import hu.nvl.nvlblocks.NVLBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;

public class NVLBlockEntityInventory extends NVLBlockEntity implements MenuProvider, Container {
	protected ArrayList<Integer> dropList = new ArrayList<>();
	protected ItemStackHandler inventory;
	protected boolean noChange = true;
	protected void createInventory(int size) {
		inventory = new ItemStackHandler(size) {
			@Override
			protected void onContentsChanged(int slot) {
				super.onContentsChanged(slot);
//				logLine("Slot changed: "+slot);
				NVLBlockEntityInventory.this.setChanged();
				if (level != null && level.isClientSide) level.getLightEngine().checkBlock(worldPosition);
			}
		};
//		logLine("New inventory created: "+this.worldPosition.toShortString()+":"+size);
	}
	private final LazyOptional<ItemStackHandler> optional = LazyOptional.of(() -> this.inventory);
	public NVLBlockEntityInventory(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
//		createInventory(10);
	}
    // -------------------- NBT Compound Data functions ----------------------
	// Get data from Compound
	@Override
	public void load(@NotNull CompoundTag tag) {
		super.load(tag);
		CompoundTag data = tag.getCompound(NVLBlocks.MODID);
		inventory.deserializeNBT(data.getCompound("Inventory"));
	}
	// Save data to Compound
	@Override
	protected void saveAdditional(@NotNull CompoundTag tag) {
		super.saveAdditional(tag);
		var data = new CompoundTag();
		data.put("Inventory",this.inventory.serializeNBT());
		tag.put(NVLBlocks.MODID,data);
	}
	// Sync initiated from server
	@Override
	public @NotNull CompoundTag getUpdateTag() {
		CompoundTag tag = super.getUpdateTag();
		var data = new CompoundTag();
		data.put("Inventory",this.inventory.serializeNBT());
		tag.put(NVLBlocks.MODID,data);
		return tag;
	}
	@Override
	public void handleUpdateTag(CompoundTag tag) {
		load(tag);
	}
	// ------------------ Forge Capabilities ---------------------------
	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) return this.optional.cast();
		else return super.getCapability(cap);
	}
	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		this.optional.invalidate();
	}

	// --------- Stack helper functions
	public ItemStackHandler getInventory() {
		return this.inventory;
	}

	public void drops() {
		SimpleContainer inventory = new SimpleContainer(this.inventory.getSlots());
		for (int i = 0; i < this.inventory.getSlots(); i++) {
			inventory.setItem(i, this.inventory.getStackInSlot(i));
		}
		if (this.level != null) Containers.dropContents(this.level, this.worldPosition, inventory);
	}
	// Sync packet
	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
//		logSided("getUpdatePacket");
		return ClientboundBlockEntityDataPacket.create(this);
	}
 	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
//		logSided("onDataPacket");
		super.onDataPacket(net, pkt); // calling load
	}

	// ---- MenuProvider interface functions will be implemented in the child classes
	public @NotNull Component getDisplayName() {
		return Component.literal("");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer)
	{
		return null;
	}
	// ---- Container interface functions ----------------------------------------
	// Returns the number of slots in the inventory.
	@Override
	public int getContainerSize() {
//		logSided("getContainerSize");
		return inventory.getSlots();
	}
	@Override
	public boolean isEmpty() {
		boolean r = true;
		for(int i = 0;i < inventory.getSlots() && r;i++) r = inventory.getStackInSlot(i).isEmpty();
		return r;
	}
	// Returns the stack in the given slot.
	@Override
	public @NotNull ItemStack getItem(int slot) {
//		logSided("getItem in slot "+slot);
		if (slot < inventory.getSlots()) return inventory.getStackInSlot(slot);
		else return ItemStack.EMPTY;
	}
	// Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	@Override
	public @NotNull ItemStack removeItem(int slot, int amount) {
//		logSided("removeItem from slot "+slot + ", amount "+amount);
		ItemStack s = ItemStack.EMPTY;
		if (slot < inventory.getSlots()) {
			s = inventory.getStackInSlot(slot).copy();
			if (!s.isEmpty()) {
				int a = s.getCount();
				if (a > amount) {
					inventory.getStackInSlot(slot).setCount(a-amount);
					s.setCount(amount);
				} else inventory.setStackInSlot(slot,ItemStack.EMPTY);
				this.setChanged();
			}
		}
		return s;
	}
	// Removes a stack from the given slot and returns it.
	@Override
	public @NotNull ItemStack removeItemNoUpdate(int slot) {
//		logSided("removeItem from slot "+slot);
		ItemStack s = ItemStack.EMPTY;
		if (slot < inventory.getSlots()) {
			s = inventory.getStackInSlot(slot);
			inventory.setStackInSlot(slot,ItemStack.EMPTY);
			this.setChanged();
		}
		return s;
	}
	// Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	@Override
	public void setItem(int slot, ItemStack stack) {
//		logSided("setItem to slot "+slot + ", stack "+stack.toString());
		if (slot < inventory.getSlots()) {
			inventory.setStackInSlot(slot,stack.copy());
			this.setChanged();
		}
	}
	// Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
	// For guis use Slot.isItemValid
	@Override
	public boolean canPlaceItem(int pIndex, ItemStack pStack) {
		return Container.super.canPlaceItem(pIndex, pStack);
	}
	// Returns { true } if the given stack can be extracted into the target inventory.
	// Params: pTarget – the container into which the item should be extracted
	// 		   pIndex – the slot from which to extract the item
	// 		   pStack – the item to extract
	// Returns:	{ true } if the given stack can be extracted into the target inventory
	@Override
	public boolean canTakeItem(Container pTarget, int pIndex, ItemStack pStack) {
		return Container.super.canTakeItem(pTarget, pIndex, pStack);
	}
	@Override
	public boolean stillValid(Player player) {
//		logSided("stillValid "+player.getName());
		return true;
	}
	@Override
	public void clearContent() {
//		logSided("clearContent");
		noChange = true;
		for (int i = 0;i < inventory.getSlots();i++) inventory.setStackInSlot(i,ItemStack.EMPTY);
		noChange = false;
		setChanged();
	}
// -------------- Item Stack Helpers ----------------
	protected boolean isMatch(ItemStack one, ItemStack two) {
		boolean r = false;
		if (one != null && two != null) {
			r = one.isEmpty() && two.isEmpty();
			if (!r) {
				if (!one.isEmpty() && !two.isEmpty()) r = one.getItem().equals(two.getItem());
			}
		}
		return r;
	}
}
