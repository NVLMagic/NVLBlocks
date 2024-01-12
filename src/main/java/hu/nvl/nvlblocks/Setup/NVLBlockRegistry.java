package hu.nvl.nvlblocks.Setup;

import hu.nvl.nvlblocks.NVLBlocks;
import hu.nvl.nvlblocks.blocks.ac.NVLACBlock;
import hu.nvl.nvlblocks.blocks.de.NVLDEBlock;
import hu.nvl.nvlblocks.blocks.ett.NVLETTBlock;
import hu.nvl.nvlblocks.blocks.hs.NVLHoloSignBlock;
import hu.nvl.nvlblocks.blocks.spt.NVLSPTerminalBlock;
import hu.nvl.nvlblocks.blocks.xpt.NVLXPTBlock;
import hu.nvl.nvlblocks.components.interfaces.NVLInterfaceLogger;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;
import hu.nvl.nvlblocks.components.base_classes.NVLBlock;
public class NVLBlockRegistry implements NVLInterfaceLogger {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, NVLBlocks.MODID);

	private static RegistryObject<Block> registerBlock(String name, Supplier<Block> block) {
		RegistryObject<Block> toReturn = BLOCKS.register(name, block);
		registerBlockItem(name, toReturn);
		return toReturn;
	}
	public static final RegistryObject<Block> NVL_Block_SPT = registerBlock("nvl_block_spt", () -> new NVLSPTerminalBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
	public static final RegistryObject<Block> NVL_Block_XPT = registerBlock("nvl_block_xpt", () -> new NVLXPTBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
	public static final RegistryObject<Block> NVL_Block_ETT = registerBlock("nvl_block_ett", () -> new NVLETTBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
	public static final RegistryObject<Block> NVL_Block_HS = registerBlock("nvl_block_hs", () -> new NVLHoloSignBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
	public static final RegistryObject<Block> NVL_Block_AC = registerBlock("nvl_block_ac", () -> new NVLACBlock(BlockBehaviour.Properties.copy(Blocks.HOPPER)));
	public static final RegistryObject<Block> NVL_Block_DE = registerBlock("nvl_block_de", () -> new NVLDEBlock(BlockBehaviour.Properties.copy(Blocks.HOPPER)));
	private static void registerBlockItem(String name, RegistryObject<Block> block) {
		NVLItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
	}
	public static void register(IEventBus bus) {
		BLOCKS.register(bus);
	}
	public static void AddCreative(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey().location().getPath().equals(NVLBlocks.TOOLSTAB)) {
			for (RegistryObject<Block> e :BLOCKS.getEntries()) {
				if (((NVLBlock) e.get()).getRegisterCreativeTab()) event.accept(e);
			}
		}
	}

}
