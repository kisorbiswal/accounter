package com.vimukti.accounter.web.client.ui.fixedassets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.ClientFixedAssetNote;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.DepreciationAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.DepreciationMethodCombo;
import com.vimukti.accounter.web.client.ui.combo.FixedAssetAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.AccounterDOM;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.FixedAssetsActionFactory;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.core.PercentageField;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
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
	private ClientFixedAsset fixedAsset;

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

	public NewFixedAssetView() {
		super(true);
		this.validationCount = 4;
	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	@Override
	public void initData() {

		if (fixedAsset == null)
			initAssetNumber();
		if (fixedAsset != null) {
			selectedAssetAccount = getCompany().getAccount(
					fixedAsset.getAssetAccount());
		}
		if (fixedAsset != null) {
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
				&& selectedOption.equalsIgnoreCase(Accounter
						.getFixedAssetConstants().edit()) && fixedAsset != null
				&& fixedAsset.getAssetAccount() != 0
				&& accumltdAccVPanel != null
				&& mainVPanel.getWidgetIndex(accumltdAccVPanel) == -1) {
			showAccumltdAccountForm();
		}
	}

	private void initAssetNumber() {
		this.rpcUtilService
				.getNextFixedAssetNumber(new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(String result) {
						if (result != null)
							assetNumberTxt.setValue(result);

					}

				});

	}

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();

		Label labl = new Label(Accounter.getFixedAssetConstants().newAsset());
		labl.setStyleName(Accounter.getFixedAssetConstants().requiredField());
		if (fixedAsset != null) {
			switch (fixedAsset.getStatus()) {
			case 0:
			case ClientFixedAsset.STATUS_PENDING:
				labl.setText(Accounter.getFixedAssetConstants().pendingAsset());
				break;
			case ClientFixedAsset.STATUS_REGISTERED:
				labl.setText(Accounter.getFixedAssetConstants()
						.registeredAsset());
				break;
			case ClientFixedAsset.STATUS_SOLD_OR_DISPOSED:
				labl.setText(Accounter.getFixedAssetConstants().assetSold());
			}
		} else
			labl.setText(Accounter.getFixedAssetConstants().newAsset());

		HorizontalPanel lablHPanel = new HorizontalPanel();
		lablHPanel.setStyleName("margin-b");
		lablHPanel.setWidth("88%");
		lablHPanel.add(labl);

		itemTxt = new TextItem(fixedAssetConstants.newItem());
		itemTxt.setWidth("323px");
		itemTxt.setRequired(true);

		assetNumberTxt = new TextItem(fixedAssetConstants.assetNumber());
		assetNumberTxt.setWidth("323px");
		assetNumberTxt.setRequired(true);
		// assetNumberTxt.addChangeHandler(new ChangeHandler() {
		//
		// @Override
		// public void onChange(ChangeEvent event) {
		// String assetNum = assetNumberTxt.getValue().toString();
		// if (assetNum.length() != 0) {
		// try {
		// @SuppressWarnings("unused")
		// long assetNumber = Long.parseLong(assetNum);
		// } catch (Exception e) {
		// assetNumberTxt.focusInItem();
		// Accounter.showError(FinanceApplication
		// .getFixedAssetConstants()
		// .thisFieldAcceptsOnlyNumber());
		//
		// }
		// }
		// }
		// });

		accountCombo = new FixedAssetAccountCombo(fixedAssetConstants.account());
		accountCombo.setWidth("323px");
		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						accountCombo.getMainWidget().removeStyleName(
								Accounter.getFixedAssetConstants()
										.highlightedFormItem());
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
							DomEvent.fireNativeEvent(ne,
									purchaseDateTxt.getMainWidget());
							DomEvent.fireNativeEvent(ne,
									purchasePriceTxt.getMainWidget());

						}
					}
				});
		purchaseDateTxt = new DateField(fixedAssetConstants.purchaseDate());
		purchaseDateTxt.setDatethanFireEvent(new ClientFinanceDate());
		purchaseDateTxt.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				showAccumultdDepAmountForm(purchaseDateTxt.getEnteredDate());
			}
		});

		purchasePriceTxt = new AmountField(fixedAssetConstants.purchasePrice());
		purchasePriceTxt.setWidth(90);
		purchasePriceTxt.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				getDepreciationAmount();

			}
		});

		descrptionLabel = new Label(Accounter.getFixedAssetConstants()
				.description());
		descriptionTxtArea = new TextAreaItem();
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
				.getFixedAssetConstants().width(), "10%");
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

		assetType = new TextItem(fixedAssetConstants.assetType());

		depreciationRate = new PercentageField(
				fixedAssetConstants.depreciationRate());
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

		depreciationMethod = new DepreciationMethodCombo(
				fixedAssetConstants.depreciationMethod());
		depreciationMethod.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				getDepreciationAmount();

			}
		});
		depreciationMethod.setView(this);

		depreciationAccount = new DepreciationAccountCombo(
				fixedAssetConstants.depreciationAccount());

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
		if (fixedAsset != null) {
			assetOptions = new SelectItem(Accounter.getFixedAssetConstants()
					.assetOptions());
			if (fixedAsset.getStatus() == ClientFixedAsset.STATUS_REGISTERED) {
				assetOptions.setValueMap("", Accounter.getFixedAssetConstants()
						.edit(), Accounter.getFixedAssetConstants().sell(),
						Accounter.getFixedAssetConstants().dispose(), Accounter
								.getFixedAssetConstants().addNote(), Accounter
								.getFixedAssetConstants().showHistory());
			} else if (fixedAsset.getStatus() == ClientFixedAsset.STATUS_PENDING) {
				assetOptions.setValueMap("", Accounter.getFixedAssetConstants()
						.edit(), Accounter.getFixedAssetConstants().addNote(),
						Accounter.getFixedAssetConstants().showHistory());
			} else {
				assetOptions.setValueMap("", Accounter.getFixedAssetConstants()
						.addNote(), Accounter.getFixedAssetConstants()
						.showHistory());
			}
			assetOptions.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					selectedOption = assetOptions.getValue().toString();
					if (selectedOption.equalsIgnoreCase(Accounter
							.getFixedAssetConstants().sell())) {
						Action action = FixedAssetsActionFactory
								.getSellingRegisteredItemAction();
						action.catagory = Accounter.getFixedAssetConstants()
								.fixedAssetsNewFixedAsset();
						HistoryTokenUtils.setPresentToken(action, fixedAsset);
						action.run(fixedAsset, true);
					} else if (selectedOption.equalsIgnoreCase(Accounter
							.getFixedAssetConstants().dispose())) {
						Action action = FixedAssetsActionFactory
								.getDiposingRegisteredItemAction();
						action.catagory = Accounter.getFixedAssetConstants()
								.fixedAssetsNewFixedAsset();
						HistoryTokenUtils.setPresentToken(action, fixedAsset);
						action.run(fixedAsset, true);
					} else if (selectedOption.equalsIgnoreCase(Accounter
							.getFixedAssetConstants().showHistory())) {
						Action action = FixedAssetsActionFactory
								.getHistoryListAction();
						action.catagory = Accounter.getFixedAssetConstants()
								.fixedAssetsNewFixedAsset();
						HistoryTokenUtils.setPresentToken(action, fixedAsset);
						action.run(fixedAsset, true);
					} else if (selectedOption.equalsIgnoreCase(Accounter
							.getFixedAssetConstants().addNote())) {
						openNoteDialog();
					} else if (selectedOption.equalsIgnoreCase(Accounter
							.getFixedAssetConstants().edit())) {
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
		if (fixedAsset != null) {

			itemTxt.setValue(fixedAsset.getName() != null ? fixedAsset
					.getName() : "");
			assetNumberTxt.setValue(fixedAsset.getAssetNumber());
			if (getCompany().getAccount(fixedAsset.getAssetAccount()) != null) {
				accountCombo.setComboItem(getCompany().getAccount(
						fixedAsset.getAssetAccount()));
			}
			purchaseDateTxt.setEnteredDate(new ClientFinanceDate(fixedAsset
					.getPurchaseDate()));
			purchasePriceTxt.setAmount(fixedAsset.getPurchasePrice());
			descriptionTxtArea.setValue(fixedAsset.getDescription());
			assetType.setValue(fixedAsset.getAssetType());
			depreciationRate.setPercentage(fixedAsset.getDepreciationRate());
			depreciationMethod.setSelected(depreciationMethod
					.getNameByType(fixedAsset.getDepreciationMethod()));
			depreciationMethod.setSelectedValue(fixedAsset
					.getDepreciationMethod());
			if (fixedAsset.getDepreciationExpenseAccount() != 0) {
				if (getCompany().getAccount(
						fixedAsset.getDepreciationExpenseAccount()) != null) {
					depreciationAccount
							.setComboItem(Accounter.getCompany().getAccount(
									fixedAsset.getDepreciationExpenseAccount()));
				}
			} else
				depreciationAccount.setSelected("");
			if (!DecimalUtil.isEquals(
					fixedAsset.getAccumulatedDepreciationAmount(), 0)) {
				showAccumultdDepAmountForm(new ClientFinanceDate(
						fixedAsset.getPurchaseDate()));
			}
			Label bookValueLbl = new Label();
			bookValueLbl.setText(Accounter.getFixedAssetConstants().bookValue()
					+ fixedAsset.getBookValue());
			Label accumDepLbl = new Label();
			accumDepLbl.setText(Accounter.getFixedAssetConstants()
					.accumulatedDepreciation()
					+ fixedAsset.getAccumulatedDepreciationAmount());
			// FIXME -- need to check weder dis field is required or not
			// Label lastDepreciatedLabl = new Label();
			// // FIXME--check whether is it lastdepricaited date or not
			// lastDepreciatedLabl.setText(FinanceApplication
			// .getFixedAssetConstants().lastDepreciation()
			// + fixedAsset.getLastDate());
			ClientAccount assetAcc = accountCombo.getSelectedValue() != null ? accountCombo
					.getSelectedValue() : null;
			if (assetAcc != null) {
				long strID = assetAcc.getLinkedAccumulatedDepreciationAccount();
				if (strID != 0)
					fixedAsset.setLinkedAccumulatedDepreciationAccount(strID);
			}
			if (fixedAsset.getLinkedAccumulatedDepreciationAccount() != 0) {
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
		if (isEdit
				&& (fixedAsset.getStatus() == ClientFixedAsset.STATUS_REGISTERED || fixedAsset
						.getStatus() == ClientFixedAsset.STATUS_REGISTERED)) {
			registerButton.setVisible(false);
		} else if (isEdit
				&& (fixedAsset.getStatus() == ClientFixedAsset.STATUS_SOLD_OR_DISPOSED)) {
			registerButton.setVisible(false);
			saveAndCloseButton.setVisible(false);
		}
		canvas.add(mainVPanel);

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
		noteDialog = new NoteDialog(Accounter.getFixedAssetConstants()
				.addNote(), "");
		noteDialog.addInputDialogHandler(new InputDialogHandler() {

			@Override
			public boolean onOkClick() {

				if (noteDialog.noteArea.getValue() != null) {
					String value = noteDialog.noteArea.getValue().toString();
					if (fixedAsset != null && value.length() != 0) {
						ClientFixedAssetNote note = new ClientFixedAssetNote();
						note.setNote(value);
						List<ClientFixedAssetNote> noteList = fixedAsset
								.getFixedAssetNotes();
						noteList.add(note);
						fixedAsset.setFixedAssetNotes(noteList);
						ViewManager.getInstance().alterObject(fixedAsset,
								NewFixedAssetView.this);
					}
				}
				return true;
			}

			@Override
			public void onCancelClick() {

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
						.getFixedAssetConstants()
						.assetAccountYouHaveSelectedNeedsLinkedAccumulatedDepreciationAccount());
		infoLabl2.addStyleName("requiredField");

		accumulatedDepreciationAccount = new FixedAssetAccountCombo(Accounter
				.getFixedAssetConstants().accumulatedDepreciationAccount());
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
		if (fixedAsset != null && fixedAsset.getAssetAccount() != 0) {
			if (getCompany().getAccount(fixedAsset.getAssetAccount())
					.getLinkedAccumulatedDepreciationAccount() != 0) {
				long accumulatedAccountID = getCompany().getAccount(
						fixedAsset.getAssetAccount())
						.getLinkedAccumulatedDepreciationAccount();
				ClientAccount accumultdAcc = getCompany().getAccount(
						accumulatedAccountID);
				accumulatedDepreciationAccount
						.setSelected(accumultdAcc != null ? accumultdAcc
								.getName() : "");

			} else
				accumulatedDepreciationAccount.setSelected("");

		}
		if (isEdit) {
			accumulatedDepreciationAccount
					.setComboItem(getCompany().getAccount(
							fixedAsset
									.getLinkedAccumulatedDepreciationAccount()));
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
			Accounter
					.showError("Account and Accumulated Depreciation Account should not be same ");
			return false;
		}
		return true;

	}

	/*
	 * The AccumulatedDepreciationAmount field need to be added when given date
	 * is before the depreciation startdate
	 */
	@SuppressWarnings("deprecation")
	private void showAccumultdDepAmountForm(ClientFinanceDate enteredDate) {
		if (getDepreciationStartDate() != null) {
			if (!enteredDate.equals(getDepreciationStartDate())
					&& enteredDate.before(getDepreciationStartDate())) {
				isAssetAccumulated = true;
				infoLabel1 = new Label(
						"The purchase date is prior to the Fixed Assets start date ("
								+ UIUtils
										.getDateStringByDate(getDepreciationStartDate()
												.toString())
								+ ") so please enter: ");
				infoLabel1.setStyleName("requiredField");
				accmulatdDepreciationTxt = new AmountField(Accounter
						.getFixedAssetConstants().accumulatedDepreciationTo()
						+ " "
						+ UIUtils.getDateStringByDate(enteredDate.toString()));
				accmulatdDepreciationTxt.setValue(getDepreciationAmount());

				if (fixedAsset != null)
					accmulatdDepreciationTxt.setValue(fixedAsset
							.getAccumulatedDepreciationAmount());

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

			if (fixedAsset != null && accmulatdDepreciationTxt.getDisabled()) {
				accmulatdDepreciationTxt.setAmount(fixedAsset
						.getAccumulatedDepreciationAmount());
			} else {
				ClientFinanceDate purchaseDate = purchaseDateTxt
						.getEnteredDate();
				int depMethod = depreciationMethod.getSelectedValue() != 0 ? depreciationMethod
						.getSelectedValue() : 0;
				double depRate = depreciationRate.getPercentage();
				double purchasePrice = purchasePriceTxt.getAmount();
				ClientFinanceDate depStartDate = new ClientFinanceDate(
						getCompany().getpreferences()
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
									purchaseDate.getTime(),
									depStartDate.getTime(),
									new AsyncCallback<Double>() {

										@Override
										public void onFailure(Throwable caught) {

										}

										@Override
										public void onSuccess(Double result) {
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
		return new ClientFinanceDate(getCompany().getpreferences()
				.getDepreciationStartDate());
	}

	@Override
	public void saveAndUpdateView() throws Exception {
		try {
			ClientFixedAsset asset = getAssetObject();
			if (fixedAsset == null) {
				if (Utility.isObjectExist(getCompany().getFixedAssets(),
						itemTxt.getValue().toString())) {
					throw new InvalidEntryException("FixedAsset"
							+ AccounterErrorType.ALREADYEXIST);
				} else
					createObject(asset);
			} else
				alterObject(asset);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		ClientFixedAsset createdAsset = (ClientFixedAsset) result;
		if (createdAsset.getID() != 0) {
			// if (fixedAsset == null)
			// Accounter.showInformation(FinanceApplication
			// .getFixedAssetConstants().newAssetwithName()
			// + ((ClientFixedAsset) result).getName()
			// + FinanceApplication.getFixedAssetConstants()
			// .isCreated());
			// else
			// Accounter.showInformation(((ClientFixedAsset) result).getName()
			// + FinanceApplication.getFixedAssetConstants()
			// .IsUpdatedSuccessfully());
			super.saveSuccess(result);
		} else
			saveFailed(new Exception(Accounter.getFinanceUIConstants().failed()));

	}

	@Override
	public void saveFailed(Throwable exception) {
		super.saveFailed(exception);
		if (fixedAsset == null)
			// BaseView.errordata.setHTML(FinanceApplication
			// .getFinanceUIConstants().duplicationOfAssets());
			MainFinanceWindow.getViewManager().showError(
					Accounter.getFinanceUIConstants().duplicationOfAssets());
		// BaseView.errordata.setHTML(FinanceApplication
		else
			// .getFinanceUIConstants().assetApdationFailed());
			MainFinanceWindow.getViewManager().showError(
					Accounter.getFinanceUIConstants().assetApdationFailed());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
	}

	private ClientFixedAsset getAssetObject() {
		ClientFixedAsset asset;
		if (fixedAsset != null)
			asset = fixedAsset;
		else
			asset = new ClientFixedAsset();

		asset.setName(itemTxt.getValue() != null ? itemTxt.getValue()
				.toString() : "");
		asset.setAssetNumber(assetNumberTxt.getValue() != null ? assetNumberTxt
				.getValue().toString() : "");
		if (selectedAssetAccount != null) {
			if (accumulatedDepreciationAccount != null) {
				asset.setLinkedAccumulatedDepreciationAccount(accumulatedDepreciationAccount
						.getSelectedValue() != null ? accumulatedDepreciationAccount
						.getSelectedValue().getID() : 0);
			}
			asset.setAssetAccount(selectedAssetAccount.getID());

		}
		asset.setPurchaseDate(purchaseDateTxt.getEnteredDate().getTime());
		asset.setPurchasePrice(purchasePriceTxt.getAmount());
		asset.setDescription(descriptionTxtArea.getValue() != null ? descriptionTxtArea
				.getValue().toString() : "");

		asset.setAssetType(assetType.getValue() != null ? assetType.getValue()
				.toString() : "");
		asset.setDepreciationRate(depreciationRate.getPercentage() != null ? depreciationRate
				.getPercentage() : 0.0);
		asset.setDepreciationMethod(depreciationMethod.getSelectedValue());
		asset.setDepreciationExpenseAccount(depreciationAccount
				.getSelectedValue() != null ? depreciationAccount
				.getSelectedValue().getID() : 0);

		// if (isAssetAccumulated || fixedAsset != null
		// && accmulatdDepreciationTxt != null) {
		// asset.setAccumulatedDepreciationAmount(accmulatdDepreciationTxt
		// .getAmount());
		// }

		if (isAssetAccumulated)
			asset.setAccumulatedDepreciationAmount(accmulatdDepreciationTxt
					.getAmount());
		else
			asset.setAccumulatedDepreciationAmount(0.0);

		/* while registering the asset from viewmode or updating a registeritem */
		if ((isRegister && fixedAsset != null)
				|| (fixedAsset != null && fixedAsset.getStatus() == ClientFixedAsset.STATUS_REGISTERED)) {
			asset.setStatus(ClientFixedAsset.STATUS_REGISTERED);
		} else if (isRegister) {
			/* while creating a registeritem */
			asset.setStatus(ClientFixedAsset.STATUS_REGISTERED);
		} else {
			/* while updating/creating a pending item */
			asset.setStatus(ClientFixedAsset.STATUS_PENDING);
		}

		return asset;
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
		itmNameForm.getCellFormatter().getElement(0, 0)
				.setAttribute("width", assetNumberWidth + "");
	}

	/*
	 * If "Save" button clicked, only itemname & assetnumber fields are
	 * mandatory. If "Register" button clicked,all the fileds are mandatory
	 */
	@Override
	public boolean validate() throws InvalidEntryException,
			InvalidTransactionEntryException {
		if (!isVerified
				&& validateName(itemTxt.getValue() != null ? itemTxt.getValue()
						.toString() : "")) {
			throw new InvalidEntryException(AccounterErrorType.ALREADYEXIST);
		}

		if (isRegister) {
			assetNumberTxt.setRequired(true);
			accountCombo.setRequired(true);
			purchaseDateTxt.setRequired(true);
			purchasePriceTxt.setRequired(true);
			assetType.setRequired(true);
			depreciationRate.setRequired(true);
			depreciationMethod.setRequired(true);
			depreciationAccount.setRequired(true);
			if (acumulatedDeprcForm != null) {
				accmulatdDepreciationTxt.setRequired(true);
			}
			if (accumulatedDepreciationAccountForm != null) {
				accumulatedDepreciationAccount.setRequired(true);
			}

			if (AccounterValidator.validateForm(itmNameForm, false)
					&& AccounterValidator.validateForm(itemInfoForm, false)
					&& AccounterValidator.validateForm(purchaseInfoForm, false)
					&& AccounterValidator.validateForm(assetTypeForm, false)
					&& AccounterValidator.validateForm(depreciationForm, false)
					&& (isAssetAccumulated ? AccounterValidator.validateForm(
							acumulatedDeprcForm, false) : true)
					&& (isAccumltd ? AccounterValidator.validateForm(
							accumulatedDepreciationAccountForm, false) : true)) {
				double price = purchasePriceTxt.getAmount();
				if (DecimalUtil.isEquals(price, 0.0)) {
					throw new InvalidEntryException(Accounter
							.getFixedAssetConstants()
							.purchasePricShouldNotBeZero());

				}

				if (!AccounterValidator.validatePurchaseDate(purchaseDateTxt
						.getEnteredDate())) {
					throw new InvalidTransactionEntryException(
							AccounterErrorType.invalidPurchaseDate);
				}

				/*
				 * avoiding the invoking of validate()
				 * twice(becoz,validationcount=2)
				 */
				validationCount = 0;
				return true;
			}
		} else {
			switch (validationCount) {
			case 4:
				return AccounterValidator.validateForm(itmNameForm, false);
			case 3:
				if (assetNumberTxt.getValue().toString().length() != 0)
					return AccounterValidator.isNull(assetNumberTxt.getValue());
				else {
					throw new InvalidEntryException(Accounter
							.getFixedAssetConstants()
							.assetNumberShouldNotBeEmpty());

				}
			case 2:
				return AccounterValidator.validatePurchaseDate(purchaseDateTxt
						.getEnteredDate());

			case 1:
				if (accountCombo != null
						&& accumulatedDepreciationAccount != null)
					return validateAccount();
			default:
				return true;
			}
		}
		if (!AccounterValidator
				.isFixedAssetPurchaseDateWithinRange(purchaseDateTxt
						.getEnteredDate())) {
			throw new InvalidEntryException(
					AccounterErrorType.PURHASEDATESUDBEWITHINFISCALYEARRANGE);
		}
		return true;
	}

	private boolean validateName(String name) {
		if (!(fixedAsset == null
				&& Utility.isObjectExist(getCompany().getFixedAssets(), name) ? true
				: false)
				|| !(fixedAsset != null && !(fixedAsset.getName()
						.equalsIgnoreCase(name) ? true
						: (Utility.isObjectExist(getCompany().getFixedAssets(),
								name) ? false : true)))) {
			isVerified = true;
			return false;
		}
		return true;

	}

	@Override
	public void setData(ClientFixedAsset data) {
		super.setData(data);
		if (data != null)
			fixedAsset = data;
		else
			fixedAsset = null;
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
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.depreciationAccount.addComboItem((ClientAccount) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.accumulatedDepreciationAccount
						.addComboItem((ClientAccount) core);

		case AccounterCommand.DELETION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.depreciationAccount.removeComboItem((ClientAccount) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.accumulatedDepreciationAccount
						.removeComboItem((ClientAccount) core);

			break;

		case AccounterCommand.UPDATION_SUCCESS:
			break;
		}
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return Accounter.getActionsConstants().newFixedAsset();
	}
}
