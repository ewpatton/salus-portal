package edu.rpi.tw.mobilehealth.model;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResIterator;

import edu.rpi.tw.escience.semanteco.ModuleConfiguration;
import edu.rpi.tw.escience.semanteco.Request;
import edu.rpi.tw.escience.semanteco.query.Query;
import edu.rpi.tw.escience.semanteco.query.Query.Type;
import edu.rpi.tw.mobilehealth.util.HealthQueryResourceUtils;
import edu.rpi.tw.mobilehealth.util.HealthQueryVarUtils;
import static edu.rpi.tw.escience.semanteco.query.Query.RDF_NS;
import static edu.rpi.tw.mobilehealth.util.HealthQueryResourceUtils.HEALTH_NS;

public class DataModelBuilder {

    private final ModuleConfiguration config;
    private final Request request;

    public DataModelBuilder(final ModuleConfiguration config,
            final Request request) {
        this.config = config;
        this.request = request;
    }

    public List<Patient> getPatients() {
        return getPatients(ModelFactory.createDefaultModel());
    }

    public List<Patient> getPatients(final Model model) {
        List<Patient> patients = new ArrayList<Patient>();
        final Query query = config.getQueryFactory().newQuery(Type.DESCRIBE);
        final HealthQueryVarUtils vars = new HealthQueryVarUtils(query);
        final HealthQueryResourceUtils res = new HealthQueryResourceUtils(query);
        query.addPattern(vars.uri(), res.rdfType(), res.healthPatient());
        config.getQueryExecutor(request).accept("text/turtle").execute(query, model);
        ResIterator i = 
                model.listSubjectsWithProperty(model.getProperty(RDF_NS+"type"),
                        model.getResource(HEALTH_NS+"Patient"));
        while(i.hasNext()) {
            patients.add(new Patient(i.next()));
        }
        return patients;
    }
}
