package com.vimukti.accounter.web.client.ui.company;

/*
 * Modified by Murali A
 */

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AddButton;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientEntry;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.TransactionJournalEntryTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class JournalEntryView extends
		AbstractTransactionBaseView<ClientJournalEntry> {

	private TransactionJournalEntryTable grid;
	double debit, credit;

	Label lab1;
	DynamicForm memoForm, totalForm, dateForm;
	TextItem jourNoText;
	TextAreaItem memoText;
	protected boolean isClose;
	private String vouchNo = "1";
	boolean voucharNocame = false;
	AmountLabel creditTotalText, deditTotalText;

	// private HorizontalPanel lablPanel;
	private VerticalPanel gridPanel;

	private ArrayList<DynamicForm> listforms;
	private AddButton addButton;
	com.vimukti.accounter.web.client.externalization.AccounterConstants accounterConstants = Accounter
			.constants();
	private boolean locationTrackingEnabled;

	public JournalEntryView() {
		super(ClientTransaction.TYPE_JOURNAL_ENTRY);
		locationTrackingEnabled = ClientCompanyPreferences.get()
				.isLocationTrackingEnabled();
	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();
		super.saveAndUpdateView();
		saveOrUpdate(transaction);
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		// Validations
		// 1. is valid memo?
		// 2. is valid transaction date?
		// 3. is in prevent posting before date?
		// 4. date form valid?
		// 5. is blank transaction?
		// 6. is valid grid?
		// 7. is valid total?

		if (memoText.getValue().toString() != null
				&& memoText.getValue().toString().length() >= 256) {
			result.addError(memoText, Accounter.constants()
					.memoCannotExceedsmorethan255Characters());

		}
		// if (!AccounterValidator.isValidTransactionDate(getTransactionDate()))
		// {
		// result.addError(transactionDateItem,
		// accounterConstants.invalidateTransactionDate());
		// } else
		if (AccounterValidator
				.isInPreventPostingBeforeDate(getTransactionDate())) {
			result.addError(transactionDateItem,
					accounterConstants.invalidateDate());
		}
		result.add(dateForm.validate());
		// if (AccounterValidator.isBlankTransaction(grid)) {
		// result.addError(grid, accounterConstants.blankTransaction());
		// } else
		result.add(grid.validateGrid());
		if (!grid.isValidTotal()) {
			result.addError(grid, Accounter.constants().totalMustBeSame());
		}
		return result;

	}

	// protected boolean validateForm() {
	//
	// return dateForm.validate(false);
	// }

	public void initListGrid() {
		grid = new TransactionJournalEntryTable() {

			@Override
			public void updateNonEditableItems() {
				JournalEntryView.this.updateNonEditableItems();
			}
		};
		grid.setDisabled(isInViewMode());
		grid.getElement().getStyle().setMarginTop(10, Unit.PX);
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		AccounterException accounterException = (AccounterException) exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			// if (takenJournalEntry != null)
			// Accounter.showInformation(FinanceApplication
			// .constants().journalUpdatedSuccessfully());
			super.saveSuccess(result);
			// if (saveAndClose) {
			// save();
			// } else {

			// clearFields();

			// }
			// if (callback != null) {
			// callback.onSuccess(result);
			// }
		} else {
			saveFailed(new AccounterException(Accounter.constants().imfailed()));
		}

	}

	//
	// protected void save() {
	// MainFinanceWindow.removeFromTab(this);
	//
	// }

	public List<ClientEntry> getallEntries(ClientTransaction object) {

		List<ClientEntry> records = grid.getAllRows();
		final List<ClientEntry> transactionItems = new ArrayList<ClientEntry>();
		if (records != null) {
			for (ClientEntry record : records) {
				ClientEntry rec = record;
				final ClientEntry entry = new ClientEntry();

				// int date = Integer.parseInt(rec.getAttribute("Date"));

				// Setting Object to Transaction item in bidirectional way
				// entry.setTransaction(object);
				// vouch_no";"date";type";"account""memo";debit""credit";
				// Setting type of trasaction

				// Setting number
				try {

					entry.setVoucherNumber(rec.getVoucherNumber());

				} catch (Exception e) {
					e.printStackTrace();
				}
				// try {
				//
				// entry.setCreatedDate(UIUtils.toDate(rec
				// .getAttribute("date")));
				// } catch (Exception e) {
				// }

				try {
					entry.setType(rec.getType());
				}
				// }
				catch (Exception e) {
				}

				// Setting Unit Price
				try {
					entry.setMemo(rec.getMemo());

					// entry.setVendor(selectedVendor);

				} catch (Exception e) {
					e.printStackTrace();
				}
				// Setting Line total
				try {
					entry.setDebit(rec.getDebit());

				} catch (Exception e) {
				}
				try {
					entry.setCredit(rec.getCredit());

				} catch (Exception e) {
					e.printStackTrace();
				}

				transactionItems.add(entry);
			}
		}
		return transactionItems;
	}

	protected void updateTransaction() {
		super.updateTransaction();
		if (isInViewMode()) {
			jourNoText.setDisabled(true);
			memoText.setDisabled(true);
			// memoText.setDisabled(true);
			// FIXME--need to implement this feature
			// grid.setEnableMenu(false);

			grid.setDisabled(true);
			// Disabling the cells
			// FIXME
			// grid.setEditDisableCells(0, 1, 2, 3, 4, 5, 6, 7);
		}

		transaction.setNumber(jourNoText.getValue().toString());
		transaction.setDate(transactionDateItem.getEnteredDate().getDate());
		transaction.setMemo(memoText.getValue().toString() != null ? memoText
				.getValue().toString() : "");
		// initMemo(transaction);
		transaction.setDate(new ClientFinanceDate().getDate());
		if (DecimalUtil.isEquals(grid.getTotalDebittotal(),
				grid.getTotalCredittotal())) {
			transaction.setDebitTotal(grid.getTotalDebittotal());
			transaction.setCreditTotal(grid.getTotalCredittotal());
			transaction.setTotal(grid.getTotalDebittotal());
		}
		List<ClientEntry> allGivenRecords = grid.getAllRows();
		transaction.setEntry(allGivenRecords);
	}

	private void initMemo(ClientJournalEntry journalEntry) {
		if (memoText.getValue().toString() != null
				&& memoText.getValue().toString().length() >= 255) {
			// BaseView.errordata.setHTML("i am here");
			// BaseView.commentPanel.setVisible(true);
			// AbstractBaseView.errorOccured = true;
			addError(this, Accounter.constants().iamHere());

		} else
			journalEntry.setMemo(memoText.getValue() != null ? memoText
					.getValue().toString() : "");

	}

	protected void clearFields() {
		// FIXME-- The form values need to be reset
		// jourForm.resetValues();
		grid.clear();

	}

	@Override
	protected void createControls() {
		listforms = new ArrayList<DynamicForm>();

		lab1 = new Label(Accounter.constants().journalEntry());
		lab1.removeStyleName("gwt-Label");
		lab1.addStyleName(Accounter.constants().labelTitle());
		// lab1.setHeight("35px");
		transactionDateItem = createTransactionDateItem();
		jourNoText = new TextItem(Accounter.constants().no());
		jourNoText.setToolTip(Accounter.messages().giveNoTo(
				this.getAction().getViewName()));
		jourNoText.setHelpInformation(true);
		jourNoText.setRequired(true);
		jourNoText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String str = jourNoText.getValue().toString();
				// if (!UIUtils.isNumber(str)) {
				// Accounter
				// .showError(AccounterErrorType.INCORRECTINFORMATION);
				// jourNoText.setValue("");
				// }
			}
		});

		memoText = new TextAreaItem(Accounter.constants().memo());
		memoText.setMemo(true, this);
		memoText.setHelpInformation(true);

		initListGrid();
		// grid.initTransactionData();
		gridPanel = new VerticalPanel();
		addButton = new AddButton(this);
		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				VoucherNoreset();
			}
		});

		gridPanel.add(grid);
		gridPanel.setWidth("100%");
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(addButton);
		hPanel.getElement().getStyle().setMarginTop(8, Unit.PX);
		hPanel.getElement().getStyle().setFloat(Float.LEFT);

		gridPanel.add(hPanel);

		addButton.setEnabled(!isInViewMode());
		dateForm = new DynamicForm();
		dateForm.setNumCols(6);
		dateForm.setStyleName("datenumber-panel");
		locationCombo = createLocationCombo();
		dateForm.setFields(transactionDateItem, jourNoText);
		if (locationTrackingEnabled)
			dateForm.setFields(locationCombo);

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			dateForm.setFields(classListCombo);
		}

		HorizontalPanel datepannel = new HorizontalPanel();
		datepannel.setWidth("100%");
		datepannel.add(dateForm);
		datepannel.setCellHorizontalAlignment(dateForm, ALIGN_RIGHT);

		memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoText);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		deditTotalText = new AmountLabel(Accounter.constants()
				.debitTotalColon());
		deditTotalText.setWidth("180px");
		((Label) deditTotalText.getMainWidget())
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		deditTotalText.setDefaultValue("" + UIUtils.getCurrencySymbol()
				+ "0.00");
		deditTotalText.setDisabled(true);

		creditTotalText = new AmountLabel(Accounter.constants()
				.creditTotalColon());
		creditTotalText.setWidth("180px");
		((Label) creditTotalText.getMainWidget())
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		creditTotalText.setDefaultValue("" + UIUtils.getCurrencySymbol()
				+ "0.00");
		creditTotalText.setDisabled(true);

		totalForm = new DynamicForm();
		totalForm.setWidth("75%");
		totalForm.addStyleName("textbold");
		totalForm.setFields(deditTotalText, creditTotalText);

		HorizontalPanel bottomPanel = new HorizontalPanel();
		bottomPanel.setWidth("100%");
		bottomPanel.add(memoForm);
		bottomPanel.setCellHorizontalAlignment(memoForm, ALIGN_LEFT);
		bottomPanel.add(totalForm);
		bottomPanel.setCellHorizontalAlignment(totalForm, ALIGN_RIGHT);

		// addButton.getElement().getParentElement().addClassName("add-button");

		// ThemesUtil
		// .addDivToButton(addButton, FinanceApplication.getThemeImages()
		// .button_right_blue_image(), "blue-right-image");

		// gridPanel.add(labelPanel);

		if (isInViewMode()) {
			jourNoText.setValue(transaction.getNumber());
			memoText.setValue(transaction.getMemo());

			// journalEntry.setEntry(getallEntries(journalEntry));
			// journalEntry.setDebitTotal(totalDebittotal);
			// journalEntry.setCreditTotal(totalCredittotal);

		}

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab1);
		mainVLay.add(datepannel);
		mainVLay.add(gridPanel);
		mainVLay.add(bottomPanel);
		// mainVLay.add(labelPane);

		this.add(mainVLay);
		setSize("100%", "100%");

		listforms.add(dateForm);
		listforms.add(memoForm);
		listforms.add(totalForm);

		/* Adding dynamic forms in list */

	}

	@Override
	protected void initTransactionViewData() {
		if (transaction != null) {
			jourNoText.setValue(transaction.getNumber());
			transactionDateItem.setEnteredDate(transaction.getDate());
			// grid.setVoucherNumber(transaction.getNumber());

			List<ClientEntry> entries = transaction.getEntry();

			grid.setAllRows(entries);
			if (transaction.getMemo() != null)
				memoText.setValue(transaction.getMemo());
			updateTransaction();
		} else {
			setData(new ClientJournalEntry());
		}
		initJournalNumber();
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		if (!isInViewMode())
			initVocherNumer();

	}

	private void initJournalNumber() {
		if (isInViewMode()) {
			jourNoText.setValue(transaction.getNumber());
			return;
		} else {
			rpcUtilService.getNextTransactionNumber(
					ClientTransaction.TYPE_JOURNAL_ENTRY,
					new AccounterAsyncCallback<String>() {

						@Override
						public void onException(AccounterException caught) {
							Accounter.showError(Accounter.constants()
									.failedToGetTransactionNumber());
						}

						@Override
						public void onResultSuccess(String result) {
							if (result == null) {
								onFailure(new Exception());
							}
							jourNoText.setValue(String.valueOf(result));

						}
					});
		}

	}

	private void initVocherNumer() {
		rpcUtilService
				.getNextVoucherNumber(new AccounterAsyncCallback<String>() {

					@Override
					public void onResultSuccess(String result) {
						voucharNocame = true;
						vouchNo = result;
						// grid.setVoucherNumber(vouchNo);
					}

					@Override
					public void onException(AccounterException caught) {
						Accounter.showError(Accounter.constants()
								.failedToGetVocherNumber());

					}
				});
	}

	public void VoucherNoreset() {
		// if (!voucharNocame)
		// grid.addLoadingImagePanel();
		Timer timer = new Timer() {
			@Override
			public void run() {
				if (voucharNocame) {
					// grid.removeLoadingImage();
					addEmptRecords();
					this.cancel();
				}
			}
		};
		timer.scheduleRepeating(100);
	}

	protected void addEmptRecords() {
		ClientEntry entry = new ClientEntry();
		ClientEntry entry1 = new ClientEntry();
		entry.setAccount(0);
		entry.setMemo("");
		entry1.setAccount(0);
		entry1.setMemo("");
		entry.setType(ClientEntry.TYPE_FINANCIAL_ACCOUNT);
		entry1.setType(ClientEntry.TYPE_FINANCIAL_ACCOUNT);
		entry.setVoucherNumber(vouchNo);
		entry1.setVoucherNumber(vouchNo);

		grid.add(entry);
		if (grid.getAllRows().size() < 2)
			grid.add(entry1);

	}

	@Override
	public void updateNonEditableItems() {
		if (grid == null)
			return;
		deditTotalText.setAmount(grid.getTotalDebittotal());
		creditTotalText.setAmount(grid.getTotalCredittotal());

	}

	public static JournalEntryView getInstance() {

		return new JournalEntryView();
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.jourNoText.setFocus();
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
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		jourNoText.setDisabled(isInViewMode());
		grid.setDisabled(isInViewMode());
		// grid.setCanEdit(true);
		addButton.setEnabled(!isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		// addButton.setType(Button.ADD_BUTTON);
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().journalEntry();
	}

	@Override
	protected void refreshTransactionGrid() {

	}

	@Override
	public boolean canEdit() {
		return false;
	}
}
