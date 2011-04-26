package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterDOM;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.VATItemListGrid;

public class VATGroupView extends BaseView<ClientTAXGroup> {

	private TextItem groupName;
	private TextItem desc;
	private VATItemListGrid gridView;
	private CheckboxItem checkbox;
	private ClientTAXGroup vatGroup;
	private ClientTAXGroup takenVatGroup;
	private DynamicForm form;
	public RadioGroupItem salesTypeRadio;

	private ArrayList<DynamicForm> listforms;

	public VATGroupView() {

		super();
		validationCount = 4;

	}

	public void init() {
		super.init();
		createControls();
		initView();
		setSize("100%", "100%");
	}

	private void createControls() {

		Label infoLabel = new Label(FinanceApplication.getVATMessages()
				.newVATGroup());
		infoLabel.setStyleName(FinanceApplication.getCustomersMessages()
				.lableTitle());

		listforms = new ArrayList<DynamicForm>();

		groupName = new TextItem(FinanceApplication.getVATMessages()
				.groupNameOrNumber());
		groupName.setRequired(true);

		desc = new TextItem(FinanceApplication.getVATMessages().description());

		groupName.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				desc.setValue(groupName.getValue().toString());

			}
		});

		salesTypeRadio = new RadioGroupItem(FinanceApplication.getVATMessages()
				.groupType());
		salesTypeRadio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				gridView.filterVATItems(salesTypeRadio.getValue());
			}
		});
		salesTypeRadio.setValueMap(FinanceApplication.getVATMessages()
				.salesType(), FinanceApplication.getVATMessages()
				.purchaseType());
		if (takenVatGroup != null) {
			if (takenVatGroup.isSalesType())
				salesTypeRadio.setDefaultValue(FinanceApplication
						.getVATMessages().salesType());
			else
				salesTypeRadio.setDefaultValue(FinanceApplication
						.getVATMessages().purchaseType());
		} else
			salesTypeRadio.setDefaultValue(FinanceApplication.getVATMessages()
					.salesType());
		checkbox = new CheckboxItem(FinanceApplication.getVATMessages()
				.itemIsActive());
		checkbox.setValue(true);

		form = new DynamicForm();
		form.setFields(groupName, desc, salesTypeRadio, checkbox);

		HTML label = new HTML(FinanceApplication.getVATMessages()
				.enterEachIndividualVAT());
		Button addButton = new Button(FinanceApplication.getVATMessages().add());
		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientTAXItem vatItem = new ClientTAXItem();
				gridView.addData(vatItem);
			}
		});

		initListGrid();

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setSpacing(2);
		mainPanel.setHeight("100%");
		mainPanel.add(infoLabel);
		mainPanel.add(form);
		mainPanel.add(addButton);
		mainPanel.add(label);
		mainPanel.add(gridView);

		AccounterDOM.setParentElementHeight(form.getElement(), 10);
		AccounterDOM.setParentElementHeight(addButton.getElement(), 2);
		AccounterDOM.setParentElementHeight(label.getElement(), 2);

		canvas.add(mainPanel);

		/* Adding dynamic forms in list */
		listforms.add(form);

	}

	private void initListGrid() {

		gridView = new VATItemListGrid();

		gridView.setView(this);

		gridView.setCanEdit(true);
		gridView.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		gridView.init();
		gridView.setHeight("300px");

	}

	@Override
	public void setData(ClientTAXGroup data) {
		super.setData(data);
		if (data != null)
			takenVatGroup = (ClientTAXGroup) data;
		else
			takenVatGroup=null;
	}

	private void initView() {
		if (takenVatGroup != null) {
			groupName.setValue(takenVatGroup.getName());
			desc.setValue(takenVatGroup.getDescription());
			salesTypeRadio.setValue(takenVatGroup.isSalesType());
			checkbox.setValue(takenVatGroup.isActive());
			gridView.addRecords(takenVatGroup.getTaxItems());
			gridView.updateGroupRate();
		}

	}

	@Override
	public void saveAndUpdateView() throws Exception {

		if (takenVatGroup == null)
			vatGroup = new ClientTAXGroup();
		else
			vatGroup = takenVatGroup;

		vatGroup.setName(groupName.getValue().toString());
		vatGroup.setDescription(desc.getValue().toString());
		vatGroup.setActive((Boolean) checkbox.getValue());
		vatGroup.setTaxItems(gridView.getRecords());
		if (salesTypeRadio.getValue().equals("Sales Type"))
			vatGroup.setSalesType(true);
		else
			vatGroup.setSalesType(false);

		if (takenVatGroup == null)
			createObject(vatGroup);
		else
			alterObject(vatGroup);

	}

	@Override
	public void saveFailed(Throwable exception) {
		super.saveFailed(exception);
		Accounter.showError(FinanceApplication.getVATMessages()
				.duplicationOfVATGroupIsNotAllowed());
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			// if (takenVatGroup == null)
			// Accounter.showInformation(FinanceApplication.getVATMessages()
			// .newVATGroupCreated());
			// else
			// Accounter.showInformation(FinanceApplication.getVATMessages()
			// .VATGroupUpdatedSuccessfully());

			super.saveSuccess(result);

		} else {
			saveFailed(new Exception());
		}

	}

	@Override
	public boolean validate() throws InvalidEntryException,
			InvalidTransactionEntryException {
		switch (validationCount) {
		case 4:
			String name = groupName.getValue().toString();
			if (((takenVatGroup == null && Utility.isObjectExist(
					FinanceApplication.getCompany().getVatGroups(), name)) ? false
					: true)
					|| (takenVatGroup != null ? (takenVatGroup.getName()
							.equalsIgnoreCase(name) ? true : (Utility
							.isObjectExist(FinanceApplication.getCompany()
									.getVatGroups(), name) ? false : true))
							: true)) {
				return true;
			} else
				throw new InvalidEntryException(AccounterErrorType.ALREADYEXIST);
		case 3:
			AccounterValidator.validateForm(form, false);
			break;
		case 2:
			if (gridView != null && gridView.getRecords().isEmpty()) {
				Accounter
						.showError("Please enter a Transaction.");
				return false;
			}
		case 1:
			return gridView.validateGrid();

		default:
			break;
		}
		return true;
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.groupName.setFocus();
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void fitToSize(int height, int width) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}
}
