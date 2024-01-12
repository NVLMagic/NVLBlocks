package hu.nvl.nvlblocks.components.base_classes;

import hu.nvl.nvlblocks.components.interfaces.NVLInterfaceLogger;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class NVLBlockEntity extends BlockEntity implements NVLInterfaceLogger {
	public NVLBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	protected void logSided(String msg) {
		String s = "X";
		if (level != null) s = level.isClientSide?"C":"S";
		logLine(s + ":" + msg);
	}
}
