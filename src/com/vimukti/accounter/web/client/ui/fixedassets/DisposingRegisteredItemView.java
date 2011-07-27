package com.vimukti.accounter.web.client.ui.fixedassets;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * @author B.Srinivasa Rao
 */
public class DisposingRegisteredItemView extends SellingRegisteredItemView {
	private DateField dateDisposed;
	private ArrayList<DynamicForm> listforms;

	public DisposingRegisteredItemView() {
		this.validationCount = 3;
	}

	@Override
	public void init() {
		super.init();
	}

	/**
	 * This overwritten method is using for getting DynamicForm of Disposing
	 * View
	 * 
	 * @return
	 */
	@Override
	protected DynamicForm getDetailForm() {
		dateDisposed = new DateField(fixedAssetConstants.dateDisposed());
		dateDisposed.setEnteredDate(new ClientFinanceDate());
		yearValue = String.valueOf(dateDisposed.getYear() + 1900);
		dateDisposed.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				dateSelected();

			}
		});
		DynamicForm detailForm = new DynamicForm();
		detailForm.setFields(dateDisposed);
		return detailForm;
	}

	protected void createDisposingObject() {
		ClientFixedAsset disposingAsset = getSellOrDisposeObject();
		alterObject(disposingAsset);
	}

	@Override
	protected void okClicked() {
		createDisposingObject();
	}

	@Override
	public void saveAndUpdateView() throws Exception {
		getJournalViewObjet(getPreparedTempAssetObject());
	}

	@Override
	public boolean validate() throws InvalidEntryException,
			InvalidTransactionEntryException {
		return super.validate();
	}

	@Override
	protected DateField getSoldorDisposedDateField() {
		return dateDisposed;
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.notesArea.setFocus();
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

}
