package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.company.AddPriceLevelDialog;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

/**
 * 
 * @author Mandeep Singh
 * 
 */
public class PriceLevelListDialog extends GroupDialog<ClientPriceLevel> {

	protected GroupDialogButtonsHandler groupDialogButtonHandler;

	// private TextItem levelText;
	// private PercentageField percentText;
	// private RadioGroupItem levelRadio;
	ClientPriceLevel priceLevel;
	private List<ClientPriceLevel> prList;
	private AddPriceLevelDialog dialog;

	public PriceLevelListDialog(String title, String descript) {

		super(title, descript);
		// setSize("400", "370");
		setWidth("400");
		initialise();
		center();

	}

	public void initialise() {
		getGrid().setType(AccounterCoreType.PRICE_LEVEL);
		getGrid().addRecordClickHandler(new GridRecordClickHandler() {

			@Override
			public boolean onRecordClick(IsSerializable core, int column) {
				ClientPriceLevel clientPriceLevel = (ClientPriceLevel) core;
				if (clientPriceLevel != null)
					enableEditRemoveButtons(true);
				else
					enableEditRemoveButtons(false);

				return false;
			}

		});

		groupDialogButtonHandler = new GroupDialogButtonsHandler() {

			public void onCloseButtonClick() {

			}

			public void onFirstButtonClick() {
				showAddEditPriceLevel(null);
			}

			public void onSecondButtonClick() {
				showAddEditPriceLevel((ClientPriceLevel) listGridView
						.getSelection());
			}

			public void onThirdButtonClick() {
				deleteObject(getSelectedPriceLevelMethod());
				if (prList == null)
					enableEditRemoveButtons(false);

			}
		};
		addGroupButtonsHandler(groupDialogButtonHandler);
	}

	public ClientPriceLevel getSelectedPriceLevelMethod() {

		return (ClientPriceLevel) listGridView.getSelection();
	}

	public void showAddEditPriceLevel(ClientPriceLevel rec) {
		dialog = new AddPriceLevelDialog(FinanceApplication
				.getFinanceUIConstants().priceLevel(), "");
		priceLevel = rec;
		if (priceLevel != null) {

			dialog.levelText.setValue(priceLevel.getName());
			dialog.percentText.setPercentage(priceLevel.getPercentage());

			String increaseOrDecrease = priceLevel
					.isPriceLevelDecreaseByThisPercentage() ? FinanceApplication
					.getFinanceUIConstants().decreasePriceLevelByThisPerc()
					: FinanceApplication.getFinanceUIConstants()
							.increasePriceLevelByThisPerc();

			dialog.levelRadio.setValue(increaseOrDecrease);
			dialog.setIncrOrDecrPercentValue(increaseOrDecrease);

		}

		dialog.addInputDialogHandler(new InputDialogHandler() {

			public void onCancelClick() {

			}

			public boolean onOkClick() {
				if (dialog.nameDescForm.validate()) {
					if (priceLevel != null) {
						editPriceLevels();
					} else
						createPriceLevels();
				} else {
					Accounter.showError(FinanceApplication
							.getCustomersMessages()
							.detailsHighlightedInRedMustBeEntered());
					return false;
				}
				return true;
			}

		});
		dialog.show();

	}

	protected void editPriceLevels() {
		if (!(priceLevel.getName().equalsIgnoreCase(
				UIUtils.toStr(dialog.levelText.getValue().toString())) ? true
				: (Utility.isObjectExist(company.getPriceLevels(), UIUtils
						.toStr(dialog.levelText.getValue().toString()))) ? false
						: true)) {
			Accounter.showError(AccounterErrorType.ALREADYEXIST);
		} else {
			priceLevel
					.setName(dialog.levelText.getValue() != null ? dialog.levelText
							.getValue().toString()
							: "");
			priceLevel
					.setPercentage(dialog.percentText.getPercentage() != null ? dialog.percentText
							.getPercentage()
							: 0.0);
			String val = dialog.getIncrOrDecrPercentValue();
			if (val != null) {
				priceLevel.setPriceLevelDecreaseByThisPercentage(val
						.equals(FinanceApplication.getFinanceUIConstants()
								.decreasePriceLevelByThisPerc()));
			}
			alterObject(priceLevel);
		}
	}

	private void createPriceLevels() {
		if (Utility.isObjectExist(FinanceApplication.getCompany()
				.getPriceLevels(), dialog.levelText.getValue().toString())) {
			Accounter.showError("PriceLevel  Already Exists");
		} else {
			ClientPriceLevel priceLevel = new ClientPriceLevel();

			priceLevel
					.setName(dialog.levelText.getValue() != null ? dialog.levelText
							.getValue().toString()
							: "");
			priceLevel
					.setPercentage(dialog.percentText.getPercentage() != null ? dialog.percentText
							.getPercentage()
							: 0.0);
			String val = dialog.getIncrOrDecrPercentValue();
			if (val != null) {
				priceLevel.setPriceLevelDecreaseByThisPercentage(val
						.equals(FinanceApplication.getFinanceUIConstants()
								.decreasePriceLevelByThisPerc()));
			}
			createObject(priceLevel);
		}
	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		ClientPriceLevel priceLevel = (ClientPriceLevel) obj;
		if (priceLevel != null) {
			switch (index) {
			case 0:
				return priceLevel.getName();
			case 1:
				return priceLevel.getPercentage();
			}
		}
		return null;
	}

	@Override
	public String[] setColumns() {
		return new String[] {
				FinanceApplication.getFinanceUIConstants().Name(),
				FinanceApplication.getFinanceUIConstants().percentage() };
	}

	@Override
	protected List<ClientPriceLevel> getRecords() {
		return FinanceApplication.getCompany().getPriceLevels();
	}
}
