package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class CustomDialog extends DialogBox {

	private HorizontalPanel captionPanel;
	private HTML caption;
	private Image cross;
	private Anchor help;

	private boolean isShowHelpBtn;
	private boolean isShowCloseBtn;
	private HorizontalPanel imageHorizontalPanel;

	public CustomDialog() {
		showHeader();
	}

	public CustomDialog(boolean isShowCloseandHelpBtns) {

		this.isShowCloseBtn = isShowCloseandHelpBtns;
		this.isShowHelpBtn = isShowCloseandHelpBtns;

		sinkEvents(Event.ONCLICK);

		showHeader();
	}

	public CustomDialog(boolean isShowHelp, boolean isShowClose) {
		this.isShowCloseBtn = isShowClose;
		this.isShowHelpBtn = isShowHelp;

		sinkEvents(Event.ONCLICK);

		showHeader();
	}

	public void showHeader() {

		cross = new Image("/images/dialog-close.png");
//		help = new Image("/images/help-icon.png");
		help = new Anchor("<img class='helpAlign' border=0 src='/images/help-icon.png'/>",true,"http://help.Accounter.com/","_blank");
//		help.setStyleName("helpAlign");
		caption = new HTML();

		captionPanel = new HorizontalPanel();
		captionPanel.setWidth("100%");
		captionPanel.add(caption);

		imageHorizontalPanel = new HorizontalPanel();
//		imageHorizontalPanel.setSpacing(3);

		if (isShowHelpBtn)
			imageHorizontalPanel.add(help);
		if (isShowCloseBtn)
			imageHorizontalPanel.add(cross);

		imageHorizontalPanel.setCellHorizontalAlignment(help,
				HasHorizontalAlignment.ALIGN_RIGHT);
		imageHorizontalPanel.setCellHorizontalAlignment(cross,
				HasHorizontalAlignment.ALIGN_RIGHT);
		
		captionPanel.add(imageHorizontalPanel);
		captionPanel.setStyleName("caption");
		captionPanel.setCellHorizontalAlignment(imageHorizontalPanel,
				HasHorizontalAlignment.ALIGN_RIGHT);

		Element td = getCellElement(0, 1);
		td.setInnerHTML("");
		td.appendChild(captionPanel.getElement());
	}

	@Override
	public void setText(String text) {
		caption.setText(text);
	}

	@Override
	public String getText() {
		return caption.getText();
	}

	public void setHtml(String html) {
		caption.setHTML(html);
	}

	public String getHtml() {
		return caption.getHTML();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONCLICK:
			Element element = event.getTarget();
			if (cross.getElement().equals(element)) {
				if (onCloseClick())
					CustomDialog.this.removeFromParent();
			} else if (help.getElement().equals(element)) {
				onHelpClick();
			}
			break;

		default:
			break;
		}
		super.onBrowserEvent(event);
	}

	protected boolean onCloseClick() {
		return true;
	}

	protected void onHelpClick() {

	}

}
