package edu.rpi.tw.mobilehealth.util;

import org.junit.Test;

import edu.rpi.tw.escience.semanteco.query.Query;
import edu.rpi.tw.escience.semanteco.query.Query.Type;
import edu.rpi.tw.escience.semanteco.test.TestQuery;
import junit.framework.TestCase;

public class HealthQueryVarUtilsTestCase extends TestCase {

    @Test
    public void test() {
        TestQuery query = new TestQuery(Type.SELECT);
        HealthQueryVarUtils vars = new HealthQueryVarUtils(query);
        assertFalse(query.hasVariable(Query.VAR_NS+"patientId"));
        vars.patientId();
        assertTrue(query.hasVariable(Query.VAR_NS+"patientId"));
    }

}
