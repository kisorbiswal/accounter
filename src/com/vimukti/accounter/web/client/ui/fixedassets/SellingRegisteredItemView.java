package com.vimukti.accounter.web.client.ui.fixedassets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetSellOrDisposeReviewJournal;
import com.vimukti.accounter.web.client.core.Lists.TempFixedAsset;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.DebitAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.CustomButton;
import com.vimukti.accounter.web.client.ui.core.CustomButtonType;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * 
 * @author B.Anjaneyulu
 * @author B.Srinivasa Rao
 * 
 */
public class SellingRegisteredItemView extends BaseView<ClientFixedAsset> {
	protected DateField datesold;
	protected SelectItem dateItemCombo;
	protected DebitAccountCombo accountCombo;
	protected AmountField salepriceText;
	protected Label depriciationForFinancialyearLabel, detailsLabel,
			QuestionLabel;
	protected DynamicForm dateForm, detailsForm, textAreaForm;
	protected String yearValue, noDepOption, allDepOption, changedValue;
	protected RadioGroupItem QuestionItem;
	protected TextAreaItem notesArea;
	protected JournalViewDialog dialog;
	private ClientAccount accountForSale;
	protected LinkedHashMap<String, ClientFinanceDate> dateValueMap = new LinkedHashMap<String, ClientFinanceDate>();
	protected LinkedHashMap<Integer, Integer> monthsKey = new LinkedHashMap<Integer, Integer>();

	private ArrayList<DynamicForm> listforms;

	public SellingRegisteredItemView() {
		super(true);
		this.validationCount = 5;
	}

	@Override
	public void init() {
		super.init();
		createControls();
		dateSelected();
	}

	/**
	 * This method is for creating common controls for both selling and
	 * Disposing views
	 */

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();
		detailsLabel = new Label(Accounter.constants().details());
		detailsLabel.setStyleName(Accounter.constants().labelTitle());

		detailsForm = getDetailForm();

		depriciationForFinancialyearLabel = new Label();
		depriciationForFinancialyearLabel.setStyleName(Accounter.constants()
				.labelTitle());

		depriciationForFinancialyearLabel.setText(Accounter.constants()
				.depriciationForThe()
				+ yearValue
				+ Accounter.constants().financialYear());

		HorizontalPanel topPanel = new HorizontalPanel();
		topPanel.add(detailsForm);

		QuestionLabel = new Label();
		QuestionLabel.setText(Accounter.constants()
				.howMuchDepriciationShouldBeIcludedInThisFinancialYear());
		QuestionItem = new RadioGroupItem();
		noDepOption = Accounter.constants().noDepreciationThisFinancialYear();
		allDepOption = Accounter.constants().allDepreciationUpToIncluding();
		QuestionItem.setValues(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				changedValue = QuestionItem.getValue().toString();
				changeDateCombo(changedValue);
			}
		}, noDepOption, allDepOption);

		DynamicForm radioForm = new DynamicForm();
		radioForm.setFields(QuestionItem);
		dateItemCombo = new SelectItem(Accounter.constants().date());
		dateForm = new DynamicForm();
		dateForm.setWidth("50%");
		dateForm.setFields(dateItemCombo);

		VerticalPanel radioVlayout = new VerticalPanel();
		radioVlayout.setSpacing(10);
		radioVlayout.add(QuestionLabel);
		radioVlayout.add(radioForm);
		radioVlayout.add(dateForm);
		saveAndCloseButton = new CustomButton(CustomButtonType.REVIEW_JOURNAL,
				this);
		buttonLayout.clear();
		buttonLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		buttonLayout.add(saveAndCloseButton);
		buttonLayout
				.setCellHorizontalAlignment(saveAndCloseButton, ALIGN_RIGHT);

		notesArea = new TextAreaItem();
		notesArea.setWidth(100);
		notesArea.setTitle(Accounter.constants().notes());

		textAreaForm = new DynamicForm();
		textAreaForm.setWidth("100%");
		textAreaForm.setFields(notesArea);
		textAreaForm.setStyleName("align-form");

		VerticalPanel mainLayout = new VerticalPanel();
		mainLayout.setSpacing(15);
		mainLayout.setWidth("100%");
		mainLayout.add(detailsLabel);
		mainLayout.add(topPanel);
		mainLayout.add(depriciationForFinancialyearLabel);
		mainLayout.add(radioVlayout);
		mainLayout.add(textAreaForm);
		mainLayout.setCellHorizontalAlignment(buttonLayout, ALIGN_RIGHT);

		canvas.add(mainLayout);

		/* Adding dynamic forms in list */
		listforms.add(detailsForm);

		listforms.add(radioForm);
		listforms.add(dateForm);
		listforms.add(textAreaForm);

	}

	/**
	 * This method is using for getting DynamicForm of Selling View
	 * 
	 * @return
	 */
	protected DynamicForm getDetailForm() {
		datesold = new DateField(Accounter.constants().datesold());
		datesold.setEnteredDate(new ClientFinanceDate());
		yearValue = String.valueOf(datesold.getYear() + 1900);
		datesold.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				dateSelected();

			}
		});
		accountCombo = createDebitAccountCombo();
		accountCombo.setRequired(true);

		salepriceText = new AmountField(Accounter.constants()
				.salepriceExcludingTax());
		salepriceText.setRequired(true);
		salepriceText.setWidth(100);
		DynamicForm detailForm = new DynamicForm();
		detailForm.setFields(datesold, accountCombo, salepriceText);
		return detailForm;
	}

	/**
	 * This method is for Opening ReviewJournal Dialog Based on that
	 * FixedAssetSellOrDisposeReviewJournal
	 */

	protected void openJournalDialog(
			FixedAssetSellOrDisposeReviewJournal journalObject) {
		dialog = new JournalViewDialog(Accounter.constants().journalView(), "",
				journalObject);
		dialog.addInputDialogHandler(new InputDialogHandler() {

			@Override
			public boolean onOkClick() {
				try {
					if (dialog.validate())
						okClicked();
				} catch (InvalidEntryException e) {
					Accounter.showError(e.getMessage());
					return false;
				}
				return true;
			}

			@Override
			public void onCancelClick() {
				// Accounter.stopExecution();
			}
		});
	}

	protected void okClicked() {
		createSellingObject();
	}

	protected void createSellingObject() {
		ClientFixedAsset sellingAsset = getSellingObject();
		alterObject(sellingAsset);
	}

	/**
	 * this method is for setting the salesprice and Salesaccount to FixedAsset
	 * Object
	 */

	private ClientFixedAsset getSellingObject() {
		ClientFixedAsset sellingAsset = getSellOrDisposeObject();
		sellingAsset.setSalePrice(salepriceText.getAmount());
		if (this.accountForSale != null)
			sellingAsset.setAccountForSale(this.accountForSale);
		return sellingAsset;
	}

	/**
	 * This method is for setting entered common values in both selling and
	 * disposing views to FixedAsset Object
	 */

	protected ClientFixedAsset getSellOrDisposeObject() {
		ClientFixedAsset sellorDisposeAsset = (ClientFixedAsset) data;
		sellorDisposeAsset.setSoldOrDisposedDate(getSoldorDisposedDateField()
				.getEnteredDate().getTime());
		ClientFinanceDate date = dateValueMap.get(dateItemCombo.getValue()
				.toString());
		if (date != null)
			sellorDisposeAsset.setDepreciationTillDate(date.getTime());

		if (sellorDisposeAsset != null) {
			sellorDisposeAsset
					.setStatus(ClientFixedAsset.STATUS_SOLD_OR_DISPOSED);
		}
		sellorDisposeAsset.setNoDepreciation(QuestionItem.getValue().equals(
				noDepOption) ? true : false);
		if (notesArea.getValue() != null)
			sellorDisposeAsset.setNotes(notesArea.getValue().toString());
		if (dialog.totalCapitalGainAccount != null) {
			sellorDisposeAsset
					.setTotalCapitalGain(dialog.totalCapitalGainAccount.getID());
			sellorDisposeAsset.setTotalCapitalGainAmount(dialog
					.getTotalCapitalGainAmount());
		}
		if (dialog.LossorGainOnDisposalAccount != null) {
			sellorDisposeAsset
					.setLossOrGainOnDisposalAccount(dialog.LossorGainOnDisposalAccount
							.getID());
			sellorDisposeAsset.setLossOrGain(dialog.getLossorGainAmount());
		}
		return sellorDisposeAsset;
	}

	/**
	 * This method Calls when the sold or Disposed date is changed
	 */

	protected void dateSelected() {
		yearValue = String
				.valueOf(getSoldorDisposedDateField().getYear() + 1900);
		depriciationForFinancialyearLabel.setText(Accounter.constants()
				.depriciationForThe()
				+ yearValue
				+ "     "
				+ Accounter.constants().financialYear());
		initDateCombo();
	}

	/**
	 * Intilising Date Values in Date Combo in both Selling and Disposing views
	 */

	private void initDateCombo() {
		calucateDateCombo();
		String dateList[] = getlastDates();
		dateItemCombo.setValueMap(dateList);

	}

	/**
	 * Getting last dates of every month of selected Financial Year
	 * 
	 * @param year
	 * @return yearlist
	 */

	public String[] getlastDates() {
		dateValueMap.clear();
		String months[] = getMonthStrings();
		int lastdates[] = getLastDateValues();
		String dateList[] = new String[months.length + 1];
		dateList[0] = " ";
		String format;
		Set<Integer> monthset = monthsKey.keySet();
		int year, pos = 1;
		for (int key : monthset) {
			year = monthsKey.get(key);
			if (getSoldorDisposedDateField().isLeapYear(year) && key == 1)
				format = (lastdates[key] + 1) + " " + months[key] + " " + year;
			else
				format = lastdates[key] + " " + months[key] + " " + year;
			dateList[pos] = format;
			dateValueMap.put(format, new ClientFinanceDate((year - 1900), key,
					lastdates[key]));
			pos++;
		}

		return dateList;
	}

	private String[] getMonthStrings() {
		return new String[] { Accounter.constants().JAN(),
				Accounter.constants().FEB(), Accounter.constants().MAR(),
				Accounter.constants().APR(), Accounter.constants().may(),
				Accounter.constants().JUN(), Accounter.constants().JUL(),
				Accounter.constants().AUG(), Accounter.constants().SEPT(),
				Accounter.constants().OCT(), Accounter.constants().NOV(),
				Accounter.constants().DEC(), };
	}

	private int[] getLastDateValues() {
		return new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	}

	/**
	 * Calculate the valid Dates based on the Depreciation Start Date
	 */

	private void calucateDateCombo() {
		monthsKey.clear();
		int monthvalue = 0, year = 0;
		ClientFinanceDate startDate = new ClientFinanceDate(Accounter
				.getCompany().getPreferences().getDepreciationStartDate());
		ClientFinanceDate soldorDisposedate = getSoldorDisposedDateField()
				.getEnteredDate();
		if (soldorDisposedate.getMonth() >= startDate.getMonth()) {
			monthvalue = startDate.getMonth();
			year = soldorDisposedate.getYear() + 1900;
		} else if (soldorDisposedate.getMonth() < startDate.getMonth()) {
			monthvalue = startDate.getMonth();
			year = (soldorDisposedate.getYear() + 1900) - 1;
		}
		for (int month = 0; month < 12; month++) {
			if (monthvalue > 11) {
				monthvalue = 0;
				year++;
			}
			monthsKey.put(monthvalue, year);
			monthvalue++;

		}
	}

	/**
	 * This is for validating Required input fields in this both selling and
	 * Disposing Views
	 */

	@Override
	public boolean validate() throws InvalidEntryException,
			InvalidTransactionEntryException {
		switch (this.validationCount) {
		case 5:
			return AccounterValidator.validateForm(detailsForm, false);
		case 4:
			return AccounterValidator.validate_ZeroAmount(salepriceText
					.getAmount());
		case 3:
			return AccounterValidator.validate_Radiovalue(QuestionItem
					.getValue());
		case 2:

			if (QuestionItem.getValue().equals(allDepOption)) {
				return AccounterValidator.isNullValue(dateItemCombo.getValue());
			}
		case 1:
			ClientFixedAsset asset = (ClientFixedAsset) data;
			if (asset != null)
				return AccounterValidator.validateSellorDisposeDate(
						new ClientFinanceDate(asset.getPurchaseDate()),
						getSoldorDisposedDateField().getEnteredDate(),
						Accounter.constants().datesold());

		default:
			return true;
		}
	}

	protected DateField getSoldorDisposedDateField() {
		return datesold;
	}

	public void saveAndUpdateView() throws Exception {
		getJournalViewObjet(getTempFixedAssetObject());

	}

	/**
	 * This method is for getting ReviewJournalObject for displaying in JOurnal
	 * Dialog by passing TempFixedAsset Object
	 * 
	 * @param tempObject
	 */

	protected void getJournalViewObjet(TempFixedAsset tempObject) {
		AsyncCallback<FixedAssetSellOrDisposeReviewJournal> callback = new AsyncCallback<FixedAssetSellOrDisposeReviewJournal>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter
							.showMessage("Your session expired, Please login again to continue");
				} else {
					Accounter.showError("Review Journal is Failed");
				}
			}

			@Override
			public void onSuccess(FixedAssetSellOrDisposeReviewJournal result) {
				if (result == null) {
					onFailure(new Exception());
				}
				if (result != null) {
					FixedAssetSellOrDisposeReviewJournal journalObject = result;
					openJournalDialog(journalObject);
				}

			}
		};
		this.rpcUtilService.getReviewJournal(tempObject, callback);

	}

	/**
	 * Setting the salesprice and salesaccount for selling Item to
	 * TempFixedAssetObject
	 * 
	 * @return
	 */

	protected TempFixedAsset getTempFixedAssetObject() {
		TempFixedAsset tempFixedAsset = getPreparedTempAssetObject();
		tempFixedAsset.setSalesPrice(salepriceText.getAmount());
		if (this.accountForSale != null)
			tempFixedAsset.setSalesAccountName(this.accountForSale.getName());
		return tempFixedAsset;
	}

	/**
	 * This method for creating TempObject for getting Review Journal Object and
	 * common method for selling and Disposing Views.
	 * 
	 * @return
	 */

	protected TempFixedAsset getPreparedTempAssetObject() {
		TempFixedAsset tempFixedAsset = new TempFixedAsset();
		ClientFixedAsset asset = (ClientFixedAsset) data;
		tempFixedAsset.setFixedAssetID(asset.getID());
		tempFixedAsset.setPurchaseDate(new ClientFinanceDate(asset
				.getPurchaseDate()));
		tempFixedAsset.setNoDepreciation(QuestionItem.getValue().equals(
				noDepOption) ? true : false);
		tempFixedAsset.setSoldOrDisposedDate(getSoldorDisposedDateField()
				.getEnteredDate());
		ClientFinanceDate date = dateValueMap.get(dateItemCombo.getValue()
				.toString());
		if (date != null)
			tempFixedAsset.setDepreciationTillDate(date);
		tempFixedAsset.setPurchasePrice(asset.getPurchasePrice());
		tempFixedAsset.setBookValue(asset.getBookValue());
		ClientAccount assetAccount = getCompany().getAccount(
				asset.getAssetAccount());
		ClientAccount linkdAccount = getCompany().getAccount(
				assetAccount.getLinkedAccumulatedDepreciationAccount());
		tempFixedAsset
				.setLinkedAccumulatedDepreciatedAccountName(linkdAccount != null ? linkdAccount
						.getName() : "");
		tempFixedAsset.setAssetAccountName(assetAccount != null ? assetAccount
				.getName() : "");
		tempFixedAsset.setExpenseAccountName(getCompany().getAccount(
				asset.getDepreciationExpenseAccount()).getName());
		tempFixedAsset.setDepreciationMethod(asset.getDepreciationMethod());
		tempFixedAsset.setDepreciationRate(asset.getDepreciationRate());
		return tempFixedAsset;
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		ClientFixedAsset createdAsset = (ClientFixedAsset) result;
		if (createdAsset.getID() != 0) {
			// Accounter.showInformation(FinanceApplication
			// .constants().fixedAssetItemHasBeenSold());
			saveAndClose = true;
			super.saveSuccess(result);
			History.newItem(ActionFactory
					.getSoldDisposedListAction().getHistoryToken());
			// ActionFactory.getSoldDisposedListAction().run(null,
			// false);
		} else
			saveFailed(new Exception(Accounter.constants().failed()));

	}

	@Override
	public void saveFailed(Throwable exception) {
		super.saveFailed(exception);
	}

	/**
	 * setting the Date Item whether required or not depends on the RadioButton
	 * Option
	 * 
	 * @param value
	 */
	protected void changeDateCombo(String value) {
		if (value.equals(allDepOption)) {
			dateForm.clear();
			dateItemCombo.setDisabled(false);
			dateItemCombo.setRequired(true);
			dateForm.setFields(dateItemCombo);

		} else {
			dateForm.clear();
			initDateCombo();
			dateItemCombo.setDisabled(true);
			dateItemCombo.setRequired(false);
			dateForm.setFields(dateItemCombo);
		}
	}

	private DebitAccountCombo createDebitAccountCombo() {
		DebitAccountCombo accountCombo = new DebitAccountCombo(Accounter
				.constants().accountToDebitForSale());

		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						setAccountforsale(selectItem);
					}
				});
		return accountCombo;
	}

	protected void setAccountforsale(ClientAccount account) {
		if (account != null)
			this.accountForSale = account;
	}

	public List<DynamicForm> getForms() {
		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.salepriceText.setFocus();

	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.accountCombo.addComboItem((ClientAccount) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.accountCombo.addComboItem((ClientAccount) core);

			break;

		case AccounterCommand.DELETION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.accountCombo.removeComboItem((ClientAccount) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.accountCombo.removeComboItem((ClientAccount) core);
			break;

		case AccounterCommand.UPDATION_SUCCESS:
			break;
		}
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().sellingRegisteredItem();
	}
}
