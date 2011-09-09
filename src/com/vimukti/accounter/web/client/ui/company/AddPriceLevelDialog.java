package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.PriceLevelListDialog;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.PercentageField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class AddPriceLevelDialog extends BaseDialog<ClientPriceLevel> {

	public TextItem levelText;
	public PercentageField percentText;
	public RadioGroupItem levelRadio;
	public DynamicForm nameDescForm;
	private String incrOrDecrPercentValue;
	private PriceLevelListDialog parent;

	public AddPriceLevelDialog(PriceLevelListDialog parent, String title,
			String desc) {
		super(title, desc);
		initiliase();
		this.parent = parent;
		center();
	}

	private void initiliase() {

		levelText = new TextItem(Accounter.constants().priceLevel());
		levelText.setHelpInformation(true);
		levelText.setRequired(true);
		percentText = new PercentageField(this, Accounter.constants()
				.percentage());
		percentText.setHelpInformation(true);
		percentText.setPercentage(1.0);
		percentText.setRequired(true);
		levelRadio = new RadioGroupItem();
		levelRadio.setTitle(" ");
		levelRadio.setValues(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setIncrOrDecrPercentValue(levelRadio.getValue().toString());
			}
		}, Accounter.constants().decreasePriceLevelByThisPercentage(),
				Accounter.constants().increasePriceLevelByThisPercentage());
		levelRadio.setDefaultValue(Accounter.constants()
				.increasePriceLevelByThisPercentage());
		setIncrOrDecrPercentValue(Accounter.constants()
				.increasePriceLevelByThisPercentage());
		nameDescForm = new DynamicForm();
		nameDescForm.setFields(levelText, percentText, levelRadio);
		nameDescForm.setSize("100%", "100%");
		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.add(nameDescForm);
		// setSize("450", "250");
		okbtn.setWidth("60px");
		cancelBtn.setWidth("60px");

		setBodyLayout(mainVLay);

	}

	@Override
	public Object getGridColumnValue(ClientPriceLevel obj, int index) {
		return null;
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(AccounterException exception) {

	}

	public void setIncrOrDecrPercentValue(String incrOrDecrPercentValue) {
		this.incrOrDecrPercentValue = incrOrDecrPercentValue;
	}

	public String getIncrOrDecrPercentValue() {
		return incrOrDecrPercentValue;
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = nameDescForm.validate();
		result.add(parent.validate());
		return result;
	}

	@Override
	protected boolean onOK() {
		return parent.onOK();
	}

	@Override
	public void setFocus() {
		levelText.setFocus();

	}

}
