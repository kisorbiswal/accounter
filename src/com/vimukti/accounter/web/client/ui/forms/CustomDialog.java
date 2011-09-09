package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.vimukti.accounter.web.client.ui.Accounter;

public class CustomDialog extends DialogBox {

	private HorizontalPanel captionPanel;
	private HTML caption;
	private Image cross, help;

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

		cross = new Image(Accounter.getFinanceImages().dialougueCloseicon());
		cross.setTitle(Accounter.messages().clickThisTo(
				Accounter.constants().close().toLowerCase(),
				Accounter.constants().dialog()));
		// help = new Image("/images/help-icon.png");
		help = new Image(Accounter.getFinanceImages().helpIcon());
		help.setStyleName("dialog_help_icon");
		help.setTitle(Accounter.messages().clickThisTo(
				Accounter.constants().help(), "").replace(
				Accounter.constants().to().toLowerCase()+" ",
				Accounter.constants().For()).replace(
				Accounter.constants().This(), ""));
		// help.setStyleName("helpAlign");
		caption = new HTML();

		captionPanel = new HorizontalPanel();
		captionPanel.setWidth("100%");
		captionPanel.add(caption);

		imageHorizontalPanel = new HorizontalPanel();
		// imageHorizontalPanel.setSpacing(3);

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

	@Override
	public void onBrowserEvent(Event event) {

		switch (DOM.eventGetType(event)) {
		case Event.ONCLICK:
			Element element = event.getTarget();
			if (cross.getElement().equals(element)) {
				if (onCancel())
					CustomDialog.this.removeFromParent();
			} else if (help.getElement().equals(element)) {
				Window.open("http://help.accounterlive.com/", "_blank", "");
				onHelp();
			}
			break;
		// case Event.ONMOUSEOVER:
		// Element element1 = event.getTarget();
		// if (cross.getElement().equals(element1)) {
		// // cross.setUrl("/images/X-1.png");
		//
		// } else if (help.getElement().equals(element1)) {
		// help.setUrl("/images/Help-1.png");
		// }
		// break;
		// case Event.ONMOUSEOUT:
		// Element element2 = event.getTarget();
		// if (cross.getElement().equals(element2)) {
		// cross.setUrl("/images/dialog-close.png");
		//
		// } else if (help.getElement().equals(element2)) {
		// help.setUrl("/images/help-icon.png");
		// }
		// break;
		default:
			break;
		}
		super.onBrowserEvent(event);
	}

	protected boolean onCancel() {
		return true;
	}

	protected void onHelp() {

	}
}