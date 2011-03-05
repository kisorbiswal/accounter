package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;

@SuppressWarnings("unchecked")
public class ConversationBalancesView extends AbstractBaseView {
	private HTML superHeaderHtml, headerHtml;
	private VerticalPanel headerPanel, bodyPanel, mainPanel, tabBodyPanel;
	private HorizontalPanel buttonPanel;
	private Button addComparativeBalancesButton, conversionDateButton;
	private DecoratedTabPanel tabPanel;

	// private TabPanel

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {
		headerPanel = new VerticalPanel();
		bodyPanel = new VerticalPanel();
		mainPanel = new VerticalPanel();
		buttonPanel = new HorizontalPanel();

		superHeaderHtml = new HTML("General Settings >");
		headerHtml = new HTML("Conversation Balances");

		addComparativeBalancesButton = new Button("Add Comparative Balances");
		conversionDateButton = new Button("Conversion Date");

		buttonPanel.add(addComparativeBalancesButton);
		buttonPanel.add(conversionDateButton);

		headerPanel.add(superHeaderHtml);
		headerPanel.add(headerHtml);

		bodyPanel.add(buttonPanel);
		try {
			tabPanel = new DecoratedTabPanel();
			tabPanel.add(getBodyControls(), Utility
					.getCurrentFiscalYearStartDate().toString()
					+ " _ " + Utility.getCurrentFiscalYearEndDate().toString());

			bodyPanel.add(tabPanel);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		mainPanel.add(headerPanel);
		mainPanel.add(bodyPanel);

		add(mainPanel);
	}

	private VerticalPanel getBodyControls() {
		tabBodyPanel = new VerticalPanel();
		return tabBodyPanel;

	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

}
