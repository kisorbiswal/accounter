package com.vimukti.accounter.web.client.ui.widgets;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * 
 * @author P.Praneeth
 * 
 */

public class BizDecorPanel extends FlexTable {
	private AutoFillWidget TC;
	private AutoFillWidget MC;
	private boolean isActionsAsTitle;

	public BizDecorPanel() {
		this(null);
	}

	public BizDecorPanel(String title) {
		this(title, false);
	}

	public BizDecorPanel(String title, Boolean isActionsAsTitle) {
		setDecorator(title);
		this.setStyleName("biz-decor-panel");
		if (title != null) {
			setPanelTitle(title);
		} else if (isActionsAsTitle) {
			this.isActionsAsTitle = isActionsAsTitle;
		} else {
			this.addStyleName("no-title");
		}
	}

	/**
	 * Initiates the layout
	 */
	private void setDecorator(String title) {
		setFirstRow();
		setSecondRow();
		setThirdRow();
		setStyles(title);
	}

	/**
	 * Creates the first row of Panel
	 */
	private void setFirstRow() {
		Label TL = new Label();
		TC = new AutoFillWidget();
		Label TR = new Label();
		this.setWidget(0, 0, TL);
		this.setWidget(0, 1, TC);
		this.setWidget(0, 2, TR);
	}

	/**
	 * Creates the second row of Panel
	 */
	private void setSecondRow() {
		// Label ML = new Label();
		MC = new AutoFillWidget();
		// Label MR = new Label();
		// this.setWidget(1, 0, ML);
		AutoFillWidget c = new AutoFillWidget();
		c.add(MC);

		MC.setStyleName("decor-left");
		this.setWidget(1, 0, c);
		this.getFlexCellFormatter().setColSpan(1, 0, 3);
		// this.setWidget(1, 2, MR);
	}

	/**
	 * Creates the third row of Panel
	 */
	private void setThirdRow() {
		Label BL = new Label();
		Label BC = new Label();
		Label BR = new Label();
		this.setWidget(2, 0, BL);
		this.setWidget(2, 1, BC);
		this.setWidget(2, 2, BR);
	}

	/**
	 * Sets the required styles to cells
	 */
	private void setStyles(String title) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (i == 1 && j != 0)
					break;
				this.getCellFormatter().setStyleName(i, j, "td" + i + j);
				this.getWidget(i, j).addStyleName("decor" + i + j);
			}
		}
	}

	/**
	 * Sets the panel title.
	 * 
	 * @param title
	 */
	private void setPanelTitle(String title) {
		final Label panelTitle = new Label(title);
		panelTitle.setTitle(Accounter.messages().clickThisObjToOpen(
				Accounter.constants().link(),
				Accounter.messages().allTransactionDetails(title)));
		panelTitle.addStyleName("panel-title");
		panelTitle.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				panelTitle.getElement().getStyle().setCursor(getTitleCursor());
				panelTitle.getElement().getStyle()
						.setTextDecoration(getTitleDecoration());
			}
		});
		panelTitle.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				panelTitle.getElement().getStyle()
						.setTextDecoration(TextDecoration.NONE);
			}
		});
		panelTitle.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				panelTitle.getElement().getStyle()
						.setTextDecoration(TextDecoration.NONE);
				titleClicked();

			}
		});
		TC.add(panelTitle);
	}

	public Cursor getTitleCursor() {
		return Cursor.AUTO;
	}

	public TextDecoration getTitleDecoration() {
		return TextDecoration.NONE;
	}

	public void titleClicked() {

	}

	/**
	 * Panel header can also have tools. Those are added here
	 * 
	 * @param widget
	 */
	public void setTitleWidget(Widget widget) {
		TC.add(widget);
	}

	/**
	 * Adds the widget to the centre cell
	 * 
	 * @param widget
	 */
	public void setWidget(Widget widget) {
		MC.add(widget);
		widget.addStyleName("main-w");
	}

	/**
	 * Returns the added widget
	 * 
	 * @return
	 */
	public Widget getWidget() {
		return super.getWidget(1, 1);
	}

	@Override
	public void setHeight(String height) {
		if (height.contains("%")) {
			super.setHeight(height);
			MC.setHeight(height);
		} else {
			super.setHeight("");
			int h = Integer.parseInt(height.replace("px", ""));
			if (h > 0) {
				MC.setHeight(h + "px");
			}

		}
	}

	@Override
	public void setWidth(String width) {
		if (width.contains("%")) {
			super.setWidth(width);
			// MC.setWidth(width);
		} else {
			super.setWidth("");
			int w = Integer.parseInt(width.replace("px", ""));
			if (w > 0) {
				MC.setWidth(w + "px");
			}

		}
	}

	/**
	 * Behaves same as setWidget()
	 */
	@Override
	public void add(Widget widget) {
		MC.add(widget);
		widget.addStyleName("main-w-finance");
		widget.setWidth("");
	}

	public void setActions(Widget actions) {
		if (isActionsAsTitle) {
			TC.add(actions);
		}
	}

	public void doAnimate(final Boolean isMinimizing) {
		// new Timer() {
		//
		// @Override
		// public void run() {
		// int height = MC.getOffsetHeight();
		// if ((isMinimizing ? height - 5 <= 0 : height + 5 >= MaxHeight)) {
		// BizDecorPanel.this.getWidget(1, 0).setHeight(
		// (isMinimizing ? 0 : height
		// + (MaxHeight - height))
		// + "px");
		// BizDecorPanel.this.getWidget(1, 2).setHeight(
		// (isMinimizing ? 0 : height
		// + (MaxHeight - height))
		// + "px");
		// MC.setHeight((isMinimizing ? 0 : height
		// + (MaxHeight - height))
		// + "px");
		// cancel();
		// return;
		// }
		// BizDecorPanel.this.getWidget(1, 0).setHeight(
		// (isMinimizing ? height - 5 : height + 5) + "px");
		// BizDecorPanel.this.getWidget(1, 2).setHeight(
		// (isMinimizing ? height - 5 : height + 5) + "px");
		// MC.setHeight((isMinimizing ? height - 5 : height + 5) + "px");
		// if (isMinimizing) {
		// MC.addStyleName("decor-animate");
		// }
		// }
		// }.scheduleRepeating(1);
		if (isMinimizing) {
			// BizDecorPanel.this.getWidget(1, 0).setVisible(false);
			// BizDecorPanel.this.getWidget(1, 2).setVisible(false);
			MC.setVisible(false);
		} else {
			// BizDecorPanel.this.getWidget(1, 0).setVisible(true);
			// BizDecorPanel.this.getWidget(1, 2).setVisible(true);
			MC.setVisible(true);
		}
		// BizDecorPanel.this.getWidget(1, 0).setVisible(false);
		// BizDecorPanel.this.getWidget(1, 2).setHeight(
		// (isMinimizing ? 0 :MaxHeight)
		// + "px");
		// MC.setHeight((isMinimizing ? 0 :MaxHeight)
		// + "px");

	}
}
