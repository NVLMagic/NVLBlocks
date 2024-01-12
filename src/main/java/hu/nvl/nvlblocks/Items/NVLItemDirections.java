package hu.nvl.nvlblocks.Items;

import hu.nvl.nvlblocks.components.base_classes.NVLItem;
import hu.nvl.nvlblocks.data_classes.NVLDirection;

public abstract class NVLItemDirections extends NVLItem {
	protected NVLDirection myDirection = NVLDirection.OFF;
	public NVLItemDirections(NVLDirection dir) {
		super();
		myDirection = dir;
		this.setRegisterCreativeTab(false);
	}
	public NVLDirection getFacing() { return myDirection; }
}
