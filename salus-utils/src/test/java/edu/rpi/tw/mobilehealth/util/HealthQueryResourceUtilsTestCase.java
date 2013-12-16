package edu.rpi.tw.mobilehealth.util;

import org.junit.Test;

import edu.rpi.tw.escience.semanteco.query.Query.Type;
import edu.rpi.tw.escience.semanteco.test.TestQuery;
import junit.framework.TestCase;

public class HealthQueryResourceUtilsTestCase extends TestCase {

    @Test
    public void testExpectedUris() {
        TestQuery query = new TestQuery(Type.SELECT);
        HealthQueryResourceUtils res = new HealthQueryResourceUtils( query );
        assertNotNull( res.healthPatient() );
        assertNotNull( res.healthHasSample() );
        assertNotNull( res.healthHasMeasurement() );
        assertNotNull( res.healthHasValue() );
        assertNotNull( res.healthOfCharacteristic() );
        assertNotNull( res.healthHasUnit() );
        assertNotNull( res.healthCharacteristic() );
        assertNotNull( res.oboeUnit() );
        assertEquals( res.healthPatient().getUri(),
                HealthQueryResourceUtils.HEALTH_NS + "Patient" );
        assertEquals( res.healthHasSample().getUri(),
                HealthQueryResourceUtils.HEALTH_NS + "hasSample" );
        assertEquals( res.healthHasMeasurement().getUri(),
                HealthQueryResourceUtils.HEALTH_NS + "hasMeasurement" );
        assertEquals( res.healthHasValue().getUri(),
                HealthQueryResourceUtils.HEALTH_NS + "hasValue" );
        assertEquals( res.healthOfCharacteristic().getUri(),
                HealthQueryResourceUtils.HEALTH_NS + "ofCharacteristic" );
        assertEquals( res.healthHasUnit().getUri(), 
                HealthQueryResourceUtils.HEALTH_NS + "hasUnit" );
        assertEquals( res.healthCharacteristic().getUri(),
                HealthQueryResourceUtils.HEALTH_NS + "HealthCharacteristic" );
        assertEquals( res.oboeUnit().getUri(),
                HealthQueryResourceUtils.OBOE_NS + "Unit" );
    }

}
