package hu.nvl.nvlblocks.Items;

import hu.nvl.nvlblocks.components.base_classes.NVLItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NVLItemXPToken extends NVLItem {
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        boolean r = false;
        if (entity instanceof Player) {
            if (stack.getItem() instanceof NVLItemXPToken) {
                CompoundTag tag = stack.getTagElement("nvlxpt");
                Component msg;
                if (tag != null) {
                    Player p = (Player) entity;
                    if (tag.getString("u").equals(p.getName().getString())) {
                        p.giveExperiencePoints(tag.getInt("x"));
                        stack.removeTagKey("nvlxpt");
                        msg = Component.translatable("item.nvlblocks.nvl_item_xt.transferred");
                        r = true;
                    } else msg = Component.translatable("item.nvlblocks.nvl_item_xt.notmine");
                } else msg = Component.translatable("item.nvlblocks.nvl_item_xt.empty");
                if (entity.level().isClientSide) entity.sendSystemMessage(msg);
            }
        }
        return r;
    }
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level pLevel, @NotNull List<Component> list, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(stack, pLevel, list, pIsAdvanced);
        if (stack.getItem() instanceof NVLItemXPToken) {
            CompoundTag tag = stack.getTagElement("nvlxpt");
            Component msg = tag == null ? Component.translatable("item.nvlblocks.nvl_item_xt.empty") :
                    Component.literal(Component.translatable("item.nvlblocks.nvl_item_xt.belongsto").getString() + Component.literal(tag.getString("u")).getString());
            list.add(msg);
        }
    }
}
