package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class ItemDetailsPanel extends VerticalPanel {
	ClientItem item;
	LabelItem name, availableQty, itemGroup, wareHouse, vendorProNo;
	AmountLabel standardCost, salesPrice;
	Label heading, itemName;
	private DynamicForm leftform, rightform;
	protected static final AccounterMessages messages = Global.get().messages();

	public ItemDetailsPanel(ClientItem item) {
		this.item = item;
		createControls();
		if (item != null) {
			showItemDetails(item);
		}

	}

	private void createControls() {

		name = new LabelItem();
		name.setTitle(messages.name());

		availableQty = new LabelItem();
		availableQty.setTitle(messages.availableQty());

		standardCost = new AmountLabel(messages.standardCost());

		salesPrice = new AmountLabel(messages.salesPrice());

		itemGroup = new LabelItem();
		itemGroup.setTitle(messages.itemGroup());

		wareHouse = new LabelItem();
		wareHouse.setTitle(messages.wareHouse());

		vendorProNo = new LabelItem();
		vendorProNo.setTitle(messages.vendorProductNo(Global.get().Vendor()));

		leftform = new DynamicForm();
		rightform = new DynamicForm();

		leftform.setFields(name, availableQty, standardCost, salesPrice);

		rightform.setFields(itemGroup, wareHouse, vendorProNo);
		rightform.addStyleName("customers_detail_rightpanel");

		HorizontalPanel hp = new HorizontalPanel();
		HorizontalPanel headingPanel = new HorizontalPanel();
		headingPanel.addStyleName("customers_detail_panel");
		heading = new Label(messages.payeeDetails(messages.inventory()) + " :");
		headingPanel.add(heading);
		itemName = new Label();
		itemName.setText(messages.noItemSelected());
		headingPanel.add(heading);
		headingPanel.add(itemName);
		headingPanel.setCellWidth(heading, "50%");
		headingPanel.setCellWidth(itemName, "50%");
		add(headingPanel);
		hp.add(leftform);
		hp.add(rightform);
		leftform.setCellSpacing(10);
		rightform.setCellSpacing(10);
		hp.setCellWidth(leftform, "50%");
		hp.setCellWidth(rightform, "50%");

		add(hp);
		headingPanel.setWidth("100%");
		hp.setWidth("100%");
		this.setWidth("100%");
		hp.getElement().getParentElement().addClassName("details-Panel");
	}

	protected void showItemDetails(ClientItem item) {
		if (item != null) {

			itemName.setText(item.getName());
			name.setValue(item.getName());

			availableQty.setValue(getAvailableQty(item));

			standardCost.setAmount(item.getPurchasePrice());

			salesPrice.setAmount(item.getSalesPrice());

			ClientItemGroup group = Accounter.getCompany().getItemGroup(
					item.getItemGroup());
			if (group != null) {
				itemGroup.setValue(group.getName());
			}

			ClientWarehouse wareHouse = Accounter.getCompany().getWarehouse(
					item.getWarehouse());
			if (wareHouse != null) {
				this.wareHouse.setValue(wareHouse.getName());
			}

			vendorProNo.setValue(item.getVendorItemNumber());

		} else {
			name.setValue("");
			availableQty.setValue("");
			standardCost.setAmount(0.00);
			salesPrice.setAmount(0.00);
			itemGroup.setValue("");
			wareHouse.setValue("");
			vendorProNo.setValue("");
		}
	}

	private String getAvailableQty(ClientItem item) {
		StringBuffer result = new StringBuffer();
		ClientUnit unit = Accounter.getCompany().getUnitById(
				item.getOnhandQty().getUnit());
		result.append(item.getOnhandQty().getValue());
		result.append(" ");
		result.append(unit.getName());
		return result.toString();
	}
}
