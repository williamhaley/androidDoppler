package com.willhaley.android.mobiledoppler.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by will on 8/7/13.
 */
public class RawResourceUnserializer {
    public static String getJSONStringForInputStream(InputStream inputStream) {
        BufferedReader streamReader = null;
        try {
            streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        } catch (UnsupportedEncodingException exception) {

        }
        StringBuilder responseStringBuilder = new StringBuilder();

        String inputString;
        try {
            while ((inputString = streamReader.readLine()) != null) {
                responseStringBuilder.append(inputString);
            };
        } catch(Exception exception) {

        }
        return responseStringBuilder.toString();
    }
}
