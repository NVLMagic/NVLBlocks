package hu.nvl.nvlblocks.components.gui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class NVLButton extends Button {
    private int id = -1;
    public NVLButton(int newID, int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress) {
        super(pX,pY,pWidth,pHeight,pMessage,pOnPress,DEFAULT_NARRATION);
        id = newID;
    }
    public int getID() { return id; }
}
