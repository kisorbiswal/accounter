package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AddButton;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientJob;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddressForm;
import com.vimukti.accounter.web.client.ui.EmailForm;
import com.vimukti.accounter.web.client.ui.PhoneFaxForm;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.ContactsTable;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyChangeListener;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyComboWidget;

public class JobView extends BaseView<ClientJob> {

	private TextItem jobNameText;
	private List<String> jobstatusList;
	private SelectCombo jobstatusCombo;
	private TextAreaItem memoArea;
	private AddressForm addrsForm;
	private PhoneFaxForm fonFaxForm;
	private EmailForm emailForm;
	private ContactsTable gridView;
	private AddButton addButton;
	private AmountField openingBalText, balanceText;
	private DateField balanceDate, startDate, projectEndDate, endDate;
	private DynamicForm balanceForm, jobForm;
	private CheckboxItem statusCheck;
	private ClientCurrency selectCurrency;
	private CurrencyComboWidget currencyCombo;
	CustomerCombo customerCombo;

	public JobView() {
		super();
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	@Override
	public void initData() {
		if (data == null) {
			setData(new ClientJob());
		}
		jobNameText.setValue(data.getName());
		jobstatusCombo.setComboItem(data.getJobStatus());
		startDate.setEnteredDate(data.getStartDate());
		projectEndDate.setEnteredDate(data.getProjectEndDate());
		endDate.setEnteredDate(data.getEndDate());
		statusCheck.setValue(data.isActive());
		// memoArea.setValue(data.getMemo());
		// openingBalText.setAmount(data.getOpeningBalance());
		// balanceText.setAmount(data.getBalance());
		// balanceDate
		// .setEnteredDate(new ClientFinanceDate(data.getBalanceAsOf()));
		// customerCombo.setComboItem(Accounter.getCompany().getCustomer(
		// data.getCustomer()));
		// fonFaxForm.businessPhoneText.setValue(data.getPhoneNo());
		// fonFaxForm.businessFaxText.setValue(data.getFaxNo());
		// emailForm.businesEmailText.setValue(data.getEmail());
		// emailForm.webText.setValue(data.getWebPageAddress());
		// addrsForm.setAddress(data.getAddress());
		// int row = 0;
		// for (ClientContact clientContact : data.getContacts()) {
		// if (clientContact.isPrimary()) {
		// gridView.add(clientContact);
		// gridView.checkColumn(row, 0, true);
		// } else {
		// gridView.add(clientContact);
		// }
		// row++;
		// }
		super.initData();
	}

	private void createControls() {

		VerticalPanel mainVlay = new VerticalPanel();
		Label titleLabel = new Label(messages.job());
		titleLabel.setStyleName("label-title");

		jobForm = new DynamicForm();

		jobNameText = new TextItem(messages.jobName());
		jobNameText.setToolTip(messages.jobName());
		jobNameText.setHelpInformation(true);
		jobNameText.setRequired(true);
		jobNameText.setDisabled(isInViewMode());

		customerCombo = new CustomerCombo(Global.get().Customer());
		customerCombo.setHelpInformation(true);
		customerCombo.setRequired(true);
		customerCombo.setDisabled(isInViewMode());
		customerCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

					@Override
					public void selectedComboBoxItem(ClientCustomer selectItem) {
						currencyCombo.setSelectedCurrency(Accounter
								.getCompany().getCurrency(
										selectItem.getCurrency()));

					}
				});

		jobstatusCombo = createJobStatusSelectItem();

		startDate = new DateField(messages.startDate());
		startDate.setHelpInformation(true);
		ClientFinanceDate start_date = new ClientFinanceDate();
		start_date.setDay(start_date.getDay());
		startDate.setDatethanFireEvent(start_date);

		projectEndDate = new DateField(messages.projectendDate());
		projectEndDate.setHelpInformation(true);
		ClientFinanceDate projectEnd_date = new ClientFinanceDate();
		projectEnd_date.setDay(projectEnd_date.getDay());
		projectEndDate.setDatethanFireEvent(projectEnd_date);

		endDate = new DateField(messages.endDate());
		endDate.setHelpInformation(true);
		ClientFinanceDate end_date = new ClientFinanceDate();
		end_date.setDay(end_date.getDay());
		endDate.setDatethanFireEvent(end_date);

		statusCheck = new CheckboxItem(messages.active());
		statusCheck.setValue(true);
		statusCheck.setDisabled(isInViewMode());

		jobForm.setFields(jobNameText, jobstatusCombo, customerCombo,
				statusCheck, startDate, projectEndDate, endDate);

		memoArea = new TextAreaItem();
		memoArea.setWidth("400px");
		memoArea.setTitle(messages.notes());
		memoArea.setToolTip(messages.writeCommentsForThis(this.getAction()
				.getViewName()));

		openingBalText = new AmountField(messages.openingBalance(), this,
				getBaseCurrency());
		openingBalText.setHelpInformation(true);
		openingBalText.setDisabled(isInViewMode());

		balanceText = new AmountField(messages.balance(), this,
				getBaseCurrency());
		balanceText.setHelpInformation(true);
		balanceText.setDisabled(true);

		balanceDate = new DateField(messages.balanceAsOf());
		balanceDate.setHelpInformation(true);
		ClientFinanceDate todaydate = new ClientFinanceDate();
		todaydate.setDay(todaydate.getDay());
		balanceDate.setDatethanFireEvent(todaydate);

		balanceForm = new DynamicForm();
		currencyCombo = createCurrencyComboWidget();
		currencyCombo.setDisabled(true);

		balanceForm.setFields(openingBalText, balanceDate, balanceText);

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoArea);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		HorizontalPanel bottomLayout = new HorizontalPanel();
		bottomLayout.add(memoForm);
		Label l1 = new Label(messages.contacts());
		addButton = new AddButton(this);

		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientContact clientContact = new ClientContact();
				gridView.setDisabled(false);
				if (gridView.getRecords().isEmpty()) {
					clientContact.setPrimary(true);
				}
				gridView.add(clientContact);
			}
		});
		addButton.setEnabled(!isInViewMode());

		gridView = new ContactsTable() {

			@Override
			protected boolean isInViewMode() {
				return JobView.this.isInViewMode();
			}
		};
		gridView.setDisabled(isInViewMode());

		// gridView.setCanEdit(!isInViewMode());
		// gridView.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		// gridView.isEnable = false;
		// gridView.init();

		VerticalPanel panel = new VerticalPanel() {
			@Override
			protected void onAttach() {

				// gridView.setHeight("88px");

				super.onAttach();
			}
		};
		panel.setWidth("100%");
		panel.add(l1);
		panel.add(gridView);
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(addButton);
		hPanel.getElement().getStyle().setMarginTop(8, Unit.PX);
		hPanel.getElement().getStyle().setFloat(Float.LEFT);
		panel.add(hPanel);

		addrsForm = new AddressForm(null);
		addrsForm.setDisabled(isInViewMode());
		fonFaxForm = new PhoneFaxForm(null, null, this, this.getAction()
				.getViewName());
		fonFaxForm.setDisabled(isInViewMode());
		emailForm = new EmailForm(null, null, this, this.getAction()
				.getViewName());
		emailForm.setDisabled(isInViewMode());

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");

		leftVLay.add(jobForm);
		leftVLay.add(balanceForm);

		VerticalPanel rightVLay = new VerticalPanel();
		// rightVLay.setWidth("100%");
		if (isMultiCurrencyEnabled()) {
			leftVLay.add(currencyCombo);
		}
		rightVLay.add(addrsForm);
		rightVLay.add(fonFaxForm);
		rightVLay.add(emailForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.addStyleName("fields-panel");
		topHLay.setSpacing(5);
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "50%");
		topHLay.setCellHorizontalAlignment(rightVLay, ALIGN_RIGHT);

		HorizontalPanel contHLay = new HorizontalPanel();

		mainVlay.add(titleLabel);

		mainVlay.add(topHLay);
		mainVlay.add(contHLay);
		mainVlay.add(panel);
		// mainVlay.add(memoForm);
		memoForm.setDisabled(isInViewMode());
		mainVlay.add(bottomLayout);
		mainVlay.setWidth("100%");
		this.add(mainVlay);
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void saveAndUpdateView() {
		updateData();
		saveOrUpdate(getData());
	}

	private void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			ClientJob job = (ClientJob) result;
			if (getMode() == EditMode.CREATE) {
				// job.setBalance(job.getOpeningBalance());
			}
			super.saveSuccess(result);

		} else {
			saveFailed(new AccounterException());
		}
	}

	@Override
	public ClientJob saveView() {
		ClientJob saveView = super.saveView();
		if (saveView != null) {
			updateData();
		}
		return saveView;
	}

	private void updateData() {

		data.setJobName(jobNameText.getValue());
		data.setJobStatus(jobstatusCombo.getSelectedValue());
		data.setCustomer(customerCombo.getSelectedValue().getID());
		// data.setOpeningBalance(openingBalText.getAmount());
		// data.setBalanceAsOf(balanceDate.getEnteredDate().getDate());
		// data.setAddress(addrsForm.getAddresss());
		data.setStartDate(startDate.getEnteredDate());
		data.setProjectEndDate(projectEndDate.getEnteredDate());
		data.setEndDate(endDate.getEnteredDate());

		// if (isMultiCurrencyEnabled()) {
		// data.setCurrency(currencyCombo.getSelectedCurrency().getID());
		// }
		// data.setPhoneNo(fonFaxForm.businessPhoneText.getValue().toString());
		//
		// data.setFaxNo(fonFaxForm.businessFaxText.getValue().toString());
		//
		// data.setEmail(emailForm.businesEmailText.getValue().toString());
		//
		// data.setWebPageAddress(emailForm.getWebTextValue());
		//
		// data.setActive(statusCheck.getValue());
		// List<ClientContact> allGivenRecords = gridView.getRecords();
		// // }
		// Set<ClientContact> allContacts = new HashSet<ClientContact>();
		//
		// if (allGivenRecords.isEmpty()) {
		// data.setContacts(allContacts);
		// }
		// for (IsSerializable rec : allGivenRecords) {
		// ClientContact tempRecord = (ClientContact) rec;
		// ClientContact contact = new ClientContact();
		//
		// if (tempRecord == null) {
		// contact.setPrimary(false);
		// continue;
		// }
		//
		// contact.setName(tempRecord.getName());
		//
		// contact.setTitle(tempRecord.getTitle());
		// contact.setBusinessPhone(tempRecord.getBusinessPhone());
		// contact.setEmail(tempRecord.getEmail());
		//
		// if (tempRecord.isPrimary() == Boolean.TRUE)
		// contact.setPrimary(true);
		// else
		// contact.setPrimary(false);
		//
		// if (!contact.getName().equals("") || !contact.getTitle().equals("")
		// || !contact.getBusinessPhone().equals("")
		// || !contact.getEmail().equals("")) {
		// allContacts.add(contact);
		//
		// }
		// data.setContacts(allContacts);
		// }
		// // Setting Memo
		// if (memoArea.getValue() != null)
		// data.setMemo(memoArea.getValue().toString());

	}

	@Override
	protected String getViewTitle() {
		return messages.job();
	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void setFocus() {

	}

	public SelectCombo createJobStatusSelectItem() {
		jobstatusList = new ArrayList<String>();
		String jobstatusArray[] = new String[] { messages.none(),
				messages.pending(), messages.awarded(), messages.inprogress(),
				messages.closed(), messages.notAwarded() };

		for (int i = 0; i < jobstatusArray.length; i++) {
			jobstatusList.add(jobstatusArray[i]);
		}

		final SelectCombo jobStatusSelect = new SelectCombo(
				messages.jobStatus());

		jobStatusSelect.setHelpInformation(true);
		jobStatusSelect.setRequired(true);
		jobStatusSelect.initCombo(jobstatusList);
		jobStatusSelect.setDefaultToFirstOption(true);
		messages.none();

		jobStatusSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						jonStatusSelected(jobStatusSelect.getSelectedValue());

					}

				});
		jobStatusSelect.setDisabled(isInViewMode());

		return jobStatusSelect;

	}

	private void jonStatusSelected(String selectedValue) {

	}

	protected CurrencyComboWidget createCurrencyComboWidget() {
		ArrayList<ClientCurrency> currenciesList = getCompany().getCurrencies();
		ClientCurrency baseCurrency = getCompany().getPrimaryCurrency();
		CurrencyComboWidget widget = new CurrencyComboWidget(currenciesList,
				baseCurrency);
		widget.setListener(new CurrencyChangeListener() {

			@Override
			public void currencyChanged(ClientCurrency currency, double factor) {
				selectCurrency = currency;
				openingBalText.setCurrency(selectCurrency);
				balanceText.setCurrency(selectCurrency);
			}
		});
		widget.setDisabled(isInViewMode());
		return widget;
	}
}
