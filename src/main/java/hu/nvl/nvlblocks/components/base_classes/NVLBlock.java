package hu.nvl.nvlblocks.components.base_classes;
/* --------------------------------------------------------------------------------------------
 * File by NVL 2023
 * NVLBlockContainer for 1.20
 * 
 * v23092801: Initial version
 * ----------------------------------------------------------------------------------------- */
import hu.nvl.nvlblocks.components.interfaces.NVLInterfaceLogger;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class NVLBlock extends Block implements NVLInterfaceLogger {
	private boolean regCreativeTab = false;

	public NVLBlock(BlockBehaviour.Properties props) {
		super(props);
		setRegisterCreativeTab(true);
	}
	public void setRegisterCreativeTab(boolean newState) { regCreativeTab = newState; }
	public boolean getRegisterCreativeTab() { return regCreativeTab; }
}
