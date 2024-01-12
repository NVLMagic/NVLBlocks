package hu.nvl.nvlblocks.blocks.ett;

import hu.nvl.nvlblocks.Setup.NVLBlockEntityRegistry;
import hu.nvl.nvlblocks.components.base_classes.NVLBlockEntityInventory;
import hu.nvl.nvlblocks.components.modules.NVLModuleEnchantmentHelper;
import hu.nvl.nvlblocks.data_classes.NVLEnchantmentMergeResult;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class NVLETTBlockEntity extends NVLBlockEntityInventory {
	private static final int MULTIPLIER = 1;
	private static final int ENCMULTIPLIER = 1;
	// De-enchant section
	public static final int ETTDSource = 0;
	public static final int ETTDResultStart = ETTDSource+1;
	public static final int ETTDResultEnd = ETTDResultStart+8;
	// Requirements
	public static final int ETTBook = ETTDResultEnd +1;
	public static final int ETTLapis = ETTBook +1;
	public static final int ETTLapisBlock = ETTLapis +1;
	public static final int ETTBookRequirement = ETTLapisBlock +1;
	public static final int ETTLapisRequirement = ETTBookRequirement +1;
	public static final int ETTLapisBlockRequirement = ETTLapisRequirement +1;
	// Enchant section
	public static final int ETTESource = ETTLapisBlockRequirement+1;
	public static final int ETTEBookStart = ETTESource +1;
	public static final int ETTEBookEnd = ETTEBookStart +8;
	public static final int ETTEResult = ETTEBookEnd +1;
	// Book Merge
	public static final int BMSrc1 = ETTEResult +1;
	public static final int BMSrc2 = BMSrc1 +1;
	public static final int BMResult = BMSrc2 +1;
	public static final int ETTSize = BMResult+1;
	
	public static final ItemStack BOOKS = new ItemStack(Items.BOOK);
	public static final ItemStack ENCBOOKS = new ItemStack(Items.ENCHANTED_BOOK);
	public static final ItemStack LAPIS = new ItemStack(Items.LAPIS_LAZULI);
	public static final ItemStack LAPISLEFT = new ItemStack(Items.LAPIS_LAZULI);
	public static final ItemStack LAPISBLOCK = new ItemStack(Blocks.LAPIS_BLOCK);

	public NVLETTBlockEntity(BlockPos pos, BlockState state) {
		super(NVLBlockEntityRegistry.BE_ETT.get(),pos,state);
		createInventory(ETTSize);
		clearContent();
		fillStacks();
		dropList.add(ETTDSource);		
		dropList.add(ETTBook);		
		dropList.add(ETTLapis);		
		dropList.add(ETTLapisBlock);		
		dropList.add(ETTESource);
		dropList.add(BMSrc1);
		dropList.add(BMSrc2);
		for (int i = ETTEBookStart;i <= ETTEBookEnd;i++) dropList.add(i);
	}
	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
		return new NVLETTMenu(pContainerId, pPlayerInventory, this);
	}
	private void fillStacks() {
		BOOKS.setHoverName(getLocalTextNoClass(NVLBlockEntityRegistry.BE_ETT.getId().getPath()+".breq"));
		LAPIS.setHoverName(getLocalTextNoClass(NVLBlockEntityRegistry.BE_ETT.getId().getPath()+".lreq"));
		LAPISBLOCK.setHoverName(getLocalTextNoClass(NVLBlockEntityRegistry.BE_ETT.getId().getPath()+".lbreq"));
	}
	// Called when an automation wants to put something into the stack
	public boolean skipSlot(int slot) {
		return slot < inventory.getSlots() && (slot == ETTBookRequirement || slot == ETTLapisRequirement || slot == ETTLapisBlockRequirement);
	}
	public void processPreClick(int slot) {
//		logLine("processPreClick:"+slot);
		if (slot >= ETTDResultStart && slot <= ETTDResultEnd) RemoveDEncBook(slot);
		if (slot == ETTEResult) RemoveEnchanted();
		if (slot == BMResult) RemoveBookMerge();
	}
	private void RemoveBookMerge() {
		ItemStack src = inventory.getStackInSlot(BMSrc1);
		ItemStack tgt = inventory.getStackInSlot(BMSrc2).copy();
		NVLEnchantmentMergeResult result = NVLModuleEnchantmentHelper.mergeEnchantmentBooks(src,tgt);
		inventory.setStackInSlot(BMSrc1,result.Remaining);
		inventory.setStackInSlot(BMSrc2,ItemStack.EMPTY);
		NVLModuleEnchantmentHelper.drawEnchantmentCost(-1,result.cost * MULTIPLIER,inventory,-1,ETTLapisBlock,ETTLapis,LAPISLEFT);
	}
	// ---- Private worker routines
	// Called when an enchanted book removed from the De-enchant section
	private void RemoveDEncBook(int slot) {
//		logLine("RemoveDEncBook:"+slot);
		if (!inventory.getStackInSlot(slot).isEmpty()) {
			ItemStack book = inventory.getStackInSlot(slot);
//	logLine("RemoveEncBook: "+book);
			ArrayList<EnchantmentInstance> encData = NVLModuleEnchantmentHelper.getEnchantedBookData(book);
			if (!encData.isEmpty()) {
				int cost = (int) (Math.pow(2, encData.get(0).level-1) * MULTIPLIER);
//	logLine("RemoveEncBook: "+encData.enchantmentLevel+":"+cost);
				if (NVLModuleEnchantmentHelper.removeEnchantment(inventory.getStackInSlot(ETTDSource), encData.get(0))) {
					NVLModuleEnchantmentHelper.drawEnchantmentCost(1,cost,inventory,ETTBook,ETTLapisBlock,ETTLapis,LAPISLEFT);
					playSound();
				}
			}
		//	EvaluateCosts();
		}
	}
	private void playSound() {
		if (level != null && !level.isClientSide) level.playSound(null, (double) worldPosition.getX() + 0.5D,
				(double) worldPosition.getY() + 0.5D, (double) worldPosition.getZ() + 0.5D,
				SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
	}
	public void EvaluateCosts() {
//		logLine("EvaluateCosts");
		int bookCost = 0;
		int lapisDCost = 0;
		int lapisECost = 0;
		clearRequirements();
		// ---- De-enchant section
		ItemStack stack = inventory.getStackInSlot(ETTDSource);
		if (!stack.isEmpty() && stack.isEnchanted()) {
			bookCost = NVLModuleEnchantmentHelper.getEnchantmentBookCost(stack);
			lapisDCost = NVLModuleEnchantmentHelper.getEnchantmentLapisCost(stack, MULTIPLIER);
		}
		// ---- Enchant Section
		ItemStack target = ItemStack.EMPTY;
		stack = inventory.getStackInSlot(ETTESource);
		if (!stack.isEmpty() && stack.isEnchantable() || stack.isEnchanted()) {
			target = stack.copy();
			// Get already applied enchantments
			ArrayList<EnchantmentInstance> applied = NVLModuleEnchantmentHelper.getAllEnchantments(stack);
//			logLine(" applied:"+applied.size());
			for (int i = ETTEBookStart; i <= ETTEBookEnd;i++) {
				ItemStack s = inventory.getStackInSlot(i);
				ArrayList<EnchantmentInstance> list = NVLModuleEnchantmentHelper.getEnchantedBookData(s);
				for (EnchantmentInstance data : list) {
					if (NVLModuleEnchantmentHelper.getEnchantmentIndexInDataArray(applied,data) == -1 && data.enchantment.canEnchant(target)) {
				    	target.enchant(data.enchantment,data.level);
				    	applied.add(data);
				    	lapisECost += (int) (Math.pow(2, data.level-1) * ENCMULTIPLIER);
					}
//					else logLine(" cannot apply:"+data.enchantment.getName()+","+data.enchantmentLevel);
				}
			}
			// If no book found then remove result
		}
		// ---- Book Merge --------------------------------------------------------------------------------------------
		ItemStack BMTarget = ItemStack.EMPTY;
		if (!inventory.getStackInSlot(BMSrc1).isEmpty() && !inventory.getStackInSlot(BMSrc2).isEmpty() && inventory.getStackInSlot(BMResult).isEmpty()) {
			NVLEnchantmentMergeResult result = NVLModuleEnchantmentHelper.mergeEnchantmentBooks(inventory.getStackInSlot(BMSrc1),inventory.getStackInSlot(BMSrc2));
			BMTarget = result.Result;
			lapisECost += result.cost;
		}
//		logLine(" costs:"+bookCost+","+lapisDCost+","+lapisECost);
		// ---- Show requirements -------------------------------------------------------------------------------------
		NVLModuleEnchantmentHelper.displayEnchantmentCost(bookCost,Math.max(lapisDCost, lapisECost),inventory,ETTBookRequirement,ETTLapisBlockRequirement,ETTLapisRequirement,BOOKS,LAPISBLOCK,LAPIS);
		// ---- Calculate what we have
		int lapisHave = (isMatch(inventory.getStackInSlot(ETTLapis),LAPIS)?inventory.getStackInSlot(ETTLapis).getCount():0) +
				(isMatch(inventory.getStackInSlot(ETTLapisBlock),LAPISBLOCK)?inventory.getStackInSlot(ETTLapisBlock).getCount()*9:0);
		int bookHave = isMatch(inventory.getStackInSlot(ETTBook),BOOKS)?inventory.getStackInSlot(ETTBook).getCount():0;
//		logLine(" have:"+lapisHave);
		// ---- Display results if we have the correct amount or empty ------------------------------------------------
		if (bookCost > 0 && lapisDCost > 0 && bookHave >= bookCost && lapisHave >= lapisDCost) {
//			logLine(" showbooks");
			NVLModuleEnchantmentHelper.extractEchantments(inventory.getStackInSlot(ETTDSource), inventory, ETTDResultStart, ETTDResultEnd);
		} else for(int i = ETTDResultStart;i <= ETTDResultEnd;i++) inventory.setStackInSlot(i,ItemStack.EMPTY);
		// ---- Enchant section
		if (lapisHave >= lapisECost && lapisECost > 0) {
//			logLine(" showTarget");
			inventory.setStackInSlot(ETTEResult,target);
		} else inventory.setStackInSlot(ETTEResult,ItemStack.EMPTY);
		// ---- Book merge section
		if (lapisHave >= lapisECost) inventory.setStackInSlot(BMResult,BMTarget);
		else inventory.setStackInSlot(BMResult,ItemStack.EMPTY);
	}
	// ----------------------- When removing the enchanted item ----------------
	private void RemoveEnchanted() {
//		logLine("RemoveEnchanted");
		if (!inventory.getStackInSlot(ETTEResult).isEmpty() && !inventory.getStackInSlot(ETTESource).isEmpty()) {
			ItemStack enchanted = inventory.getStackInSlot(ETTEResult);
			int cost = 0;
			ArrayList<EnchantmentInstance> src = NVLModuleEnchantmentHelper.enchantmentMapToArrayList(EnchantmentHelper.getEnchantments(inventory.getStackInSlot(ETTESource)));
			ArrayList<EnchantmentInstance> dst = NVLModuleEnchantmentHelper.enchantmentMapToArrayList(EnchantmentHelper.getEnchantments(enchanted));
			ArrayList<EnchantmentInstance> applied = new ArrayList<>();
            for (EnchantmentInstance enchantmentInstance : dst) if (!src.contains(enchantmentInstance)) applied.add(enchantmentInstance);
//			logLine(" already present: "+applied.size());
			boolean change = false;
			for (int i = ETTEBookStart; i <= ETTEBookEnd;i++) {
				ItemStack s = inventory.getStackInSlot(i);
				boolean remove = false;
				if (!s.isEmpty() && isMatch(s,ENCBOOKS)) {
					ArrayList<EnchantmentInstance> bookDataList = NVLModuleEnchantmentHelper.getEnchantedBookData(s);
//					logLine(" bookData: "+bookData);
					for (EnchantmentInstance bookData : bookDataList) {
						int idx = NVLModuleEnchantmentHelper.getEnchantmentIndexInDataArray(applied, bookData);
						if (idx > -1) {
							cost += (int) (Math.pow(2, bookData.level-1) * ENCMULTIPLIER);
							applied.remove(idx);
							s = NVLModuleEnchantmentHelper.removeEnchantmentFromBook(s,bookData);
							remove = true;
							change = true;
						}
					}
					if (remove) inventory.setStackInSlot(i,s);
				}
			}
			if (change) playSound();
			// Remove cost
			NVLModuleEnchantmentHelper.drawEnchantmentCost(-1,cost,inventory,-1,ETTLapisBlock,ETTLapis,LAPISLEFT);
			// delete source
			inventory.setStackInSlot(ETTESource,ItemStack.EMPTY);
		}
	}
	private void clearRequirements() {
		inventory.setStackInSlot(ETTBookRequirement,ItemStack.EMPTY);
		inventory.setStackInSlot(ETTLapisRequirement,ItemStack.EMPTY);
		inventory.setStackInSlot(ETTLapisBlockRequirement,ItemStack.EMPTY);
	}
	public void drops() {
		SimpleContainer inventory = new SimpleContainer(this.inventory.getSlots());
		for (int i = 0; i < this.dropList.size(); i++) {
			inventory.setItem(i, this.inventory.getStackInSlot(dropList.get(i)));
		}
		if (this.level != null) Containers.dropContents(this.level, this.worldPosition, inventory);
	}
	@Override
	public void load(@NotNull CompoundTag tag) {
		super.load(tag);
		// Required for upgrading from 1.7
		if (inventory.getSlots() < ETTSize) {
			logLine("Upgrade!!!");
			ItemStackHandler temp = new ItemStackHandler(ETTSize);
			// Copy content
			for (int i = 0;i < inventory.getSlots();i++) temp.setStackInSlot(i,inventory.getStackInSlot(i));
			inventory = temp;
		}
	}
	// -------------------------- Container overrides -------------------------------------
	// Do not allow any automation to put anything else just the requirements
	@Override
	public boolean canPlaceItem(int pIndex, ItemStack pStack) {
		boolean r = pIndex == ETTBook && pStack.getItem().equals(Items.BOOK);
        if (pIndex == ETTLapis && pStack.getItem().equals(Items.LAPIS_LAZULI)) r = true;
		if (pIndex == ETTLapisBlock && pStack.getItem().equals(Items.LAPIS_BLOCK)) r = true;
		return r;
	}
	@Override
	public boolean canTakeItem(Container pTarget, int pIndex, ItemStack pStack) {
		boolean r = pIndex == ETTBook && pStack.getItem().equals(Items.BOOK);
		if (pIndex == ETTLapis && pStack.getItem().equals(Items.LAPIS_LAZULI)) r = true;
		if (pIndex == ETTLapisBlock && pStack.getItem().equals(Items.LAPIS_BLOCK)) r = true;
		return r;
	}
	@Override
	public @NotNull ItemStack removeItem(int slot, int amount) {
		ItemStack r = super.removeItem(slot, amount);
		EvaluateCosts();
		return r;
	}
	@Override
	public @NotNull ItemStack removeItemNoUpdate(int slot) {
		ItemStack r = super.removeItemNoUpdate(slot);
		EvaluateCosts();
		return r;
	}
}
