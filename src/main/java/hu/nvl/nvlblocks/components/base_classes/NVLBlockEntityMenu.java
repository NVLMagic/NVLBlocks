package hu.nvl.nvlblocks.components.base_classes;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class NVLBlockEntityMenu extends NVLBlockEntity implements MenuProvider, Container {
    public NVLBlockEntityMenu(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public Component getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return null;
    }

    @Override
    public int getContainerSize() { return 0; }

    @Override
    public boolean isEmpty() { return false; }

    @Override
    public ItemStack getItem(int pSlot) { return null; }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) { return null; }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) { return null; }

    @Override
    public void setItem(int pSlot, ItemStack pStack) { }

    @Override
    public boolean stillValid(Player pPlayer) { return true; }

    @Override
    public void clearContent() { }
}
