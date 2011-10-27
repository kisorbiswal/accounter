package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAbstractTAXReturn;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterDOM;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateUtil;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public abstract class AbstractFileTAXView extends
		BaseView<ClientAbstractTAXReturn> {

	private TAXAgencyCombo taxAgencyCombo;
	protected DateItem fromDate;
	protected DateItem toDate;
	private HorizontalPanel topLayout;
	private VerticalPanel mainLayout;

	private Button adjustButton;
	private Button printButton;
	protected ClientTAXAgency selectedTaxAgency;
	private ArrayList<DynamicForm> listforms;
	private double amt;
	private Button updateButton;

	boolean canSaveFileVat = false;

	public AbstractFileTAXView() {
		super();
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	private void createControls() {
		Label infolabel = new Label(Accounter.constants().fileTAX());
		infolabel.removeStyleName("gwt-Label");
		infolabel.addStyleName(Accounter.constants().labelTitle());
		taxAgencyCombo = new TAXAgencyCombo(Accounter.constants().vatAgency());
		taxAgencyCombo.setHelpInformation(true);
		taxAgencyCombo.setRequired(true);

		taxAgencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXAgency>() {

					public void selectedComboBoxItem(ClientTAXAgency selectItem) {

						taxAgencySelected(selectItem);

					}
				});
		// taxAgencyCombo.setWidth("200px");
		fromDate = new DateItem(Accounter.constants().from());
		fromDate.setHelpInformation(true);
		fromDate.setWidth(100);

		toDate = new DateItem(Accounter.constants().to());
		toDate.setHelpInformation(true);
		toDate.setWidth(100);

		fromDate.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate financeDate) {
				Date date = financeDate.getDateAsObject();
				int frequency = ClientTAXAgency.TAX_RETURN_FREQUENCY_MONTHLY;
				if (selectedTaxAgency != null) {
					frequency = selectedTaxAgency.getTAXFilingFrequency();
				}
				date = DateUtil.getMonthFirstDay(date);
				switch (frequency) {
				case ClientTAXAgency.TAX_RETURN_FREQUENCY_MONTHLY:
					date.setMonth(date.getMonth() + 1);
					break;
				case ClientTAXAgency.TAX_RETURN_FREQUENCY_QUARTERLY:
					date.setMonth(date.getMonth() + 3);
					break;
				case ClientTAXAgency.TAX_RETURN_FREQUENCY_HALF_YEARLY:
					date.setMonth(date.getMonth() + 6);
					break;
				case ClientTAXAgency.TAX_RETURN_FREQUENCY_YEARLY:
					date.setMonth(date.getMonth() + 12);
					break;
				}
				date.setDate(date.getDate() - 1);
				toDate.setValue(new ClientFinanceDate(date));
			}
		});

		Date startDate = DateUtil.getCurrentMonthFirstDate();
		fromDate.setDatethanFireEvent(new ClientFinanceDate(startDate));
		toDate.setValue(new Date());
		listforms = new ArrayList<DynamicForm>();

		updateButton = new Button(Accounter.constants().update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				canSaveFileVat = true;
				reloadGrid();
			}
		});

		DynamicForm topForm = new DynamicForm();
		topForm.setIsGroup(true);
		topForm.setGroupTitle(Accounter.constants().top());
		topForm.setNumCols(6);
		topForm.setFields(taxAgencyCombo, fromDate, toDate);
		topForm.setWidth("100%");

		ListGrid grid = getGrid();

		HTML beforeLabel = new HTML("<string>"
				+ Accounter.constants().beforeYouFile());

		HTML adjustLabel = new HTML("<strong>"
				+ Accounter.constants().doYouNeedToMakeAnAdjustment()
				+ " </strong><br>" + Accounter.constants().useAdjustButton());

		adjustButton = new Button(Accounter.constants().adjustTAXReturn());
		adjustButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (selectedTaxAgency != null) {
					AdjustTAXAction vatAdjustmentAction = ActionFactory
							.getVatAdjustmentAction();
					vatAdjustmentAction.setVatAgency(selectedTaxAgency);
					vatAdjustmentAction.run(null, true);
				}

			}
		});

		VerticalPanel adjustForm = new VerticalPanel();
		adjustForm.setSpacing(3);
		// adjustForm.add(adjustLabel);
		adjustForm.add(adjustButton);
		// if (adjustButton.isEnabled()) {
		// adjustButton.getElement().getParentElement()
		// .setClassName("ibutton");
		// ThemesUtil.addDivToButton(adjustButton, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// }
		HTML printLabel = new HTML(messages.printTAXReturnLabel());

		printButton = new Button(Accounter.constants().printTAXReturn());
		printButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (selectedTaxAgency != null
						&& fromDate.getEnteredDate() != null
						&& toDate.getEnteredDate() != null)
					printTaxReturn();
				else
					Accounter.showError(Accounter.constants()
							.pleaseselectvaliddateranges());

			}
		});

		disableButtons();

		VerticalPanel printForm = new VerticalPanel();
		printForm.setSpacing(3);
		printForm.add(printLabel);
		printForm.add(printButton);
		// if (printButton.isEnabled()) {
		// printButton.getElement().getParentElement().setClassName("ibutton");
		// ThemesUtil.addDivToButton(printButton, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// }
		topLayout = new HorizontalPanel();
		topLayout.add(topForm);
		topLayout.add(updateButton);

		// if (updateButton.isEnabled()) {
		// updateButton.getElement().getParentElement()
		// .setClassName("ibutton");
		// ThemesUtil.addDivToButton(updateButton, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// }
		mainLayout = new VerticalPanel();
		mainLayout.setHeight("100%");
		mainLayout.add(infolabel);
		mainLayout.add(topLayout);
		mainLayout.add(grid);
		mainLayout.setWidth("100%");
		// mainLayout.add(beforeLabel);
		mainLayout.add(adjustForm);
		mainLayout.add(printForm);

		AccounterDOM.setParentElementHeight(infolabel.getElement(), 5);
		AccounterDOM.setParentElementHeight(topLayout.getElement(), 10);
		AccounterDOM.setParentElementHeight(grid.getElement(), 60);

		this.add(mainLayout);

		/* Adding dynamic forms in list */
		listforms.add(topForm);

	}

	protected void taxAgencySelected(final ClientTAXAgency selectItem) {

		this.selectedTaxAgency = selectItem;

		if (selectItem == null) {
			disableButtons();
			getGrid().clear();
		} else {

			enableButtons();
			getLastTaxReturnEndDate(selectItem);
			// getVATReturnEndDate(selectItem);

		}

		fromDate.setDisabled(false);
	}

	private void getVATReturnEndDate(ClientTAXAgency selectItem) {
		List<ClientAbstractTAXReturn> vatReturns = getCompany().getVatReturns();
		ClientAbstractTAXReturn lastVATReturn = null;
		for (ClientAbstractTAXReturn vatReturn : vatReturns) {
			if (selectItem.getID() == vatReturn.getTAXAgency()) {
				lastVATReturn = vatReturn;
			}
		}
		if (lastVATReturn != null) {
			if (lastVATReturn.getPeriodEndDate() != 0) {
				ClientFinanceDate date = new ClientFinanceDate(
						lastVATReturn.getPeriodEndDate());
				int day = date.getDay();
				date.setDay(day + 1);
				fromDate.setDatethanFireEvent(date);
				// date.setDate(day + 1);
				toDate.setDatethanFireEvent(date);
			}
		} else {
			// List<ClientFiscalYear> fiscalYears = Accounter.getCompany()
			// .getFiscalYears();
			// for (ClientFiscalYear fiscalYear : fiscalYears) {
			// if (fiscalYear.getIsCurrentFiscalYear()) {
			fromDate.setDatethanFireEvent(Accounter.getCompany()
					.getCurrentFiscalYearStartDate());
			// break;
			// }
			// }
		}

	}

	public void disableButtons() {
		this.adjustButton.setEnabled(false);
		this.printButton.setEnabled(false);

	}

	public void enableButtons() {
		this.adjustButton.setEnabled(true);

	}

	public void enableprintButton() {

		this.printButton.setEnabled(true);

	}

	public void disableprintButton() {

		this.printButton.setEnabled(false);

	}

	private void getLastTaxReturnEndDate(final ClientTAXAgency taxAgency) {
		rpcGetService.getLastTAXReturnEndDate(taxAgency.getID(),
				new AccounterAsyncCallback<Long>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onResultSuccess(Long result) {
						if (result != null && result > 0) {
							Date date = new ClientFinanceDate(result)
									.getDateAsObject();
							date.setDate(date.getDate() + 1);

							fromDate.setDatethanFireEvent(new ClientFinanceDate(
									date));
							fromDate.setDisabled(true);
						} else {
							fromDate.setDatethanFireEvent(new ClientFinanceDate(
									DateUtil.getCurrentMonthFirstDate()));
						}
						reloadGrid();
					}
				});
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
		super.createButtons(buttonBar);
		this.saveAndCloseButton.setText(Accounter.constants().fileVATReturn());
		buttonBar.remove(this.saveAndNewButton);
	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		// check whether vet agency is selected or not
		// can save file vat or not

		if (this.selectedTaxAgency == null && this.isInViewMode()) {
			taxAgencyCombo.addStyleName("highlightedFormItem");
			result.addError(selectedTaxAgency, Accounter.constants()
					.pleaseSelectValidVATAgency());
		} else {
			result.addWarning(selectedTaxAgency, Accounter.constants()
					.sureToSaveFileVAT());
			// AccounterValidator.validate_FileVat(this);
		}
		if (!canSaveFileVat) {
			taxAgencyCombo.addStyleName("highlightedFormItem");
			fromDate.addStyleName("highlightedFormItem");
			toDate.addStyleName("highlightedFormItem");
			result.addError(this, Accounter.constants()
					.fileVATcantsavewithemptyvalues());
		}
		if (getGrid().getRecords().isEmpty()) {
			result.addError(this, Accounter.constants()
					.thereIsNoTrasationsToFile());
		}
		return result;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DynamicForm> getForms() {
		return listforms;
	}

	@Override
	public void setFocus() {
		this.taxAgencyCombo.setFocus();
	}

	protected abstract void reloadGrid();

	protected abstract void printTaxReturn();

	protected abstract ListGrid getGrid();
}
