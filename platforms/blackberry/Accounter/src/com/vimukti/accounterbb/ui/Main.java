package com.vimukti.accounterbb.ui;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontManager;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;

/**
 * Entry point for the application
 * 
 */
public class Main extends UiApplication {
	public Main() {

		// Set the Font
		FontManager.getInstance().setApplicationFont(
				Font.getDefault().derive(Font.PLAIN, 7, Ui.UNITS_pt));
		if (Display.getWidth() == 360 && Display.getHeight() == 480)
			FontManager.getInstance().setApplicationFont(
					Font.getDefault().derive(Font.PLAIN, 8, Ui.UNITS_pt));
		int directions = Display.DIRECTION_PORTRAIT;
		if ((Display.getWidth() == 640 && Display.getHeight() == 480)
				|| (Display.getWidth() == 480 && Display.getHeight() == 360))
			directions = Display.DIRECTION_LANDSCAPE;
		net.rim.device.api.ui.Ui.getUiEngineInstance().setAcceptableDirections(
				directions);

		UiApplication.getUiApplication().pushScreen(new ResultScreen());
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.enterEventDispatcher();
	}

}
