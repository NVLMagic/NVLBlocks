package hu.nvl.nvlblocks.blocks.ac;

import hu.nvl.nvlblocks.components.base_classes.NVLBlockContainer;
import hu.nvl.nvlblocks.components.interfaces.NVLInterfaceTickabeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NVLACBlock extends NVLBlockContainer {
    public NVLACBlock(Properties props) {
        super(props);
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new NVLACBlockEntity(pPos, pState);
    }
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        InteractionResult r;
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof NVLACBlockEntity) NetworkHooks.openScreen((ServerPlayer) player, (NVLACBlockEntity) be, pos);
        }
        r = InteractionResult.SUCCESS;
        return r;
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return NVLInterfaceTickabeBlockEntity.getTickerHelper(level);
    }
}
