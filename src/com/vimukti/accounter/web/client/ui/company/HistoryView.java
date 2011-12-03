package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientActivity;
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
	private final long activityID;

	@Override
	protected void createButtons(ButtonBar buttonBar) {

	}

	public HistoryView(ClientActivity obj) {
		objType = obj.getObjType();
		objID = obj.getObjectID();
		mainVerticalPanel = new VerticalPanel();
		pageName = obj.getDataType();
		activityID = obj.getId();
	}

	@Override
	public void init() {

		super.init();
		createControls();
		setSize("100%", "100%");

	}

	private void createControls() {
		Accounter.createHomeService().getAuditHistory(objType, objID,
				activityID, new AsyncCallback<ArrayList<ClientActivity>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ArrayList<ClientActivity> result) {
						if (result != null) {

							if (result.size() > 1) {
								compareHistoryString(result);
							}
							showItems(result);
						}
					}

				});
	}

	protected void compareHistoryString(ArrayList<ClientActivity> result) {

		for (int i = 0; i < result.size(); i++) {

			ClientActivity activity1 = result.get(i);
			ClientActivity activity2 = result.get(i + 1);

			String auditHistory1 = activity1.getAuditHistory();
			String auditHistory2 = activity2.getAuditHistory();

			JSONValue jSONValue1 = JSONParser.parseLenient(auditHistory1);
			JSONValue jSONValue2 = JSONParser.parseLenient(auditHistory2);

			compare(jSONValue1.isArray(), jSONValue2.isArray());
		}

	}

	private void compare(JSONArray array, JSONArray array2) {

	}

	protected void showItems(ArrayList<ClientActivity> activityList) {

		String name = pageName + " No: " + Long.toString(objID);

		Label pageNameLabel = new Label();
		pageNameLabel.setText(name);
		pageNameLabel.addStyleName("historyPageTitle");

		mainVerticalPanel.add(pageNameLabel);

		for (ClientActivity activity : activityList) {
			mainVerticalPanel.add(createControl(activity));
		}

		this.add(mainVerticalPanel);
		this.setCellHorizontalAlignment(mainVerticalPanel,
				HasAlignment.ALIGN_LEFT);
	}

	private DisclosurePanel createControl(ClientActivity activity) {

		// here we are creating the proper format of time to show
		DateTimeFormat datefmt = DateTimeFormat.getFormat(Accounter
				.getCompany().getPreferences().getDateFormat());
		String dateformat = datefmt.format(new Date(activity.getTime()));
		DateTimeFormat timefmt = DateTimeFormat.getFormat("h:mm a");
		String timeFormat = timefmt.format(new Date(activity.getTime()));

		// created the title
		String title = "Modified by :" + activity.getUserName() + " at "
				+ dateformat + " " + timeFormat;

		DisclosurePanel panel = new DisclosurePanel(title);
		JSONValue value = JSONParser.parseLenient(activity.getAuditHistory());
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
