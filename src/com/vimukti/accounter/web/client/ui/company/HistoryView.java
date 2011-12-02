package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;

public class HistoryView extends BaseView {

	private final int objType;
	private final long objID;
	private final VerticalPanel mainVerticalPanel;
	private final String pageName;

	@Override
	protected void createButtons(ButtonBar buttonBar) {

	}

	public HistoryView(int objectType, long objectID, String dataString) {

		objType = objectType;
		objID = objectID;
		mainVerticalPanel = new VerticalPanel();
		pageName = dataString;
	}

	@Override
	public void init() {

		super.init();
		createControls();
		setSize("100%", "100%");

	}

	private void createControls() {
		Accounter.createHomeService().getAuditHistory(objType, objID,
				new AsyncCallback<ArrayList<String>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ArrayList<String> result) {
						showItems(result);
					}

				});
	}

	protected void showItems(ArrayList<String> result) {

		String name = pageName + " No: " + Long.toString(objID);

		Label pageNameLabel = new Label();
		pageNameLabel.setText(name);
		pageNameLabel.addStyleName("historyPageTitle");

		mainVerticalPanel.add(pageNameLabel);

		for (String string : result) {
			mainVerticalPanel.add(createControl(string));
		}

		this.add(mainVerticalPanel);
		this.setCellHorizontalAlignment(mainVerticalPanel,
				HasAlignment.ALIGN_LEFT);

	}

	private DisclosurePanel createControl(String content) {
		DisclosurePanel panel = new DisclosurePanel("change");

		JSONValue value = JSONParser.parseLenient(content);
		HistoryItem item = new HistoryItem(value.isArray());

		panel.setContent(item);
		return panel;
	}

	public HistoryItem getHistoryItem(String currentItem, String previousItem) {
		return null;

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public List getForms() {
		// TODO Auto-generated method stub
		return null;
	}

}
