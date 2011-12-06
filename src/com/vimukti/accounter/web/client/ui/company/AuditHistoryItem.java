package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.Set;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;

public class AuditHistoryItem extends SimplePanel {

	private final HTML html;

	public AuditHistoryItem(JSONArray jsonArray) {
		this.html = new HTML();
		this.add(html);
		// compareJsonArrays(jsonArray);
		initItem(jsonArray);
	}

	/**
	 * this method creates the html code for the array
	 * 
	 * @param array
	 */
	private void initItem(JSONArray array) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		sb.appendHtmlConstant("<table class='historyItem'>");

		// take each element and add to html
		for (int x = 0; x < array.size();) {
			sb.appendHtmlConstant("<tr>");
			// for two columns of key:values
			for (int y = 0; y < 2 & x < array.size(); y++) {
				JSONValue value = array.get(x);
				JSONObject obj = value.isObject();
				// We will have only one key
				String key = getKey(obj);
				if (key == null) {
					// Empty object is a GAP
					sb.appendHtmlConstant("<td class='space'>&nbsp;</td><td class='space'>&nbsp;</td>");
				} else {
					value = obj.get(key);
					JSONArray list = value.isArray();
					if (list == null) {
						// It is just a key:value paid
						sb.appendHtmlConstant("<td class='historyName'><b>");
						sb.appendEscaped(key);
						sb.appendHtmlConstant("</b></td><td class='historyValue'>");
						sb.appendEscaped(toString(value));
						sb.appendHtmlConstant("</td>");
					} else if (list.size() > 0) {
						// TODO finish the previous unfinished row if any
						if (y == 0) {
							// sb.appendHtmlConstant("<td class='space'>&nbsp;</td><td class='space'>&nbsp;</td>");
						}
						// It is a list of items, we need a table for it
						// sb.appendHtmlConstant("<td colspan='5'><table class='historyItemList'>");
						sb.appendHtmlConstant("<td colspan='5'>");
						ArrayList<String> columns = new ArrayList<String>();
						JSONArray item = list.get(0).isArray();
						if (item == null) {
							// it should be array
							continue;
						}

						for (int z = 0; z < item.size(); z++) {
							String columnName = getKey(item.get(z));
							if (columnName == null) {
								columns.add("");
							} else {
								columns.add(columnName);
							}
						}
						// we got all columns
						// first create column headers
						sb.appendHtmlConstant("<table class='items' border='1px'>");
						for (String columnName : columns) {
							sb.appendHtmlConstant("<td class='historyItemListColumn'>");
							if (columnName.equals("")) {
								sb.appendHtmlConstant("&nbsp;");
							} else {
								sb.appendHtmlConstant("<b>");
								sb.appendEscaped(columnName);
								sb.appendHtmlConstant("</b>");
							}
							sb.appendHtmlConstant("</td>");
						}
						// create each row
						for (int z = 0; z < list.size(); z++) {
							sb.appendHtmlConstant("<tr>");
							item = list.get(z).isArray();
							if (item == null) {
								continue;
							}
							for (int w = 0; w < item.size(); w++) {
								String columnName = columns.get(w);
								JSONObject subObj = item.get(w).isObject();
								if (subObj == null) {
									continue;
								}
								sb.appendHtmlConstant("<td class='historyItemListCell'>");
								if (columnName.equals("")) {
									sb.appendHtmlConstant("&nbsp;");
								} else {
									JSONString string = subObj.get(columnName)
											.isString();
									if (string != null) {
										sb.appendEscaped(string.stringValue());
									} else {
										sb.appendHtmlConstant("&nbsp;");
									}
								}
								sb.appendHtmlConstant("</td>");
							}

							sb.appendHtmlConstant("</tr>");
						}
						sb.appendHtmlConstant("</table></td>");
						// break;
					}
				}
				x++;
				if (y == 0) {
					sb.appendHtmlConstant("<td class='historyGap'>&nbsp;</td>");
				}
			}
			sb.appendHtmlConstant("</tr>");
		}
		sb.appendHtmlConstant("</table>");

		html.setHTML(sb.toSafeHtml());

	}

	private String toString(JSONValue value) {

		if (value.isString() != null) {
			return value.isString().stringValue();
		} else if (value.isNumber() != null) {
			return value.isNumber().toString();
		} else if (value.isObject() != null) {
			return value.isObject().toString();
		} else if (value.isBoolean() != null) {
			return value.isBoolean().toString();
		} else {
			return value.isString().stringValue();
		}
	}

	private String getKey(JSONValue value) {
		JSONObject obj = value.isObject();
		if (obj == null) {
			// We don't know what to do if it is not object
			return null;
		}
		Set<String> keySet = obj.keySet();
		// We will have only one key
		return keySet.size() > 0 ? keySet.iterator().next() : null;
	}

}
