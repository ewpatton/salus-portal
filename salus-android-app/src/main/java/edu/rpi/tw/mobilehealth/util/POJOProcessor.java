package edu.rpi.tw.mobilehealth.util;

import java.util.List;

import org.json.JSONArray;

public class POJOProcessor {

    public static <T> void process( final JSONArray response,
            final SemantEcoModuleCaller.Callback<List<T>> callback,
            final Class<? extends T> clazz) {
        callback.onError("Not yet implemented", new UnsupportedOperationException());
    }

}
