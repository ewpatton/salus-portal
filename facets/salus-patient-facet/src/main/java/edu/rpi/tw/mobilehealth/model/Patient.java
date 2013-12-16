package edu.rpi.tw.mobilehealth.model;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

import static edu.rpi.tw.escience.semanteco.util.QueryResourceUtils.DC_NS;

public class Patient {

    private final Resource patient;

    public Patient(Resource patient) {
        if ( patient == null ) {
            throw new IllegalArgumentException( "Creating a new Patient object "
                    + "requires a non-null resource." );
        }
        this.patient = patient;
    }

    public String getUri() {
        return patient.getURI();
    }

    public String getId() {
        Model model = patient.getModel();
        if ( model == null ) {
            return "(unknown patient: <" + patient.getURI() + ">)";
        }
        Property dcId = model.getProperty(DC_NS + "identifier");
        Statement stmt = patient.getProperty(dcId);
        if ( stmt.getLiteral() == null ) {
            return "(patient with no id: <" + patient.getURI() + ">)";
        }
        return stmt.getLiteral().getString();
    }
}
