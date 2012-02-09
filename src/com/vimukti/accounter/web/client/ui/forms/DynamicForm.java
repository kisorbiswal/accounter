package com.vimukti.accounter.web.client.ui.forms;

import java.util.ArrayList;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;

public class DynamicForm extends FlexTable {

	private int noOfColumns = 2;
	private ArrayList<FormItem> formItems = new ArrayList<FormItem>();
	private int nextColumn;
	private int nextRow;
	private int noSpan;
	private boolean isGroup;
	protected AccounterMessages messages = Accounter.getMessages();

	public ValidationResult validate() {
		return FormItem.validate(this.formItems.toArray(new FormItem[formItems
				.size()]));
	}

	public FormItem getField(String name) {
		for (FormItem item : this.formItems) {
			if (item.getName() != null && item.getName().equals(name)) {
				return item;
			}
		}
		return null;
	}

	public void setNumCols(int numberOfColumn) {
		this.noOfColumns = numberOfColumn;
	}

	public void setFields(FormItem... items) {

		for (FormItem item : items) {
			this.formItems.add(item);
			item.addWidgets(this);
		}
		// if (this.getElement().getElementsByTagName("tr").getLength() != 1)
		// this.getElement().getElementsByTagName("td").getItem(0)
		// .setAttribute("class", "align-form");
	}

	/**
	 * 
	 * @param item
	 */
	public void addField(FormItem item) {
		this.formItems.add(item);
		item.addWidgets(this);
	}

	// public FormItem[] getFields() {
	// return formItems.toArray(new FormItem[formItems.size()]);
	// }
	public void setItems(FormItem... formItems) {

		setFields(formItems);
	}

	public void setDisabled(boolean isDisabled) {

		for (FormItem item : formItems) {
			item.setDisabled(isDisabled);
		}

	}

	public FormItem getItem(String name) {
		return getField(name);
	}

	public ArrayList<FormItem> getFormItems() {
		return formItems;
	}

	public int getNoOfColumns() {
		return noOfColumns;
	}

	int getIndex(FormItem formItem) {
		return this.formItems.indexOf(formItem);
	}

	private int getNextColumn() {
		return nextColumn++;
	}

	private int getNextRow() {
		// Check if we finished all columns
		if (nextColumn >= noOfColumns) {
			nextColumn = 0;
			noSpan = 0;
			nextRow++;
		}
		return nextRow;
	}

	public void add(Widget widget, int span) {
		int row = getNextRow();
		getNextColumn();

		this.setWidget(row, noSpan, widget);

		// Make sure we have used all columns in that row
		for (int x = 1; x < span; x++) {
			getNextRow();
			getNextColumn();
		}
		if (span > 1) {
			getFlexCellFormatter().setColSpan(row, noSpan, span);
		}
		noSpan++;
	}

	public void setIsGroup(boolean isGroup) {
		// this.isGroup=isGroup;

	}

	public void setGroupTitle(String frameTitle) {
		// CaptionPanel panel = new CaptionPanel(frameTitle);
		// panel.add(DynamicForm.this);
		// add(panel);
	}

	@Override
	public void removeAllRows() {
		this.formItems.clear();
		nextRow = 0;
		nextColumn = 0;
		noSpan = 0;
		super.removeAllRows();
	}

	public boolean remove(FormItem formItem) {
		// Validate.
		Widget widget = formItem.getMainWidget();
		if (widget.getParent() != this) {
			return false;
		}

		// Orphan.
		try {
			orphan(widget);
		} finally {
			// Physical detach.
			Element elem = widget.getElement();
			DOM.removeChild(DOM.getParent(elem), elem);

			// Logical detach.
			formItems.remove(formItem);

		}
		removeFormItemTitle(formItem);
		return true;
	}

	private void removeFormItemTitle(FormItem formItem) {
		Widget widget = formItem.getLabelWidget();
		if (widget != null && widget.getParent() == this) {
			orphan(widget);
			Element element = widget.getElement();
			DOM.removeChild(DOM.getParent(element), element);
		}
	}

	@Override
	public void clear() {
		this.removeAllRows();
		super.clear();
	}

	public void setFormItems(ArrayList<FormItem> formitems) {
		this.formItems = formitems;
	}

	public static ValidationResult validate(DynamicForm... dynamicForms) {
		ValidationResult result = new ValidationResult();
		for (DynamicForm form : dynamicForms) {
			if (form != null) {
				result.add(form.validate());
			}
		}
		return result;
	}
}
