package com.vimukti.accounter.web.client.ui.vendors;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.core.InputDialog;
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

	public VendorGroupListDialog() {
		super(UIUtils
				.getVendorString(
						Accounter.messages().manageSupplierGroup(
								Global.get().Vendor()), Accounter.messages()
								.manageVendorGroup(Global.get().vendor())),
				UIUtils.getVendorString(Accounter.messages()
						.toAddSupplierGroup(Global.get().Vendor()), Accounter
						.messages().toAddVendorGroup(Global.get().vendor())));
		setWidth("400px");
		initialise();
		center();
	}

	private void initialise() {
		getGrid().setType(AccounterCoreType.VENDOR_GROUP);
		getGrid().addRecordClickHandler(new GridRecordClickHandler() {

			@Override
			public boolean onRecordClick(IsSerializable core, int column) {
				if (core != null) {
					ClientVendorGroup vendorGroup = (ClientVendorGroup) core;
					if (vendorGroup.isDefault()) {
						enableEditRemoveButtons(false);
					} else {
						enableEditRemoveButtons(true);
					}
				}
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
				if (listGridView != null) {
					ClientVendorGroup selectedGroup = (ClientVendorGroup) listGridView
							.getSelection();
					if (selectedGroup != null && selectedGroup.isDefault()) {
						return;
					}

					deleteObject((IAccounterCore) listGridView.getSelection());
					if (vendorGroups == null) {
						enableEditRemoveButtons(false);
					}
				}

			}

		};
		addGroupButtonsHandler(dialogButtonsHandler);
		this.okbtn.setVisible(false);
	}

	protected void createVendorGroups() {
		ClientVendorGroup vendorGroup = new ClientVendorGroup();
		vendorGroup.setName(inputDlg.getTextValueByIndex(0));
		saveOrUpdate(vendorGroup);
	}

	public long getSelectedVendorId() {
		if (listGridView.getSelection() != null)
			return ((ClientVendorGroup) listGridView.getSelection()).getID();
		return 0;
	}

	public ClientVendorGroup getSelectedVendor() {
		return (ClientVendorGroup) listGridView.getSelection();
	}

	public void showAddEditGroupDialog(ClientVendorGroup rec) {
		vendorGroup = rec;
		inputDlg = new InputDialog(this, UIUtils.getVendorString(Accounter
				.messages().supplierGroup(Global.get().Vendor()), Accounter
				.messages().vendorCredit(Global.get().Vendor())), "",
				UIUtils.getVendorString(
						Accounter.messages().supplierGroup(
								Global.get().Vendor()), Accounter.messages()
								.vendorGroup(Global.get().Vendor()))) {
		};

		if (vendorGroup != null) {
			inputDlg.setTextItemValue(0, vendorGroup.getName());
		}

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

		vendorGroup.setName(inputDlg.getTextValueByIndex(0));
		saveOrUpdate(vendorGroup);

	}

	@Override
	public String[] setColumns() {
		return new String[] { Accounter.constants().name() };
	}

	@Override
	protected List<ClientVendorGroup> getRecords() {
		return getCompany().getVendorGroups();
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (inputDlg != null) {
			String vendorName = inputDlg.getTextItems().get(0).getValue()
					.toString();
			ClientVendor vendorByName = company.getVendorByName(vendorName);
			if (vendorGroup != null) {
				if (!(vendorGroup.getName().equalsIgnoreCase(vendorName) ? true
						: vendorByName == null)) {
					result.addError(this, Accounter.constants().alreadyExist());
				}
			} else {
				if (vendorByName != null) {
					result.addError(this, UIUtils.getVendorString(
							Accounter.messages().supplierGroupAlreadyExists(
									Global.get().vendor()),
							Accounter.messages().vendorGroupAlreadyExists(
									Global.get().Vendor())));
				}
			}
		}
		return result;
	}

	@Override
	protected boolean onOK() {

		if (inputDlg != null) {
			if (vendorGroup != null) {
				editVendorGroups();
				inputDlg = null;
			} else {
				createVendorGroups();
				inputDlg = null;
			}
		}

		return true;
	}
}
