package com.vimukti.accounter.web.client.ui.fixedassets;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * @author B.Srinivasa Rao
 */
public class DisposingRegisteredItemView extends SellingRegisteredItemView {
	private DateField dateDisposed;
	private ArrayList<DynamicForm> listforms;

	public DisposingRegisteredItemView() {
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
		dateDisposed = new DateField(messages.dateDisposed());
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
		saveOrUpdate(disposingAsset);
	}

	@Override
	protected void okClicked() {
		createDisposingObject();
	}

	@Override
	public void saveAndUpdateView() {
		getJournalViewObjet(getPreparedTempAssetObject());
	}

	@Override
	public ValidationResult validate() {
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
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

}
