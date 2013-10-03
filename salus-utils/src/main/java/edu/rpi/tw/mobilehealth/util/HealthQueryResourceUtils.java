package edu.rpi.tw.mobilehealth.util;

import edu.rpi.tw.escience.semanteco.query.Query;
import edu.rpi.tw.escience.semanteco.query.QueryResource;
import edu.rpi.tw.escience.semanteco.util.QueryResourceUtils;

public class HealthQueryResourceUtils extends QueryResourceUtils {

    public static final String HEALTH_NS = "http://mobilehealth.tw.rpi.edu/ontology/health.ttl#";
    public static final String OBOE_NS = "http://ecoinformatics.org/oboe/oboe.1.0/oboe-core.owl#";

    public HealthQueryResourceUtils(Query query) {
        super(query);
    }

    public final QueryResource healthPatient() {
        return getResource(HEALTH_NS, "Patient");
    }

    public final QueryResource healthHasSample() {
        return getResource(HEALTH_NS, "hasSample");
    }

    public final QueryResource healthHasMeasurement() {
        return getResource(HEALTH_NS, "hasMeasurement");
    }

    public final QueryResource healthHasValue() {
        return getResource(HEALTH_NS, "hasValue");
    }

    public final QueryResource healthOfCharacteristic() {
        return getResource(HEALTH_NS, "ofCharacteristic");
    }

    public final QueryResource healthHasUnit() {
        return getResource(HEALTH_NS, "hasUnit");
    }

    public final QueryResource healthCharacteristic() {
        return getResource(HEALTH_NS, "HealthCharacteristic");
    }

    public final QueryResource oboeUnit() {
        return getResource(OBOE_NS, "Unit");
    }
}
