package com.vimukti.accounter.web.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GettingStartedPortlet extends DashBoardPortlet {
	private VerticalPanel mainPanel;
	private HTML minHtml, allHtml;
	private Label moreLabel;

	public GettingStartedPortlet(String title) {
		super(title);
	}

	@Override
	public void createBody() {
		mainPanel = new VerticalPanel();
		// <li> <a href=''><font color='green'>Watch the Getting Started
		// tour.</font></a>
		// <li>Set up repeating invoices for those invoices you regularly send
		// or receive.
		// <li><a href=''><font color='green'>Create a budget</font></a> for
		// your organisation so that you can compare with actual expenditure
		// throughout the year.
		minHtml = new HTML(
				"<p>Now you are ready to start using Accounter on a regular basis to record and report on normal business transcations. There is <a href=''><font color='green'>full online help</font></a> and tips on each screen in Accounter if you need it. It's really up to you what you do next.</p><ul><li>Add <a href=''><font color='green'>accounts receivable </font></a>and <a href=''><font color='green'>accounts payable </font></a>invoices, <a href=''><font color='green'>bank transactions </font></a>and <a href=''><font color='green'>expense claims.</font></a><li>Add to <a href=''><font color='green'>customers</font></a> or <a href=''><font color='green'>vendors</font></a> the people you regularly transact with.</ul>");
		allHtml = new HTML(
				"<p>Now you are ready to start using Accounter on a regular basis to record and report on normal business transcations. There is <a href=''><font color='green'>full online help</font></a> and tips on each screen in Accounter if you need it. It's really up to you what you do next.</p><ul><li>Add <a href=''><font color='green'>accounts receivable </font></a>and <a href=''><font color='green'>accounts payable </font></a>invoices, <a href=''><font color='green'>bank transactions </font></a>and <a href=''><font color='green'>expense claims.</font></a><li>Add to <a href=''><font color='green'>customers</font></a> or <a href=''><font color='green'>vendors</font></a> the people you regularly transact with.<li><a href=''><font color='green'>Invite other users</font></a> such as your accountant or financial adviser to access your organisation.<li><a href=''><font color='green'>Create any additional bank accounts</font></a> you want to use in Accounter.<li>You can see <a href=''><font color='green'>finance categories</font></a> to check which accounts are effected by your transactions.<li>Run reports over your data to see how your business is going.<li>Click on your <a href=''><font color='green'>name</font></a> to see your details and change your password.</ul>");
		moreLabel = new Label("More");
		moreLabel.addStyleName("lesslabel");
		moreLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (moreLabel.getText().equals("Less")) {
					moreLabel.setText("More");
					if (allHtml.isAttached()) {
						mainPanel.remove(allHtml);
					}
					mainPanel.remove(moreLabel);
					mainPanel.add(minHtml);
					mainPanel.add(moreLabel);

				} else {
					moreLabel.setText("Less");
					if (minHtml.isAttached()) {
						mainPanel.remove(minHtml);
					}
					mainPanel.remove(moreLabel);
					mainPanel.add(allHtml);
					mainPanel.add(moreLabel);
				}

			}
		});

		// mainPanel.add(paraHtml);
		// mainPanel.add(minHtml);
		mainPanel.add(allHtml);
		// mainPanel.add(moreLabel);
		body.add(mainPanel);

	}

	@Override
	public String getGoToText() {
		return "Hide Getting Started";
	}

	@Override
	public void goToClicked() {
		// TODO Auto-generated method stub
		this.setVisible(false);
		Header.changeHelpBarContent("Show Getting Started");
	}

	@Override
	public void helpClicked() {

	}

	public Label getLabel(String title) {
		Label label = new Label(title);
		return label;
	}

}
