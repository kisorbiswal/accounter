package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterClientConstants;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddressDialog;
import com.vimukti.accounter.web.client.ui.Header;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.LinkItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CompanyPreferencesView extends BaseView<ClientCompanyPreferences> {
	private DecoratedTabPanel tabSet;
	private VerticalPanel companyLayOut, sysAccountsLayOut, generalLayOut;
	private ClientCompany company;
	private List<ClientAccount> accounts = new ArrayList<ClientAccount>();
	private HorizontalPanel hlLayout;
	private Button helpButt;
	private Button ok;
	private Button cancel;

	// private IAccounterGETServiceAsync getService;

	// private IAccounterCRUDServiceAsync crudService;
	private CheckboxItem useAccountscheckbox, useCustomertID, useVendorId,
			allowDocumentNos, doupaySalesChecBox, playsounds;
	private DateItem dateItem;
	private TextItem vatRegNumber;
	private RadioGroupItem ageingFromTransactionDateORDueDate;
	private RadioGroupItem whowillrunAccTransfer, paysalesTaxgroupItem;
	private Button buttonItem, taxgroupBtn, clearlogBtn,
			mangeServiceMappingsBtn;
	private OtherAccountsCombo openinBalcombo, accountsreceivablecombo,
			accountsPayablecombo, salesTaxPayablecombo, cashDiscountGivencombo,
			cashDiscountTakencombo, payrollLiabilitycombo,
			undepositedFoundscombo, bankChargecombo, retainedEarningsCombo,
			pendingItemrecicptsCombo, jobresellAccountCombo,
			writeOffAccountCombo, defaultCashCombo;
	// --Previously 'logspaceTxt' was FloatItem
	private FormItem logspaceTxt;
	private SelectItem logContentSelect, defautFileAsSettings;
	private boolean isCancel = false;
	public static final int TYPE_AGEING_FROM_DUEDATE = 2;
	public static final int TYPE_AGEING_FROM_TRANSACTIONDATE = 1;

	TextItem websiteText, taxIDText, companyNameText, trandigNameText,
			sortCodeText;
	EmailField emailText;
	IntegerField phoneText, faxText, registrationNumberText, bankAccountText;

	DynamicForm companyDetailsForm, phoneAndFaxForm, taxIDForm,
			RegistrationNumberForm;

	// private TextAreaItem address;

	// private LinkedHashMap<String, ClientAddress> addresses = new
	// LinkedHashMap<String, ClientAddress>();

	protected String str;

	// private String string;

	private TextAreaItem textareaItem, textareaItem2;

	private LinkedHashMap<Integer, ClientAddress> allAddresses;

	public CompanyPreferencesView() {
		// this.validationCount = 2;
		this.company = getCompany();
		// this.getService = Accounter.createGETService();
		// this.crudService = Accounter.createCRUDService();

	}

	protected void initCompany() {
		ClientCompanyPreferences companyPreferences = company.getPreferences();
		// Call Back to get ClientCompanyPreferences Object of Company
		if (companyPreferences != null) {

			dateItem.setValue(new ClientFinanceDate(companyPreferences
					.getPreventPostingBeforeDate()));
			// if (companyPreferences.getIsMyAccountantWillrunPayroll() !=
			// false)
			// whowillrunAccTransfer.setValue(companyPreferences
			// .getIsMyAccountantWillrunPayroll());

			useAccountscheckbox.setValue(companyPreferences
					.getUseAccountNumbers());
			useCustomertID.setValue(companyPreferences.getUseCustomerId());
			useVendorId.setValue(companyPreferences.getUseVendorId());
			if (companyPreferences.getAgeingFromTransactionDateORDueDate() == CompanyPreferencesView.TYPE_AGEING_FROM_DUEDATE)
				ageingFromTransactionDateORDueDate.setValue(Accounter
						.constants().ageingforduedate());
			else
				ageingFromTransactionDateORDueDate.setValue(Accounter
						.constants().ageingfortransactiondate());

			allowDocumentNos.setValue(companyPreferences
					.getAllowDuplicateDocumentNumbers());

			// doupaySalesChecBox.setValue(companyPreferences
			// .getDoYouPaySalesTax());

			// if ((Double) companyPreferences.getLogSpaceUsed() != null)
			// logspaceTxt.setValue(companyPreferences.getLogSpaceUsed());

			// if (companyPreferences.getIsAccuralBasis())
			// paysalesTaxgroupItem.setValue(companyPreferences
			// .getIsAccuralBasis());

		}
		this.company = getCompany();
		if (this.company != null) {
			companyNameText.setValue(company.getName());
			trandigNameText.setValue(company.getTradingName());
			this.taxIDText.setValue(company.getTaxId());
			this.faxText.setValue(company.getFax());
			this.phoneText.setValue(company.getPhone());
			this.websiteText.setValue(company.getWebSite());
			this.emailText.setValue(company.getCompanyEmail());
			this.bankAccountText.setValue(company.getBankAccountNo());
			this.sortCodeText.setValue(company.getSortCode());
			allAddresses.put(ClientAddress.TYPE_COMPANY, company
					.getTradingAddress());
			setAddressToTextItem(textareaItem, company.getTradingAddress());

			allAddresses.put(ClientAddress.TYPE_COMPANY_REGISTRATION, company
					.getRegisteredAddress());
			setAddressToTextItem(textareaItem2, company.getRegisteredAddress());
			registrationNumberText.setValue(company.getRegistrationNumber());

			doupaySalesChecBox.setValue(getCompany().getPreferences()
					.getDoYouPaySalesTax());

			if (doupaySalesChecBox.getValue() == Boolean.FALSE) {
				vatRegNumber.setDisabled(true);
			}
		}
	}

	public void setAddressToTextItem(TextAreaItem textItem,
			ClientAddress address) {
		String toToSet = new String();
		if (address.getAddress1() != null && !address.getAddress1().isEmpty()) {
			toToSet = address.getAddress1().toString() + "\n";
		}

		if (address.getStreet() != null && !address.getStreet().isEmpty()) {
			toToSet += address.getStreet().toString() + "\n";
		}

		if (address.getCity() != null && !address.getCity().isEmpty()) {
			toToSet += address.getCity().toString() + "\n";
		}

		if (address.getStateOrProvinence() != null
				&& !address.getStateOrProvinence().isEmpty()) {
			toToSet += address.getStateOrProvinence() + "\n";
		}
		if (address.getZipOrPostalCode() != null
				&& !address.getZipOrPostalCode().isEmpty()) {
			toToSet += address.getZipOrPostalCode() + "\n";
		}
		if (address.getCountryOrRegion() != null
				&& !address.getCountryOrRegion().isEmpty()) {
			toToSet += address.getCountryOrRegion();
		}
		textItem.setValue(toToSet);

	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
		// setOverflow(Overflow.AUTO);

	}

	@Override
	public void initData() {
		super.initData();
		getAccounts();
		initCompany();

	}

	private void createControls() {
		tabSet = new DecoratedTabPanel();
		tabSet.setWidth("100%");

		DeckPanel deckPanel = tabSet.getDeckPanel();
		// deckPanel.setSize("450px", "400px");
		tabSet.add(getCompanyInfo(), Accounter.constants().companyinfo());
		tabSet.add(getCompanyTab(), Accounter.constants().preferences());
		// tabSet.add(getSystemAccountsTab(),
		// Accounter.constants().systemAccounts());
		// tabSet.add(getGerneralTab(), Accounter.constants().general());
		tabSet.selectTab(0);

		hlLayout = new HorizontalPanel();
		hlLayout.setWidth("47%");
		hlLayout.setHeight("30px");
		hlLayout.setSpacing(5);
		// hlLayout.setAlign(Alignment.RIGHT);
		// hlLayout.setMembersMargin(20);
		// hlLayout.setAutoHeight();
		// hlLayout.setLayoutLeftMargin(5);
		// hlLayout.setLayoutRightMargin(5);

		helpButt = new Button(Accounter.constants().help());
		// helpButt.setHeight("30");
		// helpButt.setAutoFit(true);
		helpButt.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Accounter
						.showInformation(AccounterWarningType.NOT_YET_IMPLEMENTED);
			}

		});

		HorizontalPanel helpLayout = new HorizontalPanel();
		helpLayout.setWidth("50%");
		helpLayout.add(helpButt);
		HorizontalPanel okCancelayout = new HorizontalPanel();
		okCancelayout.setWidth("70%");
		// okCancelayout.setMembersMargin(10);
		// okCancelayout.setAlign(Alignment.RIGHT);

		ok = new Button(Accounter.constants().ok());
		// ok.setAutoFit(true);
		ok.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				savePreference();

			}
		});

		cancel = new Button(Accounter.constants().cancel());
		// cancel.setAutoFit(true);
		cancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isCancel = true;
				saveAndClose = true;
				saveSuccess(null);
			}
		});
		okCancelayout.add(ok);
		okCancelayout.setCellHorizontalAlignment(ok,
				HasHorizontalAlignment.ALIGN_RIGHT);
		okCancelayout.add(cancel);
		hlLayout.add(helpLayout);
		hlLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		hlLayout.add(okCancelayout);
		// hlLayout.setLayoutTopMargin(10);
		// hlLayout.setLayoutBottomMargin(10);
		// saveAndNewButton.setVisible(false);
		saveAndNewButton.removeFromParent();

		VerticalPanel mainLayout = new VerticalPanel();
		mainLayout.setSize("100%", "100%");
		mainLayout.add(tabSet);
		// mainLayout.add(hlLayout);

		this.add(mainLayout);
		// this.getParent().removeStyleName("main-class-pannel");
	}

	private HorizontalPanel getCompanyInfo() {
		VerticalPanel mainVLay = new VerticalPanel();
		// setTitle(Accounter.constants().companyInformation());
		LinkItem emptylabel = new LinkItem();
		emptylabel.setLinkTitle("");
		emptylabel.setShowTitle(false);

		companyNameText = new TextItem(Accounter.constants().registeredName());
		companyNameText.setToolTip(Accounter.messages().giveOfYour(
				Accounter.constants().registeredName().toLowerCase(),
				Accounter.constants().company()));
		companyNameText.setHelpInformation(true);
		companyNameText.setRequired(true);
		companyNameText.setWidth(100);
		// string = "[a-zA-Z0-9 /s]";

		textareaItem = new TextAreaItem(Accounter.constants()
				.registeredAddress());
		textareaItem.setToolTip(Accounter.messages().giveOfYour(
				Accounter.constants().registeredAddress().toLowerCase(),
				Accounter.constants().company()));
		textareaItem.setHelpInformation(true);
		textareaItem.setRequired(false);
		textareaItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new AddressDialog("title", "description", textareaItem,
						"company", allAddresses);
			}
		});

		trandigNameText = new TextItem(Accounter.constants().tradingName());
		trandigNameText.setToolTip(Accounter.messages().giveOfYour(
				Accounter.constants().tradingName().toLowerCase(),
				Accounter.constants().company()));
		trandigNameText.setHelpInformation(true);
		trandigNameText.setRequired(true);
		trandigNameText.setWidth(100);

		textareaItem2 = new TextAreaItem(Accounter.constants().tradingAddress());
		textareaItem2.setToolTip(Accounter.messages().giveOfYour(
				Accounter.constants().tradingAddress().toLowerCase(),
				Accounter.constants().company()));
		textareaItem2.setHelpInformation(true);

		allAddresses = new LinkedHashMap<Integer, ClientAddress>();
		textareaItem2.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new AddressDialog("title", "description", textareaItem2,
						"companyregistration", allAddresses);
			}
		});
		textareaItem2.setRequired(false);

		TextItem emptyText = new TextItem();
		emptyText.setVisible(false);

		companyDetailsForm = UIUtils.form(Accounter.constants()
				.companyDetails());

		VerticalPanel mainVLay2 = new VerticalPanel();

		phoneText = new IntegerField(this, Accounter.constants()
				.businessPhone());
		phoneText.setHelpInformation(true);
		phoneText.setToolTip(Accounter.messages().giveOfYour(
				Accounter.constants().phoneNumber().toLowerCase(),
				Accounter.constants().company()));

		faxText = new IntegerField(this, Accounter.constants().businessFax());
		faxText.setHelpInformation(true);
		faxText.setToolTip(Accounter.messages().giveOfYour(
				Accounter.constants().businessFax().toLowerCase(),
				Accounter.constants().company()));

		emailText = new EmailField(Accounter.constants().email());
		emailText.setHelpInformation(true);
		emailText.setToolTip(Accounter.messages().giveOfYour(
				Accounter.constants().email().toLowerCase(),
				Accounter.constants().company()));

		websiteText = new TextItem(Accounter.constants().webPageAddress());
		websiteText.setHelpInformation(true);
		websiteText.setToolTip(Accounter.messages().giveOfYour(
				Accounter.constants().webPageAddress().toLowerCase(),
				Accounter.constants().company()));

		registrationNumberText = new IntegerField(this, Accounter.constants()
				.companyRegistrationNumber());
		registrationNumberText.setHelpInformation(true);
		registrationNumberText
				.setToolTip(Accounter.messages()
						.giveOfYour(
								Accounter.constants()
										.companyRegistrationNumber()
										.toLowerCase(),
								Accounter.constants().company()));

		taxIDText = new TextItem(Accounter.constants().federalTaxId());
		taxIDText.setHelpInformation(true);
		taxIDText.setToolTip(Accounter.messages().giveOfYour(
				Accounter.constants().federalTaxId().toLowerCase(),
				Accounter.constants().company()));

		bankAccountText = new IntegerField(this, Accounter.messages()
				.bankAccountNo(Global.get().Account()));
		bankAccountText.setToolTip(Accounter.messages().giveOfYour(
				Accounter.messages().bankAccountNo(Global.get().Account())
						.toLowerCase(), Accounter.constants().company()));

		// bankAccountText.setTitle();
		bankAccountText.setHelpInformation(true);

		sortCodeText = new TextItem();
		sortCodeText.setTitle(Accounter.constants().sortCode());
		sortCodeText.setHelpInformation(true);
		sortCodeText.setToolTip(Accounter.messages().giveOfYour(
				Accounter.constants().sortCode().toLowerCase(),
				Accounter.constants().company()));

		phoneAndFaxForm = UIUtils.form(Accounter.constants()
				.phoneAndFaxNumbers());

		emailText.setRequired(true);

		phoneAndFaxForm.setFields(phoneText, faxText, websiteText, emailText,
				registrationNumberText, taxIDText, bankAccountText,
				sortCodeText);

		phoneAndFaxForm.setNumCols(8);
		phoneAndFaxForm.getCellFormatter().setWidth(0, 0, "250px");

		// DynamicForm taxesForm = new DynamicForm();
		// // taxesForm.setWidth100();
		// // taxesForm.setHeight("*");
		// taxesForm.setIsGroup(true);
		// taxesForm.setGroupTitle(Accounter.constants().taxes());
		// taxesForm.setTitleOrientation(TitleOrientation.TOP);
		// taxesForm.setPadding(10);

		doupaySalesChecBox = new CheckboxItem();
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			doupaySalesChecBox.setTitle(Accounter.constants()
					.doYoupaySalesTaxes());

		} else {
			doupaySalesChecBox.setTitle(Accounter.constants()
					.areYouRegisteredForVAT());
		}
		vatRegNumber = new TextItem(
				getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK ? Accounter
						.constants().vatRegistrationNumber()
						: Accounter.constants().taxRegNo());
		vatRegNumber.setHelpInformation(true);
		vatRegNumber.setWidth(100);
		vatRegNumber.setDisabled(false);
		vatRegNumber.setToolTip(Accounter.messages().giveOfYour(
				vatRegNumber.getTitle().toLowerCase(),
				Accounter.constants().company()));

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			doupaySalesChecBox.addChangeHandler(new ChangeHandler() {

				private FocusWidget taxgroupBtn;

				public void onChange(ChangeEvent event) {
					if ((Boolean) ((CheckboxItem) event.getSource()).getValue())
						taxgroupBtn.setEnabled(false);
					else
						taxgroupBtn.setEnabled(true);
				}
			});
			doupaySalesChecBox
					.addChangeHandler(new ValueChangeHandler<Boolean>() {
						@Override
						public void onValueChange(
								ValueChangeEvent<Boolean> event) {
							vatRegNumber.setDisabled(!event.getValue());
							vatRegNumber.setValue(Accounter.getCompany()
									.getPreferences()
									.getVATregistrationNumber());
						}
					});
		} else {

			doupaySalesChecBox
					.addChangeHandler(new ValueChangeHandler<Boolean>() {

						@Override
						public void onValueChange(
								ValueChangeEvent<Boolean> event) {
							vatRegNumber.setDisabled(!event.getValue());
							vatRegNumber.setValue(Accounter.getCompany()
									.getPreferences()
									.getVATregistrationNumber());
						}
					});
		}
		vatRegNumber.setValue(getCompany().getPreferences()
				.getVATregistrationNumber());
		taxgroupBtn = new Button(Accounter.constants().taxgroups());
		// taxgroupBtn.setColSpan("*");
		taxgroupBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				// preferencesView.taxGroupButtonClick();
			}
		});
		paysalesTaxgroupItem = new RadioGroupItem();
		paysalesTaxgroupItem.setTitle(Accounter.constants()
				.onWhatbasisdoUpaySalesTaxes());
		// paysalesTaxgroupItem.setColSpan("*");
		// paysalesTaxgroupItem.setVertical(false);
		// paysalesTaxgroupItem
		// .setValue(company.getPreferences() != null ? company
		// .getPreferences().getIsAccuralBasis() ? "1" : "2" : "1");

		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map
				.put("1", Accounter.messages().accrualBasis(
						Global.get().customer()));
		map.put("2", Accounter.messages().cashBasis(Global.get().customer()));
		paysalesTaxgroupItem.setValueMap(map);

		// if(!FinanceApplication.getCompany().getpreferences().getDoYouPaySalesTax())
		// vatRegNumber.setDisabled(true);

		companyDetailsForm.setFields(companyNameText, textareaItem,
				trandigNameText, textareaItem2, doupaySalesChecBox,
				vatRegNumber);
		companyDetailsForm.getCellFormatter().addStyleName(1, 0,
				"memoFormAlign");
		companyDetailsForm.getCellFormatter().addStyleName(3, 0,
				"memoFormAlign");
		companyDetailsForm.setNumCols(7);
		companyDetailsForm.setCellSpacing(5);
		mainVLay.add(companyDetailsForm);

		// if (FinanceApplication.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US)
		// mainVLay.add(taxgroupBtn);

		mainVLay2.add(phoneAndFaxForm);
		mainVLay2.setWidth("100%");
		// mainVLay2.getElement().getStyle().setMarginLeft(100, Unit.PX);

		HorizontalPanel mainHLay = new HorizontalPanel();
		mainHLay.setWidth("100%");
		mainHLay.setSpacing(10);
		mainHLay.add(mainVLay);
		mainHLay.add(mainVLay2);
		mainHLay.setCellWidth(mainVLay, "50%");
		mainHLay.setCellWidth(mainVLay2, "50%");
		mainHLay.setCellHorizontalAlignment(mainVLay, ALIGN_LEFT);
		mainHLay.setCellHorizontalAlignment(mainVLay2, ALIGN_RIGHT);

		// addInputDialogHandler(new InputDialogHandler() {
		//
		// public void onCancelClick() {
		//
		// }
		//
		// public boolean onOkClick() {
		//
		// try {
		// if (CompanyInfoDialog.this.validate())
		// updatedCompany();
		// return true;
		// } catch (InvalidTransactionEntryException e) {
		// e.printStackTrace();
		// } catch (InvalidEntryException e) {
		// Accounter.showError(e.getMessage());
		// }
		// return false;
		// }
		//
		// });

		// okbtn.setText(FinanceApplication.constants().update());

		mainHLay.setWidth("100%");
		mainHLay.setHeight("250px");

		return mainHLay;

		// setBodyLayout(mainHLay);

	}

	@Override
	public void saveAndUpdateView() {
		// savePreference();
		updatedCompany();
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		// validate email
		// validate company details form

		// switch (this.validationCount) {
		// case 2:
		if (!emailText.validate()) {
			result.addError(emailText, Accounter.messages().pleaseEnter(
					emailText.getTitle()));
		}
		// return AccounterValidator.validateFormItem(false, emailText);
		// case 1:
		result.add(companyDetailsForm.validate());
		// return validateCompanyDetailsForm(companyDetailsForm);
		// default:
		// return true;
		// }
		return result;

	}

	// private boolean validateCompanyDetailsForm(DynamicForm
	// companyDetailsForm)
	// throws InvalidEntryException {
	// if (!companyDetailsForm.validate()) {
	// // throw new
	// // InvalidEntryException(AccounterErrorType.REQUIRED_FIELDS);
	// }
	// return true;
	// }

	protected void savePreference() {
		ClientCompanyPreferences companyPreferences = company.getPreferences();
		if (companyPreferences == null) {
			companyPreferences = new ClientCompanyPreferences();
		}
		companyPreferences
				.setAllowDuplicateDocumentNumbers(getBooleanValue(allowDocumentNos));

		// companyPreferences
		// .setDoYouPaySalesTax(getBooleanValue(doupaySalesChecBox));
		// //
		// companyPreferences.setLegalName(legalCompanyName.getValue().toString());
		// companyPreferences.setVATregistrationNumber(vatRegNumber.getValue()
		// .toString());

		// companyPreferences.setIsAccuralBasis(paysalesTaxgroupItem.getValue()
		// .equals("2"));
		// System.out.println("radio value:" +
		// whowillrunAccTransfer.getValue());

		companyPreferences
				.setUseAccountNumbers(getBooleanValue(useAccountscheckbox));
		companyPreferences.setUseCustomerId(getBooleanValue(useCustomertID));
		companyPreferences.setUseVendorId(getBooleanValue(useVendorId));
		companyPreferences
				.setAgeingFromTransactionDateORDueDate(ageingFromTransactionDateORDueDate
						.getValue().toString().equalsIgnoreCase(
								Accounter.constants().ageingforduedate()) ? CompanyPreferencesView.TYPE_AGEING_FROM_DUEDATE
						: CompanyPreferencesView.TYPE_AGEING_FROM_TRANSACTIONDATE);
		if (dateItem.getValue() != null) {
			companyPreferences.setPreventPostingBeforeDate(dateItem.getValue()
					.getDate());
			// companyPreferences
			// .setEndOfFiscalYear(dateItem.getValue().getTime());
		}

		// companyPreferences
		// .setIsMyAccountantWillrunPayroll(whowillrunAccTransfer
		// .getValue().equals("2"));
		// companyPreferences.setLogSpaceUsed(getDoubleValue(logspaceTxt));
		// company.setpreferences(companyPreferences);

		saveAndClose = true;
		update(companyPreferences);
	}

	public void update(final ClientCompanyPreferences preferences) {

		AccounterAsyncCallback<Boolean> transactionCallBack = new AccounterAsyncCallback<Boolean>() {

			public void onException(AccounterException caught) {
				saveFailed(caught);
			}

			public void onResultSuccess(Boolean result) {
				if (result != null) {
					getCompany().processUpdateOrCreateObject(preferences);
				} else {
					onFailure(null);
				}
			}

		};
		Accounter.createCRUDService().updateCompanyPreferences(preferences,
				transactionCallBack);

	}

	protected void updatedCompany() {
		ClientCompanyPreferences companyPreferences = company.getPreferences();
		if (companyPreferences == null) {
			companyPreferences = new ClientCompanyPreferences();
		}
		companyPreferences
				.setAllowDuplicateDocumentNumbers(getBooleanValue(allowDocumentNos));

		// companyPreferences
		// .setDoYouPaySalesTax(getBooleanValue(doupaySalesChecBox));
		// //
		// companyPreferences.setLegalName(legalCompanyName.getValue().toString());
		// companyPreferences.setVATregistrationNumber(vatRegNumber.getValue()
		// .toString());

		// companyPreferences.setIsAccuralBasis(paysalesTaxgroupItem.getValue()
		// .equals("2"));
		// System.out.println("radio value:" +
		// whowillrunAccTransfer.getValue());
		companyPreferences
				.setUseAccountNumbers(getBooleanValue(useAccountscheckbox));
		companyPreferences.setUseCustomerId(getBooleanValue(useCustomertID));
		companyPreferences.setUseVendorId(getBooleanValue(useVendorId));
		companyPreferences
				.setAgeingFromTransactionDateORDueDate(ageingFromTransactionDateORDueDate
						.getValue().toString().equalsIgnoreCase(
								Accounter.constants().ageingforduedate()) ? CompanyPreferencesView.TYPE_AGEING_FROM_DUEDATE
						: CompanyPreferencesView.TYPE_AGEING_FROM_TRANSACTIONDATE);
		if (dateItem.getValue() != null) {
			companyPreferences.setPreventPostingBeforeDate(dateItem.getValue()
					.getDate());
			companyPreferences.setStartOfFiscalYear(dateItem.getValue()
					.getDate());
			// companyPreferences
			// .setEndOfFiscalYear(dateItem.getValue().getTime());
		}

		// companyPreferences
		// .setIsMyAccountantWillrunPayroll(whowillrunAccTransfer
		// .getValue().equals("2"));
		// companyPreferences.setLogSpaceUsed(getDoubleValue(logspaceTxt));
		// company.setpreferences(companyPreferences);

		saveAndClose = true;
		// ViewManager.getInstance().updateCompanyPreferences(companyPreferences,
		// this);

		/*
		 * Here we are creating a new ClientCompany Object, to avoid sending all
		 * the lists to server. Only the necessary fields which are required to
		 * be updated are only sent to server.
		 */
		ClientCompany clientCompany = new ClientCompany();
		clientCompany.id = company.id;
		clientCompany.setName(getStringValue(companyNameText));
		clientCompany.setTradingName(getStringValue(this.trandigNameText));
		clientCompany.setPhone(getStringValue(phoneText));
		clientCompany.setCompanyEmail(getStringValue(emailText));
		clientCompany.setTaxId(getStringValue(taxIDText));
		clientCompany.setFax(getStringValue(faxText));
		clientCompany.setWebSite(getStringValue(websiteText));
		clientCompany.setBankAccountNo(getStringValue(bankAccountText));
		clientCompany.setSortCode(getStringValue(sortCodeText));

		companyPreferences
				.setDoYouPaySalesTax(getBooleanValue(doupaySalesChecBox));
		companyPreferences.setVATregistrationNumber(vatRegNumber.getValue()
				.toString());

		clientCompany.setPreferences(companyPreferences);

		if (!allAddresses.isEmpty()) {
			clientCompany.setTradingAddress(allAddresses
					.get(ClientAddress.TYPE_COMPANY));
			clientCompany.setRegisteredAddress(allAddresses
					.get(ClientAddress.TYPE_COMPANY_REGISTRATION));
		} else {
			clientCompany.setTradingAddress(getCompany().getTradingAddress());
			clientCompany.setRegisteredAddress(getCompany()
					.getRegisteredAddress());
		}

		clientCompany
				.setRegistrationNumber(getStringValue(registrationNumberText));

		Accounter.updateCompany(this, clientCompany);

	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null || isCancel == true) {
			super.saveSuccess(result);
			Header.companyName.setText(this.companyNameText.getValue()
					.toString());
			ActionFactory.getCompanyHomeAction().run(null, false);

		} else {
			saveFailed(new AccounterException(Accounter.constants().failed()));
		}

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		// BaseView.errordata.setHTML(FinanceApplication.constants()
		// .failedToUpdate());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		addError(this, Accounter.constants().failedToUpdate());
	}

	private Double getDoubleValue(FormItem item) {
		return Double.parseDouble(item.getValue() != null
				&& !item.getValue().equals("") ? item.getValue().toString()
				.replace("" + UIUtils.getCurrencySymbol() + "", "") : "0.0");
	}

	public String getStringValue(FormItem item) {
		return item.getValue() != null ? item.getValue().toString() : "";

	}

	public boolean getBooleanValue(FormItem item) {
		return item.getValue() != null ? (Boolean) item.getValue() : false;

	}

	private void getAccounts() {
		// accounts = FinanceApplication.getCompany().getActiveAccounts();
		// initAllCombos(accounts);
	}

	public VerticalPanel getCompanyTab() {

		companyLayOut = new VerticalPanel();
		/**
		 * Numbers and IDs Group
		 */
		DynamicForm numbersIdsvForm = UIUtils.form(Accounter.constants()
				.getNumbersandIdsTitle());

		useAccountscheckbox = new CheckboxItem();
		useAccountscheckbox.setTitle(Accounter.messages().useAccountNos(
				Global.get().account()));

		useCustomertID = new CheckboxItem();
		useCustomertID.setTitle(Accounter.messages().useCustomerId(
				Global.get().customer()));

		useVendorId = new CheckboxItem();
		useVendorId.setTitle(Accounter.messages().useVendorId(
				Global.get().vendor()));
		ageingFromTransactionDateORDueDate = new RadioGroupItem(Accounter
				.constants().ageingDetails());

		ageingFromTransactionDateORDueDate.setValues(getClickHandler(),
				Accounter.constants().ageingforduedate(), Accounter.constants()
						.ageingfortransactiondate());
		// ageingFromTransactionDateORDueDate.setDefaultValue(FinanceApplication
		// .constants().ageingfortransactiondate());

		allowDocumentNos = new CheckboxItem();
		allowDocumentNos.setTitle(Accounter.constants().allowDocNos());

		numbersIdsvForm.setItems(useAccountscheckbox, useCustomertID,
				useVendorId, ageingFromTransactionDateORDueDate);
		numbersIdsvForm.getCellFormatter().setWidth(0, 0, "225px");
		numbersIdsvForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		// numbersIdsvForm.setCellSpacing(10);
		numbersIdsvForm.setWidth("100%");
		/**
		 * Fiscal YearGroup
		 * */
		DynamicForm fiscalYrForm = UIUtils.form(Accounter.constants()
				.fiscalYear());
		// fiscalYrForm.setPadding(10);

		dateItem = new DateItem();
		dateItem.setHelpInformation(true);
		dateItem.setTitle(Accounter.constants().preventPostBefore());
		// dateItem.setUseTextField(true);

		fiscalYrForm.setItems(dateItem);
		fiscalYrForm.getCellFormatter().setWidth(0, 0, "225px");

		/**
		 * Account Transfer Group
		 */
		DynamicForm accountantTransferForm = UIUtils.form((Accounter
				.constants().accountantTransfer()));
		// accountantTransferForm.setWidth(100);
		// accountantTransferForm.setTitleOrientation(TitleOrientation.TOP);
		// accountantTransferForm.setPadding(10);

		whowillrunAccTransfer = new RadioGroupItem();
		// whowillrunAccTransfer.setTitle(Accounter.constants()
		// .whowillrunwhenaccoutantTransfer());
		// whowillrunAccTransfer.setColSpan("*");
		whowillrunAccTransfer.setVertical(false);
		LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();
		hashMap.put("1", Accounter.constants().myAccountantwillrun());
		hashMap.put("2", Accounter.constants().iwouldliketoberunPayroll());
		whowillrunAccTransfer.setValueMap(hashMap);
		Boolean value = company.getPreferences() != null ? company
				.getPreferences().getIsMyAccountantWillrunPayroll() : false;
		if (value == null)
			value = Boolean.TRUE;
		// whowillrunAccTransfer.setDefaultValue(value ? "1" : "2");

		accountantTransferForm.setItems(whowillrunAccTransfer);

		/**
		 * Taxes Group
		 */

		DynamicForm taxesForm = new DynamicForm();
		// taxesForm.setWidth100();
		// taxesForm.setHeight("*");
		taxesForm.setIsGroup(true);
		taxesForm.setGroupTitle(Accounter.constants().taxes());
		// taxesForm.setTitleOrientation(TitleOrientation.TOP);
		// taxesForm.setPadding(10);

		// doupaySalesChecBox = new CheckboxItem();
		// // doupaySalesChecBox.setColSpan("2");
		// doupaySalesChecBox.setAttribute(FinanceApplication.constants()
		// .vertical(), false);
		// if (FinanceApplication.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US) {
		// doupaySalesChecBox.setTitle(Accounter.constants().doYoupaySalesTaxes());
		// } else
		// doupaySalesChecBox
		// .setTitle(Accounter.constants().areYouRegisteredForVAT());
		//
		// vatRegNumber = new TextItem(Accounter.constants().vatRegNo());
		// vatRegNumber.setHelpInformation(true);
		// vatRegNumber.setDisabled(false);
		//
		// if (FinanceApplication.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US) {
		// doupaySalesChecBox.addChangeHandler(new ChangeHandler() {
		//
		// public void onChange(ChangeEvent event) {
		// if ((Boolean) ((CheckboxItem) event.getSource()).getValue())
		// taxgroupBtn.setEnabled(false);
		// else
		// taxgroupBtn.setEnabled(true);
		// }
		// });
		// } else {
		//
		// doupaySalesChecBox
		// .addChangeHandler(new ValueChangeHandler<Boolean>() {
		//
		// @Override
		// public void onValueChange(
		// ValueChangeEvent<Boolean> event) {
		// vatRegNumber.setDisabled(!event.getValue());
		// vatRegNumber.setValue(FinanceApplication
		// .getCompany().getpreferences()
		// .getVATregistrationNumber());
		// }
		// });
		// }
		// vatRegNumber.setValue(FinanceApplication.getCompany().getpreferences()
		// .getVATregistrationNumber());
		// taxgroupBtn = new Button(Accounter.constants().taxgroups());
		// // taxgroupBtn.setColSpan("*");
		// taxgroupBtn.addClickHandler(new ClickHandler() {
		//
		// public void onClick(ClickEvent event) {
		// taxGroupButtonClick();
		// }
		// });
		// paysalesTaxgroupItem = new RadioGroupItem();
		// paysalesTaxgroupItem.setTitle(Accounter.constants()
		// .onWhatbasisdoUpaySalesTaxes());
		// // paysalesTaxgroupItem.setColSpan("*");
		// // paysalesTaxgroupItem.setVertical(false);
		// // paysalesTaxgroupItem
		// // .setValue(company.getPreferences() != null ? company
		// // .getPreferences().getIsAccuralBasis() ? "1" : "2" : "1");
		//
		// LinkedHashMap<String, String> map = new LinkedHashMap<String,
		// String>();
		// map.put("1", Accounter.constants().accrualBasis());
		// map.put("2", Accounter.constants().cashBasis());
		// paysalesTaxgroupItem.setValueMap(map);
		//
		// taxesForm.setItems(doupaySalesChecBox, vatRegNumber,
		// paysalesTaxgroupItem);
		// taxesForm.setWidth("100%");
		// if(!FinanceApplication.getCompany().getpreferences().getDoYouPaySalesTax())
		// vatRegNumber.setDisabled(true);

		/**
		 * add all forms to company LayOut
		 */
		companyLayOut.add(numbersIdsvForm);
		companyLayOut.add(fiscalYrForm);
		companyLayOut.add(accountantTransferForm);
		// companyLayOut.add(taxesForm);
		// if (FinanceApplication.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US)
		// companyLayOut.add(taxgroupBtn);

		VerticalPanel tab = new VerticalPanel();
		tab.setWidth("100%");
		tab.setHeight("280px");
		tab.add(companyLayOut);
		return tab;
	}

	private ClickHandler getClickHandler() {

		ClickHandler handler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String ageing = ageingFromTransactionDateORDueDate.getValue()
						.toString();
				if (ageing.equalsIgnoreCase(Accounter.constants()
						.ageingforduedate())) {
					ageingFromTransactionDateORDueDate
							.setValue(String
									.valueOf(CompanyPreferencesView.TYPE_AGEING_FROM_DUEDATE));
				} else

					ageingFromTransactionDateORDueDate
							.setValue(String
									.valueOf(CompanyPreferencesView.TYPE_AGEING_FROM_TRANSACTIONDATE));

			}
		};
		return handler;
	}

	protected void taxGroupButtonClick() {
		new ManageSalesTaxGroupsAction("").run(null, false);

	}

	public VerticalPanel getSystemAccountsTab() {
		/**
		 * System Account Group
		 */
		sysAccountsLayOut = new VerticalPanel();

		DynamicForm systemAccountForm = new DynamicForm();
		// systemAccountForm.setWidth100();
		// systemAccountForm.setHeight100();
		systemAccountForm.setIsGroup(true);
		systemAccountForm.setGroupTitle(Accounter.constants().systemAccounts());
		// systemAccountForm.setPadding(30);

		openinBalcombo = new OtherAccountsCombo(Accounter.constants()
				.openingBalances());

		openinBalcombo.initCombo(accounts);

		openinBalcombo.setWidth(150);
		// openinBalcombo.setWrapTitle(false);
		accountsreceivablecombo = new OtherAccountsCombo(Accounter.constants()
				.accountsReceivable());
		accountsreceivablecombo.initCombo(accounts);

		accountsreceivablecombo.setWidth(150);
		// accountsreceivablecombo.setWrapTitle(false);
		accountsPayablecombo = new OtherAccountsCombo(Accounter.messages()
				.accountsPayable(Global.get().Account()));
		accountsPayablecombo.initCombo(accounts);
		accountsPayablecombo.setWidth(150);
		// accountsPayablecombo.setWrapTitle(false);
		salesTaxPayablecombo = new OtherAccountsCombo(Accounter.constants()
				.salesTaxPayable());
		salesTaxPayablecombo.initCombo(accounts);

		salesTaxPayablecombo.setWidth(150);
		// salesTaxPayablecombo.setWrapTitle(false);
		cashDiscountGivencombo = new OtherAccountsCombo(Accounter.constants()
				.cashDiscountGiven());
		cashDiscountGivencombo.initCombo(accounts);

		cashDiscountGivencombo.setWidth(150);
		// cashDiscountGivencombo.setWrapTitle(false);
		cashDiscountTakencombo = new OtherAccountsCombo(Accounter.constants()
				.cashDiscountTaken());

		cashDiscountTakencombo.initCombo(accounts);
		cashDiscountTakencombo.setWidth(150);
		// cashDiscountTakencombo.setWrapTitle(false);

		payrollLiabilitycombo = new OtherAccountsCombo(Accounter.constants()
				.payrollLiability());
		payrollLiabilitycombo.initCombo(accounts);
		payrollLiabilitycombo.setWidth(150);
		undepositedFoundscombo = new OtherAccountsCombo(Accounter.constants()
				.undepositedFounds());

		undepositedFoundscombo.initCombo(accounts);
		undepositedFoundscombo.setWidth(150);
		bankChargecombo = new OtherAccountsCombo(Accounter.constants()
				.bankCharge());

		bankChargecombo.initCombo(accounts);
		bankChargecombo.setWidth(150);
		retainedEarningsCombo = new OtherAccountsCombo(Accounter.constants()
				.retainedEarnings());
		retainedEarningsCombo.initCombo(accounts);

		retainedEarningsCombo.setWidth(150);
		pendingItemrecicptsCombo = new OtherAccountsCombo(Accounter.constants()
				.pendingItemReceipts());
		pendingItemrecicptsCombo.initCombo(accounts);
		pendingItemrecicptsCombo.setWidth(150);
		// pendingItemrecicptsCombo.setWrapTitle(false);
		jobresellAccountCombo = new OtherAccountsCombo(Accounter.messages()
				.jobresellAccount(Global.get().Account()));
		jobresellAccountCombo.initCombo(accounts);

		jobresellAccountCombo.setWidth(150);
		writeOffAccountCombo = new OtherAccountsCombo(Accounter.messages()
				.writeOffAccount(Global.get().account()));
		writeOffAccountCombo.initCombo(accounts);
		writeOffAccountCombo.setWidth(150);
		defaultCashCombo = new OtherAccountsCombo(Accounter.messages()
				.defaultAccounts(Global.get().Account()));

		defaultCashCombo.initCombo(accounts);
		defaultCashCombo.setWidth(150);

		systemAccountForm.setItems(openinBalcombo, accountsreceivablecombo,
				accountsPayablecombo, salesTaxPayablecombo,
				cashDiscountGivencombo, cashDiscountTakencombo,
				payrollLiabilitycombo, undepositedFoundscombo, bankChargecombo,
				retainedEarningsCombo, pendingItemrecicptsCombo,
				jobresellAccountCombo, writeOffAccountCombo, defaultCashCombo);
		sysAccountsLayOut.add(systemAccountForm);

		VerticalPanel tab = new VerticalPanel();
		tab.add(sysAccountsLayOut);

		return tab;
	}

	public VerticalPanel getGerneralTab() {
		/**
		 * User Preferences Group
		 */
		DynamicForm userPreferenceForm = new DynamicForm();
		// userPreferenceForm.setWidth100();
		userPreferenceForm.setIsGroup(true);
		// userPreferenceForm.setTitleOrientation(TitleOrientation.TOP);
		userPreferenceForm
				.setGroupTitle(Accounter.constants().userPreference());
		playsounds = new CheckboxItem();
		// playsounds.setTitleOrientation(TitleOrientation.TOP);
		playsounds.setTitle(Accounter.constants().playsoundswithActions());

		buttonItem = new Button(Accounter.constants().restoreDefault());
		// buttonItem.setAutoFit(true);
		buttonItem.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				restoreDefault();
			}
		});
		// DynamicForm legalCompanyNameForm = new DynamicForm();
		// legalCompanyNameForm.setWidth("100%");
		//
		// legalCompanyName = new
		// TextItem(Accounter.constants().legalCompanyName());
		//
		// VerticalPanel legalCompanyPanel = new VerticalPanel();
		// legalCompanyNameForm.setFields(legalCompanyName);
		// legalCompanyPanel.add(legalCompanyNameForm);
		//
		// legalCompanyName.setValue(FinanceApplication.getCompany()
		// .getpreferences().getLegalName());

		VerticalPanel userPrefPanel = new VerticalPanel();
		userPrefPanel.add(userPreferenceForm);
		userPrefPanel.add(buttonItem);
		// userPreferenceForm.setPadding(10);
		/**
		 * filing record Name group
		 */
		DynamicForm filingRecordNameForm = new DynamicForm();
		// filingRecordNameForm.setWidth("100%");
		filingRecordNameForm.setIsGroup(true);
		// filingRecordNameForm.setAlign(Alignment.CENTER);
		filingRecordNameForm.setGroupTitle(Accounter.constants()
				.filingRecordsName());

		defautFileAsSettings = new SelectItem();
		// defautFileAsSettings.setWidth(200);
		// defautFileAsSettings.setHeight("20");
		defautFileAsSettings.setDefaultValue(Accounter.constants().company());
		defautFileAsSettings.setTitle(Accounter.constants()
				.defaultFileAsSettings());
		defautFileAsSettings.setValueMap(Accounter.constants().company(),
				Accounter.constants().firstLast(), Accounter.constants()
						.lastFirst());
		filingRecordNameForm.setItems(defautFileAsSettings);
		// filingRecordNameForm.setPadding(10);

		/**
		 * service Map Group
		 */
		DynamicForm serviceMapForm = new DynamicForm();
		// serviceMapForm.setWidth100();
		serviceMapForm.setIsGroup(true);
		serviceMapForm.setGroupTitle(Accounter.constants().serviceMapping());
		// serviceMapForm.setPadding(10);

		// --Previusly 'mangeServiceMappingsBtn' is ButtonItem
		mangeServiceMappingsBtn = new Button(Accounter.constants()
				.manageServiceMappings());
		mangeServiceMappingsBtn.setHeight("30px");

		/**
		 * supporting Logging
		 */
		DynamicForm supportLogging = new DynamicForm();
		// supportLogging.setWidth100();
		// supportLogging.setHeight("30%");
		supportLogging.setIsGroup(true);
		// supportLogging.setTitleOrientation(TitleOrientation.TOP);
		supportLogging.setGroupTitle(Accounter.constants().supportLogging());
		// supportLogging.setTop(20);
		// supportLogging.setPadding(20);

		// --Previusly 'logspaceTxt' is FloatItem
		logspaceTxt = new FormItem() {

			@Override
			public Widget getMainWidget() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		logspaceTxt.setTitle(Accounter.constants().logSpace());

		clearlogBtn = new Button();
		clearlogBtn.setTitle(Accounter.constants().clearLog());

		logContentSelect = new SelectItem();
		logContentSelect.setWidth(100);
		logContentSelect.setTitle(Accounter.constants().logContent());
		logContentSelect.setDefaultValue(Accounter.constants().doNotLog());
		logContentSelect.setValueMap(Accounter.constants().doNotLog(),
				Accounter.constants().criticalErrorsOnly(), Accounter
						.constants().allErrors(), Accounter.constants()
						.allErrorsAndMessages());

		supportLogging.setItems(logspaceTxt, logContentSelect);
		VerticalPanel suportLogPanel = new VerticalPanel();
		suportLogPanel.add(supportLogging);
		suportLogPanel.add(clearlogBtn);
		generalLayOut = new VerticalPanel();
		// generalLayOut.setSize("100%", "100%");
		generalLayOut.setWidth("100%");
		generalLayOut.setSpacing(10);
		generalLayOut.add(userPrefPanel);
		// generalLayOut.add(legalCompanyPanel);
		generalLayOut.add(filingRecordNameForm);
		generalLayOut.add(serviceMapForm);
		generalLayOut.add(mangeServiceMappingsBtn);
		generalLayOut.add(supportLogging);
		// generalLayOut.add(10);

		VerticalPanel generalTab = new VerticalPanel();
		generalTab.add(generalLayOut);
		return generalTab;
	}

	protected void restoreDefault() {

	}

	public void initAllCombos(List<ClientAccount> accountlist) {
		ClientAccount openingBalaccount = null;
		ClientAccount accountsReceivable = null;
		ClientAccount accountsPayable = null;
		ClientAccount salesTaxpayable = null;
		ClientAccount cashDiscountGiven = null;
		ClientAccount cashDiscountTaken = null;
		ClientAccount payRollLiability = null;
		ClientAccount undepositedFunds = null;
		ClientAccount bankCharge = null;
		ClientAccount retainedEarnings = null;
		ClientAccount pendingItemrecicpts = null;

		ClientAccount writeOffAccount = null;

		for (ClientAccount account : accountlist) {
			// System.out.println("Account Name " + account.getName());
			if (account.getName().equals(
					AccounterClientConstants.OPENING_BALANCE))
				openingBalaccount = account;
			else if (account.getName().equals(
					AccounterClientConstants.ACCOUNTS_RECEIVABLE))
				accountsReceivable = account;
			else if (account.getName().equals(
					AccounterClientConstants.ACCOUNTS_PAYABLE))
				accountsPayable = account;
			else if (account.getName().equals(
					AccounterClientConstants.SALES_TAX_PAYABLE))
				salesTaxpayable = account;
			else if (account.getName().equals(
					AccounterClientConstants.CASH_DISCOUNT_GIVEN))
				cashDiscountGiven = account;
			else if (account.getName().equals(
					AccounterClientConstants.CASH_DISCOUNT_TAKEN))
				cashDiscountTaken = account;
			else if (account.getName().equals(
					AccounterClientConstants.EMPLOYEE_PAYROLL_LIABILITIES))
				payRollLiability = account;
			else if (account.getName().equals(
					AccounterClientConstants.UN_DEPOSITED_FUNDS))
				undepositedFunds = account;
			else if (account.getName().equals(
					AccounterClientConstants.BANK_CHARGE))
				bankCharge = account;
			else if (account.getName().equals(
					AccounterClientConstants.RETAINED_EARNINGS))
				retainedEarnings = account;
			else if (account.getName().equals(
					AccounterClientConstants.PENDING_ITEM_RECEIPTS))
				pendingItemrecicpts = account;

			else if (account.getName().equals(
					AccounterClientConstants.WRITE_OFF))
				writeOffAccount = account;

		}
		openinBalcombo.initCombo(accountlist);
		openinBalcombo.setComboItem(openingBalaccount);

		accountsreceivablecombo.initCombo(accountlist);
		accountsreceivablecombo.setComboItem(accountsReceivable);
		accountsPayablecombo.initCombo(accountlist);
		accountsPayablecombo.setComboItem(accountsPayable);

		salesTaxPayablecombo.initCombo(accountlist);
		salesTaxPayablecombo.setComboItem(salesTaxpayable);
		cashDiscountGivencombo.initCombo(accountlist);
		cashDiscountGivencombo.setComboItem(cashDiscountGiven);

		cashDiscountTakencombo.initCombo(accountlist);
		cashDiscountTakencombo.setComboItem(cashDiscountTaken);

		payrollLiabilitycombo.initCombo(accountlist);
		payrollLiabilitycombo.setComboItem(payRollLiability);

		undepositedFoundscombo.initCombo(accountlist);
		undepositedFoundscombo.setComboItem(undepositedFunds);

		bankChargecombo.initCombo(accountlist);
		bankChargecombo.setComboItem(bankCharge);

		retainedEarningsCombo.initCombo(accountlist);
		retainedEarningsCombo.setComboItem(retainedEarnings);

		pendingItemrecicptsCombo.initCombo(accountlist);
		pendingItemrecicptsCombo.setComboItem(pendingItemrecicpts);

		jobresellAccountCombo.initCombo(accountlist);
		writeOffAccountCombo.initCombo(accountlist);
		writeOffAccountCombo.setComboItem(writeOffAccount);
		defaultCashCombo.initCombo(accountlist);
		openinBalcombo.setDisabled(true);
		accountsreceivablecombo.setDisabled(true);
		salesTaxPayablecombo.setDisabled(true);
		accountsPayablecombo.setDisabled(true);
		cashDiscountGivencombo.setDisabled(true);
		cashDiscountTakencombo.setDisabled(true);
		payrollLiabilitycombo.setDisabled(true);
		undepositedFoundscombo.setDisabled(true);
		bankChargecombo.setDisabled(true);
		retainedEarningsCombo.setDisabled(true);
		pendingItemrecicptsCombo.setDisabled(true);
		jobresellAccountCombo.setDisabled(true);
		writeOffAccountCombo.setDisabled(true);
		defaultCashCombo.setDisabled(true);
	}

	public void setDefaultValues() {

	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.cancel.setFocus(true);
	}

	@Override
	public List<DynamicForm> getForms() {
		// its not usiong any where
		return null;
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}


	@Override
	public void onEdit() {
		setMode(EditMode.EDIT);
	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().companyPrefeTitle();
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
		super.createButtons(buttonBar);
		buttonBar.remove(this.saveAndNewButton);
	}

}
