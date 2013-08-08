package com.willhaley.android.mobiledoppler.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by will on 8/7/13.
 */
public class RadarSiteFactory {
    public static RadarSite createRadarSiteWithJSON(JSONObject jsonObject) {
        RadarSite radarSite = new RadarSite();
        try {
            radarSite.siteId = jsonObject.getString("siteId");
            radarSite.area = jsonObject.getString("area");
            radarSite.territory = jsonObject.getString("territory");
        } catch (JSONException e) {

        }
        return radarSite;
    }
}
