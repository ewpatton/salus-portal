package edu.rpi.tw.mobilehealth;

import java.util.LinkedHashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;

import edu.rpi.tw.escience.semanteco.Domain;
import edu.rpi.tw.escience.semanteco.Module;
import edu.rpi.tw.escience.semanteco.ModuleConfiguration;
import edu.rpi.tw.escience.semanteco.QueryMethod;
import edu.rpi.tw.escience.semanteco.Request;
import edu.rpi.tw.escience.semanteco.SemantEcoUI;
import edu.rpi.tw.escience.semanteco.query.Query;
import edu.rpi.tw.escience.semanteco.query.QueryResource;
import edu.rpi.tw.escience.semanteco.query.Query.Type;
import edu.rpi.tw.escience.semanteco.query.Variable;
import edu.rpi.tw.mobilehealth.util.HealthQueryVarUtils;

public class CharacteristicModule implements Module {

    private static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    private static final String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";
    private static final String HEALTH_NS = "http://mobilehealth.tw.rpi.edu/ontology/health.ttl#";
	private ModuleConfiguration config = null;
	
	@Override
	public void visit(final Model model, final Request request, final Domain domain) {
		// TODO populate data model
	}

	@Override
	public void visit(final OntModel model, final Request request, final Domain domain) {
		// TODO populate ontology model
	}

	@Override
	public void visit(final Query query, final Request request) {
		// TODO modify queries
	}
	
	@Override
	public void visit(final SemantEcoUI ui, final Request request) {
	    StringBuffer result = new StringBuffer("<select name=\"characteristic\">");
        JSONObject object = (JSONObject)JSONValue.parse( listCharacteristics(request) );
        JSONArray bindings = (JSONArray)((JSONObject)object.get("results")).get("bindings");
        for(int i=0; i<bindings.size(); i++) {
            JSONObject binding = (JSONObject)bindings.get(i);
            JSONObject var = (JSONObject)binding.get("characteristic");
            String uri = (String)var.get("value");
            var = (JSONObject)binding.get("label");
            String label = (String)var.get("value");
            result.append("<option value=\"");
            result.append(uri);
            result.append("\">");
            result.append(label);
            result.append("</option>");
        }
        result.append("</select>");
        ui.addFacet(config.generateStringResource(result.toString()));
	}

	@Override
	public String getName() {
		return "Characteristic";
	}

	@Override
	public int getMajorVersion() {
		return 1;
	}

	@Override
	public int getMinorVersion() {
		return 0;
	}

	@Override
	public String getExtraVersion() {
		return null;
	}

	@Override
	public void setModuleConfiguration(final ModuleConfiguration config) {
		this.config = config;
	}

    @QueryMethod
    public String listPatientTests(final Request request) {
        final String patientUri = (String)request.getParam("patient");
        final Query query = config.getQueryFactory().newQuery(Type.SELECT);
        final HealthQueryVarUtils vars = new HealthQueryVarUtils(query);
        query.setNamespace("health", HEALTH_NS);
        Set<Variable> variables = new LinkedHashSet<Variable>();
        if (patientUri == null) {
            variables.add(vars.uri());
        }
        variables.add(vars.characteristic());
        final QueryResource patient = patientUri == null ? vars.uri()
                : query.getResource(patientUri);
        final QueryResource sampleMeasurementCharacteristic =
                query.createPropertyPath("health:hasSample/health:hasMeasurement/health:ofCharacteristic");
        final QueryResource rdfsLabel = query.getResource(RDFS_NS + "label");
        query.addPattern(patient, sampleMeasurementCharacteristic, vars.characteristic());
        query.addPattern(vars.characteristic(), rdfsLabel, vars.label());
        return config.getQueryExecutor(request).accept("application/json")
                .execute(query);
    }

    @QueryMethod
    public String listCharacteristics(final Request request) {
        final Query query = config.getQueryFactory().newQuery(Type.SELECT);
        final QueryResource rdfType = query.getResource(RDF_NS + "type");
        final QueryResource healthBloodCharacteristic =
                query.getResource(HEALTH_NS + "BloodCharacteristic");
        final QueryResource rdfsLabel =
                query.getResource(RDFS_NS + "label");
        final HealthQueryVarUtils vars = new HealthQueryVarUtils(query);
        Set<Variable> variables = new LinkedHashSet<Variable>();
        variables.add(vars.characteristic());
        variables.add(vars.label());
        query.setVariables(variables);
        query.addPattern(vars.characteristic(), rdfType, healthBloodCharacteristic);
        query.addPattern(vars.characteristic(), rdfsLabel, vars.label());
        return config.getQueryExecutor(request).accept("application/json")
                .execute(query);
    }

}
