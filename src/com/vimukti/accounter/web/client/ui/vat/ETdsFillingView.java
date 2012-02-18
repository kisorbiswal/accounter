package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientETDSFillingItem;
import com.vimukti.accounter.web.client.core.ClientTDSDeductorMasters;
import com.vimukti.accounter.web.client.core.ClientTDSResponsiblePerson;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.SaveAndCloseButton;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.ETdsCellTable;

public class ETdsFillingView extends BaseView<ClientETDSFillingItem> {

	private SelectCombo formType;
	private Label lab1;

	VerticalPanel mainVLay;

	ClientBudget budgetForEditing = new ClientBudget();

	private ETdsCellTable tdsCellTable;
	private SelectCombo slectAssecementYear;
	private SelectCombo financialYearCombo;
	private SelectCombo quaterSelectionCombo;
	private DynamicForm dynamicFormLeft;
	private DynamicForm dynamicFormRight;
	private int formNoSelected;
	private int quaterSelected;
	private int startYear;
	private int endYear;
	protected ArrayList<ClientETDSFillingItem> eTDSList;
	private ClientTDSDeductorMasters deductor;
	private ClientTDSResponsiblePerson responsiblePerson;
	private Button acknowledgementFormButton;

	public ETdsFillingView() {

	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	public void initData() {
		if (data == null) {
			setData(new ClientETDSFillingItem());
			initCallBack();
		}
		getDeductorAndResponsiblePerson();
	}

	private void getDeductorAndResponsiblePerson() {
		Accounter.createHomeService().getDeductorMasterDetails(
				new AccounterAsyncCallback<ClientTDSDeductorMasters>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onResultSuccess(ClientTDSDeductorMasters result) {
						deductor = result;
					}
				});

		Accounter.createHomeService().getResponsiblePersonDetails(
				new AccounterAsyncCallback<ClientTDSResponsiblePerson>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onResultSuccess(
							ClientTDSResponsiblePerson result) {
						responsiblePerson = result;
					}
				});
	}

	private void createControls() {

		lab1 = new Label();
		lab1.removeStyleName("gwt-Label");
		lab1.setText(messages.eTDSFilling());
		lab1.addStyleName("label-title");

		formType = new SelectCombo(messages.formNo());
		formType.setHelpInformation(true);
		formType.initCombo(getFormTypes());
		formType.setSelectedItem(0);
		formNoSelected = 1;
		formType.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

			@Override
			public void selectedComboBoxItem(String selectItem) {
				if (selectItem.equals(getFormTypes().get(0))) {
					if (formNoSelected == 2) {
						tdsCellTable.reDraw();
					}
					formNoSelected = 1;
				} else if (selectItem.equals(getFormTypes().get(1))) {
					if (formNoSelected != 2) {
						tdsCellTable.reDraw();
					}
					formNoSelected = 2;
				} else if (selectItem.equals(getFormTypes().get(2))) {
					if (formNoSelected == 2) {
						tdsCellTable.reDraw();
					}
					formNoSelected = 3;
				}
				initCallBack();
			}
		});

		slectAssecementYear = new SelectCombo(messages.assessmentYear());
		slectAssecementYear.setHelpInformation(true);
		slectAssecementYear.initCombo(getAssessmentYearList());
		slectAssecementYear.setDisabled(true);

		financialYearCombo = new SelectCombo(messages.financialYear());
		financialYearCombo.setHelpInformation(true);
		financialYearCombo.initCombo(getFinancialYearList());
		financialYearCombo.setSelectedItem(0);
		String[] tokens = financialYearCombo.getSelectedValue().split("-");
		startYear = Integer.parseInt(tokens[0]);
		endYear = Integer.parseInt(tokens[1]);
		financialYearCombo.setDisabled(isInViewMode());
		financialYearCombo.setRequired(true);
		financialYearCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						slectAssecementYear.setSelected(getAssessmentYearList()
								.get(financialYearCombo.getSelectedIndex() + 1));

						String delims = "-";
						String[] tokens = selectItem.split(delims);

						startYear = Integer.parseInt(tokens[0]);
						endYear = Integer.parseInt(tokens[1]);
						initCallBack();
					}

				});

		quaterSelectionCombo = new SelectCombo(messages.forQuarter());
		quaterSelectionCombo.setHelpInformation(true);
		quaterSelectionCombo.initCombo(getFinancialQuatersList());
		quaterSelectionCombo.setSelectedItem(0);
		quaterSelected = 1;
		quaterSelectionCombo.setDisabled(isInViewMode());
		quaterSelectionCombo.setRequired(true);
		quaterSelectionCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						if (selectItem.equals(getFinancialQuatersList().get(0))) {
							quaterSelected = 1;
						} else if (selectItem.equals(getFinancialQuatersList()
								.get(1))) {
							quaterSelected = 2;
						} else if (selectItem.equals(getFinancialQuatersList()
								.get(2))) {
							quaterSelected = 3;
						} else if (selectItem.equals(getFinancialQuatersList()
								.get(3))) {
							quaterSelected = 4;
						}
						initCallBack();
					}
				});

		dynamicFormLeft = UIUtils.form(messages.chartOfAccountsInformation());
		dynamicFormLeft.setWidth("100%");
		dynamicFormLeft.setFields(formType, financialYearCombo);

		dynamicFormRight = UIUtils.form(messages.chartOfAccountsInformation());
		dynamicFormRight.setWidth("100%");
		dynamicFormRight.setFields(quaterSelectionCombo, slectAssecementYear);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.add(dynamicFormLeft);
		topHLay.add(dynamicFormRight);

		tdsCellTable = new ETdsCellTable() {

			@Override
			protected boolean is27Q() {
				if (formType.getSelectedValue() != null
						&& formType.getSelectedValue().equals("27Q")) {
					return true;
				}
				return false;
			}
		};

		mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "300px");
		mainVLay.add(lab1);
		mainVLay.add(topHLay);
		topHLay.getElement().getStyle().setPaddingTop(10, Unit.PX);

		ScrollPanel scroll = new ScrollPanel();
		scroll.add(tdsCellTable);
		scroll.setWidth("910px");
		scroll.setTouchScrollingDisabled(false);
		scroll.addStyleName("tds-scroll-panel");

		mainVLay.add(scroll);

		this.add(mainVLay);

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return messages.eTDSFilling();

	}

	@Override
	public void saveAndUpdateView() {
		List<ClientETDSFillingItem> dataList = tdsCellTable.getAllRows();

		String panList = "";
		String remarkList = "";
		String codeList = "";
		String grossingUpList = "";
		for (ClientETDSFillingItem record : dataList) {
			if (record.getRemark() != null) {
				remarkList = remarkList + record.getRemark().trim();
			}
			remarkList = remarkList + "-";
			codeList = codeList + record.getCompanyCode() + "-";
			if (record.getPanOfDeductee() != null) {
				panList = panList + record.getPanOfDeductee().trim();
			}
			panList = panList + "-";
			if (record.getGrossingUpIndicator() != null) {
				grossingUpList = grossingUpList
						+ record.getGrossingUpIndicator().substring(0, 1);
			}
			grossingUpList = grossingUpList + "-";
		}

		UIUtils.generateETDSFillingtext(formNoSelected, quaterSelected,
				startYear, endYear, panList, codeList, remarkList,
				grossingUpList);
		changeButtonBarMode(false);
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
			super.saveSuccess(result);

		} else {
			saveFailed(new AccounterException());
		}
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		if (getCompany().getTdsDeductor() == null) {
			result.addError("deductor", messages
					.tdsDeductorDetailsNotEnteredYetPleaseFillTheDetailsFirst());
		}

		if (getCompany().getTdsResposiblePerson() == null) {
			result.addError(
					"responsible",
					messages.tDSResponsiblePersonDetailsNotEnteredYetPleaseFillTheDetailsFirst());
		}

		List<ClientETDSFillingItem> records = tdsCellTable.getAllRows();

		for (ClientETDSFillingItem row : records) {
			if (row.getCompanyCode() == null || row.getCompanyCode().isEmpty()) {
				result.addError(tdsCellTable,
						messages.pleaseSelectDeducteeCodeForAllRecords());
			}
		}

		return result;

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void setFocus() {

	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	private List<String> getFormTypes() {
		ArrayList<String> list = new ArrayList<String>();

		list.add("26Q");
		list.add("27Q");
		// list.add("27EQ");

		return list;
	}

	private void initCallBack() {
		Accounter.createHomeService().getEtdsDetails(formNoSelected,
				quaterSelected, startYear, endYear,
				new AccounterAsyncCallback<ArrayList<ClientETDSFillingItem>>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onResultSuccess(
							ArrayList<ClientETDSFillingItem> result) {
						eTDSList = result;
						tdsCellTable.setAllRows(eTDSList);
					}
				});
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
		// super.createButtons(buttonBar);
		this.saveAndCloseButton = new SaveAndCloseButton(this);
		this.saveAndCloseButton.setText(messages.downloadTxtFile());

		acknowledgementFormButton = new Button(messages.enterAckNumber());
		acknowledgementFormButton.setFocus(true);

		acknowledgementFormButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				openAcknowledgementForm();
			}
		});

		// buttonBar.add(acknowledgementFormButton);
		buttonBar.add(saveAndCloseButton);
	}

	protected void openAcknowledgementForm() {

		TDSAcknowlegmentForm ackFormDialogue = new TDSAcknowlegmentForm();
		ackFormDialogue.show();

	}
}
