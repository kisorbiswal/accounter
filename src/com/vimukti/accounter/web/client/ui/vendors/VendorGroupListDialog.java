package com.vimukti.accounter.web.client.ui.vendors;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.core.InputDialog;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

/**
 * 
 * @author V.L.Pavani
 * 
 */

public class VendorGroupListDialog extends GroupDialog<ClientVendorGroup> {

	private GroupDialogButtonsHandler dialogButtonsHandler;
	List<ClientVendorGroup> vendorGroups;
	ClientVendorGroup vendorGroup;
	private InputDialog inputDlg;
	private VendorsMessages vendorConstants = GWT.create(VendorsMessages.class);

	public VendorGroupListDialog(String title, String descript) {
		super(title, descript);
		setWidth("400");
		initialise();
		center();
	}

	private void initialise() {
		getGrid().setType(AccounterCoreType.VENDOR_GROUP);
		getGrid().addRecordClickHandler(new GridRecordClickHandler() {

			@Override
			public boolean onRecordClick(IsSerializable core, int column) {
				enableEditRemoveButtons(true);
				return true;
			}

		});

		dialogButtonsHandler = new GroupDialogButtonsHandler() {

			public void onCloseButtonClick() {

			}

			public void onFirstButtonClick() {
				showAddEditGroupDialog(null);
			}

			public void onSecondButtonClick() {
				showAddEditGroupDialog((ClientVendorGroup) getSelectedRecord());

			}

			public void onThirdButtonClick() {
				deleteObject((IAccounterCore) listGridView.getSelection());
				if (vendorGroups == null) {
					enableEditRemoveButtons(false);
				}
			}

		};
		addGroupButtonsHandler(dialogButtonsHandler);
	}

	protected void createVendorGroups() {
		ClientVendorGroup vendorGroup = new ClientVendorGroup();
		vendorGroup.setName(inputDlg.getTextValueByIndex(0));
		if (Utility.isObjectExist(FinanceApplication.getCompany()
				.getVendorGroups(), vendorGroup.getName())) {
			Accounter.showError(UIUtils.getVendorString(
					"Supplier Group Already Exists",
					"Vendor Group Already Exists"));
		} else {
			createObject(vendorGroup);
		}
	}

	public String getSelectedVendorId() {
		if (listGridView.getSelection() != null)
			return ((ClientVendorGroup) listGridView.getSelection())
					.getStringID();
		return null;
	}

	public ClientVendorGroup getSelectedVendor() {
		return (ClientVendorGroup) listGridView.getSelection();
	}

	public void showAddEditGroupDialog(ClientVendorGroup rec) {
		vendorGroup = rec;
		inputDlg = new InputDialog(UIUtils.getVendorString(vendorConstants
				.supplierGroup(), vendorConstants.vendorGroup()), "", UIUtils
				.getVendorString(vendorConstants.supplieRGroup(),
						vendorConstants.vendorGroup())) {
			@Override
			protected String getViewTitle() {
				return UIUtils.getVendorString(vendorConstants.supplierGroup(),
						vendorConstants.vendorGroup());
			}
		};

		if (vendorGroup != null) {
			inputDlg.setTextItemValue(0, vendorGroup.getName());
		}
		InputDialogHandler dialogHandler = new InputDialogHandler() {

			public void onCancelClick() {

			}

			public boolean onOkClick() {
				if (inputDlg.getForm().validate(true)) {
					if (vendorGroup != null) {
						editVendorGroups();
					} else
						createVendorGroups();
				} else {
					// Accounter.showError(FinanceApplication
					// .getCustomersMessages()
					// .detailsHighlightedInRedMustBeEntered());
					return false;
				}
				return true;
			}

		};
		inputDlg.addInputDialogHandler(dialogHandler);
		inputDlg.center();
	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		ClientVendorGroup group = (ClientVendorGroup) obj;
		switch (index) {
		case 0:
			if (group != null)
				return group.getName();
		}
		return null;
	}

	protected void editVendorGroups() {

		if (!(vendorGroup.getName().equalsIgnoreCase(
				UIUtils.toStr(inputDlg.getTextItems().get(0).getValue()
						.toString())) ? true : (Utility.isObjectExist(company
				.getVendorGroups(), UIUtils.toStr(inputDlg.getTextItems()
				.get(0).getValue().toString())) ? false : true))) {
			Accounter.showError(AccounterErrorType.ALREADYEXIST);
		} else {
			vendorGroup.setName(inputDlg.getTextValueByIndex(0));
			alterObject(vendorGroup);
		}

	}

	@Override
	public String[] setColumns() {
		return new String[] { FinanceApplication.getVendorsMessages().name() };
	}

	@Override
	protected List<ClientVendorGroup> getRecords() {
		return FinanceApplication.getCompany().getVendorGroups();
	}

	@Override
	protected String getViewTitle() {
		return UIUtils.getVendorString(FinanceApplication.getCompanyMessages()
				.manageSupplierGroup(), FinanceApplication.getCompanyMessages()
				.manageVendorGroup());
	}
}
