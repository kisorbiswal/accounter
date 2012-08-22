package com.vimukti.accounterbb.utils;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.TouchGesture;

import com.vimukti.accounterbb.result.Cell;
import com.vimukti.accounterbb.result.Record;
import com.vimukti.accounterbb.ui.RecordSelectionListener;

public class RecordCellsField extends Field {
	private int height;
	private Record record;
	private RecordSelectionListener listener;

	private static final int RIGHT_MARGIN = 15;
	private static int CELL_HEIGHT = 38;
	private static final int SECOND_CELL_HEIGHT = 18;
	private static final int LEFT_MARGIN = 3;
	private static int TOP_MARGIN = 3;// 12

	private int MULTIPLE_CELLS = 0;

	private int total_Width = Display.getWidth() - 10;

	private Bitmap arrowBitmap = Bitmap.getBitmapResource("images/arrow.png");

	public static final int[] drawFousGainColors = { 0x318CE6, 0x318CE6,
			0x318CE6, 0x318CE6 };

	public static final int[] drawFousLostColors = { Color.WHITE, Color.WHITE,
			Color.WHITE, Color.WHITE };

	public RecordCellsField(Record record) {
		super(FOCUSABLE);
		if (Display.getWidth() == 320 && Display.getHeight() == 240) {
			TOP_MARGIN = 2;
			CELL_HEIGHT = 30;
		}
		this.record = record;
		this.height = calculateHeight();
		this.setPadding(0, 0, 1, 0);
	}

	/**
	 * For calculating the Height of the field based on no.of cells and their
	 * text length
	 * 
	 * @return
	 */
	private int calculateHeight() {
		Font font = Font.getDefault();
		int height = 0;
		int width = total_Width;
		int halfWidth = (width / 2) - RIGHT_MARGIN;
		boolean hasMultipleCells = false;
		if (record.getCells().size() > 1) {
			hasMultipleCells = true;
			MULTIPLE_CELLS = 10;
		}
		for (int i = 0; i < record.getCells().size(); i++) {
			Cell cell = (Cell) record.getCells().elementAt(i);
			String title = cell.getTitle().trim();
			String value = cell.getValue().trim();

			if (value == null || value.trim().length() == 0) {

				boolean titleFitsInHalfWidth = font.getAdvance(title) <= total_Width
						- RIGHT_MARGIN;
				// for checking if value did not contain any value
				if (titleFitsInHalfWidth) {
					height += CELL_HEIGHT - MULTIPLE_CELLS;
				} else {
					// We need height for two rows
					height += CELL_HEIGHT - MULTIPLE_CELLS;
					height += SECOND_CELL_HEIGHT;
					// We will ignore anything that does not fit in two rows...
				}

			} else {
				// for checking for both title and value
				// Check if title fits in half width

				boolean titleFitsInHalfWidth = font.getAdvance(title) <= halfWidth;
				boolean valueFitsInHalfWidth = font.getAdvance(value) <= halfWidth;
				if (titleFitsInHalfWidth && valueFitsInHalfWidth) {
					height += CELL_HEIGHT - MULTIPLE_CELLS;
				} else {
					// We need height for two rows
					height += CELL_HEIGHT - MULTIPLE_CELLS;
					height += SECOND_CELL_HEIGHT;
					// We will ignore anything that does not fit in two rows...
				}
			}
		}
		return height;
	}

	/**
	 * for handling key events
	 */
	protected boolean keyChar(char character, int status, int time) {
		if (character == Keypad.KEY_ENTER) {
			fieldChangeNotify(0);
			return true;
		}
		return super.keyChar(character, status, time);
	}

	/**
	 * for actions
	 */
	protected boolean invokeAction(int action) {
		switch (action) {
		case ACTION_INVOKE:
			// fieldChangeNotify(0);
			return true;
		default:
			return super.invokeAction(action);
		}

	}

	protected boolean navigationClick(int status, int time) {

		fieldChangeNotify(0);
		return true;
	}

	protected boolean trackwheelClick(int status, int time) {
		fieldChangeNotify(0);
		return true;
	}

	protected void layout(int width, int height) {
		setExtent(total_Width, this.height);

	}

	protected void drawFocus(Graphics graphics, boolean on) {
		drawRecord(graphics, true);
	}

	protected void paint(Graphics graphics) {
		drawRecord(graphics, false);
	}

	/**
	 * for drawing the Record with its Cell Title and Values
	 * 
	 * @param graphics
	 * @param b
	 */
	private void drawRecord(Graphics graphics, boolean haveFocus) {
		// graphics.setColor(Color.BLACK);
		// graphics.fillRoundRect(10, 2, Display.getWidth() - 10, height + 5,
		// 10,
		// 10);
		// graphics.drawRoundRect(10, 2, Display.getWidth() - 10, height + 5,
		// 10,
		// 10);

		int color = Color.BLACK;
		int[] colors = drawFousLostColors;
		int[] X_PTS = { getWidth(), getWidth(), getPreferredWidth(),
				getPreferredWidth() };

		int[] Y_PTS = { height, getPreferredHeight(), getPreferredHeight(),
				height };
		if (haveFocus) {
			colors = drawFousGainColors;
			color = Color.WHITE;
		}
		graphics.drawShadedFilledPath(X_PTS, Y_PTS, null, colors, null);
		graphics.setColor(color);

		// for placing the arrow icon
		int image_height = (height / 2) - 8;
		int img_x_position = total_Width - 18;
		graphics.drawBitmap(img_x_position, image_height, arrowBitmap
				.getHeight(), arrowBitmap.getWidth(), arrowBitmap, 0, 0);

		int halfWidth = (getWidth() / 2) - RIGHT_MARGIN;

		int yPosition = 0;
		// Draw each cell

		for (int i = 0; i < record.getCells().size(); i++) {
			Cell cell = (Cell) record.getCells().elementAt(i);

			// Draw the title of the cell
			String text = cell.getTitle().trim();

			// take the value of the cell to draw
			String value = cell.getValue().trim();

			int numberOfRowsForTitle = drawText(text, value, graphics, 0,
					yPosition);

			int numberOfRowsForValue = drawText(value, value, graphics,
					halfWidth, yPosition);

			if (numberOfRowsForTitle == 1 && numberOfRowsForValue == 1) {
				yPosition += CELL_HEIGHT - MULTIPLE_CELLS;
			} else {
				yPosition += CELL_HEIGHT - MULTIPLE_CELLS;
				yPosition += SECOND_CELL_HEIGHT;
			}
		}

	}

	private int drawText(String text, String value, Graphics graphics,
			int xPosition, int yPosition) {

		if (text.trim().length() == 0) {
			return 1;
		}
		int halfWidth = 0;
		if (value.trim().length() == 0) {// if value contains no text, then
			// display title in complete width
			halfWidth = total_Width - RIGHT_MARGIN;
		} else {
			// if value contains text, then display title in half width
			halfWidth = (total_Width / 2) - RIGHT_MARGIN;
		}

		int index = getIndexEnoughToFitIn(text, halfWidth);
		String firstHalf = text.substring(0, index);

		graphics.drawText(firstHalf, xPosition + LEFT_MARGIN, yPosition
				+ TOP_MARGIN);

		// Check if we have more to draw
		if (index < text.length()) {
			String secondHalf = text.substring(index);
			graphics.drawText(secondHalf, xPosition + LEFT_MARGIN, yPosition
					+ SECOND_CELL_HEIGHT + TOP_MARGIN);
			return 2;
		} else {
			return 1;
		}
	}

	/**
	 * Check the text and returns the position up to which we can draw in that
	 * given width
	 * 
	 * @param text
	 * @param halfWidth
	 * @return
	 */
	private int getIndexEnoughToFitIn(String text, int width) {
		Font font = Font.getDefault();
		int index = text.length();
		while (font.getAdvance(text.substring(0, index)) >= width) {
			index--;
		}
		return index;
	}

	/**
	 * for handling touch events
	 */
	protected boolean touchEvent(TouchEvent message) {

		int event = message.getEvent();
		if (event == TouchEvent.GESTURE) {
			int swipeDirection = message.getGesture().getSwipeDirection();
			if (swipeDirection == TouchGesture.SWIPE_EAST
					|| swipeDirection == TouchGesture.SWIPE_WEST
					|| swipeDirection == TouchGesture.DOUBLE_TAP) {
				if (listener != null) {
					listener.recordSelected(null, record);
					return true;
				}
				return true;
			}
		}

		return super.touchEvent(message);
	}

	public void setListener(RecordSelectionListener listener) {
		this.listener = listener;
	}

	public RecordSelectionListener getListener() {
		return listener;
	}
}
