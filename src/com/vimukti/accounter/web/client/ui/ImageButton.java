package com.vimukti.accounter.web.client.ui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.core.Action;

public class ImageButton {
	private String title;
	private String url;
	private Action action;
	private VerticalPanel panel;
	private Image imgLabel;
	private Label txtLabel;

	public ImageButton(String title) {

		createControls();

	}

	public ImageButton(String title, String url) {
		this.title = title;
		this.url = url;

		createControls();
		addClickHandler();
	}

	private void createControls() {

		panel = new VerticalPanel() {
			@Override
			protected void onAttach() {
				this.getParent().getElement().setAttribute("width", "198px");
				super.onAttach();
			}
		};
		panel.setTitle(title);
		panel.setSpacing(5);
		imgLabel = new Image(url);
		panel.add(imgLabel);
		txtLabel = new Label(title);
		txtLabel.addStyleName("link-lable");
		panel.add(txtLabel);
		int value = title.length();
		int paddingValue = (value / 2) * 5 - 1;
		if (value > 6)
			imgLabel.getElement().getParentElement().getStyle().setPaddingLeft(
					paddingValue, Unit.PX);
		panel.getElement().setAttribute("align", "center");

	}

	public VerticalPanel getImagePanel() {
		return panel;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public VerticalPanel getPanel() {
		return panel;
	}

	private void addClickHandler() {
		imgLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!History.getToken().equals(getAction().getHistoryToken())) {
					
					
				}
				getAction().run(null, false);
			}
		});
		txtLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!History.getToken().equals(getAction().getHistoryToken())) {
					
					
				}
				getAction().run(null, false);
			}
		});
	}

}
