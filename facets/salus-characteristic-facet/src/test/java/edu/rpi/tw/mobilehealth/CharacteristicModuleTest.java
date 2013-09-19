package edu.rpi.tw.mobilehealth;

import org.junit.Test;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;

import edu.rpi.tw.escience.semanteco.query.Query;
import edu.rpi.tw.escience.semanteco.test.TestModuleConfiguration;
import edu.rpi.tw.escience.semanteco.test.TestQueryExecutor;
import edu.rpi.tw.escience.semanteco.test.TestRequest;
import edu.rpi.tw.escience.semanteco.test.TestUI;
import edu.rpi.tw.mobilehealth.CharacteristicModule;
import junit.framework.TestCase;

public class CharacteristicModuleTest extends TestCase {
	
	@Test
	public void testVisitModel() {
		CharacteristicModule module = new CharacteristicModule();
		TestModuleConfiguration config = new TestModuleConfiguration();
		module.setModuleConfiguration(config);
		module.visit((Model)null, null, null);
	}
	
	@Test
	public void testVisitOntModel() {
		CharacteristicModule module = new CharacteristicModule();
		TestModuleConfiguration config = new TestModuleConfiguration();
		module.setModuleConfiguration(config);
		module.visit((OntModel)null, null, null);
	}
	
	@Test
	public void testVisitQuery() {
		CharacteristicModule module = new CharacteristicModule();
		TestModuleConfiguration config = new TestModuleConfiguration();
		module.setModuleConfiguration(config);
		//module.visit((Query)null, null);
	}
	
	@Test
	public void testVisitUI() {
		CharacteristicModule module = new CharacteristicModule();
        TestUI ui = new TestUI();
		TestModuleConfiguration config = new TestModuleConfiguration();
        TestRequest request = new TestRequest();
		TestQueryExecutor executor = (TestQueryExecutor)config.getQueryExecutor(request);
		executor.setDefault("endpoint", "https://mobilehealth.tw.rpi.edu/sparql");
		executor.expect("Content-Type", "application/json");
		executor.expect("endpoint", "https://mobilehealth.tw.rpi.edu/sparql");
		executor.expect("query", "testVisitUI.rq");
		executor.andReturn("testVisitUI.json");
		module.setModuleConfiguration(config);
		module.visit(ui, request);
	}
	
	@Test
	public void testProperties() {
		CharacteristicModule module = new CharacteristicModule();
		assertNotNull(module.getName());
		assertFalse(module.getName().equals(""));
		assertEquals(1, module.getMajorVersion());
		assertEquals(0, module.getMinorVersion());
		assertNull(module.getExtraVersion());
	}

}
