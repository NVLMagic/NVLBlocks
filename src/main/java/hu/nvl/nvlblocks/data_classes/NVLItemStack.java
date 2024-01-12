package hu.nvl.nvlblocks.data_classes;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class NVLItemStack {
    private int id = -1;
    private int amount = 0;
    private int index = -1; // the index of the recipe position
    public NVLItemStack() {}
    public NVLItemStack(ItemStack stack) {
        setFromItemStack(stack);
    }
    public NVLItemStack(String packed) {
        if (!packed.isEmpty()) setFromString(packed);
    }
    public int getId() {
        return id;
    }
    public int getAmount() {
        return amount;
    }
    public int getIndex() {
        return index;
    }
    public ItemStack getStack() {
        return id == -1?ItemStack.EMPTY:new ItemStack(Item.byId(id),amount);
    }
    public String getAsString() {
        return String.valueOf(index) + ":" + id + ":" + amount;
    }
    public void setFromString(String value) {
        String[] parts = value.split(":");
        if (parts.length == 3) {
            index = Integer.parseInt(parts[0]);
            id = Integer.parseInt(parts[1]);
            amount = Integer.parseInt(parts[2]);
        }
    }
    public void setFromItemStack(ItemStack stack) {
        if (stack != null) {
            id = Item.getId(stack.getItem());
            amount = stack.getCount();
        }
    }
    public boolean match(ItemStack check) {
        boolean r = false;
        if (check != null) r = Item.getId(check.getItem()) == id && amount == check.getCount();
        return r;
    }
    public boolean match(NVLItemStack check) {
        return check.id == id && check.amount == amount;
    }
    public void setIndex(int value) {
        index = value;
    }
    public boolean isEmpty() {
        return id == -1;
    }
}
