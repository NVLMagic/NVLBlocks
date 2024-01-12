package hu.nvl.nvlblocks.Items;

import hu.nvl.nvlblocks.blocks.spt.NVLSPTerminalBlock;
import hu.nvl.nvlblocks.components.base_classes.NVLItem;
import hu.nvl.nvlblocks.Setup.NVLSoundRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class NVLItemEmergencyTeleporter extends NVLItem {
	public NVLItemEmergencyTeleporter()	{ super(); }
	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
//		logLine("onEntitySwing:" + stack.toString());
		boolean r = false;
		if (stack.getItem() == this) {
			if(!entity.level().isClientSide) {
				if (entity instanceof ServerPlayer p) r = useTeleport(p, stack);
			}
		}
		return r;
	}
	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		return super.onLeftClickEntity(stack, player, entity);
	}
	@Override
	public @NotNull InteractionResult useOn(UseOnContext pContext) {
		InteractionResult r;
		Block b = pContext.getLevel().getBlockState(pContext.getClickedPos()).getBlock();
		if (b instanceof NVLSPTerminalBlock && pContext.getPlayer() != null) {
			CompoundTag tag = new CompoundTag();
			tag.putString("u",pContext.getPlayer().getName().getString());
			Vec3 p = pContext.getPlayer().getPosition(0);
			tag.putDouble("x",p.x);
			tag.putDouble("y",p.y);
			tag.putDouble("z",p.z);
			tag.putString("d",pContext.getLevel().dimension().location().getPath());
			Component s = Component.literal(" " + pContext.getLevel().dimension().location().getPath()+" ["+
					(int)p.x + "." + (int)p.y + "." + (int)p.z+"]");
			pContext.getItemInHand().addTagElement("NVL_ETT",tag);
			if (!pContext.getLevel().isClientSide) {
				pContext.getLevel().playSound(null, p.x, p.y, p.z, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
				pContext.getPlayer().sendSystemMessage(mergeComponents(getLocalTextNoClass("spt_item.set"),s));
			}
			pContext.getItemInHand().resetHoverName();
			pContext.getItemInHand().setHoverName(mergeComponents(getLocalTextNoClass("spt_item.belongs"),pContext.getPlayer().getDisplayName(),s));
			r = InteractionResult.PASS; // do not swing
		} else r = super.useOn(pContext);
		return r;
	}
	private boolean useTeleport(ServerPlayer p, ItemStack stack) {
		boolean r = false;
		if (stack != null) {
			if (stack.getItem() == this) {
				if (stack.hasTag()) {
					CompoundTag tag = stack.getTagElement("NVL_ETT");
					if (tag != null) {
						Level w = p.level();
						if (tag.getString("u").equals(p.getName().getString())) {
							if (tag.getString("d").equals(w.dimension().location().getPath())) {
								Vec3 pos = new Vec3(tag.getDouble("x"),tag.getDouble("y"),tag.getDouble("z"));
								p.setHealth(2);
								p.teleportTo(pos.x,pos.y,pos.z);
								p.level().playSeededSound(null, p.position().x, p.position().y, p.position().z,
									NVLSoundRegistry.ETP_SWING.get(), SoundSource.BLOCKS, 0.5f, 1f, 0);
								p.sendSystemMessage(getLocalTextNoClass("spt_item.teleport"));
								r = true;
							} else p.sendSystemMessage(getLocalTextNoClass("spt_item.wrong_dimension"));
						} else p.sendSystemMessage(getLocalTextNoClass("spt_item.wrong_person"));
					} else p.sendSystemMessage(getLocalTextNoClass("spt_item.not_set"));
				} else p.sendSystemMessage(getLocalTextNoClass("spt_item.not_set"));
			} else logLine("useTeleport: wrong item:"+stack);
		} else logLine("useTeleport: stack is NULL");
		return r;
	}
}

