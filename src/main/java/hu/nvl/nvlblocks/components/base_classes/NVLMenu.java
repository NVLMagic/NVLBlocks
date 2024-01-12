package hu.nvl.nvlblocks.components.base_classes;

import hu.nvl.nvlblocks.components.interfaces.NVLInterfaceLogger;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class NVLMenu extends AbstractContainerMenu implements NVLInterfaceLogger {
    public final NVLBlockEntityMenu be;
    protected final Level level;
    protected NVLMenu(@Nullable MenuType<?> pMenuType, int pContainerId, BlockEntity entity, Level level) {
        super(pMenuType, pContainerId);
        be = entity instanceof NVLBlockEntityMenu?(NVLBlockEntityMenu)entity:null;
        this.level = level;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) { return null; }

    @Override
    public boolean stillValid(Player pPlayer) { return false; }
}
