package hu.nvl.nvlblocks.blocks.hs;

import hu.nvl.nvlblocks.components.base_classes.NVLBlockContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class NVLHoloSignBlock extends NVLBlockContainer {
	public NVLHoloSignBlock(BlockBehaviour.Properties props) {
		super(props);
	}

	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
//		logLine("createNewTileEntity: " + pos.toShortString());
		return new NVLHoloSignBlockEntity(pos, state);
	}
	@Override
	public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
		InteractionResult r;
		if (!level.isClientSide) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof NVLHoloSignBlockEntity) NetworkHooks.openScreen((ServerPlayer) player, (NVLHoloSignBlockEntity) be, pos);
		}
		r = InteractionResult.SUCCESS;
		return r;
	}
	@Override
	public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
		int r = 0;
		BlockEntity e = level.getBlockEntity(pos);
		if (e instanceof NVLHoloSignBlockEntity) r = ((NVLHoloSignBlockEntity)e).isProjecting()?15:0;
		return r;
	}
}
