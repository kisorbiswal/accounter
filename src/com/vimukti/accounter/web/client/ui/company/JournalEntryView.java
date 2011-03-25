package com.vimukti.accounter.web.client.ui.company;

/*
 * Modified by Murali A
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientEntry;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionMakeDeposit;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.TransactionJournalEntryGrid;

public class JournalEntryView extends AbstractTransactionBaseView<ClientEntry> {

	private TransactionJournalEntryGrid grid;
	double debit, credit;

	DynamicForm jourForm;
	Label lab1;

	TextItem memoText, jourNoText;
	protected boolean isClose;
	private String vouchNo = "1";
	boolean voucharNocame = false;

	private ClientJournalEntry takenJournalEntry;
	private CompanyMessages companyConstants = GWT
			.create(CompanyMessages.class);
	// private HorizontalPanel lablPanel;
	private VerticalPanel gridPanel;

	private ArrayList<DynamicForm> listforms;
	private Button addButton;

	public JournalEntryView() {
		super(ClientTransaction.TYPE_JOURNAL_ENTRY,
				JOURNALENTRY_TRANSACTION_GRID);
		this.validationCount = 6;
	}

	@Override
	public void saveAndUpdateView() throws Exception {
		transactionObject = getJournalEntryObject();
		ClientJournalEntry journalEntry = getJournalEntryObject();
		super.saveAndUpdateView();
		if (takenJournalEntry == null)
			createObject(journalEntry);
		else
			alterObject(journalEntry);

	}

	@Override
	public boolean validate() throws InvalidEntryException,
			InvalidTransactionEntryException {
		switch (this.validationCount) {
		case 6:
			return AccounterValidator.validateForm(jourForm);
		case 5:
			return AccounterValidator.isBlankTransaction(grid);
		case 4:
			return grid.validateGrid();
		case 3:

			// return grid.validateCustomers();

		case 2:
			// return grid.validateVendors();
			return true;
		case 1:
			return grid.validateTotal();
		default:
			return false;
		}

	}

	protected boolean validateForm() {

		return jourForm.validate();
	}

	public void initListGrid() {
		grid = new TransactionJournalEntryGrid(isEdit);
		grid.setTransactionView(this);
		grid.setCanEdit(true);
		grid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		grid.init();
		grid.setDisabled(isEdit);
	}

	@Override
	public void saveFailed(Throwable exception) {
		super.saveFailed(exception);
		Accounter.showError(FinanceApplication.getCompanyMessages()
				.duplicationOfJournalEntriesNotAllowed());
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			// if (takenJournalEntry != null)
			// Accounter.showInformation(FinanceApplication
			// .getCompanyMessages().journalUpdatedSuccessfully());
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
			saveFailed(new JavaScriptException("I m failed"));
		}

	}

	//
	// protected void save() {
	// MainFinanceWindow.removeFromTab(this);
	//	
	// }

	public List<ClientEntry> getallEntries(ClientTransaction object) {

		List<ClientEntry> records = grid.getRecords();
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
					// TODO: handle exception
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
					// TODO: handle exception
				}
				// Setting Line total
				try {
					entry.setDebit(rec.getDebit());

				} catch (Exception e) {
				}
				try {
					entry.setCredit(rec.getCredit());

				} catch (Exception e) {
					// TODO: handle exception
				}

				transactionItems.add(entry);
			}
		}
		return transactionItems;
	}

	private ClientJournalEntry getJournalEntryObject() {
		ClientJournalEntry journalEntry;

		if (takenJournalEntry != null)

		{
			journalEntry = takenJournalEntry;

			saveAndCloseButton.setEnabled(true);
			jourNoText.setDisabled(true);
			memoText.setDisabled(true);
			// memoText.setDisabled(true);
			// FIXME--need to implement this feature
			// grid.setEnableMenu(false);

			grid.canDeleteRecord(false);
			grid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
			grid.setCanEdit(false);
			grid.setDisabled(true);
			// Disabling the cells
			// FIXME
			// grid.setEditDisableCells(0, 1, 2, 3, 4, 5, 6, 7);
		}

		else
			journalEntry = new ClientJournalEntry();

		journalEntry.setNumber(jourNoText.getValue().toString());
		journalEntry.setMemo(memoText.getValue().toString() != null ? memoText
				.getValue().toString() : "");
		journalEntry.setDate(new ClientFinanceDate().getTime());
		if (DecimalUtil.isEquals(grid.getTotalDebittotal(), grid
				.getTotalCredittotal())) {
			journalEntry.setDebitTotal(grid.getTotalDebittotal());
			journalEntry.setCreditTotal(grid.getTotalCredittotal());
			journalEntry.setTotal(grid.getTotalDebittotal());
		}

		List<ClientEntry> allGivenRecords = grid.getRecords();
		// for (ClientEntry entry : allGivenRecords) {
		// entry.setStringID("");
		// }
		journalEntry.setEntry(allGivenRecords);
		transactionObject = journalEntry;
		return journalEntry;
	}

	protected void clearFields() {
		// FIXME-- The form values need to be reset
		// jourForm.resetValues();
		grid.removeAllRecords();

	}

	@Override
	protected void createControls() {
		listforms = new ArrayList<DynamicForm>();

		setTitle(companyConstants.journalEntry());
		lab1 = new Label(FinanceApplication.getCompanyMessages()
				.journalEntryNew());
		lab1.addStyleName(FinanceApplication.getCompanyMessages().lableTitle());
        lab1.setHeight("35px");
		jourNoText = new TextItem(companyConstants.journalNumber());
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

		memoText = new TextItem(companyConstants.memo());
		memoText.setHelpInformation(true);
		jourForm = new DynamicForm();
		jourForm.setIsGroup(true);
		jourForm.setGroupTitle(companyConstants.journal());
		jourForm.setWidth("50%");
		jourForm.setHeight("50%");
		jourForm.setFields(jourNoText, memoText);

		initListGrid();
		grid.initTransactionData();
		gridPanel = new VerticalPanel();
		addButton = new Button(FinanceApplication.getCompanyMessages().add());
		addButton.setEnabled(!isEdit);
		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				VoucherNoreset();
			}
		});

		gridPanel.add(grid);

		gridPanel.add(addButton);
		
        addButton.getElement().getParentElement().addClassName("add-button");
		
		Element addseparator=DOM.createSpan();
		addseparator.addClassName("add-separator");
		DOM.appendChild(addButton.getElement(),addseparator);
		
		Element addimage=DOM.createSpan();
		addimage.addClassName("add-image");
		DOM.appendChild(addButton.getElement(),addimage);
		
		ThemesUtil.addDivToButton(addButton,FinanceApplication.getThemeImages().button_right_blue_image(),"blue-right-image");
		
		
		
		// gridPanel.add(labelPanel);

		if (takenJournalEntry != null) {
			jourNoText.setValue(takenJournalEntry.getNumber());
			memoText.setValue(takenJournalEntry.getMemo());

			// journalEntry.setEntry(getallEntries(journalEntry));
			// journalEntry.setDebitTotal(totalDebittotal);
			// journalEntry.setCreditTotal(totalCredittotal);

		}

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab1);
		mainVLay.add(jourForm);
		mainVLay.add(gridPanel);
		// mainVLay.add(labelPane);

		canvas.add(mainVLay);
		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(jourForm);

	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {
		takenJournalEntry = (ClientJournalEntry) transactionObject;
		jourNoText.setValue(takenJournalEntry.getNumber());
		grid.setVoucherNumber(takenJournalEntry.getNumber());

		List<ClientEntry> entries = takenJournalEntry.getEntry();

		ClientEntry rec[] = new ClientEntry[entries.size()];
		int i = 0;
		@SuppressWarnings("unused")
		ClientEntry temp = null;
		for (ClientEntry entry : takenJournalEntry.getEntry()) {

			rec[i] = takenJournalEntry.getEntry().get(i);
			@SuppressWarnings("unused")
			ClientCompany company = FinanceApplication.getCompany();
			rec[i].setVoucherNumber(entry.getVoucherNumber());

			// FIXME--The date need to be set for every record
			// rec[i].setAttribute(ATTR_DATE, takenJournalEntry.getDate());

			if (entry.getType() == ClientEntry.TYPE_FINANCIAL_ACCOUNT) {
				rec[i].setType(ClientEntry.TYPE_FINANCIAL_ACCOUNT);
				if (entry.getAccount() != null)
					rec[i].setAccount(entry.getAccount());
			} else if (entry.getType() == ClientTransactionMakeDeposit.TYPE_VENDOR) {
				rec[i].setType(ClientEntry.TYPE_VENDOR);
				if (entry.getVendor() != null)
					rec[i].setVendor(entry.getVendor());
			} else {
				rec[i].setType(ClientEntry.TYPE_CUSTOMER);
				if (entry.getCustomer() != null)
					rec[i].setCustomer(entry.getCustomer());

			}

			rec[i].setMemo(entry.getMemo());
			rec[i].setDebit(entry.getDebit());
			rec[i].setCredit(entry.getCredit());

			// if (temp != null)
			// grid.selectRecord(grid.getSelectedRecordIndex(temp)));

			i++;
		}
		grid.setAllRecords(Arrays.asList(rec));
		if (takenJournalEntry.getMemo() != null)
			memoText.setValue(takenJournalEntry.getMemo());
		getJournalEntryObject();

		initTransactionViewData();

	}

	@Override
	protected void initTransactionViewData() {
		initJournalNumber();
		if (!isEdit)
			initVocherNumer();
	}

	private void initJournalNumber() {
		if (isEdit && takenJournalEntry != null) {
			jourNoText.setValue(takenJournalEntry.getNumber());
			return;
		} else {
			rpcUtilService.getNextTransactionNumber(
					ClientTransaction.TYPE_JOURNAL_ENTRY,
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							Accounter.showError(FinanceApplication
									.getCompanyMessages()
									.failedToGetTransactionNumber());
						}

						@Override
						public void onSuccess(String result) {
							if (result == null) {
								onFailure(new Exception());
							}
							jourNoText.setValue(String.valueOf(result));

						}
					});
		}

	}

	private void initVocherNumer() {
		rpcUtilService.getNextVoucherNumber(new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				voucharNocame = true;
				vouchNo = result;
				grid.setVoucherNumber(vouchNo);
			}

			@Override
			public void onFailure(Throwable caught) {
				Accounter.showError(FinanceApplication.getCompanyMessages()
						.failedToGetVocherNumber());

			}
		});
	}

	public void VoucherNoreset() {
		if (!voucharNocame)
			grid.addLoadingImagePanel();
		Timer timer = new Timer() {
			@Override
			public void run() {
				if (voucharNocame) {
					grid.removeLoadingImage();
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
		entry.setAccount("");
		entry.setMemo("");
		entry1.setAccount("");
		entry1.setMemo("");
		entry.setType(ClientEntry.TYPE_FINANCIAL_ACCOUNT);
		entry1.setType(ClientEntry.TYPE_FINANCIAL_ACCOUNT);

		grid.addData(entry);
		if (grid.getRecords().size() < 2)
			grid.addData(entry1);

	}

	@Override
	public void updateNonEditableItems() {

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
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	public void onEdit() {
		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				Accounter.showError(((InvalidOperationException) (caught))
						.getDetailedMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				// if (result)
				// enableFormItems();
				Accounter.showError("Journal Entry can't be edited.");
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transactionObject
				.getType());
		this.rpcDoSerivce.canEdit(type, transactionObject.stringID,
				editCallBack);

	}

	protected void enableFormItems() {
		isEdit = false;
		jourNoText.setDisabled(isEdit);
		grid.setDisabled(isEdit);
		grid.setCanEdit(true);
		addButton.setEnabled(!isEdit);

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
