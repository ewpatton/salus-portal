package edu.rpi.tw.mobilehealth.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties extends Properties {

    /**
     * 
     */
    private static final long serialVersionUID = 6477322386635906724L;
    protected static final AppProperties PROPS = new AppProperties();

    protected AppProperties() {
        
    }

    protected void doLoad(InputStream propStream) throws IOException {
        super.load( propStream );
    }

    @Override
    public void load( InputStream propStream ) throws IOException {
        throw new IllegalStateException( "AppProperties should be initialized "
                + "via the static init() method.");
    }

    public static void init(InputStream propStream) throws IOException {
        PROPS.doLoad( propStream );
    }

    public static AppProperties get() {
        return PROPS;
    }

}
