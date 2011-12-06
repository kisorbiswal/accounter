package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCustomField;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.customers.CustomerView;
import com.vimukti.accounter.web.client.ui.edittable.CustomFieldTable;
import com.vimukti.accounter.web.client.ui.vendors.VendorView;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class CustomFieldDialog extends BaseDialog {

	public CustomFieldTable customFieldTable;
	public Button addNew;
	private AbstractBaseView parentView;

	private List<ClientCustomField> allRows = new ArrayList<ClientCustomField>();

	public CustomFieldDialog(AbstractBaseView view, String title,
			String description) {
		super(title, description);
		this.parentView = view;
		setWidth("650px");
		okbtn.setText(Accounter.messages().save());
		createControls();
		setPopupPosition(200, 200);
		initData();

	}

	private void initData() {
		customFieldTable.addRows(getCompany().getCustomFields());

	}

	public void createControls() {
		customFieldTable = new CustomFieldTable();

		addNew = new Button();
		addNew.setText(Accounter.messages().add());
		addNew.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				customFieldTable.setDisabled(false);
				ClientCustomField customField = new ClientCustomField();
				customFieldTable.add(customField);

			}
		});
		VerticalPanel layout = new VerticalPanel();
		layout.setWidth("100%");
		layout.setSpacing(10);
		layout.add(customFieldTable);
		layout.add(addNew);
		setBodyLayout(layout);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean onOK() {

		allRows = customFieldTable.getAllRows();
		if (!allRows.isEmpty())
			for (ClientCustomField cf : allRows) {
				saveOrUpdate(cf);
				if (!getCompany().getCustomFields().contains(cf)) {
					getCompany().getCustomFields().add(cf);
				}
			}
		if (parentView instanceof CustomerView) {
			((CustomerView) parentView).updateCustomFields();
		} else {
			((VendorView) parentView).updateCustomFields();
		}

		return true;

	}

	@Override
	public void setFocus() {

	}

	@Override
	protected ValidationResult validate() {
		return super.validate();
		// ValidationResult v = new ValidationResult();
		// List<ClientCustomField> allRows = customFieldTable.getAllRows();
		// for (ClientCustomField c : allRows) {
		//
		// }
		// return v;
	}

}
