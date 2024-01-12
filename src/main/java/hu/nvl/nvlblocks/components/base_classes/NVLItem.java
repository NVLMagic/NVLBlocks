package hu.nvl.nvlblocks.components.base_classes;

import hu.nvl.nvlblocks.components.interfaces.NVLInterfaceLogger;
import net.minecraft.world.item.Item;

public abstract class NVLItem extends Item implements NVLInterfaceLogger {
	public NVLItem() {
		super(new Properties().stacksTo(1));
	}
	private boolean regCreativeTab = true;
	public void setRegisterCreativeTab(boolean newState) { regCreativeTab = newState; }
	public boolean getRegisterCreativeTab() { return regCreativeTab; }
}
