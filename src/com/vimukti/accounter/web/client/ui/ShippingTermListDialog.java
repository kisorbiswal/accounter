package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.core.InputDialog;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

/**
 * 
 * @author Mandeep Singh
 * 
 */
public class ShippingTermListDialog extends GroupDialog<ClientShippingTerms> {
	TextItem methodText, descText;
	boolean isClose;
	ClientShippingTerms shippingTerm;
	private GroupDialogButtonsHandler dialogButtonsHandler;
	List<ClientShippingTerms> shippingTerms;
	private InputDialog inputDlg;

	public ShippingTermListDialog(String title, String descript) {
		super(title, descript);
		this.getElement().setId("ShippingTermListDialog");
		// setSize("400", "330");
		// setWidth("400px");
		initialise();
		center();
	}

	private void initialise() {
		getGrid().setType(AccounterCoreType.SHIPPING_TERM);
		getGrid().setCellsWidth(new Integer[] { 125, 175 });
		getGrid().addRecordClickHandler(
				new GridRecordClickHandler<ClientShippingTerms>() {

					@Override
					public boolean onRecordClick(ClientShippingTerms core,
							int column) {
						ClientShippingTerms clientShippingTerms = (ClientShippingTerms) core;
						if (clientShippingTerms != null)
							enableEditRemoveButtons(true);
						else
							enableEditRemoveButtons(false);

						return true;
					}

				});

		dialogButtonsHandler = new GroupDialogButtonsHandler() {

			public void onCloseButtonClick() {
			}

			public void onFirstButtonClick() {
				showAddEditTermDialog(null);
			}

			public void onSecondButtonClick() {
				showAddEditTermDialog((ClientShippingTerms) listGridView
						.getSelection());

			}

			public void onThirdButtonClick() {
				deleteObject(getSelectedShippingTerms());
				if (shippingTerms == null)
					enableEditRemoveButtons(false);
			}

		};
		addGroupButtonsHandler(dialogButtonsHandler);
		this.okbtn.setVisible(false);
	}

	public void createShippingTerms() {
		ClientShippingTerms shippingTerm = new ClientShippingTerms();
		shippingTerm.setName(inputDlg.getTextValueByIndex(0));
		shippingTerm.setDescription(inputDlg.getTextValueByIndex(1));
		saveOrUpdate(shippingTerm);
	}

	public ClientShippingTerms getSelectedShippingTerms() {
		return (ClientShippingTerms) listGridView.getSelection();
	}

	private void EditShippingTerms() {

		shippingTerm.setName(inputDlg.getTextValueByIndex(0));
		shippingTerm.setDescription(inputDlg.getTextValueByIndex(1));
		saveOrUpdate(shippingTerm);

	}

	public void showAddEditTermDialog(ClientShippingTerms rec) {
		String arr[] = new String[2];
		arr[0] = messages.shippingTerm();
		arr[1] = messages.description();
		inputDlg = new InputDialog(this, messages.shippingTerm(), "", arr) {
		};
		inputDlg.getTextItems().get(1).setRequired(false);

		shippingTerm = rec;

		if (shippingTerm != null) {
			inputDlg.setTextItemValue(0, shippingTerm.getName());
			inputDlg.setTextItemValue(1, shippingTerm.getDescription());
		}

		inputDlg.show();
	}

	@Override
	public Object getGridColumnValue(ClientShippingTerms shippingTerms,
			int index) {
		if (shippingTerms != null) {
			switch (index) {
			case 0:
				return shippingTerms.getName();
			case 1:
				return shippingTerms.getDescription();
			}
		}
		return null;
	}

	@Override
	public String[] setColumns() {
		return new String[] { messages.name(), messages.description() };
	}

	@Override
	public String getHeaderStyle(int index) {
		switch (index) {
		case 0:
			return "name";
		case 1:
			return "description";
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
			return "descriptionValue";
		default:
			break;

		}
		return "";
	}

	@Override
	protected List<ClientShippingTerms> getRecords() {
		return getCompany().getShippingTerms();
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (inputDlg != null) {
			String termName = inputDlg.getTextValueByIndex(0).toString();
			ClientShippingTerms shippingTermByName = getCompany()
					.getShippingTermByName(termName);
			if (shippingTerm != null) {
				if (!(shippingTerm.getName().equalsIgnoreCase(termName) ? true
						: shippingTermByName == null)) {
					result.addError(this, messages.alreadyExist());
				}
			} else {
				if (shippingTermByName != null) {
					result.addError(this, messages.shippingTermAlreadyExists());
				}
			}
		}

		return result;
	}

	@Override
	protected boolean onOK() {
		if (inputDlg != null) {
			if (shippingTerm != null) {
				EditShippingTerms();
				inputDlg = null;
			} else {
				createShippingTerms();
				inputDlg = null;
			}

		}
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
