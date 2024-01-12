package hu.nvl.nvlblocks.components.modules;

import hu.nvl.nvlblocks.components.base_classes.NVLBaseClass;
import hu.nvl.nvlblocks.data_classes.NVLEnchantmentMergeResult;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.items.ItemStackHandler;
import java.util.ArrayList;
import java.util.Map;

public class NVLModuleEnchantmentHelper extends NVLBaseClass {
	// ------------------------------------------------- Extract enchantment as book in inventory slots
	public static String extractEchantments(ItemStack enchantedItem, ItemStackHandler inventory, int StartSlot, int EndSlot) {
		String r = "";
		// Clear slots
		for (int i = StartSlot;i <= EndSlot;i++) if (inventory.getSlots() > i) inventory.setStackInSlot(i,ItemStack.EMPTY);
		// Put books
		if (enchantedItem != null && enchantedItem.isEnchanted()) {
			Map<Enchantment, Integer> srcmap = EnchantmentHelper.getEnchantments(enchantedItem);
//			r = "Enchantment count: "+srcmap.size();
			for (int i = 0; i < srcmap.size();i++) {
				ItemStack NewBook = new ItemStack(Items.ENCHANTED_BOOK,1);
				EnchantedBookItem.addEnchantment(NewBook, getEnchanetmentData(srcmap,i));
				inventory.setStackInSlot(StartSlot + i,NewBook);
			}
		} else r = "Stack not enchanted! ["+enchantedItem+"]";
		return r;
	}
	private static ItemStack setStack(ItemStack source, int count) {
		ItemStack r = count > 0?source.copy():ItemStack.EMPTY;
		r.setCount(count);
		return r;
	}
	public static int getEnchantmentLapisCost(ItemStack enchantedItem, int multiplier) {
		int r = 0;
		if (enchantedItem != null && enchantedItem.isEnchanted()) {
			Map<Enchantment, Integer> srcmap = EnchantmentHelper.getEnchantments(enchantedItem);
			for (int i = 0; i < srcmap.size();i++) {
				EnchantmentInstance d = getEnchanetmentData(srcmap,i);
				r += (int) (Math.pow(2, d.level) * multiplier);
			}
		}
		return r;
	}
	// Returns an enchantment instance list of the enchantments the book have
	public static ArrayList<EnchantmentInstance> getEnchantedBookData(ItemStack book) {
		ArrayList<EnchantmentInstance> l = new ArrayList<EnchantmentInstance>();
		if (book != null && book.is(Items.ENCHANTED_BOOK)) {
			Map<Enchantment, Integer> em = EnchantmentHelper.getEnchantments(book);
			if (!em.isEmpty()) for (int i = 0;i < em.size();i++)l.add(getEnchanetmentData(em,i));
		}
		return l;
	}
	private static EnchantmentInstance getEnchanetmentData(Map<Enchantment,Integer> enchantments, int index) {
		EnchantmentInstance r = null;
		if (index < enchantments.size()) {
			Enchantment e = (Enchantment) enchantments.keySet().toArray()[index];
			int level = (int) enchantments.get(e);
			r = new EnchantmentInstance(e,level);
		}
		return r;
	}
	public static boolean checkEnchantRequirements(ItemStack[] inv, int rBookIndex, int rLapisBlockIndex, int rLapisIndex, int bookIndex, int lapisBlockIndex, int lapisIndex) {
		Boolean r = true;
		if (rBookIndex > 0 && bookIndex > 0) {
			r = inv[bookIndex].getCount() >= inv[rBookIndex].getCount();
		}
		if (r) {
			int have =inv[lapisBlockIndex].getCount() * 9 + inv[lapisIndex].getCount();
			int req =  inv[rLapisBlockIndex].getCount() * 9 + inv[rLapisIndex].getCount();
			r = have >= req;
		}
		return r;
	}
	public static int getEnchantmentBookCost(ItemStack enchantedItem) {
		int r = 0;
		if (enchantedItem != null && enchantedItem.isEnchanted()) {
			Map<Enchantment, Integer> srcmap = EnchantmentHelper.getEnchantments(enchantedItem);
			r = srcmap.size();
		}
		return r;
	}
	public static void drawEnchantmentCost(int bcost, int lcost, ItemStackHandler inv, int bookIdx, int lBlockIdx, int lIdx, ItemStack Lapis) {
		// ---- Book cost
		if (bookIdx > 0 && bookIdx < inv.getSlots()) {
			if (bcost > 0 && !inv.getStackInSlot(bookIdx).isEmpty()) {
				int have = inv.getStackInSlot(bookIdx).getCount();
				if (have > bcost) inv.getStackInSlot(bookIdx).setCount(have - bcost);
				else inv.setStackInSlot(bookIdx,ItemStack.EMPTY);
			}
		}
		// ---- Lapis Cost
		if (lcost > 0) {
			int have = inv.getStackInSlot(lIdx).getCount()+inv.getStackInSlot(lBlockIdx).getCount()*9;
			int leftSmall = 0;
			int leftBig = 0;
			int left = have - lcost;
			if (left > 0) {
				leftSmall = left % 9;
				leftBig = left / 9;
			}
			inv.setStackInSlot(lIdx,new ItemStack(Items.LAPIS_LAZULI, leftSmall));
			if (leftBig > 0) inv.getStackInSlot(lBlockIdx).setCount(leftBig);
			else inv.setStackInSlot(lBlockIdx,ItemStack.EMPTY);
		}
	}
	public static void displayEnchantmentCost(int bcost, int lcost, ItemStackHandler inv, int bookIdx, int lBlockIdx, int lIdx,
			ItemStack Books, ItemStack LapisBlock, ItemStack Lapis) {
		// ---- Book cost
		if (bookIdx > 0 && bookIdx < inv.getSlots()) {
			inv.setStackInSlot(bookIdx,setStack(Books,bcost));
//			if (bcost > 0) inv.getStackInSlot(bookIdx).setHoverName(Component.literal(inv.getStackInSlot(bookIdx).getDisplayName().getString() + " " + bcost));
		}
		// ---- Lapis Cost
		int leftSmall = lcost % 9;
		inv.setStackInSlot(lIdx,setStack(Lapis, leftSmall));
//		if (leftSmall > 0) inv.getStackInSlot(lIdx).setHoverName(Component.literal(inv.getStackInSlot(lIdx).getDisplayName().getString() + " " + leftSmall));
		int leftBig = lcost / 9;
		inv.setStackInSlot(lBlockIdx,setStack(LapisBlock, leftBig));
//		if (leftBig > 0) inv.getStackInSlot(lBlockIdx).setHoverName(Component.literal(inv.getStackInSlot(lBlockIdx).getDisplayName().getString()+" "+leftBig));
	}
	public static ArrayList<EnchantmentInstance> enchantmentMapToArrayList(Map<Enchantment,Integer> emap) {
		ArrayList<EnchantmentInstance> list = new ArrayList<EnchantmentInstance>();
		for (int i = 0;i < emap.size();i++) {
			Enchantment e = (Enchantment) emap.keySet().toArray()[i];
			int level = (int) emap.get(e);
			list.add(new EnchantmentInstance(e,level));
		}
		return list;
	}
	public static ItemStack removeEnchantmentFromBook(ItemStack book, EnchantmentInstance enc) {
		ItemStack r = new ItemStack(Items.ENCHANTED_BOOK,1);
		ArrayList<EnchantmentInstance> data = getEnchantedBookData(book);
		boolean empty = true;
		for (EnchantmentInstance benc : data) {
			if (!isEnchantmentMatchWithLevel(benc,enc)) {
				EnchantedBookItem.addEnchantment(r, benc);
				empty = false;
			}
		}
		if (empty) r = ItemStack.EMPTY;
		return r;
	}
	public static int getEnchantmentIndexInDataArray(ArrayList<EnchantmentInstance> list, EnchantmentInstance compare) {
		int r = -1;
		for (int i = 0;i<list.size() && r==-1;i++) {
			if (isEnchantmentMatch(list.get(i),compare)) r = i;
		}
		return r;
	}
	public static boolean isEnchantmentMatchWithLevel(EnchantmentInstance one, EnchantmentInstance two) {
		return one.enchantment.equals(two.enchantment) && one.level == two.level;
	}
	public static boolean isEnchantmentMatch(EnchantmentInstance one, EnchantmentInstance two) {
		return one.enchantment.equals(two.enchantment);
	}
	public static boolean removeEnchantment(ItemStack stack, EnchantmentInstance e) {
		Boolean r = false;
		if (!stack.isEmpty()) {
			Map<Enchantment,Integer> src = EnchantmentHelper.getEnchantments(stack);
			for (int i = 0;i<src.size() && !r;i++) {
				EnchantmentInstance d = getEnchanetmentData(src,i);
				if (isEnchantmentMatchWithLevel(e,d)) {
					src.remove(d.enchantment,d.level);
					EnchantmentHelper.setEnchantments(src, stack);
					r = true;
				}
			}
		}
		return r;
	}
	public static ArrayList<EnchantmentInstance> getAllEnchantments(ItemStack stack) {
		ArrayList<EnchantmentInstance> r = new ArrayList<EnchantmentInstance>();
		if (stack.isEnchanted()) {
			Map<Enchantment,Integer> srcmap = EnchantmentHelper.getEnchantments(stack);
			for (int i = 0;i < srcmap.size();i++) r.add(getEnchanetmentData(srcmap, i));
		}
		return r;
	}
	public static NVLEnchantmentMergeResult mergeEnchantmentBooks(ItemStack source1, ItemStack source2) {
		NVLEnchantmentMergeResult r = new NVLEnchantmentMergeResult();
		r.Remaining = new ItemStack(Items.ENCHANTED_BOOK,1);
		ArrayList<EnchantmentInstance> src1 = getEnchantedBookData(source1);
		ArrayList<EnchantmentInstance> src2 = getEnchantedBookData(source2);
		ArrayList<EnchantmentInstance> src;
		ArrayList<EnchantmentInstance> dst;
		if (src1.size()>src2.size()) {
			src = src2;
			dst = src1;
			r.Result = source1.copy();
		} else {
			src = src1;
			dst = src2;
			r.Result = source2.copy();
		}
		for (EnchantmentInstance instance : src) {
			if (getEnchantmentIndexInDataArray(dst,instance) == -1) {
				EnchantedBookItem.addEnchantment(r.Result, instance);
				r.cost += (int) (Math.pow(2, instance.level-1));
			} else EnchantedBookItem.addEnchantment(r.Remaining, instance);
		} // for
		if (EnchantmentHelper.getEnchantments(r.Remaining).isEmpty())r.Remaining = ItemStack.EMPTY;
		return r;
	}
}
