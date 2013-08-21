package edu.rpi.tw.mobilehealth;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;

import edu.rpi.tw.escience.semanteco.query.Query;
import edu.rpi.tw.escience.semanteco.SemantEcoUI;
import edu.rpi.tw.escience.semanteco.test.TestModuleConfiguration;
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
		module.visit((SemantEcoUI)null, null);
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

}
