package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.PercentageField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

@SuppressWarnings("unchecked")
public class AddPriceLevelDialog extends BaseDialog {

	public TextItem levelText;
	public PercentageField percentText;
	public RadioGroupItem levelRadio;
	public DynamicForm nameDescForm;
	private String incrOrDecrPercentValue;

	public AddPriceLevelDialog(String title, String desc) {
		super(title, desc);
		initiliase();
		center();
	}

	private void initiliase() {

		levelText = new TextItem(Accounter.constants().priceLevel());
		levelText.setHelpInformation(true);
		levelText.setRequired(true);
		percentText = new PercentageField(Accounter.constants().percentage());
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
		}, Accounter.constants().decreasePriceLevelPercentage(), Accounter
				.constants().increasePriceLevelPercentage());
		levelRadio.setDefaultValue(Accounter.constants()
				.increasePriceLevelPercentage());
		setIncrOrDecrPercentValue(Accounter.constants()
				.increasePriceLevelPercentage());
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
	public Object getGridColumnValue(IsSerializable obj, int index) {
		return null;
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(Throwable exception) {

	}

	public void setIncrOrDecrPercentValue(String incrOrDecrPercentValue) {
		this.incrOrDecrPercentValue = incrOrDecrPercentValue;
	}

	public String getIncrOrDecrPercentValue() {
		return incrOrDecrPercentValue;
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().priceLevel();
	}

}
