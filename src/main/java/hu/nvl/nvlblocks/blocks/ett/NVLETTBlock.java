package hu.nvl.nvlblocks.blocks.ett;

import hu.nvl.nvlblocks.components.base_classes.NVLBlockContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class NVLETTBlock extends NVLBlockContainer {
	public NVLETTBlock(BlockBehaviour.Properties props) {
		super(props);
	}

	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
//		logLine("createNewTileEntity: " + pos.toShortString());
		return new NVLETTBlockEntity(pos, state);
	}
	@Override
	public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
		InteractionResult r;
		if (!level.isClientSide) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof NVLETTBlockEntity) NetworkHooks.openScreen((ServerPlayer) player, (NVLETTBlockEntity) be, pos);
		}
		r = InteractionResult.SUCCESS;
		return r;
	}
}
