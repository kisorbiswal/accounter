package com.vimukti.accounter.web.client.ui.forms;

import java.util.ArrayList;
import java.util.Set;

import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCustomField;
import com.vimukti.accounter.web.client.core.ClientCustomFieldValue;

public class CustomFieldForm extends DynamicForm {
	public CustomFieldForm() {
		setStyleName("customFields");
	}

	public void createControls(ClientCompany company,
			Set<ClientCustomFieldValue> customFieldValues, boolean isCustomer) {
		clear();
		ArrayList<ClientCustomField> customFields = company.getCustomFields();
		for (ClientCustomField c : customFields) {
			if (c.isShowCustomer() && isCustomer) {
				TextItem t = new TextItem(c.getName());
				setFields(t);
			}
			if (c.isShowVendor() && !isCustomer) {
				TextItem t = new TextItem(c.getName());
				setFields(t);
			}
		}
		if (customFieldValues != null) {
			for (ClientCustomFieldValue cv : customFieldValues) {
				ClientCustomField cf = company.getClientCustomField(cv
						.getCustomField());
				for (FormItem<String> t : getFormItems()) {
					if (t.getTitle().equals(cf.getName())) {
						t.setValue(cv.getValue());
						break;
					}
				}
			}
		}
	}

	public void updateValues(Set<ClientCustomFieldValue> list,
			ClientCompany company, boolean isCustomer) {

		if (list.isEmpty()) {
			for (FormItem<String> textItem : getFormItems()) {
				ClientCustomField field = company
						.getCustomFieldByTitle(textItem.getTitle());
				if (isCustomer) {
					if (field != null && field.isShowCustomer()) {
						ClientCustomFieldValue clientCustomFieldValue = new ClientCustomFieldValue();
						clientCustomFieldValue.setCustomField(field.getID());
						clientCustomFieldValue.setValue(textItem.getValue());
						list.add(clientCustomFieldValue);
					}
				} else {
					if (field != null && field.isShowVendor()) {
						ClientCustomFieldValue clientCustomFieldValue = new ClientCustomFieldValue();
						clientCustomFieldValue.setCustomField(field.getID());
						clientCustomFieldValue.setValue(textItem.getValue());
						list.add(clientCustomFieldValue);
					}
				}
			}
		} else {
			for (FormItem<String> textItem : getFormItems()) {
				ClientCustomField field = company
						.getCustomFieldByTitle(textItem.getTitle());
				if (isCustomer) {
					if (field != null && field.isShowCustomer()) {

						ClientCustomFieldValue clientCustomFieldValue = null;
						for (ClientCustomFieldValue f : list) {
							if (f.getCustomField() == field.getID()) {
								clientCustomFieldValue = f;
								break;
							}
						}
						if (clientCustomFieldValue == null) {// New Custom Field
							clientCustomFieldValue = new ClientCustomFieldValue();
							clientCustomFieldValue
									.setCustomField(field.getID());
							clientCustomFieldValue
									.setValue(textItem.getValue());
							list.add(clientCustomFieldValue);
						} else {
							clientCustomFieldValue
									.setValue(textItem.getValue());
						}
					}
				} else {
					if (field != null && field.isShowVendor()) {

						ClientCustomFieldValue clientCustomFieldValue = null;
						for (ClientCustomFieldValue f : list) {
							if (f.getCustomField() == field.getID()) {
								clientCustomFieldValue = f;
								break;
							}
						}
						if (clientCustomFieldValue == null) {// New Custom Field
							clientCustomFieldValue = new ClientCustomFieldValue();
							clientCustomFieldValue
									.setCustomField(field.getID());
							clientCustomFieldValue
									.setValue(textItem.getValue());
							list.add(clientCustomFieldValue);
						} else {
							clientCustomFieldValue
									.setValue(textItem.getValue());
						}
					}
				}
			}
		}
	}

	@Override
	public void setDisabled(boolean isDisabled) {
		for (FormItem<String> textItem : getFormItems()) {
			textItem.setDisabled(isDisabled);
		}
		super.setDisabled(isDisabled);
	}
}
