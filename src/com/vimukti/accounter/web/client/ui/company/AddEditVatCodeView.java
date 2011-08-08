//package com.vimukti.accounter.web.client.ui.company;
//
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.user.client.rpc.AccounterAsyncCallback;
//import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.HorizontalPanel;
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.VerticalPanel;
//import com.vimukti.accounter.web.client.core.AccounterCommand;
//import com.vimukti.accounter.web.client.core.AccounterCoreType;
//import com.vimukti.accounter.web.client.core.ClientCompany;
//import com.vimukti.accounter.web.client.core.ClientTaxCode;
//import com.vimukti.accounter.web.client.core.ClientTaxRates;
//import com.vimukti.accounter.web.client.core.ClientVATAgency;
//import com.vimukti.accounter.web.client.core.IAccounterCore;
//import com.vimukti.accounter.web.client.core.Utility;
//import com.vimukti.accounter.web.client.ui.AbstractBaseView;
//import com.vimukti.accounter.web.client.ui.FinanceApplication;
//import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
//import com.vimukti.accounter.web.client.ui.UIUtils;
//import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
//import com.vimukti.accounter.web.client.ui.core.Accounter;
//import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
//import com.vimukti.accounter.web.client.ui.core.PercentageField;
//import com.vimukti.accounter.web.client.ui.core.ViewManager;
//import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
//import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
//import com.vimukti.accounter.web.client.ui.forms.TextItem;
//import com.vimukti.accounter.web.client.ui.grids.VatCodeGrid;
//
//public class AddEditVatCodeView extends AbstractBaseView<ClientTaxRates> {
//	static final String ATTR_RATE = "rate";
//	static final String ATTR_AS_OF = "asof";
//
//	String info = FinanceApplication.constants().addOrEditVAT();
//	PercentageField vatCodeText;
//	TextItem descriptionText;
//	CustomCombo<ClientVATAgency> vatAgencyCombo;
//
//	CheckboxItem statusCheck;
//	DynamicForm vatCodeForm;
//	VatCodeGrid gridView;
//	private ClientVATAgency selectedVatAgency;
//	private ClientTaxCode selectedVatCode;
//	protected ClientCompany company;
//	private ClientTaxCode takenVatCode;
//
//	boolean isRateInValid;
//
//	public AddEditVatCodeView() {
//
//	}
//
//	protected void initCompany() {
//
//		this.company = FinanceApplication.getCompany();
//
//	}
//
//	protected void updateCompany() {
//
//		rpcGetService.getObjectById(AccounterCoreType.COMPANY, company
//				.getID(), new AccounterAsyncCallback<ClientCompany>() {
//
//			public void onException(AccounterException caught) {
//				Accounter.showError(FinanceApplication.constants()
//						.failedToUpdateCompany());
//				// SC.logWarn("Failed to Update the Company...");
//
//			}
//
//			public void onSuccess(ClientCompany result) {
//
//				if (result == null) {
//					onFailure(new Exception());
//					return;
//				}
//
//				FinanceApplication.setCompany(result);
//
//			}
//		});
//	}
//
//	private void initVatAgencyCombo() {
//		vatAgencyCombo.initCombo(FinanceApplication.getCompany()
//				.getVatAgencies());
//		selectedVatAgency = vatAgencyCombo.getSelectedValue();
//		if (selectedVatCode != null) {
//			vatAgencyCombo.setComboItem(FinanceApplication.getCompany()
//					.getVatAgency(selectedVatCode.getID()));
//		}
//
//	}
//
//	private void createControls() {
//
//		Label infolabel = new Label(info);
//		vatCodeText = new PercentageField(FinanceApplication
//				.constants().vatCode());
//		// vatCodeText.setWidth("100%");
//		vatCodeText.setRequired(true);
//
//		descriptionText = new TextItem(FinanceApplication.constants()
//				.description());
//		// descriptionText.setWidth("100%");
//		//
//		// vatAgencyCombo = new CustomCombo("VAT Agency",
//		// SelectItemType.TAX_AGENCY, false);
//		// vatAgencyCombo.setRequired(true);
//		//
//		// vatAgencyCombo
//		// .addSelectionChangeHandler(new
//		// IAccounterComboSelectionChangeHandler() {
//		//
//		// public void selectedComboBoxItem(IsSerializable selectItem) {
//		//
//		// selectedVatAgency = (ClientTaxAgency) selectItem;
//		//
//		// }
//		// });
//
//		statusCheck = new CheckboxItem(FinanceApplication.constants()
//				.active());
//		// statusCheck.setWidth("100%");
//		statusCheck.setValue(true);
//
//		vatCodeForm = new DynamicForm();
//		vatCodeForm = UIUtils.form(FinanceApplication.constants()
//				.vatCode());
//		vatCodeForm.setFields(vatCodeText, descriptionText, statusCheck);
//		// vatCodeForm.setWidth100();
//		// vatCodeForm.setHeight100();
//
//		Label vatRates = new Label(FinanceApplication.constants()
//				.fontWeigtht());
//		// vatRates.setAutoFit(true);
//		// vatRates.setWidth100();
//		// vatRates.setWrap(false);
//		// vatRates.setMargin(10);
//
//		initListGrid();
//
//		HorizontalPanel buttonsLayout = new HorizontalPanel();
//		// buttonsLayout.setHeight("20%");
//		buttonsLayout.setWidth("100%");
//		// buttonsLayout.setMembersMargin(5);
//		// buttonsLayout.setLayoutLeftMargin(10);
//		// buttonsLayout.setLayoutRightMargin(5);
//
//		Button button1 = new Button();
//		button1.setTitle(FinanceApplication.constants().ok());
//		button1.addClickHandler(new ClickHandler() {
//
//			public void onClick(ClickEvent event) {
//				createOrEditVatCode();
//			}
//		});
//
//		Button button2 = new Button();
//		button2.setTitle(FinanceApplication.constants().cancel());
//		button2.addClickHandler(new ClickHandler() {
//
//			public void onClick(ClickEvent event) {
//				// if(listGrid.getSelectedRecord()!= null)
//				// Record record=()listGrid.getSelectedRecord();
//
//				MainFinanceWindow.getViewManager().closeView(
//						AddEditVatCodeView.this.getAction(), null);
//			}
//		});
//
//		Button button3 = new Button();
//		button3.setTitle(FinanceApplication.constants().help());
//		button3.addClickHandler(new ClickHandler() {
//
//			public void onClick(ClickEvent event) {
//				// if (dialogButtonsHandler != null)
//				// thirdButtonClick();
//			}
//		});
//
//		buttonsLayout.add(button1);
//		buttonsLayout.add(button2);
//		buttonsLayout.add(button3);
//
//		VerticalPanel bodyLayout = new VerticalPanel();
//		bodyLayout.setSize("100%", "100%");
//		bodyLayout.add(infolabel);
//		bodyLayout.add(vatCodeForm);
//		bodyLayout.add(vatRates);
//		bodyLayout.add(gridView);
//		bodyLayout.add(buttonsLayout);
//
//		add(bodyLayout);
//
//	}
//
//	private void initListGrid() {
//		gridView = new VatCodeGrid(false);
//		gridView.init();
//		// gridView.setWidth100();
//		// gridView.addRecordAddHandler(new RecordAddhandler() {
//		//
//		// public boolean onRecordAdd(ListGridRecord record) {
//		// if (record != null
//		// && record.getAttributeAsDate(ATTR_AS_OF) == null) {
//		// return false;
//		// }
//		// return true;
//		// }
//		// });
//		// gridView.addRecordAddCompletedHandler(new RecordAddCompleteHandler()
//		// {
//		//
//		// @Override
//		// public void onRecordAddComplete(ListGridRecord record) {
//		//
//		// record.setAttribute(ATTR_AS_OF, new Date());
//		//
//		// }
//		// });
//		// 
//		// gridView.setShowMenu(false);
//		// gridView.setHeight("20%");
//		// gridView.setGroupTitle("VAT Rate");
//
//		// rateField.setEditorType(new TextItem());
//		// rateField.setAlign(Alignment.CENTER);
//
//		// asOfField.setEditorType(new DateItem());
//		// asOfField.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
//		// asOfField.setAlign(Alignment.CENTER);
//
//		// gridView.setDefaultValue(ATTR_RATE, "0%");
//		// 
//		// gridView.addEditCompleteHandler(new
//		// EditCompleteHandler<ClientTaxCode>() {
//		// @Override
//		// public void OnEditComplete(ClientTaxCode core, Object value, int col)
//		// {
//		// isRateInValid = false;
//		// String vaString = (String) value;
//		// if (col == 0)
//		// validateRateField(vaString);
//		// else if (col == 1)
//		// validateDateField(vaString);
//		//
//		// }
//		// });
//
//		// if (selectedTaxCode == null) {
//		// ListGridRecord record = new ListGridRecord();
//		// record.setAttribute(ATTR_RATE, "0.0%");
//		// record.setAttribute(ATTR_AS_OF, JSOHelper.toDate(company
//		// .getPreferences().getStartOfFiscalYear()));
//		// gridView.addRecords(record);
//		// }
//
//	}
//
//	// validating the "as of" field for each record
//	
//	protected void validateDateField(String selectedRecord) {
//		String invalidRatealert = " ";
//		// if (isRateInValid) {
//		// invalidRatealert = "VAT Rate should be in the range 0% to 100%!!";
//		// Accounter.showError(invalidRatealert);
//		// selectedRecord.setAttribute(ATTR_RATE, "0%");
//		// }
//		try {
//			Date selectedRecordDate = UIUtils.stringToDate(selectedRecord);
//			// Date selectedRecordDate = selectedRecord
//			// .getAttributeAsDate(ATTR_AS_OF);
//
//			if (selectedRecordDate != null) {
//				List<ClientTaxRates> records = gridView.getRecords();
//				if (records.size() > 1) {
//
//					// boolean isDeleted = false;
//					// for (int i = 0; i < records.size(); i++) {
//					for (ClientTaxRates taxRates : records) {
//						if (!selectedRecord.equals(taxRates.getAsOf())) {
//							// Date date = records[i]
//							// .getAttributeAsDate(ATTR_AS_OF);
//							Date date = new Date(taxRates.getAsOf());
//							if ((date.getYear() == selectedRecordDate.getYear())
//									&& date.getMonth() == selectedRecordDate
//											.getMonth()
//									&& date.getDate() == selectedRecordDate
//											.getDate()) {
//								Accounter.showError(invalidRatealert
//										+ FinanceApplication
//												.constants()
//												.dateShouldBeUnique());
//								// gridView.deleteRecord(selectedRecord);
//								// isDeleted = true;
//							}
//
//							//								
//							// if (date.equals(selectedRecordDate)) {
//							// SC.say(invalidRatealert
//							// + " Date should be unique");
//							// gridView.deleteRecord(selectedRecord);
//							// isDeleted = true;
//							//						
//							// }
//						}
//						// if (isDeleted)
//						// break;
//					}
//				}
//			} else {
//				Accounter.showError(invalidRatealert
//						+ FinanceApplication.constants()
//								.dateShouldNotBeNull());
//
//			}
//		} catch (Exception e) {
//			Accounter.showError(invalidRatealert
//					+ FinanceApplication.constants().invalidDate());
//			// gridView.deleteRecord(selectedRecord);
//		}
//	}
//
//	protected void validateRateField(String selectedvalue) {
//		// String selectedRecordRate = selectedRecord.getAttribute(ATTR_RATE);
//		String selectedRecordRate = selectedvalue;
//		String invalidRatealert = " ";
//		if (selectedRecordRate.endsWith("%"))
//			selectedRecordRate = selectedRecordRate.substring(0,
//					selectedRecordRate.length() - 1);
//		try {
//			Double rate = UIUtils.toDbl(selectedRecordRate);
//			if (rate < 0.00 || rate > 100.00) {
//				invalidRatealert = FinanceApplication.constants()
//						.VATRateShouldRange0to100();
//				Accounter.showError(invalidRatealert);
//			}
//			// isRateInValid = true;
//			// } else
//			// selectedRecord.setAttribute(ATTR_RATE, rate + "%");
//		} catch (Exception e) {
//			isRateInValid = true;
//		}
//	}
//
//	public boolean validForm() {
//
//		boolean isGridEmpty = false;
//		if (gridView.getRecords().size() == 0) {
//			Accounter.showError(FinanceApplication.constants()
//					.provideAtleastOneVATRate());
//			isGridEmpty = true;
//		}
//		return vatCodeForm.validate() && !isGridEmpty;
//
//	}
//
//	public ClientVATAgency getSelectedVatAgency() {
//		return selectedVatAgency;
//	}
//
//	// public boolean checkLastRecord() {
//	// ListGridRecord records[] = gridView.getRecords();
//	// if (records[records.length - 1].getAttributeAsDate(ATTR_AS_OF) == null) {
//	// Accounter.showError("Date should not be null");
//	// return false;
//	//
//	// }
//	// return true;
//	// }
//
//	@Override
//	public void init() {
//		super.init();
//		initCompany();
//		createControls();
//		setSize("100%", "100%");
//		// setOverflow(Overflow.AUTO);
//
//	}
//
//	
//	@Override
//	public void initData() {
//		this.selectedVatCode = (ClientTaxCode) this.getData();
//		if (selectedVatCode != null) {
//			vatCodeText.setValue(selectedVatCode.getName());
//			descriptionText.setValue(selectedVatCode.getDescription());
//			Set<ClientTaxRates> clientVatRates = selectedVatCode.getTaxRates();
//			// int i = 0;
//			// ListGridRecord records[] = new
//			// ListGridRecord[clientVatRates.size()];
//			//
//			// for (ClientTaxRates clientVatRates2 : clientVatRates) {
//			// records[i] = new ListGridRecord();
//			// records[i].setAttribute(ATTR_RATE, clientVatRates2.getRate());
//			// records[i].setAttribute(ATTR_AS_OF, JSOHelper
//			// .toDate(clientVatRates2.getAsOf()));
//			// i++;
//			// }
//
//			List<ClientTaxRates> list = (List<ClientTaxRates>) clientVatRates;
//			gridView.setRecords(list);
//		}
//
//		initVatAgencyCombo();
//	}
//
//	// creates a new tax code OR edits an existing tax code.
//	protected void createOrEditVatCode() {
//		final ClientTaxCode vatCode = getVatCode();
//		if (takenVatCode == null) {
//			if (Utility.isObjectExist(FinanceApplication.getCompany()
//					.getAccounts(), vatCode.getName())) {
//				Accounter.showError(AccounterErrorType.ALREADYEXIST);
//			}
//			ViewManager.getInstance().createObject(vatCode, this);
//		} else
//			ViewManager.getInstance().alterObject(vatCode, this);
//	}
//
//	private ClientTaxCode getVatCode() {
//
//		ClientTaxCode vatCode;
//		takenVatCode = (ClientTaxCode) this.getData();
//		if (takenVatCode != null)
//			vatCode = takenVatCode;
//		else
//			vatCode = new ClientTaxCode();
//
//		// Setting Tax Code
//		vatCode.setName(UIUtils.toStr(vatCodeText.getValue()));
//
//		// Setting description
//		Object obj = descriptionText.getValue();
//		selectedVatAgency = vatAgencyCombo.getSelectedValue();
//		if (obj != null)
//			vatCode.setDescription(UIUtils.toStr(obj));
//
//		// Setting Tax Agency
//		vatCode.setTaxAgency(getSelectedVatAgency().getID());
//
//		// Setting status check
//		boolean isActive = (Boolean) statusCheck.getValue();
//		vatCode.setIsActive(isActive);
//
//		// Setting Tax Rates
//		Set<ClientTaxRates> allTaxRates = new HashSet<ClientTaxRates>();
//		List<ClientTaxRates> records = gridView.getRecords();
//
//		for (ClientTaxRates rates : records) {
//			ClientTaxRates taxRate = new ClientTaxRates();
//			String rate = UIUtils.dateToString(rates.getRate());
//			;
//
//			taxRate
//					.setRate(UIUtils
//							.toDbl(rate.substring(0, rate.length() - 1)));
//			taxRate.setAsOf(rates.getAsOf());
//			allTaxRates.add(taxRate);
//
//		}
//
//		vatCode.setTaxRates(allTaxRates);
//
//		return vatCode;
//	}
//
//	/**
//	 * call this method to set focus in View
//	 */
//	@Override
//	public void setFocus() {
//		this.vatCodeText.setFocus();
//	}
//
//	@Override
//	public void deleteFailed(AccounterException caught) {
//
//	}
//
//	@Override
//	public void deleteSuccess(Boolean result) {
//
//	}
//
//	@Override
//	public void saveSuccess(IAccounterCore result) {
//		MainFinanceWindow.getViewManager().closeView(
//				AddEditVatCodeView.this.getAction(), result);
//		super.saveSuccess(result);
//	}
//
//	@Override
//	public void saveFailed(AccounterException exception) {
//		Accounter.showError(FinanceApplication.constants()
//				.duplicationVATCodeAreNotAllowed());
//		super.saveFailed(exception);
//	}
//
//	@Override
//	public void fitToSize(int height, int width) {
//
//	}
//
//	@Override
//	public void processupdateView(IAccounterCore core, int command) {
//
//		switch (command) {
//
//		case AccounterCommand.CREATION_SUCCESS:
//
//			if (core.getObjectType() == AccounterCoreType.VATAGENCY)
//				this.vatAgencyCombo.addComboItem((ClientVATAgency) core);
//
//			break;
//		case AccounterCommand.DELETION_SUCCESS:
//
//			if (core.getObjectType() == AccounterCoreType.VATAGENCY)
//				this.vatAgencyCombo.removeComboItem((ClientVATAgency) core);
//			break;
//
//		case AccounterCommand.UPDATION_SUCCESS:
//			break;
//
//		}
//	}
//
// }
