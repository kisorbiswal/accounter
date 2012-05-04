package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.company.AddPriceLevelDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
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
		this.getElement().setId("PriceLevelListDialog");
		// setSize("400px", "");
		// setWidth("400px");
		initialise();
		center();

	}

	public void initialise() {
		getGrid().setType(AccounterCoreType.PRICE_LEVEL);
		getGrid().setCellsWidth(new Integer[] { 160, 80 });
		getGrid().addRecordClickHandler(
				new GridRecordClickHandler<ClientPriceLevel>() {

					@Override
					public boolean onRecordClick(ClientPriceLevel core,
							int column) {
						if (core != null)
							enableEditRemoveButtons(true);
						else
							enableEditRemoveButtons(false);

						return true;
					}

				});

		groupDialogButtonHandler = new GroupDialogButtonsHandler() {

			@Override
			public void onCloseButtonClick() {

			}

			@Override
			public void onFirstButtonClick() {
				showAddEditPriceLevel(null);
			}

			@Override
			public void onSecondButtonClick() {
				showAddEditPriceLevel((ClientPriceLevel) listGridView
						.getSelection());
			}

			@Override
			public void onThirdButtonClick() {
				deleteObject(getSelectedPriceLevelMethod());
				if (prList == null)
					enableEditRemoveButtons(false);

			}
		};
		addGroupButtonsHandler(groupDialogButtonHandler);
		this.okbtn.setVisible(false);
	}

	public ClientPriceLevel getSelectedPriceLevelMethod() {

		return (ClientPriceLevel) listGridView.getSelection();
	}

	public void showAddEditPriceLevel(ClientPriceLevel rec) {
		dialog = new AddPriceLevelDialog(this, messages.priceLevel(), "");
		priceLevel = rec;
		if (priceLevel != null) {

			dialog.levelText.setValue(priceLevel.getName());
			dialog.percentText.setPercentage(priceLevel.getPercentage());

			String increaseOrDecrease = priceLevel
					.isPriceLevelDecreaseByThisPercentage() ? messages
					.decreasePriceLevelByThisPercentage() : messages
					.increasePriceLevelByThisPercentage();

			dialog.levelRadio.setValue(increaseOrDecrease);
			dialog.setIncrOrDecrPercentValue(increaseOrDecrease);

		}

		dialog.show();

	}

	protected void editPriceLevels() {
		priceLevel
				.setName(dialog.levelText.getValue() != null ? dialog.levelText
						.getValue().toString() : "");
		priceLevel
				.setPercentage(dialog.percentText.getPercentage() != null ? dialog.percentText
						.getPercentage() : 0.0);
		String val = dialog.getIncrOrDecrPercentValue();
		if (val != null) {
			priceLevel.setPriceLevelDecreaseByThisPercentage(val
					.equals(messages.decreasePriceLevelByThisPercentage()));
		}
		saveOrUpdate(priceLevel);
	}

	private void createPriceLevels() {
		ClientPriceLevel priceLevel = new ClientPriceLevel();

		priceLevel
				.setName(dialog.levelText.getValue() != null ? dialog.levelText
						.getValue().toString() : "");
		priceLevel
				.setPercentage(dialog.percentText.getPercentage() != null ? dialog.percentText
						.getPercentage() : 0.0);
		String val = dialog.getIncrOrDecrPercentValue();
		if (val != null) {
			priceLevel.setPriceLevelDecreaseByThisPercentage(val
					.equals(messages.decreasePriceLevelByThisPercentage()));
		}
		saveOrUpdate(priceLevel);
	}

	@Override
	public Object getGridColumnValue(ClientPriceLevel obj, int index) {
		if (obj != null) {
			switch (index) {
			case 0:
				return obj.getName();
			case 1:
				return DataUtils.getNumberAsPercentString(String.valueOf(obj
						.getPercentage()));
			}
		}
		return null;
	}

	@Override
	public String[] setColumns() {
		return new String[] { messages.name(), messages.percentage() };
	}

	@Override
	public String getHeaderStyle(int index) {
		switch (index) {
		case 0:
			return "name";
		case 1:
			return "percentage";
		default:
			break;

		}
		return "";
	}

	@Override
	public String getRowElementsStyle(int index) {
		switch (index) {
		case 0:
			return "nameValue";
		case 1:
			return "percentageValue";
		default:
			break;

		}
		return "";
	}

	@Override
	protected List<ClientPriceLevel> getRecords() {
		return getCompany().getPriceLevels();
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		if (dialog != null) {
			String name = dialog.levelText.getValue();
			ClientPriceLevel priceLevelByName = company
					.getPriceLevelByName(name);
			if (priceLevel != null) {
				if (!(priceLevel.getName().equalsIgnoreCase(name) ? true
						: priceLevelByName == null)) {
					result.addError(this, messages.alreadyExist());
				}
			} else {
				if (priceLevelByName != null) {
					result.addError(this, messages.priceLevelAlreadyExists());
				}
			}
		}
		return result;
	}

	@Override
	public boolean onOK() {
		if (dialog != null) {
			if (priceLevel != null) {
				editPriceLevels();
				dialog = null;
			} else {
				createPriceLevels();
				dialog = null;
			}
		}

		return true;
	}

	@Override
	public void setFocus() {
	}

}
