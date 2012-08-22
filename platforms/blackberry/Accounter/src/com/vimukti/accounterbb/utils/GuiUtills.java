package com.vimukti.accounterbb.utils;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class GuiUtills {
	public static VerticalFieldManager getMainLayout() {
		VerticalFieldManager manager = new VerticalFieldManager(
				Field.USE_ALL_WIDTH) {
			protected void paintBackground(Graphics graphics) {
				graphics.setGlobalAlpha(180);
				graphics.setColor(0xeeeeee);
				graphics.fillRoundRect(2, 2, getScreen().getVirtualWidth() - 5,
						getHeight() - 4, 15, 15);
				graphics.setColor(0x999999);
				graphics.drawRoundRect(2, 2, getScreen().getVirtualWidth() - 5,
						getHeight() - 4, 15, 15);
				graphics.setGlobalAlpha(255);
				graphics.setColor(0x000000);

				graphics.fillRect(5, 10, getScreen().getVirtualWidth() - 10, getHeight() - 20);
			}
		};
		manager.setPadding(5, 5, 5, 5);
		return manager;
	}

}
