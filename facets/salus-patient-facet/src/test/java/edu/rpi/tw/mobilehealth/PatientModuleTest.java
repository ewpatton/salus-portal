package edu.rpi.tw.mobilehealth;

import java.net.URI;
import java.util.List;

import org.junit.Test;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;

import edu.rpi.tw.escience.semanteco.Domain;
import edu.rpi.tw.escience.semanteco.Resource;
import edu.rpi.tw.escience.semanteco.query.Query;
import edu.rpi.tw.escience.semanteco.test.TestModuleConfiguration;
import edu.rpi.tw.escience.semanteco.test.TestRequest;
import edu.rpi.tw.escience.semanteco.test.TestUI;
import edu.rpi.tw.mobilehealth.PatientModule;
import junit.framework.TestCase;

public class PatientModuleTest extends TestCase {
	
	@Test
	public void testVisitModel() {
		PatientModule module = new PatientModule();
		TestModuleConfiguration config = new TestModuleConfiguration();
		module.setModuleConfiguration(config);
		module.visit((Model)null, null, null);
	}
	
	@Test
	public void testVisitOntModel() {
		PatientModule module = new PatientModule();
		TestModuleConfiguration config = new TestModuleConfiguration();
		module.setModuleConfiguration(config);
		module.visit((OntModel)null, null, null);
	}
	
	@Test
	public void testVisitQuery() {
		PatientModule module = new PatientModule();
		TestModuleConfiguration config = new TestModuleConfiguration();
		module.setModuleConfiguration(config);
		module.visit((Query)null, null);
	}
	
	@Test
	public void testVisitUI() {
		PatientModule module = new PatientModule();
		TestModuleConfiguration config = new TestModuleConfiguration();
		module.setModuleConfiguration(config);
		module.visit(new TestUI(), null);
	}
	
	@Test
	public void testProperties() {
		PatientModule module = new PatientModule();
		assertNotNull(module.getName());
		assertFalse(module.getName().equals(""));
		assertEquals(1, module.getMajorVersion());
		assertEquals(0, module.getMinorVersion());
		assertNull(module.getExtraVersion());
	}

	@Test
	public void testDomains() {
	    TestModuleConfiguration config = new TestModuleConfiguration() {
	        /**
             * 
             */
            private static final long serialVersionUID = -640275327933036903L;

            @Override
	        public Domain getDomain(URI uri, boolean create) {
	            return new Domain() {

	                String label;

                    @Override
                    public URI getUri() {
                        return null;
                    }

                    @Override
                    public void addSource(URI sourceUri, String label) {
                    }

                    @Override
                    public void addDataType(String id, String name,
                            Resource icon) {
                    }

                    @Override
                    public void addRegulation(URI regulationUri, String label) {
                    }

                    @Override
                    public List<URI> getSources() {
                        return null;
                    }

                    @Override
                    public List<URI> getRegulations() {
                        return null;
                    }

                    @Override
                    public List<String> getDataTypes() {
                        return null;
                    }

                    @Override
                    public String getDataTypeName(String id) {
                        return null;
                    }

                    @Override
                    public Resource getDataTypeIcon(String id) {
                        return null;
                    }

                    @Override
                    public String getLabelForSource(URI uri) {
                        return null;
                    }

                    @Override
                    public String getLabel() {
                        return label;
                    }

                    @Override
                    public void setLabel(String label) {
                        this.label = label;
                    }

                    @Override
                    public String getLabelForRegulation(URI uri) {
                        return null;
                    }
	                
	            };
	        }
	    };
	    TestRequest request = new TestRequest();
	    PatientModule module = new PatientModule();
	    module.setModuleConfiguration(config);
	    List<Domain> domains = module.getDomains(request);
	    assertEquals(1, domains.size());
	    Domain d = domains.get(0);
	    assertNotNull(d.getLabel());
	}
}
