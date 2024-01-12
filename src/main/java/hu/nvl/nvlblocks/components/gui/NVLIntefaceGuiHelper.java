package hu.nvl.nvlblocks.components.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;

public interface NVLIntefaceGuiHelper {
    default void drawRight(GuiGraphics g, Font font, String text, int rightX, int y, int color) {
        int w = font.width(text);
        g.drawString(font,text,rightX-w,y,color);
    }
}
