package com.vimukti.accounter.taxreturn.vat;

import net.n3.nanoxml.XMLElement;

public class ExtensionPart {
	private ExtensionPartType extensionPartType;

	public ExtensionPartType getExtensionPartType() {
		return extensionPartType;
	}

	public void setExtensionPartType(ExtensionPartType extensionPartType) {
		this.extensionPartType = extensionPartType;
	}

	public void toXML(XMLElement vATDeclarationRequestElement) {
		XMLElement extensionPart = new XMLElement("ExtensionPart");
		vATDeclarationRequestElement.addChild(extensionPart);
		if (extensionPartType != null) {
			extensionPartType.toXML(extensionPartType);
		}
	}
}