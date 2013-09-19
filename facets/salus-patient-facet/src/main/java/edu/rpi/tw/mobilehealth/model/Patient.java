package edu.rpi.tw.mobilehealth.model;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

import static edu.rpi.tw.escience.semanteco.util.QueryResourceUtils.DC_NS;

public class Patient {

    private final Resource patient;

    public Patient(Resource patient) {
        this.patient = patient;
    }

    public String getUri() {
        return patient.getURI();
    }

    public String getId() {
        try {
            Property dcId = patient.getModel().getProperty(DC_NS + "identifier");
            Statement stmt = patient.getProperty(dcId);
            return stmt.getLiteral().getString();
        } catch(NullPointerException e) {
            return "(unknown: " + patient.getURI() + ")";
        }
    }
}
