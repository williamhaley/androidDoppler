package com.willhaley.android.mobiledoppler.test;

import android.content.Context;
import android.util.Log;

import com.willhaley.android.mobiledoppler.R;
import com.willhaley.android.mobiledoppler.model.RadarSite;
import com.willhaley.android.mobiledoppler.model.RawResourceUnserializer;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;

/**
 * Created by will on 8/7/13.
 * TODO manually invoke tests from App until this is made into a proper test
 */
public class RadarSitesTest {
    private Context context;

    public RadarSitesTest(Context context) {
        this.context = context;
    }

    /**
     * Ensure that all of the radar sites have required and valid data
     */
    public void validateRadarSites(RadarSite[] radarSites) {
        // arbitrary test.  I know there are more than 5 radar sites
        if(radarSites == null || radarSites.length <= 5) {
            Log.e(this.getClass().toString(), "Radar sites not properly loaded");
        }
        String[] territories = loadTerritories();
        for(RadarSite radarSite : radarSites) {
            if(radarSite.area == null) {
                Log.e(radarSite.toString(), "Area missing");
            }
            if(radarSite.siteId == null) {
                Log.e(radarSite.toString(), "site id missing");
            }
            if(radarSite.territory == null) {
                Log.e(radarSite.toString(), "Territory missing");
            }
            int index = java.util.Arrays.asList(territories).indexOf(radarSite.territory);
            if(index < 0) {
                Log.e(radarSite.toString(), "Not a valid territory");
            }
        }
    }

    /**
     * Ensure our list of territories populated correctly
     */
    public void validateTerritories() {
        String[] territories = loadTerritories();
        // arbitrary test.  I know there are more than 5 territories
        if(territories.length < 5) {
            Log.e(this.getClass().toString(), "Territories not properly loaded");
        }
    }

    private String[] loadTerritories() {
        JSONArray jsonTerritories = null;
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.territories);
            jsonTerritories = new JSONArray(RawResourceUnserializer.getJSONStringForInputStream(inputStream));
        } catch(JSONException exception) {
            exception.printStackTrace();
        }
        String[] states = new String[jsonTerritories.length()];
        for(int index = 0; index < jsonTerritories.length(); index++) {
            try {
                states[index] = jsonTerritories.getString(index);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return states;
    }
}
