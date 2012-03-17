package com.vimukti.accounter.server.imports;

import java.util.Set;

import com.vimukti.accounter.core.Measurement;

public class ImporterUtils {

	public static Measurement getMeasurementByID(Set<Measurement> list,
			long measurmentId) {
		for (Measurement measurement : list) {
			if (measurement.getID() == measurmentId) {
				return measurement;
			}
		}
		return null;
	}
}