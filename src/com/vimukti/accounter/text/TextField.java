package com.vimukti.accounter.text;

public @interface TextField {
	FieldType type();

	boolean collection() default false;
}
