package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AddButton;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterDOM;
import com.vimukti.accounter.web.client.ui.core.BaseView;
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
	private DynamicForm form;
	public RadioGroupItem salesTypeRadio;

	private ArrayList<DynamicForm> listforms;

	public VATGroupView() {

		super();

	}

	@Override
	public void init() {
		super.init();
		createControls();
		initView();
		setSize("100%", "100%");
	}

	private void createControls() {

		Label infoLabel = new Label(Accounter.constants().newVATGroup());
		infoLabel.setStyleName(Accounter.constants().labelTitle());

		listforms = new ArrayList<DynamicForm>();

		groupName = new TextItem(Accounter.constants().groupNameOrNumber());
		groupName.setRequired(true);

		desc = new TextItem(Accounter.constants().description());

		groupName.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				desc.setValue(groupName.getValue().toString());

			}
		});

		salesTypeRadio = new RadioGroupItem(Accounter.constants().groupType());
		salesTypeRadio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				gridView.filterVATItems(salesTypeRadio.getValue());
			}
		});
		salesTypeRadio.setValueMap(Accounter.constants().salesType(), Accounter
				.constants().purchaseType());
		if (getData() != null) {
			if (data.isSalesType())
				salesTypeRadio.setDefaultValue(Accounter.constants()
						.salesType());
			else
				salesTypeRadio.setDefaultValue(Accounter.constants()
						.purchaseType());
		} else {
			setData(new ClientTAXGroup());
			salesTypeRadio.setDefaultValue(Accounter.constants().salesType());
		}
		checkbox = new CheckboxItem(Accounter.constants().itemIsActive());
		checkbox.setValue(true);

		form = new DynamicForm();
		form.setFields(groupName, desc, salesTypeRadio, checkbox);

		HTML label = new HTML(Accounter.constants().enterEachIndividualVAT());
		AddButton addButton = new AddButton(this);

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

		this.add(mainPanel);

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

	private void initView() {
		if (isEdit) {
			groupName.setValue(data.getName());
			desc.setValue(data.getDescription());
			// salesTypeRadio.setValue(data.isSalesType());
			checkbox.setValue(data.isActive());
			gridView.addRecords(data.getTaxItems());
			gridView.updateGroupRate();
		}

	}

	@Override
	public void saveAndUpdateView() {

		data.setName(groupName.getValue().toString());
		data.setDescription(desc.getValue().toString());
		data.setActive((Boolean) checkbox.getValue());
		data.setTaxItems(gridView.getRecords());
		if (salesTypeRadio.getValue().equals("Sales Type"))
			data.setSalesType(true);
		else
			data.setSalesType(false);

		saveOrUpdate(data);

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		// BaseView.errordata.setHTML(FinanceApplication.constants()
		// .duplicationOfVATGroupIsNotAllowed());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		// addError(this, Accounter.constants()
		// .duplicationOfVATGroupIsNotAllowed());
		AccounterException accounterException = (AccounterException) exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			// if (takenVatGroup == null)
			// Accounter.showInformation(FinanceApplication.constants()
			// .newVATGroupCreated());
			// else
			// Accounter.showInformation(FinanceApplication.constants()
			// .VATGroupUpdatedSuccessfully());

			super.saveSuccess(result);

		} else {
			saveFailed(new AccounterException());
		}

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		String name = groupName.getValue().toString();

		ClientTAXGroup vatGroupsbyname = getCompany().getVatGroupsbyname(name);

		if (!((isEdit && vatGroupsbyname != null || vatGroupsbyname.getID() == this
				.getData().getID()) ? false : true)
				|| (isEdit ? (data.getName().equalsIgnoreCase(name) ? true
						: (vatGroupsbyname != null) ? false : true) : true)) {
			result.addError(groupName, Accounter.constants().alreadyExist());
			result.add(form.validate());
		}

		if (gridView != null && gridView.getRecords().isEmpty()) {
			result.addError(gridView, Accounter.constants()
					.pleaseenteraTransaction());
		}
		result.add(gridView.validateGrid());

		return result;
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
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void fitToSize(int height, int width) {

	}

	@Override
	public void onEdit() {
		// not required

	}

	@Override
	public void print() {
		// not required

	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public void printPreview() {
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().newVATGroup();
	}
}
