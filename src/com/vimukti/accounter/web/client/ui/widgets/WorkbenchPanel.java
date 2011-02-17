package com.vimukti.accounter.web.client.ui.widgets;

import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.ui.Widget;

public class WorkbenchPanel extends BizDecorPanel {
	// private boolean showTitle;
	// private AutoFillWidget body;
	// private String titleText;
	// private Label title;
	// private AutoFillWidget content;
	private boolean isActionsAsTitle;

	public WorkbenchPanel() {
		this(false, null, false);
	}

	public WorkbenchPanel(String title) {
		this(true, title, false);
	}

	public WorkbenchPanel(Boolean isActionsAsTitle) {
		this(false, null, true);
		this.isActionsAsTitle = isActionsAsTitle;
	}

	public WorkbenchPanel(boolean showTitle, String title,
			Boolean isActionsAsTitle) {
		super(title, isActionsAsTitle);
		// addStyleName("panel");
		// this.showTitle = showTitle;
		// this.titleText = title;
		//
		// createBody();
		// if (showTitle) {
		// createTitle();
		// }else{
		//			
		//			
		// }
	}

	// private void createTitle() {
	// AutoFillWidget title = new AutoFillWidget();
	// title.setHeight("25px");
	// title.addStyleName("panelTitle");
	// super.add(title);
	// createBorders(title.getElement());
	//
	// // Create the title text
	// this.title = new Label(titleText);
	// title.addStyleName("panelTitleText");
	// title.add(this.title);
	// }
	//
	// private void createBody() {
	// body = new AutoFillWidget();
	// body.addStyleName("panelBody");
	// if (showTitle) {
	// body.setTop("25px");
	// }
	// super.add(body);
	// createBorders(body.getElement());
	// body.getElement().getStyle().setOverflow(Overflow.HIDDEN);
	// this.content=new AutoFillWidget();
	// this.content.addStyleName("panelContent");
	// if(!showTitle){
	// this.content.addStyleName("no-title-container");
	// }
	// body.add(content);
	// }

	/**
	 * This method creates 8 empty divs in the element to create a rounding
	 * border affect
	 * 
	 * @param parent
	 */
	// private void createBorders(Element parent) {
	// Element top = DOM.createDiv();
	// Element leftTop = DOM.createDiv();
	// Element rightTop = DOM.createDiv();
	// Element right = DOM.createDiv();
	// if(showTitle){
	// top.setClassName("panelTop");
	// leftTop.setClassName("panelLeftTop");
	// rightTop.setClassName("panelRightTop");
	// right.setClassName("panelRight");
	// }else{
	// top.addClassName("shade-round-c");
	// leftTop.addClassName("shade-round-l");
	// rightTop.addClassName("shade-round-r");
	// right.setClassName("shade-right");
	// }
	//
	// Element left = DOM.createDiv();
	// left.setClassName("panelLeft");
	//		
	// Element bottom = DOM.createDiv();
	// bottom.setClassName("panelBottom");
	//
	// Element leftBottom = DOM.createDiv();
	// leftBottom.setClassName("panelLeftBottom");
	//
	// Element rightBottom = DOM.createDiv();
	// rightBottom.setClassName("panelrightBottom");
	//
	// DOM.appendChild(parent, top);
	// DOM.appendChild(parent, leftTop);
	// DOM.appendChild(parent, rightTop);
	//
	// DOM.appendChild(parent, left);
	// DOM.appendChild(parent, right);
	// DOM.appendChild(parent, bottom);
	// DOM.appendChild(parent, leftBottom);
	// DOM.appendChild(parent, rightBottom);
	//
	// }

	@Override
	public void add(Widget w) {
		super.add(w);
	}

	public void addToMain(Widget w) {
		super.setTitleWidget(w);
	}

	@Override
	public void clear() {
		super.clear();
	}

	public void setScroll(boolean b) {
		// if (b) {
		// content.getElement().getStyle().setOverflow(Overflow.AUTO);
		// } else {
		// content.getElement().getStyle().setOverflow(Overflow.HIDDEN);
		// }
	}

	// public void setText(String text){
	// if(this.title!=null){
	// this.title.setText(text);
	// }
	// }

	public void addMouseOutHandler(MouseMoveHandler mouseMoveHandler) {
		addDomHandler(mouseMoveHandler, MouseMoveEvent.getType());
	}

	// public void doAnimate(){
	// super.doAnimate();
	// }
	public void setActionsAsTitle(Widget child) {
		if (isActionsAsTitle)
			super.setActions(child);
	}

}
