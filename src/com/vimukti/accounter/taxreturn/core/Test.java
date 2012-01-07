package com.vimukti.accounter.taxreturn.core;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Test {
	public static void main(String[] args) throws Exception {

		XStream xstream = new XStream(new DomDriver()); // does not require XPP3
														// library

		xstream.alias("GovTalkMessage", GovTalkMessage.class);

		// new PreprocessingConverter(xstream)
		// .Register(new Annotation2AttribListener());

		GovTalkMessage o = new GovTalkMessage();
		String xml = xstream.toXML(o);
		// String xml = XStreamUtil.getXstream().toXML(o);
		System.out.println(o.toXML(true));
	}
}
