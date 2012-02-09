package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.CustomLabel;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;

/**
 * 
 * @author vimukti10
 * 
 */
public class FormatingConfilctDialog extends BaseDialog {

	private CustomLabel previousHeadingLabel, previousLabel, priviousValue,
			nextLabel, nextValue, nextHeadingLabel;

	private HorizontalPanel prevoiusPanel, nextPanel;

	private String presentheading;

	private String nextSelectIndexValue;

	private String previousSelectedValue;

	private String previpousHeading;

	public FormatingConfilctDialog(String previpousHeading,
			String previousSelectedValue, String presentheading,
			String nextSelectIndexValue) {
		super(messages.formattingConflict(), "");
		this.previousSelectedValue = previousSelectedValue;
		this.previpousHeading = previpousHeading;
		this.presentheading = presentheading;
		this.nextSelectIndexValue = nextSelectIndexValue;
		this.createControls();
		center();
	}

	/**
	 * Creating the body
	 */
	private void createControls() {

		VerticalPanel mainPanel = new VerticalPanel();
		prevoiusPanel = new HorizontalPanel();
		previousHeadingLabel = new CustomLabel(
				messages.alreadyAssigned(previpousHeading));

		previousLabel = new CustomLabel(previpousHeading);
		priviousValue = new CustomLabel(previousSelectedValue);
		prevoiusPanel.add(previousLabel);
		prevoiusPanel.add(priviousValue);

		nextPanel = new HorizontalPanel();
		nextHeadingLabel = new CustomLabel(
				messages.attemptingToreassign(presentheading));
		nextLabel = new CustomLabel(presentheading);
		nextValue = new CustomLabel(nextSelectIndexValue);
		nextPanel.add(nextLabel);
		nextPanel.add(nextValue);

		mainPanel.add(previousHeadingLabel);
		mainPanel.add(prevoiusPanel);
		mainPanel.add(nextHeadingLabel);
		mainPanel.add(nextPanel);
		mainPanel
				.add(new CustomLabel(messages.headingReassign(presentheading)));
		setBodyLayout(mainPanel);
		show();

	}

	@Override
	protected boolean onOK() {

		return true;
	}

	@Override
	protected boolean onCancel() {

		return true;
	}

	@Override
	public void setFocus() {

	}

}
