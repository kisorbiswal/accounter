package com.vimukti.accounter.web.client.ui.fixedassets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.ClientFixedAssetNote;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.DepreciationAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.DepreciationMethodCombo;
import com.vimukti.accounter.web.client.ui.combo.FixedAssetAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.AccounterDOM;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.CancelButton;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.core.PercentageField;
import com.vimukti.accounter.web.client.ui.core.SaveAndCloseButton;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * @author Murali.A
 * 
 */
public class NewFixedAssetView extends BaseView<ClientFixedAsset> {

	private TextItem itemTxt, assetType, assetNumberTxt;
	private FixedAssetAccountCombo accountCombo;
	private DateField purchaseDateTxt;
	private AmountField purchasePriceTxt, accmulatdDepreciationTxt;
	private Label descrptionLabel, infoLabel1, infoLabl2;
	private TextAreaItem descriptionTxtArea;
	private DynamicForm itemInfoForm, purchaseInfoForm, descriptionForm,
			itmNameForm, assetTypeForm, depreciationForm, acumulatedDeprcForm,
			accumulatedDepreciationAccountForm;
	private VerticalPanel accumltdAccVPanel, mainVPanel,
			accumulateDeprctVPanel;
	private HorizontalPanel itemHPanel, depreciationHPanel;
	private DepreciationMethodCombo depreciationMethod;
	private DepreciationAccountCombo depreciationAccount;
	private PercentageField depreciationRate;
	// private String financialYrStartDate = "1st Jan 2010",
	// deprciatedDate = "1st Jan 2009";

	private ClientAccount selectedAssetAccount;

	// private double givenDepRate;
	private boolean isAssetAccumulated;
	private FixedAssetAccountCombo accumulatedDepreciationAccount;
	private boolean isAccumltd;
	private SelectItem assetOptions;
	private NoteDialog noteDialog;

	private ArrayList<DynamicForm> listforms;
	private double depAmount;
	protected String selectedOption;
	private boolean isVerified = false;
	private ImageButton registerButton;

	public NewFixedAssetView() {
		super();
	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	@Override
	public void initData() {

		if (!isInViewMode()) {
			initAssetNumber();
		} else {
			selectedAssetAccount = getCompany().getAccount(
					data.getAssetAccount());
			disableFields(true);
		}
	}

	private void disableFields(boolean disable) {
		itmNameForm.setDisabled(disable);
		itemInfoForm.setDisabled(disable);
		purchaseInfoForm.setDisabled(disable);
		assetTypeForm.setDisabled(disable);
		depreciationForm.setDisabled(disable);
		descriptionForm.setDisabled(disable);
		saveAndCloseButton.setEnabled(!disable);
		registerButton.setEnabled(!disable);
		if (acumulatedDeprcForm != null)
			acumulatedDeprcForm.setDisabled(disable);
		if (accumulatedDepreciationAccountForm != null)
			accumulatedDepreciationAccountForm.setDisabled(disable);
		/*
		 * If an asset saved as a pending item,the linkedaccount might be
		 * null.But while registering,this asset must hav a linked account.So,we
		 * need to display when "edit" is Selected
		 */
		if (selectedOption != null
				&& selectedOption
						.equalsIgnoreCase(Accounter.constants().edit())
				&& isInViewMode() && data.getAssetAccount() != 0
				&& accumltdAccVPanel != null
				&& mainVPanel.getWidgetIndex(accumltdAccVPanel) == -1) {
			showAccumltdAccountForm();
		}
	}

	private void initAssetNumber() {
		this.rpcUtilService
				.getNextFixedAssetNumber(new AccounterAsyncCallback<String>() {

					@Override
					public void onException(AccounterException caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onResultSuccess(String result) {
						if (result != null)
							assetNumberTxt.setValue(result);

					}

				});

	}

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();

		Label labl = new Label(Accounter.constants().newAsset());
		labl.setStyleName(Accounter.constants().requiredField());
		if (getData() != null) {
			switch (data.getStatus()) {
			case 0:
			case ClientFixedAsset.STATUS_PENDING:
				labl.setText(Accounter.constants().pendingAsset());
				break;
			case ClientFixedAsset.STATUS_REGISTERED:
				labl.setText(Accounter.constants().registeredAsset());
				break;
			case ClientFixedAsset.STATUS_SOLD_OR_DISPOSED:
				labl.setText(Accounter.constants().assetSold());
			}
		} else {
			setData(new ClientFixedAsset());
			labl.setText(Accounter.constants().newAsset());
		}

		HorizontalPanel lablHPanel = new HorizontalPanel();
		lablHPanel.setStyleName("margin-b");
		lablHPanel.setWidth("88%");
		lablHPanel.add(labl);

		itemTxt = new TextItem(Accounter.constants().newItem());
		itemTxt.setWidth("323px");
		itemTxt.setRequired(true);

		assetNumberTxt = new TextItem(Accounter.constants().assetNumber());
		assetNumberTxt.setWidth("323px");
		assetNumberTxt.setRequired(true);
		// assetNumberTxt.addChangeHandler(new ChangeHandler() {
		//
		// @Override
		// public void onChange(ChangeEvent event) {
		// String assetNum = assetNumberTxt.getValue().toString();
		// if (assetNum.length() != 0) {
		// try {
		//
		// long assetNumber = Long.parseLong(assetNum);
		// } catch (Exception e) {
		// assetNumberTxt.focusInItem();
		// Accounter.showError(FinanceApplication
		// .constants()
		// .thisFieldAcceptsOnlyNumber());
		//
		// }
		// }
		// }
		// });

		accountCombo = new FixedAssetAccountCombo(Accounter.messages().account(
				Global.get().account()));
		accountCombo.setWidth("323px");
		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						accountCombo.getMainWidget().removeStyleName(
								Accounter.constants().highlightedFormItem());
						if (accountCombo.getSelectedValue() != null) {
							selectedAssetAccount = accountCombo
									.getSelectedValue();
							// if (givenDepRate != 0.0) {
							if (accumltdAccVPanel != null) {
								accumltdAccVPanel.clear();
								mainVPanel.remove(accumltdAccVPanel);
							}
							if (selectedAssetAccount
									.getLinkedAccumulatedDepreciationAccount() == 0) {
								showAccumltdAccountForm();
							}
							// } else {
							// if (accumltdAccVPanel != null)
							// accumltdAccVPanel.clear();
							// }
							NativeEvent ne = Document.get().createChangeEvent();
							DomEvent.fireNativeEvent(ne, purchaseDateTxt
									.getMainWidget());
							DomEvent.fireNativeEvent(ne, purchasePriceTxt
									.getMainWidget());

						}
					}
				});
		purchaseDateTxt = new DateField(Accounter.constants().purchaseDate());
		purchaseDateTxt.setDatethanFireEvent(new ClientFinanceDate());
		purchaseDateTxt.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				showAccumultdDepAmountForm(purchaseDateTxt.getEnteredDate());
			}
		});

		purchasePriceTxt = new AmountField(Accounter.constants()
				.purchasePrice(), this);
		purchasePriceTxt.setWidth(90);
		purchasePriceTxt.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				getDepreciationAmount();

			}
		});

		descrptionLabel = new Label(Accounter.constants().description());
		descriptionTxtArea = new TextAreaItem();
		descriptionTxtArea.setToolTip(Accounter.messages()
				.writeCommentsForThis(this.getAction().getViewName()).replace(
						Accounter.constants().comments(),
						Accounter.constants().description()));
		descriptionTxtArea.setWidth(98);

		itmNameForm = new DynamicForm();
		itmNameForm.setStyleName("margin-b");
		itmNameForm.setWidth("45%");
		itmNameForm.setFields(itemTxt);
		itmNameForm.getCellFormatter().setWidth(0, 0, "185");

		itemInfoForm = new DynamicForm();
		itemInfoForm.setWidth("100%");
		itemInfoForm.setFields(assetNumberTxt, accountCombo);

		purchaseInfoForm = new DynamicForm();
		purchaseInfoForm.setWidth("100%");
		purchaseInfoForm.setFields(purchaseDateTxt, purchasePriceTxt);

		descriptionForm = new DynamicForm();
		descriptionForm.setWidth("100%");
		descriptionForm.setFields(descriptionTxtArea);
		descriptionForm.removeCell(0, 0);

		VerticalPanel purchaseInfoVPanel = new VerticalPanel();
		purchaseInfoVPanel.add(purchaseInfoForm);

		itemHPanel = new HorizontalPanel();
		itemHPanel.setStyleName("margin-b");
		itemHPanel.setWidth("100%");
		itemHPanel.add(itemInfoForm);
		DynamicForm emptyPanel = new DynamicForm();
		emptyPanel.setWidth("10%");
		itemHPanel.add(emptyPanel);
		AccounterDOM.setAttribute(emptyPanel.getElement(), Accounter
				.constants().width(), "10%");
		itemHPanel.add(purchaseInfoVPanel);

		VerticalPanel itmInfoVPanel = new VerticalPanel();
		itmInfoVPanel.setWidth("100%");
		itmInfoVPanel.setCellHorizontalAlignment(itemInfoForm, ALIGN_LEFT);
		itmInfoVPanel.add(itmNameForm);
		itmInfoVPanel.add(itemHPanel);

		VerticalPanel descriptionVPanel = new VerticalPanel();
		descriptionVPanel.setStyleName("margin-b");
		descriptionVPanel.setWidth("100%");
		descriptionVPanel.add(descrptionLabel);
		descriptionVPanel.add(descriptionForm);

		assetType = new TextItem(Accounter.constants().assetType());

		depreciationRate = new PercentageField(this, Accounter.constants()
				.depreciationRate());
		depreciationRate.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				getDepreciationAmount();

			}
		});
		// depreciationRate.addBlurHandler(new BlurHandler() {
		//
		// @Override
		// public void onBlur(BlurEvent event) {
		// /*
		// * The form should be displayed only if rate ! =0.0 and rate it
		// * should be removed if it already added iff rate value doesn't
		// * met the condition
		// */
		// String val = depreciationRate.getValue() != null ? depreciationRate
		// .getValue().toString()
		// : "";
		// val = val.replaceAll("%", "");
		// if (val.length() != 0) {
		// try {
		// double value = Double.parseDouble(val);
		// givenDepRate = value;
		// if (accountCombo.getSelectedValue() != null) {
		// depreciationRate.setPercentage(value);
		// if (accumltdAccVPanel != null)
		// accumltdAccVPanel.clear();
		// showAccumltdAccountForm();
		// } else {
		// if (accumltdAccVPanel != null)
		// accumltdAccVPanel.clear();
		// }
		//
		// } catch (Exception e) {
		// Accounter.showError(AccounterErrorType.INVALIDENTRY);
		// depreciationRate.setPercentage(0.0);
		// if (accumltdAccVPanel != null)
		// accumltdAccVPanel.clear();
		// }
		// } else {
		// if (accumltdAccVPanel != null)
		// accumltdAccVPanel.clear();
		//
		// }
		// }
		// });

		depreciationMethod = new DepreciationMethodCombo(Accounter.constants()
				.depreciationMethod());
		depreciationMethod.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				getDepreciationAmount();

			}
		});
		depreciationMethod.setView(this);

		depreciationAccount = new DepreciationAccountCombo(Accounter.messages()
				.depreciationAccount(Global.get().Account()));

		assetTypeForm = new DynamicForm();
		assetTypeForm.setFields(assetType, depreciationRate);

		depreciationForm = new DynamicForm();
		depreciationForm.setFields(depreciationMethod, depreciationAccount);

		depreciationHPanel = new HorizontalPanel();
		depreciationHPanel.setStyleName("margin-t");
		depreciationHPanel.setStyleName("margin-b");
		depreciationHPanel.add(assetTypeForm);
		depreciationHPanel.add(depreciationForm);

		accumulateDeprctVPanel = new VerticalPanel();
		accumulateDeprctVPanel.setStyleName("margin-b");
		accumltdAccVPanel = new VerticalPanel();
		accumltdAccVPanel.setStyleName("margin-b");

		mainVPanel = new VerticalPanel();
		mainVPanel.setSize("100%", "");
		if (isInViewMode()) {
			assetOptions = new SelectItem(Accounter.constants().assetOptions());
			if (data.getStatus() == ClientFixedAsset.STATUS_REGISTERED) {
				assetOptions.setValueMap("", Accounter.constants().edit(),
						Accounter.constants().sell(), Accounter.constants()
								.dispose(), Accounter.constants().addNote(),
						Accounter.constants().showHistory());
			} else if (data.getStatus() == ClientFixedAsset.STATUS_PENDING) {
				assetOptions.setValueMap("", Accounter.constants().edit(),
						Accounter.constants().addNote(), Accounter.constants()
								.showHistory());
			} else {
				assetOptions.setValueMap("", Accounter.constants().addNote(),
						Accounter.constants().showHistory());
			}
			assetOptions.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					selectedOption = assetOptions.getValue().toString();
					if (selectedOption.equalsIgnoreCase(Accounter.constants()
							.sell())) {
						Action action = ActionFactory
								.getSellingRegisteredItemAction();
						action.catagory = Accounter.constants()
								.fixedAssetsNewFixedAsset();
						action.run(data, true);
					} else if (selectedOption.equalsIgnoreCase(Accounter
							.constants().dispose())) {
						Action action = ActionFactory
								.getDiposingRegisteredItemAction();
						action.catagory = Accounter.constants()
								.fixedAssetsNewFixedAsset();
						action.run(data, true);
					} else if (selectedOption.equalsIgnoreCase(Accounter
							.constants().showHistory())) {
						Action action = ActionFactory.getHistoryListAction();
						action.catagory = Accounter.constants()
								.fixedAssetsNewFixedAsset();
						action.run(data, true);
					} else if (selectedOption.equalsIgnoreCase(Accounter
							.constants().addNote())) {
						openNoteDialog();
					} else if (selectedOption.equalsIgnoreCase(Accounter
							.constants().edit())) {
						disableFields(false);
					} else {
						disableFields(true);
					}
				}
			});
			DynamicForm assetOptionsForm = new DynamicForm();
			listforms.add(assetOptionsForm);
			assetOptionsForm.setFields(assetOptions);
			lablHPanel.setHorizontalAlignment(ALIGN_RIGHT);
			lablHPanel.setCellHorizontalAlignment(assetOptionsForm,
					HasHorizontalAlignment.ALIGN_RIGHT);
			HorizontalPanel assetOptionsHPanel = new HorizontalPanel();
			assetOptionsHPanel.setHorizontalAlignment(ALIGN_RIGHT);
			assetOptionsHPanel.setCellHorizontalAlignment(assetOptionsForm,
					ALIGN_RIGHT);
			assetOptionsHPanel.add(assetOptionsForm);
			lablHPanel.add(assetOptionsHPanel);
		}
		mainVPanel.add(lablHPanel);
		mainVPanel.add(itmInfoVPanel);
		mainVPanel.add(descriptionVPanel);
		mainVPanel.add(depreciationHPanel);

		showAccumultdDepAmountForm(purchaseDateTxt.getEnteredDate());

		// populating the data into respective fields in edit mode
		if (isInViewMode()) {

			itemTxt.setValue(data.getName() != null ? data.getName() : "");
			assetNumberTxt.setValue(data.getAssetNumber());
			if (getCompany().getAccount(data.getAssetAccount()) != null) {
				accountCombo.setComboItem(getCompany().getAccount(
						data.getAssetAccount()));
			}
			purchaseDateTxt.setEnteredDate(new ClientFinanceDate(data
					.getPurchaseDate()));
			purchasePriceTxt.setAmount(data.getPurchasePrice());
			descriptionTxtArea.setValue(data.getDescription());
			assetType.setValue(data.getAssetType());
			depreciationRate.setPercentage(data.getDepreciationRate());
			depreciationMethod.setSelected(depreciationMethod
					.getNameByType(data.getDepreciationMethod()));
			depreciationMethod.setSelectedValue(data.getDepreciationMethod());
			if (data.getDepreciationExpenseAccount() != 0) {
				if (getCompany().getAccount(
						data.getDepreciationExpenseAccount()) != null) {
					depreciationAccount.setComboItem(Accounter.getCompany()
							.getAccount(data.getDepreciationExpenseAccount()));
				}
			} else
				depreciationAccount.setSelected("");
			if (!DecimalUtil.isEquals(data.getAccumulatedDepreciationAmount(),
					0)) {
				showAccumultdDepAmountForm(new ClientFinanceDate(data
						.getPurchaseDate()));
			}
			Label bookValueLbl = new Label();
			bookValueLbl.setText(Accounter.constants().bookValue()
					+ data.getBookValue());
			Label accumDepLbl = new Label();
			accumDepLbl.setText(Accounter.constants().accumulatedDepreciation()
					+ data.getAccumulatedDepreciationAmount());
			// XXX THIS CLASS IS NOT USED.
			// -- need to check weder dis field is required or not
			// Label lastDepreciatedLabl = new Label();
			// --check whether is it lastdepricaited date or not
			// lastDepreciatedLabl.setText(FinanceApplication
			// .constants().lastDepreciation()
			// + data.getLastDate());
			ClientAccount assetAcc = accountCombo.getSelectedValue() != null ? accountCombo
					.getSelectedValue()
					: null;
			if (assetAcc != null) {
				long strID = assetAcc.getLinkedAccumulatedDepreciationAccount();
				if (strID != 0)
					data.setLinkedAccumulatedDepreciationAccount(strID);
			}
			if (data.getLinkedAccumulatedDepreciationAccount() != 0) {
				showAccumltdAccountForm();
			}
			showAccumultdDepAmountForm(purchaseDateTxt.getEnteredDate());
			VerticalPanel bookValueVPanel = new VerticalPanel();
			mainVPanel.add(bookValueVPanel);

		}

		/*
		 * In edit mode,if the asset is other than the pending item,"Register"
		 * button should be hide
		 */
		if (isInViewMode()
				&& (data.getStatus() == ClientFixedAsset.STATUS_REGISTERED || data
						.getStatus() == ClientFixedAsset.STATUS_REGISTERED)) {
			registerButton.setVisible(false);
		} else if (isInViewMode()
				&& (data.getStatus() == ClientFixedAsset.STATUS_SOLD_OR_DISPOSED)) {
			registerButton.setVisible(false);
			saveAndCloseButton.setVisible(false);
		}
		this.add(mainVPanel);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(itmNameForm);

		listforms.add(itemInfoForm);

		listforms.add(purchaseInfoForm);
		listforms.add(depreciationForm);
		listforms.add(emptyPanel);
		listforms.add(assetTypeForm);

	}

	/*
	 * The note dialog get opened in edit mode on selecting the "Add Note"
	 * option in options combo And this note is saved into the asset object on
	 * clicking "save"
	 */
	private void openNoteDialog() {
		noteDialog = new NoteDialog(Accounter.constants().addNote(), "");
		noteDialog.addInputDialogHandler(new InputDialogHandler() {

			@Override
			public boolean onOK() {

				if (noteDialog.noteArea.getValue() != null) {
					String value = noteDialog.noteArea.getValue().toString();
					if (isInViewMode() && value.length() != 0) {
						ClientFixedAssetNote note = new ClientFixedAssetNote();
						note.setNote(value);
						List<ClientFixedAssetNote> noteList = data
								.getFixedAssetNotes();
						noteList.add(note);
						data.setFixedAssetNotes(noteList);
						saveOrUpdate(data);
					}
				}
				return true;
			}

			@Override
			public void onCancel() {

			}
		});

	}

	/*
	 * This method adds the AccumulatedDepreciationAccount combo to mainPanel
	 * This combo should be displayed only if the selected AssetAccount doesn't
	 * contains Linked Account. This account will updated in the database with
	 * the selected LinkedAccount after saving the FixedAsset
	 */
	private void showAccumltdAccountForm() {
		isAccumltd = true;
		infoLabl2 = new Label(
				Accounter
						.messages()
						.assetAccountYouHaveSelectedNeedsLinkedAccumulatedDepreciationAccount(
								Global.get().account()));
		infoLabl2.addStyleName("requiredField");

		accumulatedDepreciationAccount = new FixedAssetAccountCombo(Accounter
				.messages().accumulatedDepreciationAccount(
						Global.get().account()));
		accumulatedDepreciationAccount
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {

						validateAccount();

					}

				});
		/*
		 * An AccumulatedDepreciation account can be an account which is not
		 * used as an Asset Acount in existing Fixed Assets.And AssetAccount and
		 * AccumulatedDep.Account must not be same.
		 */
		List<ClientAccount> accumulatedAccounts = accumulatedDepreciationAccount
				.getAccounts();
		List<ClientFixedAsset> fixedAssets = getCompany().getFixedAssets();
		for (ClientFixedAsset asset : fixedAssets) {
			for (ClientAccount accumulatedAccount : accumulatedAccounts) {
				if (asset.getAssetAccount() != accumulatedAccount.getID()
						&& selectedAssetAccount.getID() != accumulatedAccount
								.getID())
					accumulatedDepreciationAccount.setValue(accumulatedAccount);
			}
		}

		/*
		 * setting the linked Account(AccumulatedDep.Account) for the selected
		 * AssetAccount in edit mode
		 */
		if (isInViewMode() && data.getAssetAccount() != 0) {
			if (getCompany().getAccount(data.getAssetAccount())
					.getLinkedAccumulatedDepreciationAccount() != 0) {
				long accumulatedAccountID = getCompany().getAccount(
						data.getAssetAccount())
						.getLinkedAccumulatedDepreciationAccount();
				ClientAccount accumultdAcc = getCompany().getAccount(
						accumulatedAccountID);
				accumulatedDepreciationAccount
						.setSelected(accumultdAcc != null ? accumultdAcc
								.getName() : "");

			} else
				accumulatedDepreciationAccount.setSelected("");

		}
		if (isInViewMode()) {
			accumulatedDepreciationAccount
					.setComboItem(getCompany().getAccount(
							data.getLinkedAccumulatedDepreciationAccount()));
		}
		accumulatedDepreciationAccountForm = new DynamicForm();
		accumulatedDepreciationAccountForm
				.setFields(accumulatedDepreciationAccount);
		accumltdAccVPanel.add(infoLabl2);
		accumltdAccVPanel.add(accumulatedDepreciationAccountForm);
		mainVPanel.remove(accumltdAccVPanel);
		mainVPanel.add(accumltdAccVPanel);
		listforms.add(accumulatedDepreciationAccountForm);
	}

	protected boolean validateAccount() {
		ClientAccount assetAccount = accountCombo.getSelectedValue();
		ClientAccount selectItem = accumulatedDepreciationAccount
				.getSelectedValue();
		if (assetAccount != null && selectItem != null
				&& assetAccount.getID() == selectItem.getID()) {
			return false;
		}
		return true;

	}

	/*
	 * The AccumulatedDepreciationAmount field need to be added when given date
	 * is before the depreciation startdate
	 */

	private void showAccumultdDepAmountForm(ClientFinanceDate enteredDate) {
		if (getDepreciationStartDate() != null) {
			if (!enteredDate.equals(getDepreciationStartDate())
					&& enteredDate.before(getDepreciationStartDate())) {
				isAssetAccumulated = true;
				infoLabel1 = new Label(Accounter.constants()
						.purchaseDatePriorToFixedAssetsStartDate()
						+ UIUtils
								.getDateStringByDate(getDepreciationStartDate()
										.toString())
						+ Accounter.constants().openBraseSoPleaseSelect());
				infoLabel1.setStyleName("requiredField");
				accmulatdDepreciationTxt = new AmountField(Accounter
						.constants().accumulatedDepreciationTo()
						+ " "
						+ UIUtils.getDateStringByDate(enteredDate.toString()),
						this);
				accmulatdDepreciationTxt.setValue(String
						.valueOf(getDepreciationAmount()));

				if (isInViewMode())
					accmulatdDepreciationTxt.setValue(String.valueOf(data
							.getAccumulatedDepreciationAmount()));

				acumulatedDeprcForm = new DynamicForm();
				listforms.add(acumulatedDeprcForm);
				acumulatedDeprcForm.setFields(accmulatdDepreciationTxt);
				accumulateDeprctVPanel.clear();
				accumulateDeprctVPanel.add(infoLabel1);
				accumulateDeprctVPanel.add(acumulatedDeprcForm);
				accumulateDeprctVPanel.removeFromParent();
				mainVPanel.remove(accumulateDeprctVPanel);
				mainVPanel.add(accumulateDeprctVPanel);
			} else if (enteredDate.after(getDepreciationStartDate())
					|| enteredDate.equals(getDepreciationStartDate())) {
				isAssetAccumulated = false;
				accumulateDeprctVPanel.clear();
				accumulateDeprctVPanel.removeFromParent();
				mainVPanel.remove(accumulateDeprctVPanel);
				// Need to update the conversion balances.
				// See http://help.xero.com/uk/#Settings_ConversionDate for
				// reference
			}
		}
	}

	public double getDepreciationAmount() {
		try {

			if (isInViewMode() && accmulatdDepreciationTxt.getDisabled()) {
				accmulatdDepreciationTxt.setAmount(data
						.getAccumulatedDepreciationAmount());
			} else {
				ClientFinanceDate purchaseDate = purchaseDateTxt
						.getEnteredDate();
				int depMethod = depreciationMethod.getSelectedValue() != 0 ? depreciationMethod
						.getSelectedValue()
						: 0;
				double depRate = depreciationRate.getPercentage();
				double purchasePrice = purchasePriceTxt.getAmount();
				ClientFinanceDate depStartDate = new ClientFinanceDate(
						getCompany().getPreferences()
								.getDepreciationStartDate());
				depAmount = 0.0;
				/*
				 * Make an rpc call to get the calculatedDepreciation amount if
				 * the following condtions are satisfy
				 */
				if (depMethod != 0 && !DecimalUtil.isEquals(depRate, 0)
						&& !DecimalUtil.isEquals(purchasePrice, 0)
						&& purchaseDate.before(depStartDate)) {
					Accounter.createHomeService()
							.getAccumulatedDepreciationAmount(depMethod,
									depRate, purchasePrice,
									purchaseDate.getDate(),
									depStartDate.getDate(),
									new AccounterAsyncCallback<Double>() {

										@Override
										public void onException(
												AccounterException caught) {

										}

										@Override
										public void onResultSuccess(
												Double result) {
											depAmount = result;
											if (accmulatdDepreciationTxt != null) {
												accmulatdDepreciationTxt
														.setAmount(depAmount);
											}

										}
									});
				}
			}
		} catch (Exception e) {

		}
		return depAmount;
	}

	public ClientFinanceDate getDepreciationStartDate() {
		return new ClientFinanceDate(getCompany().getPreferences()
				.getDepreciationStartDate());
	}

	@Override
	public void saveAndUpdateView() {
		updateAssetObject();
		saveOrUpdate(data);
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		ClientFixedAsset createdAsset = (ClientFixedAsset) result;
		if (createdAsset.getID() != 0) {
			// if (fixedAsset == null)
			// Accounter.showInformation(FinanceApplication
			// .constants().newAssetwithName()
			// + ((ClientFixedAsset) result).getName()
			// + FinanceApplication.constants()
			// .isCreated());
			// else
			// Accounter.showInformation(((ClientFixedAsset) result).getName()
			// + FinanceApplication.constants()
			// .IsUpdatedSuccessfully());
			super.saveSuccess(result);
		} else
			saveFailed(new AccounterException(Accounter.constants().failed()));

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		// if (!isEdit)
		// BaseView.errordata.setHTML(FinanceApplication
		// .constants().duplicationOfAssets());
		// addError(this, Accounter.constants().duplicationOfAssets());
		// BaseView.errordata.setHTML(FinanceApplication
		// else
		// .constants().assetApdationFailed());
		// addError(this, Accounter.constants().accountUpdationFailed());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		AccounterException accounterException = (AccounterException) exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	private void updateAssetObject() {
		data.setName(itemTxt.getValue() != null ? itemTxt.getValue().toString()
				: "");
		data.setAssetNumber(assetNumberTxt.getValue() != null ? assetNumberTxt
				.getValue().toString() : "");
		if (selectedAssetAccount != null) {
			if (accumulatedDepreciationAccount != null) {
				data
						.setLinkedAccumulatedDepreciationAccount(accumulatedDepreciationAccount
								.getSelectedValue() != null ? accumulatedDepreciationAccount
								.getSelectedValue().getID()
								: 0);
			}
			data.setAssetAccount(selectedAssetAccount.getID());

		}
		data.setPurchaseDate(purchaseDateTxt.getEnteredDate().getDate());
		data.setPurchasePrice(purchasePriceTxt.getAmount());
		data
				.setDescription(descriptionTxtArea.getValue() != null ? descriptionTxtArea
						.getValue().toString()
						: "");

		data.setAssetType(assetType.getValue() != null ? assetType.getValue()
				.toString() : "");
		data
				.setDepreciationRate(depreciationRate.getPercentage() != null ? depreciationRate
						.getPercentage()
						: 0.0);
		data.setDepreciationMethod(depreciationMethod.getSelectedValue());
		data.setDepreciationExpenseAccount(depreciationAccount
				.getSelectedValue() != null ? depreciationAccount
				.getSelectedValue().getID() : 0);

		// if (isAssetAccumulated || fixedAsset != null
		// && accmulatdDepreciationTxt != null) {
		// data.setAccumulatedDepreciationAmount(accmulatdDepreciationTxt
		// .getAmount());
		// }

		if (isAssetAccumulated)
			data.setAccumulatedDepreciationAmount(accmulatdDepreciationTxt
					.getAmount());
		else
			data.setAccumulatedDepreciationAmount(0.0);

		/* while registering the data from viewmode or updating a registeritem */
		if ((false && data != null)
				|| (data != null && data.getStatus() == ClientFixedAsset.STATUS_REGISTERED)) {
			data.setStatus(ClientFixedAsset.STATUS_REGISTERED);
		} else if (false) {
			/* while creating a registeritem */
			data.setStatus(ClientFixedAsset.STATUS_REGISTERED);
		} else {
			/* while updating/creating a pending item */
			data.setStatus(ClientFixedAsset.STATUS_PENDING);
		}

	}

	@Override
	protected void onLoad() {
		int assetNumberWidth = itemInfoForm.getCellFormatter().getElement(0, 0)
				.getOffsetWidth();
		adjustFormWidths(assetNumberWidth);
		super.onLoad();
	}

	@Override
	protected void onAttach() {
		Element e = itemInfoForm.getCellFormatter().getElement(0, 0).cast();
		int assetNumberWidth = e.getOffsetWidth();
		adjustFormWidths(assetNumberWidth);
		super.onAttach();
	};

	private void adjustFormWidths(int assetNumberWidth) {
		itmNameForm.getCellFormatter().getElement(0, 0).setAttribute("width",
				assetNumberWidth + "");
	}

	/*
	 * If "Save" button clicked, only itemname & assetnumber fields are
	 * mandatory. If "Register" button clicked,all the fileds are mandatory
	 */
	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		// fixed asset already available or not?
		// related form validations
		// purchase date validation

		ClientFixedAsset fixedAssetByName = getCompany().getFixedAssetByName(
				itemTxt.getValue().toString());
		if (fixedAssetByName != null) {
			result.addError(itemTxt, Accounter.constants().alreadyExist());
			return result;
		}
		// if (Utility.isObjectExist(getCompany().getFixedAssets(), itemTxt
		// .getValue().toString())) {
		// result.addError(itemTxt, Accounter.constants().alreadyExist());
		// }

		// Commented due to isRegister variable removed
		// if (isRegister) {
		// assetNumberTxt.setRequired(true);
		// accountCombo.setRequired(true);
		// purchaseDateTxt.setRequired(true);
		// purchasePriceTxt.setRequired(true);
		// assetType.setRequired(true);
		// depreciationRate.setRequired(true);
		// depreciationMethod.setRequired(true);
		// depreciationAccount.setRequired(true);
		// if (acumulatedDeprcForm != null) {
		// accmulatdDepreciationTxt.setRequired(true);
		// }
		// if (accumulatedDepreciationAccountForm != null) {
		// accumulatedDepreciationAccount.setRequired(true);
		// }
		//
		// result.add(itmNameForm.validate());
		// result.add(itemInfoForm.validate());
		// result.add(purchaseInfoForm.validate());
		// result.add(assetTypeForm.validate());
		// result.add(depreciationForm.validate());
		// if (isAssetAccumulated) {
		// result.add((acumulatedDeprcForm.validate()));
		// }
		// if (isAccumltd) {
		// result.add((accumulatedDepreciationAccountForm.validate()));
		// }
		// double price = purchasePriceTxt.getAmount();
		// if (DecimalUtil.isEquals(price, 0.0)) {
		// result.addError(purchasePriceTxt, Accounter.constants()
		// .purchasePricShouldNotBeZero());
		//
		// }
		//
		// if (!AccounterValidator.isValidPurchaseDate(purchaseDateTxt
		// .getEnteredDate())) {
		// result.addError(purchaseDateTxt, Accounter.constants()
		// .invalidPurchaseDate());
		// }
		//
		// } else {
		result.add(itmNameForm.validate());
		result.add(itemInfoForm.validate());

		if (!AccounterValidator.isValidPurchaseDate(purchaseDateTxt
				.getEnteredDate())) {
			result.addError(purchaseDateTxt, Accounter.constants()
					.invalidPurchaseDate());
		}

		if (accountCombo != null && accumulatedDepreciationAccount != null) {
			if (validateAccount()) {
				result.addError(accountCombo, Accounter.messages()
						.accandaccumulatedDepreciationAccShouldnotbesame(
								Global.get().account()));
			}
		}
		// }
		if (!AccounterValidator
				.isFixedAssetPurchaseDateWithinRange(purchaseDateTxt
						.getEnteredDate())) {
			result.addError(purchaseDateTxt, Accounter.constants()
					.purchaseDatesShoudBewithInFiscalYearRange());
		}
		return result;
	}

	private boolean validateName(String name) {
		if (!(!isInViewMode() && getCompany().getFixedAssetByName(name) != null ? true
				: false)
				|| !(isInViewMode() && !(data.getName().equalsIgnoreCase(name) ? true
						: getCompany().getFixedAssetByName(name) != null ? false
								: true))) {
			isVerified = true;
			return false;
		}
		return true;

	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.itemTxt.setFocus();
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
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().newFixedAsset();
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
		this.saveAndCloseButton = new SaveAndCloseButton(this);
		buttonBar.add(this.saveAndCloseButton,
				HasHorizontalAlignment.ALIGN_LEFT);

		registerButton = new ImageButton(Accounter.constants().register(),
				Accounter.getFinanceImages().register());
		registerButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				registerAsset();

			}
		});
		buttonBar.add(registerButton, HasHorizontalAlignment.ALIGN_RIGHT);
		this.cancelButton = new CancelButton(this);
		buttonBar.add(this.cancelButton, HasHorizontalAlignment.ALIGN_RIGHT);
	}

	protected void registerAsset() {
		// TODO Auto-generated method stub

	}
}
