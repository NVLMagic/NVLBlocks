package hu.nvl.nvlblocks.blocks.xpt;

import hu.nvl.nvlblocks.NVLBlocks;
import hu.nvl.nvlblocks.components.gui.NVLButton;
import hu.nvl.nvlblocks.components.gui.NVLEditBoxNumbers;
import hu.nvl.nvlblocks.components.gui.NVLIntefaceGuiHelper;
import hu.nvl.nvlblocks.components.interfaces.NVLInterfaceLogger;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class NVLXPTScreen extends AbstractContainerScreen<NVLXPTMenu> implements NVLInterfaceLogger, NVLIntefaceGuiHelper {
	protected static ResourceLocation CONTAINER_BACKGROUND =
			new ResourceLocation(NVLBlocks.MODID, "textures/gui/xpt.png");
	protected int SIZEX = 256;
	protected int SIZEY = 256;
	protected int SHIFTY = 63;
	protected static final int SCREENHEIGHT = 124;
	protected static final int SCREENWIDTH = 256;
	protected static final Component TITLE = Component.translatable("gui." + NVLBlocks.MODID + ".nvl_xpt_screen");
	private NVLEditBoxNumbers pAddE;
	private int tick = 0;
	private int cachePlayerStoredXP = 0;
	private int cachePlayerXP = 0;
	private long cacheStoredCommonXP = 0;
	protected Player player;
	private NVLButton BtnAddPersonal;
	private NVLButton BtnRemovePersonal;
	private NVLButton BtnAddCommon;
	private NVLButton BtnRemoveCommon;
	public NVLXPTScreen(NVLXPTMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, TITLE);
		player = pPlayerInventory.player;
	}
	@Override
	protected void init()
	{	this.imageHeight = SIZEY;
		this.imageWidth = SIZEX;
		super.init();
		int left = this.width /2 - SCREENWIDTH /2;
		int top = this.height /2 - SCREENHEIGHT /2;
		// Personal Add
		BtnAddPersonal = addButton(0,10,44,118,20,"gui.nvlblocks.xpt.btn_add");
		BtnRemovePersonal = addButton(1,128,44,118,20,"gui.nvlblocks.xpt.btn_draw");
		BtnAddCommon = addButton(2,10,68,118,20,"gui.nvlblocks.xpt.btn_cmnadd");
		BtnRemoveCommon = addButton(3,128,68,118,20,"gui.nvlblocks.xpt.btn_cmndraw");
		pAddE = new NVLEditBoxNumbers(font,left+128,top+24,52,14,Component.literal(""));
		pAddE.setValue("");
		addRenderableWidget(pAddE);
		// set button states
	}
	private NVLButton addButton(int newID, int x, int y, int w, int h, String key) {
		int left = this.width /2 - this.imageWidth /2;
		int top = this.height /2 - this.imageHeight /2 + SHIFTY;
		NVLButton btn = new NVLButton(newID, left + x, top + y, w, h, Component.translatable(key), this::clicked);
		btn.active = false;
		addRenderableWidget(btn);
		return btn;
	}
	private void updateButtonStates() {
	//	logLine(String.valueOf((long) renderables.size()));
		cachePlayerStoredXP = ((NVLXPTBlockEntity)this.getMenu().be).getStoredPlayerXP(player.getName().getString());
		cacheStoredCommonXP = ((NVLXPTBlockEntity)this.getMenu().be).getCommonXP();
		//cachePlayerXP = player.totalExperience;
		cachePlayerXP = player.experienceLevel;
		if (pAddE.getValue().isEmpty()) {
//			logLine("empty");
		} else {
			int value = pAddE.getValue().isEmpty() ? 0 : Integer.parseInt(pAddE.getValue());
//			logLine("Found data:"+String.valueOf(value)+","+cachePlayerStoredXP+","+cacheStoredCommonXP+","+cachePlayerXP);
			if (BtnAddPersonal != null) BtnAddPersonal.active = value > 0 && cachePlayerXP > 0 && value <= cachePlayerXP;
			if (BtnRemovePersonal != null) BtnRemovePersonal.active = value > 0 && cachePlayerStoredXP > 0 && value <= cachePlayerStoredXP;
			if (BtnAddCommon != null) BtnAddCommon.active = value > 0 && cachePlayerXP > 0 && value <= cachePlayerXP;
			if (BtnRemoveCommon != null) BtnRemoveCommon.active = value > 0 && cacheStoredCommonXP > 0 && value <= cacheStoredCommonXP;
		}
		tick = 0;
	}
	@Override
	protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		pGuiGraphics.blit(CONTAINER_BACKGROUND, i, j+SHIFTY, 0, 0, this.imageWidth, this.imageHeight);
	}
	private String getT(String key) {
		return Component.translatable("gui.nvlblocks.xpt."+key).getString();
	}
	// ---- to disable default table text
	@Override
	protected void renderLabels(@NotNull GuiGraphics g, int pMouseX, int pMouseY) {
		// User
		g.drawString(this.font,player.getDisplayName().getString(),12,SHIFTY + 12,0x00FFFF);
		drawRight(g,font,getT("playerxp"),178,SHIFTY + 12,0xFFFF00);
		g.drawString(this.font, String.valueOf(cachePlayerXP),180,SHIFTY + 12,0x00FFFF);
		drawRight(g,font,getT("amount"),126,SHIFTY + 29,0xFFFF00);
		// Vault private
		drawRight(g,font,getT("privatexp"),75,SHIFTY + 100,0xFFFF00);
		g.drawString(this.font, String.valueOf(cachePlayerStoredXP),78,SHIFTY + 100,0x00FFFF);
		// Vault common
		drawRight(g,font,getT("commonxp"),178,SHIFTY + 100,0xFFFF00);
		g.drawString(this.font, String.valueOf(cacheStoredCommonXP),180,SHIFTY + 100,0x00FFFF);
	}
	// ---- to show tooltips
	@Override
	public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
		if (tick++ > 20) updateButtonStates();
		super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
//		renderTooltip(pGuiGraphics, pMouseX, pMouseY);
	}
	public void clicked(Button button) {
		int btn = ((NVLButton) button).getID();
		if (!pAddE.getValue().isEmpty()) {
			try {
				int amount = Integer.parseInt(pAddE.getValue());
			//		logLine("Clicked: " + btn);
				if (btn == 0) {
					((NVLXPTBlockEntity) this.getMenu().be).sendFromClient(player, amount, 0);
					pAddE.setValue("");
	//				updateButtonStates();
				}
				if (btn == 1) {
					((NVLXPTBlockEntity) this.getMenu().be).sendFromClient(player, -amount, 0);
					pAddE.setValue("");
	//				updateButtonStates();
				}
				if (btn == 2) {
					((NVLXPTBlockEntity) this.getMenu().be).sendFromClient(player, 0, amount);
					pAddE.setValue("");
	//				updateButtonStates();
				}
				if (btn == 3) {
					((NVLXPTBlockEntity) this.getMenu().be).sendFromClient(player,0, -amount);
					pAddE.setValue("");
	//				updateButtonStates();
				}
			} finally {}
		}
//		updateButtonStates();
	}
}
