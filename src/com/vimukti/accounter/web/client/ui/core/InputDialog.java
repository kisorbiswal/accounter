package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * InputDialog is especially for take input from user using one or more text
 * fields.create its object where you want get some value from user.
 * 
 * @author kumar kasimala
 * 
 */

public class InputDialog extends BaseDialog {

	/**
	 * Instance variables
	 */
	private DynamicForm form;
	private List<TextItem> textItems = new ArrayList<TextItem>();
	private TextItem textItem;
	private String[] itemsNames;
	private GroupDialog<?> parent;

	public InputDialog(GroupDialog<?> parentDialog, String title, String desc,
			String... itemNames) {
		super(title, desc);
		this.itemsNames = itemNames;
		initialise();
		center();
		this.parent = parentDialog;
	}

	/**
	 * Create GUI controls for this Dialog
	 */
	private void initialise() {
		mainPanel.setSpacing(3);
		form = new DynamicForm();
		form.setWidth("100%");
		VerticalPanel layout = new VerticalPanel();
		for (String item : itemsNames)
			addTextItem(item);
		form.setItems(textItems.toArray(new TextItem[1]));
		layout.add(form);
		okbtn.setWidth("60px");
		cancelBtn.setWidth("60px");

		setBodyLayout(layout);
		// setSize("300", "100");
	}

	/**
	 * Add items to this Dialog.It will create one TextItem and append to this
	 * Dialog.
	 * 
	 * @param title
	 */
	private void addTextItem(String title) {
		textItem = new TextItem(title);
		textItem.setHelpInformation(true);
		textItem.setRequired(true);
		textItems.add(textItem);
		// form.setFields(textItems.toArray(new TextItem[1]));
	}

	/**
	 * get All Items that you have added on this Dialog
	 * 
	 * @return
	 */
	public List<TextItem> getTextItems() {
		return this.textItems;
	}

	/**
	 * Get value of textItem by passing it position or location
	 * 
	 * @param index
	 * @return
	 */
	public String getTextValueByIndex(int index) {
		if (this.textItems.get(index) != null
				&& this.textItems.get(index).getValue() != null)
			return this.textItems.get(index).getValue().toString();
		return "";
	}

	/**
	 * set value of textItem by passing it position or location
	 * 
	 * @param index
	 * @return
	 */
	public void setTextItemValue(int index, String value) {
		this.textItems.get(index).setValue(value);
	}

	public DynamicForm getForm() {
		return this.form;
	}

	public void setFocus() {
		cancelBtn.setFocus(true);
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = form.validate();
		result.add(parent.validate());
		return result;
	}

	@Override
	protected boolean onOK() {
		return parent.onOK();
	}

}
