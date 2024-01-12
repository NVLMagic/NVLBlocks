package hu.nvl.nvlblocks.Setup;

import hu.nvl.nvlblocks.Items.*;
import hu.nvl.nvlblocks.NVLBlocks;
import hu.nvl.nvlblocks.components.base_classes.NVLItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NVLItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NVLBlocks.MODID);
    public static final RegistryObject<Item> Item_Logo = ITEMS.register("nvl_item_logo", NVLItemLogo::new);
    public static final RegistryObject<Item> Item_et = ITEMS.register("nvl_item_et", NVLItemEmergencyTeleporter::new);
    public static final RegistryObject<Item> Item_xp_token = ITEMS.register("nvl_item_xp_token", NVLItemXPToken::new);
    public static final RegistryObject<Item> Item_dir_up = ITEMS.register("nvl_item_dir_up", NVLItemDirUp::new);
    public static final RegistryObject<Item> Item_dir_down = ITEMS.register("nvl_item_dir_down", NVLItemDirDown::new);
    public static final RegistryObject<Item> Item_dir_east = ITEMS.register("nvl_item_dir_east", NVLItemDirEast::new);
    public static final RegistryObject<Item> Item_dir_west = ITEMS.register("nvl_item_dir_west", NVLItemDirWest::new);
    public static final RegistryObject<Item> Item_dir_north = ITEMS.register("nvl_item_dir_north", NVLItemDirNorth::new);
    public static final RegistryObject<Item> Item_dir_south = ITEMS.register("nvl_item_dir_south", NVLItemDirSouth::new);
    public static final RegistryObject<Item> Item_dir_northsouth = ITEMS.register("nvl_item_dir_northsouth", NVLItemDirNorthSouth::new);
    public static final RegistryObject<Item> Item_dir_eastwest = ITEMS.register("nvl_item_dir_eastwest", NVLItemDirEastWest::new);
    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
    public static void AddCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().location().getPath().equals(NVLBlocks.TOOLSTAB)) {
            for (RegistryObject<Item> e :ITEMS.getEntries()) {
                if (e.get() instanceof NVLItem && ((NVLItem) e.get()).getRegisterCreativeTab()) event.accept(e);
            }
        }
    }

}
