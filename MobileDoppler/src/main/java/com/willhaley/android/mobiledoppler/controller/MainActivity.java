package com.willhaley.android.mobiledoppler.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import com.willhaley.android.mobiledoppler.R;
import com.willhaley.android.mobiledoppler.model.RadarSite;
import com.willhaley.android.mobiledoppler.model.RadarSiteFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener, TextWatcher {

    private Context context;
    private RadarSite[] radarSites;
    private RadarSitesAdapter radarSitesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        loadRadarSites();
        validateRadarSites(); //TODO this should probably be broken out to a unit test

        radarSitesAdapter = new RadarSitesAdapter(radarSites);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(radarSitesAdapter);
        listView.setOnItemClickListener(this);
        listView.setTextFilterEnabled(true);

        ((EditText) findViewById(R.id.editText)).addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        radarSitesAdapter.getFilter().filter(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) { }

    private String getJSONStringForResourceId(int resourceId) {
        InputStream inputStream = getResources().openRawResource(resourceId);
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

    /**
     * Ensure that all of the radar sites have required and valid data
     * TODO this should be a test
     */
    private void validateRadarSites() {
        String[] states = loadTerritories();
        for(RadarSite radarSite : radarSites) {
            System.out.println(radarSite.area);
            if(radarSite.area == null) {
                Log.e("Error", "Area missing");
            }
            if(radarSite.siteId == null) {
                Log.e("Error", "site id missing");
            }
            if(radarSite.territory == null) {
                Log.e("Error", "Territory missing");
            }
            int index = java.util.Arrays.asList(states).indexOf(radarSite.territory);
            if(index < 0) {
                Log.e("Error", "Not a valid territory");
            }
        }
    }

    private String[] loadTerritories() {
        JSONArray jsonTerritories = null; //TODO Am I consistent in naming?  jsonVar or varJSON ?
        try {
            jsonTerritories = new JSONArray(getJSONStringForResourceId(R.raw.territories));
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

    private void loadRadarSites() {
        JSONArray jsonRadarSites = null;
        try {
            jsonRadarSites = new JSONArray(getJSONStringForResourceId(R.raw.radar_sites));
        } catch(JSONException exception) {
            exception.printStackTrace();
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
            RadarSite radarSite = RadarSiteFactory.createRadarSiteWithJSON(jsonRadarSite);
            radarSites[index] = radarSite;
        }
        Arrays.sort(radarSites);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        RadarSite radarSite = (RadarSite) radarSitesAdapter.getItem(index);
        Intent intent = new Intent(context, RadarSiteActivity.class);
        intent.putExtra("RadarSite", radarSite);
        startActivity(intent);
    }

    class RadarSitesAdapter extends ArrayAdapter {
        private static final int LAYOUT_ID = R.layout.radar_site_list_item;
        private RadarSite[] radarSites;
        private RadarSite[] filteredRadarSites;
        private RadarSiteFilter filter;

        public RadarSitesAdapter(RadarSite[] radarSites) {
            super(context, LAYOUT_ID, radarSites);
            this.radarSites = radarSites;
            this.filteredRadarSites = this.radarSites.clone();
            this.filter = new RadarSiteFilter();
        }

        @Override
        public int getCount() {
            return filteredRadarSites.length;
        }

        @Override
        public Object getItem(int position) {
            return this.filteredRadarSites[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if(rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(LAYOUT_ID, null);
            }

            RadarSite radarSite = (RadarSite) getItem(position);

            if(radarSite.area != null) {
                ((TextView) rowView.findViewById(R.id.title)).setText(radarSite.area);
            }
            if(radarSite.siteId != null) {
                ((TextView) rowView.findViewById(R.id.subTitle)).setText(radarSite.siteId);
            }
            return rowView;
        }

        @Override
        public Filter getFilter() {
            return this.filter;
        }

        class RadarSiteFilter extends Filter {
            // http://stackoverflow.com/questions/2718202/custom-filtering-in-android-using-arrayadapter
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                // NOTE: this function is *always* called from a background thread, and
                // not the UI thread.
                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();
                if(constraint != null && constraint.toString().length() > 0) {
                    ArrayList<RadarSite> filteredSites = new ArrayList<RadarSite>();
                    for(RadarSite radarSite : radarSites) {
                        if(radarSite.siteId.toLowerCase().contains(constraint)) {
                            filteredSites.add(radarSite);
                        } else if(radarSite.area.toLowerCase().contains(constraint)) {
                            filteredSites.add(radarSite);
                        } else if(radarSite.territory.toLowerCase().contains(constraint)) {
                            filteredSites.add(radarSite);
                        }
                    }
                    result.count = filteredSites.size();
                    result.values = filteredSites.toArray();
                } else {
                    result.count = radarSites.length;
                    result.values = radarSites;
                }
                return result;
            }

            // NOTE: this function is *always* called from the UI thread.
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                Object[] values = (Object[]) filterResults.values;
                filteredRadarSites = new RadarSite[values.length];
                System.arraycopy(values, 0, filteredRadarSites, 0, values.length);
                radarSitesAdapter.notifyDataSetInvalidated();
            }
        }
    }

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
}
