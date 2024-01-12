package hu.nvl.nvlblocks.Setup;

import hu.nvl.nvlblocks.NVLBlocks;
import hu.nvl.nvlblocks.blocks.ac.NVLACBlock;
import hu.nvl.nvlblocks.blocks.ac.NVLACBlockEntity;
import hu.nvl.nvlblocks.blocks.de.NVLDEBlockEntity;
import hu.nvl.nvlblocks.blocks.hs.NVLHoloSignBlockEntity;
import hu.nvl.nvlblocks.blocks.xpt.NVLXPTBlockEntity;
import hu.nvl.nvlblocks.blocks.ett.NVLETTBlockEntity;
import hu.nvl.nvlblocks.components.interfaces.NVLInterfaceLogger;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NVLBlockEntityRegistry implements NVLInterfaceLogger {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
			DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, NVLBlocks.MODID);
	public static final RegistryObject<BlockEntityType<NVLXPTBlockEntity>> BE_XPT =
			BLOCK_ENTITIES.register("nvl_be_xpt", () -> BlockEntityType.Builder.of(NVLXPTBlockEntity::new, NVLBlockRegistry.NVL_Block_XPT.get()).build(null));
	public static final RegistryObject<BlockEntityType<NVLETTBlockEntity>> BE_ETT =
			BLOCK_ENTITIES.register("nvl_be_ett", () -> BlockEntityType.Builder.of(NVLETTBlockEntity::new, NVLBlockRegistry.NVL_Block_ETT.get()).build(null));
	public static final RegistryObject<BlockEntityType<NVLHoloSignBlockEntity>> BE_HS =
			BLOCK_ENTITIES.register("nvl_be_hs", () -> BlockEntityType.Builder.of(NVLHoloSignBlockEntity::new, NVLBlockRegistry.NVL_Block_HS.get()).build(null));
	public static final RegistryObject<BlockEntityType<NVLACBlockEntity>> BE_AC =
			BLOCK_ENTITIES.register("nvl_be_ac", () -> BlockEntityType.Builder.of(NVLACBlockEntity::new, NVLBlockRegistry.NVL_Block_AC.get()).build(null));
	public static final RegistryObject<BlockEntityType<NVLDEBlockEntity>> BE_DE =
			BLOCK_ENTITIES.register("nvl_be_de", () -> BlockEntityType.Builder.of(NVLDEBlockEntity::new, NVLBlockRegistry.NVL_Block_DE.get()).build(null));
	public static void register(IEventBus bus) {
		BLOCK_ENTITIES.register(bus);
	}
}
