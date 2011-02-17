//package com.vimukti.accounter.web.client.ui.widgets.RichTextArea;
//
//import com.google.gwt.user.client.ui.RichTextArea;
//import com.google.gwt.user.client.ui.VerticalPanel;
//
//
//
//public class RichTextEditor extends VerticalPanel {
//	
//	private RichTextArea textBox;
//	private RichTextToolbar toolbar; 
//	
//	public RichTextEditor(){
//		createControls();
//	}
//
//	private void createControls() {
//		
//		textBox = new RichTextArea();
//		textBox.setWidth("100%");
//		toolbar = new RichTextToolbar(textBox);
//		toolbar.setWidth("100%");		
//		add(toolbar);
//		add(textBox);
//		setWidth("100%");
//		setHeight("100px");
//		
//	}
//	
//	/*
//	 * This method allows to set whether textformating toolbar for RichTextArea is visible or not!
//	 */
//	public void showToolBar(boolean isVisible)
//	{
//			toolbar.setVisible(isVisible);
//	}
//	
//	public void setHeight(String height){
//		textBox.setHeight(height);
//	}
//	
//	public void setHTML(String html){
//		textBox.setHTML(html);
//	}
//	
//	public void setText(String text){
//		textBox.setText(text);
//		
//	}
//	
//	public String getText(){
//		return textBox.getText();
//	}
//		
//	public String getHTML() {
//		return textBox.getHTML();
//	}
//}
