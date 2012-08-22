package com.vimukti.accounterbb.utils;

/**
 * All screen classes extend this screen
 */

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

/**
 * class used to create custom screen with our background as image or a
 * predefined color
 * 
 * @author Jitendra.Balla
 * */
public class AbstractScreen extends MainScreen {

	public static final int TYPE_NON_MODEL = 0;
	public static final int TYPE_MODEL = 1;

	private Bitmap bitmap;
	private int type;
	private VerticalFieldManager scrollManager;

	/**
	 * class used to create custom screen with our background as image
	 * 
	 * @author Jitendra.Balla
	 * */
	public AbstractScreen() {
		this(null);
	}

	public AbstractScreen(int type) {
		this(type, null);
	}

	/**
	 * class used to create custom screen with our background as image
	 * 
	 * @author Jitendra.Balla
	 * */
	public AbstractScreen(Bitmap map) {
		this(TYPE_NON_MODEL, map);
	}

	/**
	 * class used to create custom screen with our background as image
	 * 
	 * @author Jitendra.Balla
	 * */
	public AbstractScreen(int type, Bitmap map) {
		super(NO_VERTICAL_SCROLL);

		this.bitmap = map;
		this.type = type;
		Manager backgrundManager = initManager();
		super.add(backgrundManager);

		scrollManager = new VerticalFieldManager(VERTICAL_SCROLL
				| VERTICAL_SCROLLBAR | USE_ALL_WIDTH);
		backgrundManager.add(scrollManager);
	}

	private Manager initManager() {
		VerticalFieldManager verticalFieldManager = new VerticalFieldManager(
				USE_ALL_HEIGHT | USE_ALL_WIDTH
						| VerticalFieldManager.FIELD_VCENTER) {

			protected void paintBackground(Graphics graphics) {

				Bitmap bmp = new Bitmap(6, 1);

				Graphics g = new Graphics(bmp);

				g.setColor(0xe3ecf9);

				g.drawLine(0, 0, 4, 0);

				g.setColor(Color.WHITE);// 0xe0ecff);

				g.drawLine(5, 0, 5, 0);
				if (bitmap != null) {
					bmp = bitmap;
				}
				int w = Display.getWidth();

				int h = Display.getHeight();
				graphics.drawTexturedPath(new int[] { 0, w, w, 0 }, new int[] {
						0, 0, h, h }, null, null, 0, 0, Fixed32.ONE, 0, 0,
						Fixed32.ONE, bmp);

			}
		};
		return verticalFieldManager;
	}

	protected void onUiEngineAttached(boolean attached) {
		super.onUiEngineAttached(attached);
		if (type == TYPE_NON_MODEL && attached) {
			checkAndAddTabBar();
		}
	}

	/**
	 * This method checks if tab bar is existing, and create it as a status
	 * field
	 */
	private void checkAndAddTabBar() {
	}

	public void add(Field field) {
		scrollManager.add(field);
	}

	public void add(Manager manager) {
	}

	// protected void paintBackground(Graphics graphics) {
	// Bitmap drawBmp = bitmap;
	//
	// if (drawBmp == null) {
	// drawBmp = new Bitmap(6, 1);
	//
	// Graphics g = Graphics.create(drawBmp);
	//
	// g.setColor(0xe3ecf9);
	//
	// g.drawLine(0, 0, 4, 0);
	//
	// g.setColor(Color.WHITE);// 0xe0ecff);
	//
	// g.drawLine(5, 0, 5, 0);
	// }
	//
	// int w = Display.getWidth();
	//
	// int h = Display.getHeight();
	//
	// graphics.drawTexturedPath(new int[] { 0, w, w, 0 }, new int[] { 0, 0,
	// h, h }, null, null, 0, 0, Fixed32.ONE, 0, 0, Fixed32.ONE,
	// drawBmp);
	// }

	protected boolean onSavePrompt() {
		return true;
	}

	/**
	 * @deprecated
	 */
	public void setTitle(Field title) {
		// TODO Auto-generated method stub
		super.setTitle(title);
	}

	public void setTitle(String title) {
		// setTitle(null, title, null);
	}

	public void updateTitle(String updatedTitle) {
	}

}
