package edu.rpi.tw.mobilehealth;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;

import edu.rpi.tw.escience.semanteco.Domain;
import edu.rpi.tw.escience.semanteco.Module;
import edu.rpi.tw.escience.semanteco.ModuleConfiguration;
import edu.rpi.tw.escience.semanteco.QueryMethod;
import edu.rpi.tw.escience.semanteco.Request;
import edu.rpi.tw.escience.semanteco.SemantEcoUI;
import edu.rpi.tw.escience.semanteco.query.Query;
import edu.rpi.tw.escience.semanteco.query.Query.Type;
import edu.rpi.tw.escience.semanteco.query.QueryResource;
import edu.rpi.tw.mobilehealth.util.HealthQueryVarUtils;

public class PatientModule implements Module {

    private static final String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";
    private ModuleConfiguration config = null;

    @Override
    public void visit(final Model model, final Request request,
            final Domain domain) {
        // TODO populate data model
    }

    @Override
    public void visit(final OntModel model, final Request request,
            final Domain domain) {
        // TODO populate ontology model
    }

    @Override
    public void visit(final Query query, final Request request) {
        // TODO modify queries
    }

    @Override
    public void visit(final SemantEcoUI ui, final Request request) {
        // TODO add resources to display
    }

    @Override
    public String getName() {
        return "Patient";
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
    public String listPatientInformation(final Request request) {
        final Query query = config.getQueryFactory().newQuery(Type.SELECT);
        final HealthQueryVarUtils vars = new HealthQueryVarUtils(query);
        final QueryResource rdfsLabel = query.getResource(RDFS_NS + "label");
        query.addPattern(vars.uri(), rdfsLabel, vars.patientId());
        return null;
    }

    @QueryMethod
    public String listPatientTests(final Request request) {
        return null;
    }

}
