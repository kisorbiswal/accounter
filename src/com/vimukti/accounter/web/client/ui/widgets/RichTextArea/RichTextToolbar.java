//package com.vimukti.accounter.web.client.ui.widgets.RichTextArea;
//
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.user.client.Window;
//import com.google.gwt.user.client.ui.AbstractImagePrototype;
//import com.google.gwt.user.client.ui.ChangeListener;
//import com.google.gwt.user.client.ui.ClickListener;
//import com.google.gwt.user.client.ui.Composite;
//import com.google.gwt.user.client.ui.HorizontalPanel;
//import com.google.gwt.user.client.ui.KeyboardListener;
//import com.google.gwt.user.client.ui.ListBox;
//import com.google.gwt.user.client.ui.PushButton;
//import com.google.gwt.user.client.ui.RichTextArea;
//import com.google.gwt.user.client.ui.ToggleButton;
//import com.google.gwt.user.client.ui.VerticalPanel;
//import com.google.gwt.user.client.ui.Widget;
//
//public class RichTextToolbar extends Composite {
//
//	/**
//	 * We use an inner EventListener class to avoid exposing event methods on
//	 * the RichTextToolbar itself.
//	 */
//	private class EventListener implements ClickListener, ChangeListener,
//			KeyboardListener {
//
//		public void onChange(Widget sender) {
//			if (sender == backColors) {
//				basic.setBackColor(backColors.getValue(backColors
//						.getSelectedIndex()));
//				backColors.setSelectedIndex(0);
//			} else if (sender == foreColors) {
//				basic.setForeColor(foreColors.getValue(foreColors
//						.getSelectedIndex()));
//				foreColors.setSelectedIndex(0);
//			} else if (sender == fonts) {
//				basic.setFontName(fonts.getValue(fonts.getSelectedIndex()));
//				fonts.setSelectedIndex(0);
//			} else if (sender == fontSizes) {
//				basic.setFontSize(fontSizesConstants[fontSizes
//						.getSelectedIndex() - 1]);
//				fontSizes.setSelectedIndex(0);
//			}
//		}
//
//		public void onClick(Widget sender) {
//			if (sender == bold) {
//				basic.toggleBold();
//			} else if (sender == italic) {
//				basic.toggleItalic();
//			} else if (sender == underline) {
//				basic.toggleUnderline();
//			} else if (sender == subscript) {
//				basic.toggleSubscript();
//			} else if (sender == superscript) {
//				basic.toggleSuperscript();
//			} else if (sender == strikethrough) {
//				extended.toggleStrikethrough();
//			} else if (sender == indent) {
//				extended.rightIndent();
//			} else if (sender == outdent) {
//				extended.leftIndent();
//			} else if (sender == justifyLeft) {
//				basic.setJustification(RichTextArea.Justification.LEFT);
//			} else if (sender == justifyCenter) {
//				basic.setJustification(RichTextArea.Justification.CENTER);
//			} else if (sender == justifyRight) {
//				basic.setJustification(RichTextArea.Justification.RIGHT);
//			} else if (sender == insertImage) {
//				String url = Window.prompt("Enter an image URL:", "http://");
//				if (url != null) {
//					extended.insertImage(url);
//				}
//			} else if (sender == createLink) {
//				String url = Window.prompt("Enter a link URL:", "http://");
//				if (url != null) {
//					extended.createLink(url);
//				}
//			} else if (sender == removeLink) {
//				extended.removeLink();
//			} else if (sender == hr) {
//				extended.insertHorizontalRule();
//			} else if (sender == ol) {
//				extended.insertOrderedList();
//			} else if (sender == ul) {
//				extended.insertUnorderedList();
//			} else if (sender == removeFormat) {
//				extended.removeFormat();
//			} else if (sender == richText) {
//				// We use the RichTextArea's onKeyUp event to update the toolbar
//				// status.
//				// This will catch any cases where the user moves the cursur
//				// using the
//				// keyboard, or uses one of the browser's built-in keyboard
//				// shortcuts.
//				updateStatus();
//			}
//		}
//
//		public void onKeyDown(Widget sender, char keyCode, int modifiers) {
//		}
//
//		public void onKeyPress(Widget sender, char keyCode, int modifiers) {
//		}
//
//		public void onKeyUp(Widget sender, char keyCode, int modifiers) {
//			if (sender == richText) {
//				// We use the RichTextArea's onKeyUp event to update the toolbar
//				// status.
//				// This will catch any cases where the user moves the cursur
//				// using the
//				// keyboard, or uses one of the browser's built-in keyboard
//				// shortcuts.
//				updateStatus();
//			}
//		}
//	}
//
//	private static final RichTextArea.FontSize[] fontSizesConstants = new RichTextArea.FontSize[] {
//			RichTextArea.FontSize.XX_SMALL, RichTextArea.FontSize.X_SMALL,
//			RichTextArea.FontSize.SMALL, RichTextArea.FontSize.MEDIUM,
//			RichTextArea.FontSize.LARGE, RichTextArea.FontSize.X_LARGE,
//			RichTextArea.FontSize.XX_LARGE };
//
//	private Images images;
////	private Messages messages;
//	private EventListener listener = new EventListener();
//
//	private RichTextArea richText;
//	private RichTextArea.Formatter basic;
//	private RichTextArea.ExtendedFormatter extended;
//
//	private VerticalPanel outer = new VerticalPanel();
//	private HorizontalPanel topPanel = new HorizontalPanel();
//	private HorizontalPanel bottomPanel = new HorizontalPanel();
//	private ToggleButton bold;
//	private ToggleButton italic;
//	private ToggleButton underline;
//	private ToggleButton subscript;
//	private ToggleButton superscript;
//	private ToggleButton strikethrough;
//	private PushButton indent;
//	private PushButton outdent;
//	private PushButton justifyLeft;
//	private PushButton justifyCenter;
//	private PushButton justifyRight;
//	private PushButton hr;
//	private PushButton ol;
//	private PushButton ul;
//	private PushButton insertImage;
//	private PushButton createLink;
//	private PushButton removeLink;
//	private PushButton removeFormat;
//
//	private ListBox backColors;
//	private ListBox foreColors;
//	private ListBox fonts;
//	private ListBox fontSizes;
//
//	// private Messages messages;
//
//	/**
//	 * Creates a new toolbar that drives the given rich text area.
//	 * 
//	 * @param richText
//	 *            the rich text area to be controlled
//	 */
//	@SuppressWarnings("deprecation")
//	public RichTextToolbar(RichTextArea richText) {
//
//		messages = GWT.create(Messages.class);
//		images = GWT.create(Images.class);
//
//		this.richText = richText;
//		this.basic = richText.getFormatter();
//		this.extended = richText.getFormatter();
//
//		outer.add(topPanel);
//		outer.add(bottomPanel);
//		topPanel.setWidth("100%");
//		bottomPanel.setWidth("100%");
//
//		initWidget(outer);
//		setStyleName("gwt-RichTextToolbar");
//
//		if (basic != null) {
//			topPanel.add(bold = createToggleButton(images.bold(), messages
//					.bold()));
//			topPanel.add(italic = createToggleButton(images.italic(), messages
//					.italic()));
//			topPanel.add(underline = createToggleButton(images.underline(),
//					messages.underline()));
//			topPanel.add(subscript = createToggleButton(images.subscript(),
//					messages.subscript()));
//			topPanel.add(superscript = createToggleButton(images.superscript(),
//					messages.superscript()));
//			topPanel.add(justifyLeft = createPushButton(images.justifyLeft(),
//					messages.justifyLeft()));
//			topPanel.add(justifyCenter = createPushButton(images
//					.justifyCenter(), messages.justifyCenter()));
//			topPanel.add(justifyRight = createPushButton(images.justifyRight(),
//					messages.justifyRight()));
//		}
//
//		if (extended != null) {
////			 topPanel.add(strikethrough =
////			 createToggleButton(images.strikeThrough(),
////					 messages.strikeThrough()));
//			topPanel.add(indent = createPushButton(images.indent(), messages
//					.indent()));
//			topPanel.add(outdent = createPushButton(images.outdent(), messages
//					.outdent()));
//			// topPanel.add(hr = createPushButton(images.hr(), strings.hr()));
//			topPanel.add(ol = createPushButton(images.ol(), messages.ol()));
//			topPanel.add(ul = createPushButton(images.ul(), messages.ul()));
//			// topPanel.add(insertImage = createPushButton(images.insertImage(),
//			// strings.insertImage()));
//			topPanel.add(createLink = createPushButton(images.createLink(),
//					messages.createLink()));
//			topPanel.add(removeLink = createPushButton(images.removeLink(),
//					messages.removeLink()));
//			// topPanel.add(removeFormat =
//			// createPushButton(images.removeFormat(),
//			// strings.removeFormat()));
//		}
//
//		if (basic != null) {
//			bottomPanel.add(backColors = createColorList("Background"));
//			bottomPanel.add(foreColors = createColorList("Foreground"));
//			bottomPanel.add(fonts = createFontList());
//			bottomPanel.add(fontSizes = createFontSizes());
//
//			// We only use these listeners for updating status, so don't hook
//			// them up
//			// unless at least basic editing is supported.
//			richText.addKeyboardListener(listener);
//			richText.addClickListener(listener);
//		}
//	}
//
//	private ListBox createColorList(String caption) {
//		ListBox lb = new ListBox();
//		lb.addChangeListener(listener);
//		lb.setVisibleItemCount(1);
//
//		lb.addItem(caption);
//		lb.addItem(messages.white(), "white");
//		lb.addItem(messages.black(), "black");
//		lb.addItem(messages.red(), "red");
//		lb.addItem(messages.green(), "green");
//		lb.addItem(messages.yellow(), "yellow");
//		lb.addItem(messages.blue(), "blue");
//		return lb;
//	}
//
//	private ListBox createFontList() {
//		ListBox lb = new ListBox();
//		lb.addChangeListener(listener);
//		lb.setVisibleItemCount(1);
//
//		lb.addItem(messages.font(), "");
//		lb.addItem(messages.normal(), "");
//		lb.addItem("Times New Roman", "Times New Roman");
//		lb.addItem("Arial", "Arial");
//		lb.addItem("Courier New", "Courier New");
//		lb.addItem("Georgia", "Georgia");
//		lb.addItem("Trebuchet", "Trebuchet");
//		lb.addItem("Verdana", "Verdana");
//		return lb;
//	}
//
//	private ListBox createFontSizes() {
//		ListBox lb = new ListBox();
//		lb.addChangeListener(listener);
//		lb.setVisibleItemCount(1);
//
//		lb.addItem(messages.size());
//		lb.addItem(messages.xxsmall());
//		lb.addItem(messages.xsmall());
//		lb.addItem(messages.small());
//		lb.addItem(messages.medium());
//		lb.addItem(messages.large());
//		lb.addItem(messages.xlarge());
//		lb.addItem(messages.xxlarge());
//		return lb;
//	}
//
//	private PushButton createPushButton(AbstractImagePrototype img, String tip) {
//		PushButton pb = new PushButton(img.createImage());
//		pb.addClickListener(listener);
//		pb.setTitle(tip);
//		return pb;
//	}
//
//	private ToggleButton createToggleButton(AbstractImagePrototype img,
//			String tip) {
//		ToggleButton tb = new ToggleButton(img.createImage());
//		tb.addClickListener(listener);
//		tb.setTitle(tip);
//		return tb;
//	}
//
//	/**
//	 * Updates the status of all the stateful buttons.
//	 */
//	@SuppressWarnings("deprecation")
//	private void updateStatus() {
//		if (basic != null) {
//			bold.setDown(basic.isBold());
//			italic.setDown(basic.isItalic());
//			underline.setDown(basic.isUnderlined());
//			subscript.setDown(basic.isSubscript());
//			superscript.setDown(basic.isSuperscript());
//		}
//
//		if (extended != null) {
////			strikethrough.setDown(extended.isStrikethrough());
//		}
//	}
//}
