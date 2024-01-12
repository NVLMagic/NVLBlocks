package hu.nvl.nvlblocks.Setup;

import hu.nvl.nvlblocks.NVLBlocks;
import hu.nvl.nvlblocks.blocks.ac.NVLACMenu;
import hu.nvl.nvlblocks.blocks.ac.NVLACScreen;
import hu.nvl.nvlblocks.blocks.de.NVLDEMenu;
import hu.nvl.nvlblocks.blocks.de.NVLDEScreen;
import hu.nvl.nvlblocks.blocks.hs.NVLHoloSignMenu;
import hu.nvl.nvlblocks.blocks.hs.NVLHoloSignScreen;
import hu.nvl.nvlblocks.blocks.xpt.NVLXPTMenu;
import hu.nvl.nvlblocks.blocks.xpt.NVLXPTScreen;
import hu.nvl.nvlblocks.blocks.ett.NVLETTMenu;
import hu.nvl.nvlblocks.blocks.ett.NVLETTScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NVLBlockMenuRegistry {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, NVLBlocks.MODID);
    public static final RegistryObject<MenuType<NVLXPTMenu>> Menu_XPT = registerMenuType(NVLXPTMenu::new, "nvl_xpt_menu");
    public static final RegistryObject<MenuType<NVLETTMenu>> Menu_ETT = registerMenuType(NVLETTMenu::new, "nvl_ett_menu");
    public static final RegistryObject<MenuType<NVLHoloSignMenu>> Menu_HS = registerMenuType(NVLHoloSignMenu::new, "nvl_hs_menu");
    public static final RegistryObject<MenuType<NVLACMenu>> Menu_AC = registerMenuType(NVLACMenu::new, "nvl_ac_menu");
    public static final RegistryObject<MenuType<NVLDEMenu>> Menu_DE = registerMenuType(NVLDEMenu::new, "nvl_de_menu");
    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }
    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }

    public static void registerScreens() {
        MenuScreens.register(NVLBlockMenuRegistry.Menu_XPT.get(), NVLXPTScreen::new);
        MenuScreens.register(NVLBlockMenuRegistry.Menu_ETT.get(), NVLETTScreen::new);
        MenuScreens.register(NVLBlockMenuRegistry.Menu_HS.get(), NVLHoloSignScreen::new);
        MenuScreens.register(NVLBlockMenuRegistry.Menu_AC.get(), NVLACScreen::new);
        MenuScreens.register(NVLBlockMenuRegistry.Menu_DE.get(), NVLDEScreen::new);
    }
}
