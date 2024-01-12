package hu.nvl.nvlblocks.blocks.ett;

import hu.nvl.nvlblocks.Setup.NVLBlockMenuRegistry;
import hu.nvl.nvlblocks.Setup.NVLBlockRegistry;
import hu.nvl.nvlblocks.components.base_classes.NVLContainerMenu;
import hu.nvl.nvlblocks.components.gui.NVLReadOnlySlot;
import hu.nvl.nvlblocks.components.gui.NVLRestrictedSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;

public class NVLETTMenu extends NVLContainerMenu {
	private static final int SHIFTX = 0;
    private static final int SHIFTY = 8;
	public NVLETTMenu(int pContainerId, Inventory inv, BlockEntity entity) {
		super(NVLBlockMenuRegistry.Menu_ETT.get(), pContainerId, inv, entity);
	}
    public NVLETTMenu (int pContainerId, Inventory inv, FriendlyByteBuf extra) {
		this(pContainerId, inv, inv.player.level().getBlockEntity(extra.readBlockPos()));
    }
	@Override
	protected void addSlots() {
		this.be.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
			addSlot(new NVLETTSlotDeEnc(handler, NVLETTBlockEntity.ETTDSource, 66, SHIFTY+36,true));
			// Result slots
			for (int y = 0; y < 3; y++) for (int x = 0; x < 3; x++)
				addSlot(new NVLReadOnlySlot(handler,NVLETTBlockEntity.ETTDResultStart + x + y*3, 48 + x * 18, SHIFTY + 78 + y * 18,true));
			// Requirements
			addSlot(new NVLRestrictedSlot(handler, NVLETTBlockEntity.ETTBook, 112, SHIFTY + 78,NVLETTBlockEntity.BOOKS,true));
			addSlot(new NVLRestrictedSlot(handler, NVLETTBlockEntity.ETTLapis, 112, SHIFTY + 96,NVLETTBlockEntity.LAPIS,true));
			addSlot(new NVLRestrictedSlot(handler, NVLETTBlockEntity.ETTLapisBlock, 112, SHIFTY + 114,NVLETTBlockEntity.LAPISBLOCK,true));
			addSlot(new NVLReadOnlySlot(handler, NVLETTBlockEntity.ETTBookRequirement, 130, SHIFTY +78,true));
			addSlot(new NVLReadOnlySlot(handler, NVLETTBlockEntity.ETTLapisRequirement, 130, SHIFTY +96,true));
			addSlot(new NVLReadOnlySlot(handler, NVLETTBlockEntity.ETTLapisBlockRequirement, 130, SHIFTY +114,true));
			// The Source slot
			addSlot(new NVLETTSlotEnc(handler, NVLETTBlockEntity.ETTESource, 174, SHIFTY+20,true));
			// Book slots
			for (int y = 0; y < 3; y++) for (int x = 0; x < 3; x++)
				addSlot(new NVLRestrictedSlot(handler, NVLETTBlockEntity.ETTEBookStart + x + y*3, 156 + x * 18, SHIFTY + 44 + y * 18,NVLETTBlockEntity.ENCBOOKS,true));
			// The Result slot
			addSlot(new NVLReadOnlySlot(handler, NVLETTBlockEntity.ETTEResult, 174, SHIFTY +114,true));
			// Book Merge slots
			addSlot(new NVLRestrictedSlot(handler, NVLETTBlockEntity.BMSrc1, 130, SHIFTY +20,NVLETTBlockEntity.ENCBOOKS,true));
			addSlot(new NVLRestrictedSlot(handler, NVLETTBlockEntity.BMSrc2, 112, SHIFTY +38,NVLETTBlockEntity.ENCBOOKS,true));
			addSlot(new NVLReadOnlySlot(handler, NVLETTBlockEntity.BMResult, 130, SHIFTY +56,true));
		});
	}
	@Override
	protected void addPlayerInventory(Inventory playerInventory) {
		for (int r = 0; r < 3; ++r) {
			for (int c = 0; c < 9; ++c) {
				addSlot(new Slot(playerInventory, c + r * 9 + 9, 48 + SHIFTX + c * 18, SHIFTY + 142 + r * 18));
			}
		}
	}
	@Override
	protected void addPlayerHotbar(Inventory playerInventory) {
		for (int i = 0; i < 9; ++i) {
			addSlot(new Slot(playerInventory, i,  48 + SHIFTX + i * 18, SHIFTY + 200));
		}
	}
	@Override
	public void clicked(int pSlotId, int pButton, @NotNull ClickType pClickType, @NotNull Player pPlayer) {
//		logSided("slotClick:"+pSlotId+","+pButton+","+pClickType+","+pPlayer);
		// Disable clicking on the read-only slots
		if (!((NVLETTBlockEntity)be).skipSlot(pSlotId)) {
			((NVLETTBlockEntity)be).processPreClick(pSlotId);
			// This will call the Transfer routines
			super.clicked(pSlotId,pButton,pClickType,pPlayer);
		}
		((NVLETTBlockEntity)be).EvaluateCosts();
	}
	@Override
	public boolean stillValid(@NotNull Player player) {
		return stillValid(ContainerLevelAccess.create(level, be.getBlockPos()), player, NVLBlockRegistry.NVL_Block_ETT.get());
	}
}
