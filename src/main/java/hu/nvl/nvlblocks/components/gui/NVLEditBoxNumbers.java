package hu.nvl.nvlblocks.components.gui;

import hu.nvl.nvlblocks.components.interfaces.NVLInterfaceLogger;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.ArrayUtils;

public class NVLEditBoxNumbers extends EditBox implements NVLInterfaceLogger {
    protected boolean editable = true;
    private static final char[] CHARS = {'0','1','2','3','4','5','6','7','8','9'};
    public NVLEditBoxNumbers(Font pFont, int pX, int pY, int pWidth, int pHeight, Component pMessage) {
        super(pFont, pX, pY, pWidth, pHeight, pMessage);
    }

    @Override
    public boolean charTyped(char pCodePoint, int pModifiers) {
        boolean r = false;
        if (this.canConsumeInput()) {
            if (SharedConstants.isAllowedChatCharacter(pCodePoint)) {
                if (this.editable) {
                    if (ArrayUtils.contains(CHARS,pCodePoint)) {
                        this.insertText(Character.toString(pCodePoint));
                        r = true;
                    }
                }
            }
        }
        return r;
    }
    // Had to do this because the builtin has this as private
    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
