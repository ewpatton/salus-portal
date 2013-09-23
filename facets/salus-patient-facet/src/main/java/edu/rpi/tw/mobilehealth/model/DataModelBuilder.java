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
import edu.rpi.tw.escience.semanteco.query.QueryResource;
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

        // switch to describe once the data are compatible with Jena
        final Query query = config.getQueryFactory().newQuery(Type.CONSTRUCT);
        final HealthQueryVarUtils vars = new HealthQueryVarUtils(query);
        final HealthQueryResourceUtils res = new HealthQueryResourceUtils(query);
        query.addPattern(vars.uri(), res.rdfType(), res.healthPatient());
        query.addPattern(vars.uri(), res.dcIdentifier(), vars.id());
        config.getQueryExecutor(request).accept("text/turtle").execute(query, model);
        ResIterator i = 
                model.listSubjectsWithProperty(model.getProperty(RDF_NS+"type"),
                        model.getResource(HEALTH_NS+"Patient"));
        while(i.hasNext()) {
            patients.add(new Patient(i.next()));
        }
        return patients;
    }

    public void loadPatientData(final String patientUri, final Model model) {
        final Query query = config.getQueryFactory().newQuery(Type.CONSTRUCT);
        final HealthQueryVarUtils vars = new HealthQueryVarUtils(query);
        final HealthQueryResourceUtils res = new HealthQueryResourceUtils(query);
        final QueryResource patient = query.getResource(patientUri);
        query.addPattern(patient, res.healthHasSample(), vars.sample());
        query.addPattern(vars.sample(), res.healthHasMeasurement(), vars.measurement());
        query.addPattern(vars.measurement(), res.healthHasValue(), vars.value());
        query.addPattern(vars.measurement(), res.healthOfCharacteristic(), vars.characteristic());
        query.addPattern(vars.measurement(), res.healthHasUnit(), vars.unit());
        query.addPattern(vars.measurement(), res.dcDate(), vars.date());
    }
}
