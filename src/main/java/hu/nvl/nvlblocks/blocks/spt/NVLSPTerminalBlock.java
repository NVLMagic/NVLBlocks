package hu.nvl.nvlblocks.blocks.spt;

import hu.nvl.nvlblocks.components.base_classes.NVLBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class NVLSPTerminalBlock extends NVLBlock {
	public NVLSPTerminalBlock(BlockBehaviour.Properties props) {
		super(props.sound(SoundType.STONE).explosionResistance(6f)
				.noOcclusion()); // This make adjacent blocks rendered correctly
	}
}
