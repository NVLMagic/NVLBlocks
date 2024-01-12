package hu.nvl.nvlblocks.blocks.de;

import hu.nvl.nvlblocks.Setup.NVLBlockEntityRegistry;
import hu.nvl.nvlblocks.components.base_classes.NVLBlockEntityInventory;
import hu.nvl.nvlblocks.components.interfaces.NVLInterfaceTickabeBlockEntity;
import hu.nvl.nvlblocks.data_classes.NVLRecipePattern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;

public class NVLDEBlockEntity extends NVLBlockEntityInventory implements NVLInterfaceTickabeBlockEntity {
    private static final Item[] DYEITEMS = {
            Items.BLACK_DYE, Items.BLUE_DYE,Items.BROWN_DYE, Items.LIGHT_BLUE_DYE,
            Items.CYAN_DYE, Items.GRAY_DYE, Items.YELLOW_DYE, Items.GREEN_DYE,
            Items.LIGHT_GRAY_DYE, Items.LIME_DYE, Items.MAGENTA_DYE, Items.ORANGE_DYE,
            Items.PINK_DYE, Items.PURPLE_DYE, Items.RED_DYE, Items.WHITE_DYE
    };
    public static final ArrayList<ItemStack> SRCLIST = new ArrayList<>();
    public static final ArrayList<ItemStack> DSTLIST = new ArrayList<>();
    public static final int SRCSTART = 0;
    public static final int SRCEND = SRCSTART + 18;
    public static final int DSTSTART = SRCEND + 1;
    public static final int DSTEND = DSTSTART + 18;
    public static final int INVSIZE = DSTEND + 1;
    private int tick = 0;
    public NVLDEBlockEntity(BlockPos pos, BlockState state) {
        super(NVLBlockEntityRegistry.BE_DE.get(),pos,state);
        createInventory(INVSIZE);
        clearContent();
        // Add to drop list
        for (int i = SRCSTART;i <= INVSIZE;i++) dropList.add(i);
    }
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new NVLDEMenu(pContainerId, pPlayerInventory, this);
    }
    // -------------------------- Utilities -------------------------------------
    private boolean hasMatch(ItemStack test) {
        boolean r = false;
        if (!test.isEmpty())
            for (int i = 0;i < SRCLIST.size() && !r;i++) r = test.getItem().equals(SRCLIST.get(i).getItem());
        return r;
    }
    private int getSrcIndex(ItemStack test) {
        int r = -1;
        for (int i = 0;i < SRCLIST.size() && r == -1;i++) if (test.getItem().equals(SRCLIST.get(i).getItem())) r = i;
        return r;
    }
    public void fillRecipes() {
        if (SRCLIST.isEmpty()) {
            if (level != null && !level.isClientSide && level.getServer() != null) {
                for (CraftingRecipe r : level.getServer().getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING)) {
                    for (int i = 0;i < NVLRecipePattern.getRecipeNum(r);i++) {
                        NVLRecipePattern p = new NVLRecipePattern(r, i);
                        Item res = r.getResultItem(RegistryAccess.EMPTY).getItem();
                        if (Arrays.asList(DYEITEMS).contains(res) && p.ingredientCount() == 1) {
                            SRCLIST.add(p.getPattern()[0].copy());
                            DSTLIST.add(r.getResultItem(RegistryAccess.EMPTY).copy());
       //                     logSided("From:"+p.getPattern()[0].toString()+", to:"+r.getResultItem(RegistryAccess.EMPTY));
                        }
                    }
                }
            }
        }
    }
    // -------------------------- Container overrides -------------------------------------
    // Do not allow any automation to put anything else just the requirements
    @Override
    public boolean canPlaceItem(int pIndex, ItemStack pStack) {
        if (SRCLIST.isEmpty()) fillRecipes();
        boolean r = pIndex >= SRCSTART && pIndex <= SRCEND && hasMatch(pStack) && !pStack.isEmpty();
        if (r) {
            ItemStack stackInSlot = inventory.getStackInSlot(pIndex);
            r = stackInSlot.isEmpty() || (stackInSlot.getItem().equals(pStack.getItem()) && stackInSlot.getCount() < stackInSlot.getMaxStackSize());
        } else r = pIndex >= DSTSTART && pIndex <= DSTEND;
        return r;
    }
    // Allowed only from the destination slots
    @Override
    public boolean canTakeItem(Container pTarget, int pIndex, ItemStack pStack) {
        return pIndex >= DSTSTART && pIndex <= DSTEND;
    }
    // ----------------------- Actual work ----------------------
    @Override
    public void tick() {
        if (level != null && !level.isClientSide && ++tick > 10) processItems();
    }
    private int getTargetSlot(ItemStack check) {
        // get a target slot with the destination item type
        int idx = -1;
        for (int d = DSTSTART;d <= DSTEND && idx == -1;d++) {
            ItemStack test = inventory.getStackInSlot(d);
            if (!test.isEmpty() && test.getItem().equals(check.getItem()) && test.getCount()+check.getCount() <= test.getMaxStackSize()) idx = d;
        }
        // if not found search for empty slot
        for (int d = DSTSTART;d <= DSTEND && idx == -1;d++) if (inventory.getStackInSlot(d).isEmpty()) idx = d;
        return idx;
    }
    private void processItems() {
        boolean done = false;
        // go through all the slots
        for (int i = SRCSTART;i <= SRCEND && !done;i++) {
            // first get the source index
            int idx = -1;
            ItemStack test = inventory.getStackInSlot(i);
            if (!test.isEmpty()) idx = getSrcIndex(test);
            if (idx > -1) {
  //              logLine("found:"+idx+":"+test.toString());
                // get a target slot with the destination item type
                int didx = getTargetSlot(DSTLIST.get(idx));
                // if found a target slot to place
                if (didx > -1) {
                    // put the destination
                    test = inventory.getStackInSlot(didx);
      //              logLine("target found:"+didx+":"+test.toString());
                    if (test.isEmpty()) inventory.setStackInSlot(didx,DSTLIST.get(idx).copy());
                    else test.setCount(test.getCount()+DSTLIST.get(idx).getCount());
                    // remove source amount
                    test = inventory.getStackInSlot(i);
                    if (test.getCount() == 1) inventory.setStackInSlot(i,ItemStack.EMPTY);
                    else test.setCount(test.getCount()-1);
          //          logSided("set:"+test.toString());
                    done = true;
                }
            }
        }
        tick = 0;
    }
}
