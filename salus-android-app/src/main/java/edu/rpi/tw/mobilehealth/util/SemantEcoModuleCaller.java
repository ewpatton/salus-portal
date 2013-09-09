package edu.rpi.tw.mobilehealth.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SemantEcoModuleCaller {

    public static interface Callback<T> {
        void onSuccess( T results );
        void onError( String error, Exception ex );
    }

    protected SemantEcoModuleCaller() {
    }

    protected static class HTTPRunner<T> implements Runnable {

        private static final String TAG = HTTPRunner.class.getSimpleName();
        private final String module;
        private final String method;
        private final Map<String, Object> params;
        private final Callback<List<T>> callback;
        private final Class<? extends T> clazz;

        protected HTTPRunner(String module, String method,
                Map<String, Object> params, Callback<List<T>> callback,
                Class<? extends T> clazz) {
            this.module = module;
            this.method = method;
            this.params = new HashMap<String, Object>(params);
            this.callback = callback;
            this.clazz = clazz;
        }

        public void run() {
            StringBuffer sb = new StringBuffer();
            sb.append( (String) AppProperties.get().get( "service.url" ) )
                .append( "rest/" )
                .append( module )
                .append( "/" )
                .append( method );
            if ( params.size() > 0 ) {
                sb.append( "?" );
                for ( Entry<String, Object> param : params.entrySet() ) {
                    sb.append( param.getKey() );
                    sb.append( "=" );
                    try {
                        sb.append( URLEncoder.encode( param.getValue()
                                .toString(), "UTF-8") );
                    } catch (UnsupportedEncodingException e) {
                        callback.onError( "Exception generating service URL "
                                + "while encoding value of param '" + 
                                param.getKey(), e );
                        return;
                    }
                }
            }
            doHTTPRequest( sb.toString() );
        }

        protected void doHTTPRequest( String targetUrl ) {
            try {
                final int SIZE = 4096;
                URL url = new URL( targetUrl );
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod( "GET" );
                conn.setDoInput( true );
                conn.setRequestProperty("Accept", "application/sparql-results+json, application/json, text/plain");
                conn.connect();
                String contentType = conn.getContentType();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[SIZE];
                int read = 0;
                InputStream is = conn.getInputStream();
                read = is.read( buffer );
                while ( read > 0 ) {
                    baos.write( buffer, 0, read );
                    read = is.read( buffer );
                }
                String content = baos.toString( "UTF-8" ).trim();
                conn.disconnect();
                if ( contentType != null && contentType.equals( "application/sparql-results+json" ) ) {
                    
                } else {
                    if ( content.charAt( 0 ) == '{' ) {
                        JSONObject obj = new JSONObject( content );
                        SPARQLResultsProcessor.process( obj, callback, clazz );
                    } else if ( content.charAt( 0 ) == '[' ) {
                        JSONArray arr = new JSONArray( content );
                        //SPARQLResultsProcessor.process( arr, callback, clazz );
                    } else if ( content.charAt( 0 ) == '\"' ) {
                        
                    }
                }
            } catch (MalformedURLException e) {
                Log.w( TAG, "Invalid URL: " + targetUrl, e );
                callback.onError( "Invalid request URL", e );
            } catch (IOException e) {
                Log.w( TAG, "Unable to open URL: " + targetUrl, e );
                callback.onError( "Unable to open URL", e );
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static <T> void call(final String module, final String method,
            final Map<String, Object> params,
            final Callback<List<T>> callback,
            final Class<? extends T> clazz) {
        final Runnable runner = new HTTPRunner<T>( module, method, params,
                callback, clazz );
        final Thread helperThread = new Thread( runner );
        helperThread.start();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <K, V> Class<Map<K, V>> specializedMapClass(Class<K> clazz1,
            Class<V> clazz2) {
        return (Class<Map<K, V>>)(Class)Map.class;
    }
}
