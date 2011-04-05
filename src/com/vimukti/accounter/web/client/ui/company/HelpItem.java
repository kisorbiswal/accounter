package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vimukti.accounter.web.client.core.HelpLink;
import com.vimukti.accounter.web.client.ui.forms.CustomDialog;

public class HelpItem extends HorizontalPanel {
	public FlexTable table;
	public int currentrow, currentcolumn;

	public HelpItem() {
		createControls();
	}

	private void createControls() {
		CaptionPanel panel = new CaptionPanel();
		panel.setCaptionText("Help Links");
		panel.setStyleName("help-links1");
		table = new FlexTable();
		panel.add(table);
		add(panel);
		this.setCellHorizontalAlignment(panel, ALIGN_LEFT);
		this.setSize("100%", "100%");

	}

	public void addLinks(List<HelpLink> helplinks) {
		for (HelpLink helpLink : helplinks) {
			addLink(helpLink);
		}
	}

	public void addLink(final HelpLink helpLink) {
		final Anchor hyperlink = new Anchor();
		hyperlink.setText(helpLink.getTitle());
		hyperlink.addClickHandler(new ClickHandler() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(ClickEvent event) {

				CustomDialog dialog = new CustomDialog(false, true);
				HTML data = new HTML("<p>" + helpLink.getMessage() + "</p");
				data.setStyleName("help-data");
				dialog.add(data);
				dialog.setSize("350px", "350px");
				dialog.setPopupPosition(hyperlink.getAbsoluteLeft(), hyperlink
						.getAbsoluteTop() - 360);
				dialog.setAutoHideEnabled(true);
				dialog.show();

			}
		});
		table.setWidget(currentrow, currentcolumn, hyperlink);
		table.setWidth("100%");
		currentcolumn++;
		if (currentcolumn > 1) {
			currentrow++;
			currentcolumn = 0;
		}
	}

}
