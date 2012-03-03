package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientJob;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class JobView extends BaseView<ClientJob> {

	private TextItem jobNameText;
	private List<String> jobstatusList;
	private SelectCombo jobstatusCombo;
	private DateField startDate, projectEndDate, endDate;
	private DynamicForm form, jobForm;
	private CheckboxItem statusCheck;
	CustomerCombo customerCombo;
	private ClientCustomer customer;

	public JobView(ClientCustomer customer) {
		super();
		this.customer = customer;
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
		if (customer != null) {
			customerCombo.setComboItem(customer);
		} else {
			customerCombo.setComboItem(getCompany().getCustomer(
					data.getCustomer()));
		}
		super.initData();
	}

	private void createControls() {

		VerticalPanel mainVlay = new VerticalPanel();
		Label titleLabel = new Label(messages.job());
		titleLabel.setStyleName("label-title");

		jobForm = new DynamicForm();
		form = new DynamicForm();

		jobNameText = new TextItem(messages.jobName());
		jobNameText.setToolTip(messages.jobName());
		jobNameText.setHelpInformation(true);
		jobNameText.setRequired(true);
		jobNameText.setDisabled(isInViewMode());

		customerCombo = new CustomerCombo(Global.get().Customer());
		customerCombo.setHelpInformation(true);
		customerCombo.setRequired(true);
		customerCombo.setDisabled(isInViewMode());

		jobstatusCombo = createJobStatusSelectItem();

		startDate = new DateField(messages.startDate());
		startDate.setHelpInformation(true);
		ClientFinanceDate start_date = new ClientFinanceDate();
		start_date.setDay(start_date.getDay());
		startDate.setDatethanFireEvent(start_date);
		startDate.setDisabled(isInViewMode());
		projectEndDate = new DateField(messages.projectendDate());
		projectEndDate.setHelpInformation(true);
		ClientFinanceDate projectEnd_date = new ClientFinanceDate();
		projectEnd_date.setDay(projectEnd_date.getDay());
		projectEndDate.setDatethanFireEvent(projectEnd_date);
		projectEndDate.setDisabled(isInViewMode());
		endDate = new DateField(messages.endDate());
		endDate.setHelpInformation(true);
		ClientFinanceDate end_date = new ClientFinanceDate();
		end_date.setDay(end_date.getDay());
		endDate.setDatethanFireEvent(end_date);
		endDate.setDisabled(isInViewMode());
		statusCheck = new CheckboxItem(messages.active());
		statusCheck.setValue(true);
		statusCheck.setDisabled(isInViewMode());

		jobForm.setFields(jobNameText, statusCheck, jobstatusCombo,
				customerCombo);
		form.setFields(startDate, projectEndDate, endDate);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");

		leftVLay.add(jobForm);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.add(form);

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

	}

	@Override
	public void onEdit() {

		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter.showMessage(messages.sessionExpired());
				} else {
					int errorCode = ((AccounterException) caught)
							.getErrorCode();
					Accounter.showError(AccounterExceptions
							.getErrorString(errorCode));

				}
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		this.rpcDoSerivce.canEdit(AccounterCoreType.JOB, data.getID(),
				editCallBack);

	}

	private void enableFormItems() {
		setMode(EditMode.EDIT);
		jobNameText.setDisabled(isInViewMode());
		statusCheck.setDisabled(isInViewMode());
		jobstatusCombo.setDisabled(isInViewMode());
		startDate.setDisabled(isInViewMode());
		endDate.setDisabled(isInViewMode());
		projectEndDate.setDisabled(isInViewMode());
		customerCombo.setDisabled(isInViewMode());
		super.onEdit();

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
		if (customerCombo.getSelectedValue() != null) {
			data.setCustomer(customerCombo.getSelectedValue().getID());
		}
		data.setStartDate(startDate.getEnteredDate());
		data.setProjectEndDate(projectEndDate.getEnteredDate());
		data.setEndDate(endDate.getEnteredDate());
		data.setActive(statusCheck.isChecked());

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
		jobstatusCombo.setSelected(selectedValue);
	}
}
