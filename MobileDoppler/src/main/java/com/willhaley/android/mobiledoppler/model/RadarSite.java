package com.willhaley.android.mobiledoppler.model;

import java.io.Serializable;

/**
 * Created by will on 7/31/13.
 */
public class RadarSite implements Serializable, Comparable {
    public String siteId;
    public String area;
    public String territory;

    @Override
    public int compareTo(Object object) {
        if(!(object instanceof RadarSite)) {
            return 1;
        }
        RadarSite radarSite = (RadarSite)object;
        return this.area.compareTo(radarSite.area);
    }
}