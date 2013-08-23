package edu.rpi.tw.mobilehealth.util;

import edu.rpi.tw.escience.semanteco.query.Query;
import edu.rpi.tw.escience.semanteco.query.Variable;
import edu.rpi.tw.escience.semanteco.util.QueryVariableUtils;

/**
 * Utilities for referncing commonly used variables in queries against the
 * health ontology used by Salus.
 * 
 * @author ewpatton
 * 
 */
public class HealthQueryVarUtils extends QueryVariableUtils {

	public HealthQueryVarUtils(Query query) {
		super(query);
	}

	public final Variable patientId() {
		return getVariable("patientId");
	}

}
