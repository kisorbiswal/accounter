package com.vimukti.accounter.web.client.ui;

//import java.awt.Canvas;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Portlet Column for placing Widgets
 * 
 * @author Raj Vimal
 * 
 */
// FIXME Vstack need to be implemented rather than vertical panel
public class PortalColumn extends VerticalPanel {
	private int colNum;

	public PortalColumn(int colNum) {

		// setMembersMargin(3);
		setSize("100%", "100%");
		setColNum(colNum);
		// setAnimateMembers(true);

		// setAnimateMemberTime(2);
		// setCanAcceptDrop(true);
		// setDropLineThickness(10);

		@SuppressWarnings("unused")
		HorizontalPanel dropLineProperties = new HorizontalPanel();
		// dropLineProperties.setBackgroundColor("pink");
		// setDropLineProperties(dropLineProperties);

		// setShowDragPlaceHolder(true);

		@SuppressWarnings("unused")
		SimplePanel placeHolderProperties = new SimplePanel();
		// placeHolderProperties.setBorder("2px solid #8289A6");
		// setPlaceHolderProperties(placeHolderProperties);
		// animateShow(AnimationEffect.SLIDE);
	}

	/**
	 * Getter for Column Number
	 * 
	 * @return colNum
	 */
	public int getColNum() {
		return colNum;
	}

	/**
	 * Setter for Column Number
	 * 
	 * @param colNum
	 */
	public void setColNum(int colNum) {
		this.colNum = colNum;
	}

}