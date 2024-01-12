package hu.nvl.nvlblocks.blocks.ac;

import hu.nvl.nvlblocks.NVLBlocks;
import hu.nvl.nvlblocks.components.interfaces.NVLInterfaceLogger;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class NVLACScreen extends AbstractContainerScreen<NVLACMenu> implements NVLInterfaceLogger {
    protected static ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(NVLBlocks.MODID, "textures/gui/ac.png");
    protected int SIZEX = 256;
    protected int SIZEY = 200;
    protected static final Component TITLE = Component.translatable("gui." + NVLBlocks.MODID + ".nvl_ac_screen");
    public NVLACScreen(NVLACMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, TITLE);
    }
    @Override
    protected void init() {
        this.imageHeight = SIZEY;
        this.imageWidth = SIZEX;
        super.init();
    }
    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        pGuiGraphics.blit(CONTAINER_BACKGROUND,i, j,0,0,this.imageWidth, this.imageHeight);
    }
    // ---- to disable default table text
    @Override
    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) { }
    // ---- to show tooltips
    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}