package com.vimukti.accounter.web.client.ui.forms;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;

public abstract class FormItem<T> {

	private String title;
	private String name;
	private T value;
	private List<Validator> validator = new ArrayList<Validator>();
	private boolean required;
	private Label label;

	protected boolean isDisabled = false;
	private boolean showTitle = true;
	private T defaultValue;
	private int columnSpan = 1;
	private boolean isHighlighted = false;
	private String titleStyleName;
	private boolean ishelp = false;

	public T getValue() {
		return this.value;
	}

	public String getTitle() {
		return title;
	}

	public boolean isShowTitle() {
		return showTitle;
	}

	public String getDisplayValue() {
		return this.value.toString();

	}

	public void setColSpan(int colSpan) {
		this.columnSpan = colSpan;
	}

	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}

	public void highlight() {
		getMainWidget().addStyleName("highlightedFormItem");
		// getMainWidget().setTitle(Accounter.constants().invalidValue());
		this.isHighlighted = true;

	}

	public boolean isHighLighted() {
		return isHighlighted;
	}

	public String getValueField() {
		return this.name;
	}

	public void setTitle(String string) {
		this.title = string;
		if (label != null) {
			label.setText(this.title);
		}
	}

	public void setWidth(int width) {
		getMainWidget().setWidth(new Integer(width).toString() + "%");

	}

	public void setWidth(String width) {
		getMainWidget().setWidth(width);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public void setValidators(Validator validate) {
		this.getValidator().add(validate);
	}

	public void setDisabled(boolean b) {
		this.isDisabled = b;

	}

	public void setRequired(boolean required) {
		this.required = required;
		if (required) {
			this.getValidator().add(new RequiredFieldValidator());
		} else {
			this.getValidator().clear();
			removeLabelStar();
		}
	}

	private void removeLabelStar() {
		if (label == null)
			return;
		else {
			Node parentNode = null;
			if (label.getElement().hasChildNodes())
				parentNode = label.getElement().getFirstChild().getParentNode();
			if (parentNode != null) {
				parentNode.removeChild(label.getElement().getChild(1));
			}
		}
	}

	public void show() {
		getLabelWidget().setVisible(true);
		getMainWidget().setVisible(true);
	}

	public Label getLabelWidget() {
		return this.label;
	}

	public void hide() {
		getLabelWidget().setVisible(false);
		getMainWidget().setVisible(false);
	}

	public void addClickHandler(ClickHandler handler) {
		// getMainWid/get().addClickHandler(handler);

	}// TODO Auto-generated method stub

	public void setDefaultValue(T value) {
		defaultValue = value;
	}

	public void setToolTip(String toolTip) {

	}

	public boolean getDisabled() {
		return isDisabled;
	}

	public String getName() {
		return this.name;

	}

	void addWidgets(DynamicForm parent) {

		addLabelWidget(parent);
		if (ishelp) {
			addHelpImageWidget(parent);
		} else {
			parent.add(getMainWidget(), columnSpan);
		}
	}

	private void addHelpImageWidget(DynamicForm parent) {
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(getMainWidget());
		Image helpImg = new Image(Accounter.getFinanceImages().helpIcon());
		helpImg.getElement().getStyle().setCursor(Cursor.POINTER);
		helpImg.addMouseUpHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {
				displayHelpMessage(event);
			}
		});
		hPanel.add(helpImg);
		hPanel.setCellVerticalAlignment(helpImg,
				HasVerticalAlignment.ALIGN_MIDDLE);
		hPanel.setCellHorizontalAlignment(helpImg,
				HasHorizontalAlignment.ALIGN_RIGHT);
		parent.add(hPanel, columnSpan);
		// Image helpImg = new Image("/images/icons/help.png");
		// parent.add(helpImg,0);

	}

	public void setHelpInformation(Boolean isHelp) {
		this.ishelp = false;
	}

	public abstract Widget getMainWidget();

	private void addLabelWidget(DynamicForm parent) {
		if (showTitle) {
			label = new Label(this.title);
			label.addStyleName("NoWrapping");
			if (this.titleStyleName != null && !this.titleStyleName.equals(""))
				label.addStyleName(titleStyleName);
			if (required) {
				label.addStyleName("requiredField");
				Element ele = DOM.createSpan();
				ele.setInnerText("*");
				ele.addClassName("star");
				DOM.appendChild(label.getElement(), ele);
			}
			parent.add(label, 1);
		}
	}

	public void setVisible(boolean visible) {
		getMainWidget().setVisible(visible);
		if (label != null)
			label.setVisible(visible);
	}

	public void setHeight(int height) {
		getMainWidget().setHeight(new Integer(height).toString());
	}

	public void addChangeHandler(ChangeHandler changeHandler) {

	}

	public void addChangedHandler(ChangeHandler changeHandler) {

	}

	public void addFocusHandler(FocusHandler focusHandler) {

	}

	public void addBlurHandler(BlurHandler blurHandler) {

	}

	public void addKeyBoardHandler(KeyPressHandler keyPressHandler) {

	}

	public List<Validator> getValidator() {
		return validator;
	}

	public void setValidator(List<Validator> validator) {
		this.validator = validator;
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public boolean validate() {
		if (this.getValidator() != null) {
			int matchingStatus = 0;
			for (Validator validator : this.getValidator()) {

				if (validator.validate(this)) {
					matchingStatus++;
				}

			}
			if (matchingStatus == this.getValidator().size()) {
				return true;

			} else
				highlight();

			return false;

		}
		return true;

	}

	public void showValidated() {

		getMainWidget().removeStyleName("highlightedFormItem");
		getMainWidget().setTitle("");

	}

	public void setTitleStyleName(String titleStyleName) {
		this.titleStyleName = titleStyleName;

	}

	public void setType(String type) {

	}

	public String getType() {

		return null;
	}

	public void setAttribute(String attribute, String value) {

	}

	public void setAttribute(String attribute, Object value) {

	}

	/**
	 * call this method to set focus to Item
	 */
	public void setFocus() {
		if (this.getMainWidget() instanceof FocusWidget) {
			((FocusWidget) this.getMainWidget()).setFocus(true);
		}
	}

	public String helpMessage = Accounter.constants().help();
	public PopupPanel popupPanel;

	public void displayHelpMessage(MouseUpEvent event) {
		popupPanel = new PopupPanel(true);
		Widget source = (Widget) event.getSource();
		int x = source.getAbsoluteLeft() + 10;
		int y = source.getAbsoluteTop() + 10;
		popupPanel.setHeight("100px");
		popupPanel.setWidth("100px");
		popupPanel.setAutoHideEnabled(true);
		popupPanel.add(helpContent());
		popupPanel.setPopupPosition(x, y);
		popupPanel.show();

	}

	private HTML helpContent() {
		HTML content;
		content = new HTML(Accounter.messages().aboutThisFieldHelp());
		return content;
	}

	public static ValidationResult validate(FormItem<?>... items) {
		ValidationResult result = new ValidationResult();
		for (FormItem<?> item : items) {
			if (!item.validate()) {
				result.addError(item, Accounter.messages().pleaseEnter(
						item.getTitle()));
			}
		}
		return result;
	}

}
