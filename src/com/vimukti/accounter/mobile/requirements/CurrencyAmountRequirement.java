package com.vimukti.accounter.mobile.requirements;

public abstract class CurrencyAmountRequirement extends AmountRequirement {

	public CurrencyAmountRequirement(String requirementName,
			String displayString, String recordName, boolean isOptional2,
			boolean isAllowFromContext2) {
		super(requirementName, displayString, recordName, isOptional2,
				isAllowFromContext2);
	}

	@Override
	public String getRecordName() {
		String formalName;
		if (getPreferences().isEnableMultiCurrency()) {
			formalName = getFormalName();
		} else {
			formalName = getPreferences().getPrimaryCurrency().getFormalName();
		}
		return super.getRecordName() + "(" + formalName + ")";
	}

	protected abstract String getFormalName();
}
