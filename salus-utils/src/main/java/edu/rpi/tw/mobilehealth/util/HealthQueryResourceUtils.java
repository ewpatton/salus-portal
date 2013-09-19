package edu.rpi.tw.mobilehealth.util;

import edu.rpi.tw.escience.semanteco.query.Query;
import edu.rpi.tw.escience.semanteco.query.QueryResource;
import edu.rpi.tw.escience.semanteco.util.QueryResourceUtils;

public class HealthQueryResourceUtils extends QueryResourceUtils {

    public static final String HEALTH_NS = "http://mobilehealth.tw.rpi.edu/ontology/health.ttl#";

    public HealthQueryResourceUtils(Query query) {
        super(query);
    }

    public QueryResource healthPatient() {
        return getResource(HEALTH_NS, "Patient");
    }
}
