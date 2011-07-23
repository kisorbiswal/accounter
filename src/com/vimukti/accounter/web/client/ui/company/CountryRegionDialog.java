package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

@SuppressWarnings("unchecked")
public class CountryRegionDialog extends BaseDialog {

	CompanyMessages companyConstants = GWT.create(CompanyMessages.class);
	private DynamicForm form1;
	private DynamicForm form2;
	private VerticalPanel bodyLayout;

	public CountryRegionDialog(String title, String desc) {

		super(title, desc);

		createControls();
		center();
	}

	private void createControls() {

		bodyLayout = new VerticalPanel();
		form1 = new DynamicForm();
		form2 = new DynamicForm();

		TextItem countryRegionNameItem = new TextItem();
		countryRegionNameItem.setTitle(companyConstants.countryRegionName());
		// countryRegionNameItem.setWidth("100%");
		countryRegionNameItem.setRequired(true);

		TextItem a3CodeItem = new TextItem();
		a3CodeItem.setTitle(companyConstants.athreeCode());
		// a3CodeItem.setWrapTitle(false);

		TextItem a2CodeItem = new TextItem();
		a2CodeItem.setTitle(companyConstants.atwoCode());
		// a2CodeItem.setWrapTitle(false);

		TextItem isoCodeItem = new TextItem();
		isoCodeItem.setTitle(companyConstants.isoCode());
		// isoCodeItem.setWrapTitle(false);

		form1.setFields(countryRegionNameItem);
		form2.setFields(a3CodeItem, a2CodeItem, isoCodeItem);
		form2.setGroupTitle(companyConstants.countryRegionCode());
		form2.setIsGroup(true);

		bodyLayout.add(form1);
		bodyLayout.add(form2);
		// bodyLayout.setWidth("100%");
		// bodyLayout.setMargin(20);

		setBodyLayout(bodyLayout);

		setSize("350", "300");

	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(Throwable exception) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return Accounter.getActionsConstants().countryRegionList();
	}

}
