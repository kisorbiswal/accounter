package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterDialog;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class TDSAcknowlegmentForm extends BaseDialog {

	private DynamicForm form;
	private DynamicForm form1;

	private SelectCombo formTypeCombo;
	private SelectCombo financialYearCombo;
	private SelectCombo quaterCombo;

	private TextItem ackNoField;

	private DateField dateField;

	public TDSAcknowlegmentForm() {
		super(messages.TDSAcknowledgementForm(), messages
				.addTheDetailsYouGetFromTheTINWebsiteAndPressCreate16AForm());
		setWidth("650px");
		createControls();
		center();
	}

	private void createControls() {
		form = new DynamicForm("form");
		form1 = new DynamicForm("form1");
		form.setWidth("100%");
		form.setHeight("100%");
		form1.setHeight("100%");
		form1.setWidth("100%");
		HorizontalPanel layout1 = new HorizontalPanel();
		VerticalPanel vPanel = new VerticalPanel();

		formTypeCombo = new SelectCombo(messages.formNo());
		formTypeCombo.setRequired(true);
		formTypeCombo.initCombo(getFormTypes());
		formTypeCombo.setSelectedItem(0);

		financialYearCombo = new SelectCombo(messages.financialYear());
		financialYearCombo.setRequired(true);
		financialYearCombo.initCombo(getFinancialYearList());
		financialYearCombo.setSelectedItem(0);
		financialYearCombo.setRequired(true);

		quaterCombo = new SelectCombo(messages.forQuarter());
		quaterCombo.setRequired(true);
		quaterCombo.initCombo(getFinancialQuatersList());
		quaterCombo.setSelectedItem(0);
		quaterCombo.setRequired(true);

		ackNoField = new TextItem(messages.acknowledgmentNo(),"ackNoField");
		ackNoField.setRequired(true);

		ClientFinanceDate todaysDate = new ClientFinanceDate();
		dateField = new DateField(messages.date(),"dateField");
		dateField.setRequired(true);
		dateField.setTitle(messages.date());
		dateField.setValue(todaysDate);

		form.add(formTypeCombo, quaterCombo, financialYearCombo);
		form1.add(ackNoField, dateField);

		layout1.add(form);
		layout1.add(form1);
		vPanel.add(layout1);
		setBodyLayout(vPanel);

	}

	private List<String> getFormTypes() {
		ArrayList<String> list = new ArrayList<String>();

		list.add("26Q");
		list.add("27Q");
		list.add("27EQ");

		return list;
	}

	@Override
	protected ValidationResult validate() {
		final ValidationResult result = form.validate();
		result.add(form1.validate());
		if (!result.haveErrors()) {
			int formType = 0;
			int quater = 0;
			int startYear = 0;
			int endYear = 0;
			if (formTypeCombo.getSelectedValue() != null) {
				formType = formTypeCombo.getSelectedIndex() + 1;
			}
			if (quaterCombo.getSelectedValue() != null) {
				quater = quaterCombo.getSelectedIndex() + 1;
			}
			if (financialYearCombo.getSelectedValue() != null) {
				String[] tokens = financialYearCombo.getSelectedValue().split(
						"-");
				startYear = Integer.parseInt(tokens[0]);
				endYear = Integer.parseInt(tokens[1]);
			}
			Accounter.createHomeService().isChalanDetailsFiled(formType,
					quater, startYear, endYear,
					new AccounterAsyncCallback<Boolean>() {

						@Override
						public void onException(AccounterException exception) {
							Accounter.showError(exception.getMessage());
						}

						@Override
						public void onResultSuccess(Boolean isFiled) {
							if (isFiled) {
								result.addWarning(ackNoField, messages
										.theAcknowledgementNumberAlreadyFiled());
							}
						}
					});
		}
		return result;
	}

	@Override
	protected boolean onOK() {
		int formType = 0;
		int quater = 0;
		int startYear = 0;
		int endYear = 0;
		if (formTypeCombo.getSelectedValue() != null) {
			formType = formTypeCombo.getSelectedIndex() + 1;
		}
		if (quaterCombo.getSelectedValue() != null) {
			quater = quaterCombo.getSelectedIndex() + 1;
		}
		if (financialYearCombo.getSelectedValue() != null) {
			String[] tokens = financialYearCombo.getSelectedValue().split("-");
			startYear = Integer.parseInt(tokens[0]);
			endYear = Integer.parseInt(tokens[1]);
		}
		Accounter.createHomeService().isChalanDetailsFiled(formType, quater,
				startYear, endYear, new AccounterAsyncCallback<Boolean>() {

					@Override
					public void onException(AccounterException exception) {
						Accounter.showError(exception.getMessage());
					}

					@Override
					public void onResultSuccess(Boolean isFiled) {
						if (isFiled) {
							showWarning();
						} else {
							updateChalanDetails();
						}
					}
				});

		return false;
	}

	protected void showWarning() {
		AccounterDialog accounterDialog = new AccounterDialog(
				messages.thisAcknowledgementNumberAlreadyFiled(),
				AccounterType.WARNING) {
			@Override
			protected void yesClicked() throws Exception {
				super.yesClicked();
				this.removeFromParent();
				updateChalanDetails();
			}

			@Override
			protected void noClicked() throws InvalidEntryException {
				super.noClicked();
				this.removeFromParent();
			}
		};
		accounterDialog.show();
	}

	protected void updateChalanDetails() {
		int formType = 0;
		int quater = 0;
		int startYear = 0;
		int endYear = 0;
		if (formTypeCombo.getSelectedValue() != null) {
			formType = formTypeCombo.getSelectedIndex() + 1;
		}
		if (quaterCombo.getSelectedValue() != null) {
			quater = quaterCombo.getSelectedIndex() + 1;
		}
		if (financialYearCombo.getSelectedValue() != null) {
			String[] tokens = financialYearCombo.getSelectedValue().split("-");
			startYear = Integer.parseInt(tokens[0]);
			endYear = Integer.parseInt(tokens[1]);
		}
		Accounter.createHomeService().updateAckNoForChallans(formType, quater,
				startYear, endYear, ackNoField.getValue(),
				dateField.getValue().getDate(),
				new AccounterAsyncCallback<Boolean>() {

					@Override
					public void onException(AccounterException exception) {
						Accounter.showError(messages.updateFailed());
					}

					@Override
					public void onResultSuccess(Boolean result) {
						if (result) {
							saveSuccess(null);
						} else {
							Accounter.showError(messages.updateFailed());
						}
					}
				});
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		onCancel();
		this.removeFromParent();
	}

	@Override
	public void setFocus() {
		ackNoField.setFocus();
	}

}
