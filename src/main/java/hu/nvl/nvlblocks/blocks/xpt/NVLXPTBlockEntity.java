package hu.nvl.nvlblocks.blocks.xpt;

import hu.nvl.nvlblocks.NVLBlocks;
import hu.nvl.nvlblocks.Setup.NVLMessageHandler;
import hu.nvl.nvlblocks.Setup.NVLBlockEntityRegistry;
import hu.nvl.nvlblocks.Setup.NVLItemRegistry;
import hu.nvl.nvlblocks.components.base_classes.NVLBlockEntityMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;

public class NVLXPTBlockEntity extends NVLBlockEntityMenu {
	private final ArrayList<ItemStack> players = new ArrayList<>();
	private long commonXP = 0;
	public NVLXPTBlockEntity(BlockPos pos, BlockState state) {
		super(NVLBlockEntityRegistry.BE_XPT.get(), pos, state);
	}
	@Override
	public @NotNull Component getDisplayName() {
		return Component.translatable("blockentity." + NVLBlocks.MODID + ".nvl_xpt");
	}
	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
		return new NVLXPTMenu(pContainerId, this);
	}
	// Player XP
	public int getStoredPlayerXP(String playerName) {
		int amount = 0;
		if (!players.isEmpty()) {
			for (int i = 0; i < players.size() && amount == 0; i++) {
				if (players.get(i) != null) {
					CompoundTag tag = players.get(i).getTagElement("nvlxpt");
					if (tag != null && tag.getString("u").equals(playerName)) amount = tag.getInt("x");
				} else logSided("getStoredPlayerXP:" + i + " is null!");
			}
		}
//		logSided("getStoredPlayerXP:"+playerName+","+amount);
		return amount;
	}
	public void addStoredPlayerXP(Player player, int amount) {
		String pn = player.getName().getString();
//		logSided("addStoredPlayerXP:"+pn+":"+amount);
		int idx = -1;
		int removeIdx = -1;
		int r = -amount;
		for (int i = 0;i < players.size() && idx == -1;i++) {
			ItemStack stack = players.get(i);
			if (stack != null) {
				CompoundTag tag = players.get(i).getTagElement("nvlxpt");
				if (tag != null && tag.getString("u").equals(pn)) {
					idx = i;
					int xp = tag.getInt("x");
					if (amount < 0) {
						int v = xp + amount;
						if (v <= 0) {
							removeIdx = i;
							r += v;
						} else tag.putInt("x", v);
					} else {
						r = -Math.min(amount, player.totalExperience);
						tag.putInt("x", xp - r);
					}
					//player.giveExperiencePoints(r);
					player.giveExperienceLevels(r);
				}
			} else logSided("addStoredPlayerXP:stack is null:"+i);
		}
		if (idx == -1) {
			// Add to not existing player
			if (amount > 0) {
				r = -Math.min(amount,player.totalExperience);
				players.add(newPlayer(pn,-r));
				//player.giveExperiencePoints(r);
				player.giveExperienceLevels(r);
			}
		}
		if (removeIdx > -1)  {
			ArrayList<ItemStack> newList = new ArrayList<>();
			for(int i = 0;i < players.size();i++) if (i != removeIdx) newList.add(players.get(i));
			players.clear();
			players.addAll(newList);
		}
		setChanged();
	}
	// Common XP
	private ItemStack newPlayer(String name, int amount) {
		ItemStack stack = new ItemStack(NVLItemRegistry.Item_xp_token.get());
		CompoundTag tag = new CompoundTag();
		tag.putString("u",name);
		tag.putInt("x",amount);
		stack.addTagElement("nvlxpt",tag);
		return stack;
	}
	private String getStackAsString(ItemStack stack) {
		String r = "";
		if (stack != null) {
			CompoundTag tag = stack.getTagElement("nvlxpt");
			if (tag != null) r = tag.getString("u") + ":" + tag.getInt("x");
		}
		return r;
	}
	private ItemStack getStackFromString(String packed) {
		ItemStack r = null;
		String[] parts = packed.split(":");
		if (parts.length == 2) {
			r = newPlayer(parts[0],Integer.parseInt(parts[1]));
		}
		return r;
	}
	public long getCommonXP() { return commonXP; }
	public void addCommonXP(Player player, int value) {
		int r = -value;
		if (value < 0) {
			commonXP += value;
			if (commonXP < 0) {
				r = (int)commonXP - value;
				commonXP = 0;
			}
		} else {
			r = -Math.min(player.totalExperience,value);
			commonXP -= r;
		}
		//player.giveExperiencePoints(r);
		player.giveExperienceLevels(r);
		setChanged();
	}
	private String compilePlayerXPs() {
		ArrayList<String> temp = new ArrayList<>();
		for (ItemStack p : players) temp.add(getStackAsString(p));
		return String.join(",",temp);
	}
	// NBT functions
	private void setToNBT(CompoundTag nbt) {
		nbt.putLong("nvlxpt_xp",commonXP);
		nbt.putString("nvlxpt_uxp",compilePlayerXPs());
//		logSided("setToNBT");
	}
	private void importPlayerDataFromString(String data) {
		players.clear();
		if (!data.isEmpty()) {
			String[] parts = data.split(",");
			for (String part : parts) players.add(getStackFromString(part));
		}
	}
	private void getFromNBT(CompoundTag nbt) {
		if (nbt.contains("nvlxpt_xp")) {
//			logSided("getFromNBT:xp");
			commonXP = nbt.getLong("nvlxpt_xp");
		}
		if (nbt.contains("nvlxpt_uxp")) {
//			logSided("getFromNBT:uxp");
			importPlayerDataFromString(nbt.getString("nvlxpt_uxp"));
		}
	}
	// NBT
	@Override
	protected void saveAdditional(@NotNull CompoundTag nbt) {
	//	logSided("saveAdditional:");
		super.saveAdditional(nbt);
		setToNBT(nbt);
	}
	@Override
	public void load(@NotNull CompoundTag nbt) {
	//	logSided("load:");
		super.load(nbt);
		getFromNBT(nbt);
	}
	@Override
	public @NotNull CompoundTag getUpdateTag() {
	//	logSided("getUpdateTag:");
		CompoundTag tag = super.getUpdateTag();
		setToNBT(tag);
		return tag;
	}
	// Network packet handlers
	public void setFromClient(Player player, int pXP, int cXP) {
	//	logSided("setFromClient:"+pXP+","+cXP);
		if (cXP != 0) addCommonXP(player,cXP);
		if (pXP != 0) addStoredPlayerXP(player,pXP);
		NVLMessageHandler.sendToAllClients(new NVLXPTNetworkMessageSyncAll(getBlockPos(),compilePlayerXPs(),this.commonXP));
	}
	// Called by the initiating player screen sending it to Server
	public void sendFromClient(Player player, int privateAmount, int commonAmount) {
	//	logSided("sendFromClient:"+privateAmount+":"+commonAmount);
		// Handle self
		if (privateAmount != 0) addStoredPlayerXP(player, privateAmount);
		if (commonAmount != 0) addCommonXP(player, privateAmount);
		// Send amount to server
		NVLMessageHandler.sendToServer(new NVLXPTNetworkMessage(getBlockPos(), privateAmount, commonAmount));
	}
	public void setFromServer(String privateAmount, long commonAmount) {
	//	logSided("setFromServer:"+privateAmount+","+commonAmount);
		importPlayerDataFromString(privateAmount);
		commonXP = commonAmount;
	}
	@Override
	public boolean isEmpty() {
		return commonXP == 0 && players.isEmpty();
	}
    public void dropExperience(BlockPos pos) {
		if (this.level instanceof ServerLevel) {
			while (commonXP >= 100) {
				ExperienceOrb.award((ServerLevel) this.level, Vec3.atCenterOf(pos), 100);
				commonXP -= 100;
			}
			if (commonXP > 0) ExperienceOrb.award((ServerLevel) this.level, Vec3.atCenterOf(pos), (int) commonXP);
			// User tokens
			if (!players.isEmpty()) {
	//			logSided("dropping");
				SimpleContainer inventory = new SimpleContainer(players.size());
				for (int i = 0; i < players.size(); i++) if (players.get(i) != null) inventory.setItem(i, players.get(i));
				Containers.dropContents(this.level, this.worldPosition, inventory);
			}
		}
    }
}