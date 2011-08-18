/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Administrator
 * 
 */
public class SetupTrackEmployeesPage extends AbstractSetupPage {

	private static SetupTrackEmployeesPageUiBinder uiBinder = GWT
			.create(SetupTrackEmployeesPageUiBinder.class);

	interface SetupTrackEmployeesPageUiBinder extends
			UiBinder<Widget, SetupTrackEmployeesPage> {
	}

	public SetupTrackEmployeesPage() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	protected VerticalPanel getViewBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getViewHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSave() {
		// TODO Auto-generated method stub

	}

}
