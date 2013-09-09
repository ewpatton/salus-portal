package edu.rpi.tw.mobilehealth.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

class SPARQLResultsProcessor {

    public static <T> void process( final JSONObject response,
            final SemantEcoModuleCaller.Callback<List<T>> callback,
            final Class<? extends T> clazz) {
        List<T> results = null;
        try {
            if ( Map.class.isAssignableFrom( clazz ) ) {
                results = processAsMap( response, clazz );
                callback.onSuccess( results );
            } else {
                throw new IllegalArgumentException("Reflection not enabled. "
                        + "Use type Map or String for now.");
            }
        } catch(Exception e) {
            callback.onError( "Unable to coerce response into list of type " +
                    clazz.getSimpleName(), e );
        }
    }

    @SuppressWarnings("unchecked")
    protected static <T> List<T> processAsMap( JSONObject response,
            final Class<? extends T> clazz) throws InstantiationException,
            IllegalAccessException {
        List<T> results = new ArrayList<T>();
        JSONArray bindings = getBindings( response );
        for ( int i = 0; i < bindings.length(); i++ ) {
            T obj = null;
            try {
                obj = clazz.newInstance();
            } catch( Exception e ) {
                obj = null;
            }
            if ( obj == null || obj instanceof Map ) {
                Map<String, Object> map = ( obj == null ?
                        new HashMap<String, Object>() : (Map<String, Object>)obj );
                JSONObject binding = bindings.optJSONObject( i );
                Iterator<String> keys = binding.keys();
                while ( keys.hasNext() ) {
                    String key = keys.next();
                    JSONObject info = binding.optJSONObject( key );
                    String value = info.optString( "value" );
                    if ( info.optString( "type" ).equals( "uri" ) ) {
                        map.put( key, URI.create( value ) );
                    } else {
                        map.put( key, value );
                    }
                }
                results.add( (T)map );
            }
        }
        return results;
    }

    protected static JSONArray getBindings( JSONObject response ) {
        return response.optJSONObject("results").optJSONArray("bindings");
    }
}
