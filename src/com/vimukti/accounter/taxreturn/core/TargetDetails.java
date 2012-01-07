package com.vimukti.accounter.taxreturn.core;

import java.util.ArrayList;
import java.util.List;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class TargetDetails {

	/**
	 * 0..8
	 */
	private List<String> organisations = new ArrayList<String>();

	public TargetDetails() {
		getOrganisations().add("Organisation_1");
		getOrganisations().add("Organisation_2");
		getOrganisations().add("Organisation_3");
		getOrganisations().add("Organisation_4");
		getOrganisations().add("Organisation_5");
		getOrganisations().add("Organisation_6");
		getOrganisations().add("Organisation_7");
		getOrganisations().add("Organisation_8");
	}

	public List<String> getOrganisations() {
		return organisations;
	}

	public void setOrganisations(List<String> organisations) {
		this.organisations = organisations;
	}

	public IXMLElement toXML() {
		XMLElement element = new XMLElement("TargetDetails");
		if (organisations != null) {
			for (String organisation : organisations) {
				if (organisation != null) {
					XMLElement organisationElement = new XMLElement(
							"Organisation", organisation);
					element.addChild(organisationElement);
				}
			}
		}
		return element;
	}
}
