package com.vimukti.accounterbb.utils;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;

public class GridFieldManager extends Manager {
	private int[] columnWidths;
	private int columns;
	private int allRowHeight = -1;
	private boolean useScreenWidth = false;

	/**
	 * Constructs a new GridFieldManager with the specified number of columns.
	 * Rows will be added as needed to display fields. Fields will be added to
	 * the grid in the order they are added to this manager, filling up each row
	 * left-to-right:
	 * 
	 * For example a 2 column manager:
	 * 
	 * [Field1][Field2] [Field3][Field4] [Field5]
	 * 
	 * Column widths are all equal, and the manager will attempt to use all
	 * available width. The height of each row will be equal to the height of
	 * the tallest Field in that row. Field positional styles are respected, so
	 * fields that are smaller than the row/column they are in can be positioned
	 * left, right, top bottom, or centered. They default to top left.
	 * 
	 * @param columns
	 *            Number of columns in the grid
	 * @param style
	 */
	public GridFieldManager(int columns, long style) {
		super(style|FOCUSABLE);
		this.columns = columns;
	}

	public GridFieldManager(int columns, long style, boolean useScreenWidth) {
		super(style);
		this.columns = columns;
		this.useScreenWidth = useScreenWidth;
	}

	public GridFieldManager(int[] columnWidths, long style) {
		super(style);
		this.columnWidths = columnWidths;
		this.columns = columnWidths.length;
	}

	public GridFieldManager(int[] columnWidths, int rowHeight, long style) {
		this(columnWidths, style);
		this.allRowHeight = rowHeight;
	}

	protected boolean navigationMovement(int dx, int dy, int status, int time) {
		int focusIndex = getFieldWithFocusIndex();
		while (dy > 0) {
			focusIndex += columns;
			if (focusIndex >= getFieldCount()) {
				return false; // Focus moves out of this manager
			} else {
				Field f = getField(focusIndex);
				if (f.isFocusable()) { // Only move the focus onto focusable
					// fields
					f.setFocus();
					dy--;
				}
			}
		}
		while (dy < 0) {
			focusIndex -= columns;
			if (focusIndex < 0) {
				return false;
			} else {
				Field f = getField(focusIndex);
				if (f.isFocusable()) {
					f.setFocus();
					dy++;
				}
			}
		}
		while (dx > 0) {
			focusIndex++;
			if (focusIndex >= getFieldCount()) {
				return false;
			} else {
				Field f = getField(focusIndex);
				if (f.isFocusable()) {
					f.setFocus();
					dx--;
				}
			}
		}
		while (dx < 0) {
			focusIndex--;
			if (focusIndex < 0) {
				return false;
			} else {
				Field f = getField(focusIndex);
				if (f.isFocusable()) {
					f.setFocus();
					dx++;
				}
			}
		}
		return true;
	}

	protected void sublayout(int width, int height) {
		int y = 0;
		columnWidths = null;
		if (columnWidths == null) {
			columnWidths = new int[columns];
			for (int i = 0; i < columns; i++) {
				if (useScreenWidth) {
					columnWidths[i] = Graphics.getScreenWidth() / columns;
				} else {
					columnWidths[i] = (width) / columns;
				}
			}
		}
		Field[] fields = new Field[columnWidths.length];
		int currentColumn = 0;
		int rowHeight = 0;
		for (int i = 0; i < getFieldCount(); i++) {
			fields[currentColumn] = getField(i);
//			if (fields[currentColumn] instanceof TabBarItem) {
//				((TabBarItem) fields[i]).setWidthHeight(Display.getWidth()
//						/ getFieldCount(), fields[i].getHeight());
//			}
			layoutChild(fields[currentColumn], columnWidths[currentColumn],
					height - y);
			if (fields[currentColumn].getHeight() > rowHeight) {
				rowHeight = fields[currentColumn].getHeight();
			}
			currentColumn++;
			if (currentColumn == columnWidths.length
					|| i == getFieldCount() - 1) {
				int x = 0;
				if (this.allRowHeight >= 0) {
					rowHeight = this.allRowHeight;
				}
				for (int c = 0; c < currentColumn; c++) {
					long fieldStyle = fields[c].getStyle();
					int fieldXOffset = 0;
					long fieldHalign = fieldStyle & Field.FIELD_HALIGN_MASK;
					if (fieldHalign == Field.FIELD_RIGHT) {
						fieldXOffset = columnWidths[c] - fields[c].getWidth();
					} else if (fieldHalign == Field.FIELD_HCENTER) {
						fieldXOffset = (columnWidths[c] - fields[c].getWidth()) / 2;
					}
					int fieldYOffset = 0;
					long fieldValign = fieldStyle & Field.FIELD_VALIGN_MASK;
					if (fieldValign == Field.FIELD_BOTTOM) {
						fieldYOffset = rowHeight - fields[c].getHeight();
					} else if (fieldValign == Field.FIELD_VCENTER) {
						fieldYOffset = (rowHeight - fields[c].getHeight()) / 2;
					}
					setPositionChild(fields[c], x + fieldXOffset, y
							+ fieldYOffset);
					x += columnWidths[c];
				}
				currentColumn = 0;
				y += rowHeight;
			}
			if (y >= height) {
				break;
			}
		}
		int totalWidth = 0;
		for (int i = 0; i < columnWidths.length; i++) {
			totalWidth += columnWidths[i];
		}
		setExtent(totalWidth, Math.min(y, height));
	}
}
