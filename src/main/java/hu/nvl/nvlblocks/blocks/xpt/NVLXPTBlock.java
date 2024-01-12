package hu.nvl.nvlblocks.blocks.xpt;

import hu.nvl.nvlblocks.components.base_classes.NVLBlockContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NVLXPTBlock extends NVLBlockContainer {
	public NVLXPTBlock(Properties props) {
		super(props);
		RESISTANCE_WHEN_NOT_EMPTY = 1000f;
	}

	@Override
	public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
		InteractionResult r;
		if (!level.isClientSide) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof NVLXPTBlockEntity) NetworkHooks.openScreen((ServerPlayer) player, (NVLXPTBlockEntity) be, pos);
		}
//		logLine((level.isClientSide?'C':'S')+":"+player.totalExperience+":"+player.experienceLevel+":"+player.experienceProgress);
		r = InteractionResult.SUCCESS;
		return r;
	}
	@Nullable
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
//		logLine("createNewTileEntity: " + pos.toShortString());
		return new NVLXPTBlockEntity(pos, state);
	}
	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
		if (!level.isClientSide()) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof NVLXPTBlockEntity) ((NVLXPTBlockEntity) be).dropExperience(pos);
		}
		return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
	}
	@Override
	public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
		if (!level.isClientSide()) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof NVLXPTBlockEntity) ((NVLXPTBlockEntity) be).dropExperience(pos);
		}
		super.onBlockExploded(state, level, pos, explosion);
	}
}
