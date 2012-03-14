package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.PriceLevelListDialog;
import com.vimukti.accounter.web.client.ui.StyledPanel;
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
	private final PriceLevelListDialog parent;

	public AddPriceLevelDialog(PriceLevelListDialog parent, String title,
			String desc) {
		super(title, desc);
		this.getElement().setId("AddPriceLevelDialog");
		initiliase();
		this.parent = parent;
		center();
	}

	private void initiliase() {

		levelText = new TextItem(messages.priceLevel(), "levelText");
		levelText.setRequired(true);
		percentText = new PercentageField(this, messages.percentage());
		percentText.setPercentage(1.0);
		percentText.setRequired(true);
		levelRadio = new RadioGroupItem();
		levelRadio.setTitle(" ");
		levelRadio.setValues(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setIncrOrDecrPercentValue(levelRadio.getValue().toString());
			}
		}, messages.decreasePriceLevelByThisPercentage(),
				messages.increasePriceLevelByThisPercentage());
		levelRadio.setDefaultValue(messages
				.increasePriceLevelByThisPercentage());
		setIncrOrDecrPercentValue(messages.increasePriceLevelByThisPercentage());
		nameDescForm = new DynamicForm("nameDescForm");
		nameDescForm.add(levelText, percentText, levelRadio);
		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(nameDescForm);

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

	@Override
	protected boolean onCancel() {
		return true;
	}

}
