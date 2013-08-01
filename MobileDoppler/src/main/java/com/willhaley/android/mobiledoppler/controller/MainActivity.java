package com.willhaley.android.mobiledoppler.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.willhaley.android.mobiledoppler.R;
import com.willhaley.android.mobiledoppler.model.RadarSite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private Context context;
    private RadarSite[] radarSites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        loadRadarSites();

        RadarSitesAdapter radarSitesAdapter = new RadarSitesAdapter(radarSites);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(radarSitesAdapter);
        listView.setOnItemClickListener(this);
    }

    private void loadRadarSites() {
        InputStream inputStream = getResources().openRawResource(R.raw.radar_sites);
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

        JSONArray jsonRadarSites = null;
        try {
            jsonRadarSites = new JSONArray(responseStringBuilder.toString());
        } catch(JSONException exception) {

        }

        if(jsonRadarSites != null) {
            radarSites = new RadarSite[jsonRadarSites.length()];
        } else {
            radarSites = new RadarSite[0];
        }

        for(int index = 0; index < jsonRadarSites.length(); index++) {
            JSONObject jsonRadarSite = null;
            try {
                jsonRadarSite = jsonRadarSites.getJSONObject(index);
            } catch (JSONException e) {

            }
            RadarSite radarSite = new RadarSite();
            try {
                radarSite.siteId = jsonRadarSite.getString("siteId");
                radarSite.area = jsonRadarSite.getString("area");
            } catch (JSONException e) {

            }
            radarSites[index] = radarSite;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        RadarSite radarSite = radarSites[index];
        Intent intent = new Intent(context, RadarSiteActivity.class);
        intent.putExtra("RadarSite", radarSite);
        startActivity(intent);
    }

    class RadarSitesAdapter extends ArrayAdapter {
        private static final int LAYOUT_ID = R.layout.radar_site_layout;
        private RadarSite[] radarSites;

        public RadarSitesAdapter(RadarSite[] radarSites) {
            super(context, LAYOUT_ID, radarSites);
            this.radarSites = radarSites;
        }

        @Override
        public int getCount() {
            return radarSites.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if(rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(LAYOUT_ID, null);
            }
            RadarSite radarSite = radarSites[position];
            if(radarSite.area != null) {
                ((TextView) rowView.findViewById(R.id.title)).setText(radarSite.area);
            }
            if(radarSite.siteId != null) {
                ((TextView) rowView.findViewById(R.id.subTitle)).setText(radarSite.siteId);
            }
            return rowView;
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
        return super.onMenuItemSelected(featureId, item);
    }
    */
}
