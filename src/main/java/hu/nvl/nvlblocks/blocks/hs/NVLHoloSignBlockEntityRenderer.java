package hu.nvl.nvlblocks.blocks.hs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import hu.nvl.nvlblocks.components.interfaces.NVLInterfaceLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NVLHoloSignBlockEntityRenderer implements BlockEntityRenderer<NVLHoloSignBlockEntity>, NVLInterfaceLogger {
    public NVLHoloSignBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}
    @Override
    public void render(NVLHoloSignBlockEntity be, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = be.getDisplay();
        if (!stack.isEmpty() && be.isProjecting()) {
            if (be.showWest()) {
                pPoseStack.pushPose();
                pPoseStack.translate(-0.5f, 0.5f, 0.5f);
                pPoseStack.scale(0.7f, 0.7f, 0.7f);
                itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, 255, OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, be.getLevel(), 1);
                pPoseStack.popPose();
            }
            if (be.showEast()) {
                pPoseStack.pushPose();
                pPoseStack.translate(1.5f, 0.5f, 0.5f);
                pPoseStack.scale(0.7f, 0.7f, 0.7f);
                pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
                itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, 255, OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, be.getLevel(), 1);
                pPoseStack.popPose();
            }
            if (be.showNorth()) {
                pPoseStack.pushPose();
                pPoseStack.translate(0.5f, 0.5f, -0.5f);
                pPoseStack.scale(0.7f, 0.7f, 0.7f);
                pPoseStack.mulPose(Axis.YP.rotationDegrees(270));
                itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, 255, OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, be.getLevel(), 1);
                pPoseStack.popPose();
            }
            if (be.showSouth()) {
                pPoseStack.pushPose();
                pPoseStack.translate(0.5f, 0.5f, 1.5f);
                pPoseStack.scale(0.7f, 0.7f, 0.7f);
                pPoseStack.mulPose(Axis.YP.rotationDegrees(90));
                itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, 255, OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, be.getLevel(), 1);
                pPoseStack.popPose();
            }
            if (be.showTop() > 0) {
                pPoseStack.pushPose();
                pPoseStack.translate(0.5f, 1.5f, 0.5f);
                pPoseStack.scale(0.7f, 0.7f, 0.7f);
                if (be.showTop() == 2) pPoseStack.mulPose(Axis.YP.rotationDegrees(90));
                itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, 255, OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, be.getLevel(), 1);
                pPoseStack.popPose();
            }
            if (be.showBottom() > 0) {
                pPoseStack.pushPose();
                pPoseStack.translate(0.5f, -0.5f, 0.5f);
                pPoseStack.scale(0.7f, 0.7f, 0.7f);
                if (be.showBottom() == 2) pPoseStack.mulPose(Axis.YP.rotationDegrees(90));
                itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, 255, OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, be.getLevel(), 1);
                pPoseStack.popPose();
            }
        }
    }
/*
    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        int l = LightTexture.pack(bLight, sLight);
        logLine("lightlevel:"+l);
        return l;
    }
*/
}
