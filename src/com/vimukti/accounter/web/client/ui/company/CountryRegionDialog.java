package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CountryRegionDialog extends BaseDialog {

	private DynamicForm form1;
	private DynamicForm form2;
	private VerticalPanel bodyLayout;
	private TextItem countryRegionNameItem;

	public CountryRegionDialog(String title, String desc) {

		super(title, desc);

		createControls();
		center();
	}

	private void createControls() {

		bodyLayout = new VerticalPanel();
		form1 = new DynamicForm();
		form2 = new DynamicForm();

		countryRegionNameItem = new TextItem();
		countryRegionNameItem.setTitle(Accounter.constants()
				.countryRegionName());
		// countryRegionNameItem.setWidth("100%");
		countryRegionNameItem.setRequired(true);

		TextItem a3CodeItem = new TextItem();
		a3CodeItem.setTitle(Accounter.constants().athreeCode());
		// a3CodeItem.setWrapTitle(false);

		TextItem a2CodeItem = new TextItem();
		a2CodeItem.setTitle(Accounter.constants().atwoCode());
		// a2CodeItem.setWrapTitle(false);

		TextItem isoCodeItem = new TextItem();
		isoCodeItem.setTitle(Accounter.constants().isoCode());
		// isoCodeItem.setWrapTitle(false);

		form1.setFields(countryRegionNameItem);
		form2.setFields(a3CodeItem, a2CodeItem, isoCodeItem);
		form2.setGroupTitle(Accounter.constants().countryRegionCode());
		form2.setIsGroup(true);

		bodyLayout.add(form1);
		bodyLayout.add(form2);
		// bodyLayout.setWidth("100%");
		// bodyLayout.setMargin(20);

		setBodyLayout(bodyLayout);

		setSize("350px", "300px");

	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFocus() {
		countryRegionNameItem.setFocus();

	}

}
