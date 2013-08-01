package com.willhaley.android.mobiledoppler.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ImageView;

import com.willhaley.android.mobiledoppler.R;
import com.willhaley.android.mobiledoppler.model.RadarSite;

import java.net.MalformedURLException;
import java.net.URL;

public class RadarSiteActivity extends Activity {

    private URL topographyURL;
    private URL weatherURL;
    private URL countiesURL;
    private URL citiesURL;

    private static final String TOPOGRAPHY_PATTERN = "http://radar.weather.gov/Overlays/Topo/Short/SITE_Topo_Short.jpg";
    private static final String WEATHER_PATTERN = "http://radar.weather.gov/RadarImg/N0R/SITE_N0R_0.gif";
    private static final String COUNTY_PATTERN = "http://radar.weather.gov/Overlays/County/Short/SITE_County_Short.gif";
    private static final String CITY_PATTERN = "http://radar.weather.gov/Overlays/Cities/Short/SITE_City_Short.gif";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_site);

        RadarSite radarSite = (RadarSite) getIntent().getSerializableExtra("RadarSite");
        loadImagesForSiteId(radarSite.siteId);
    }

    private void loadImagesForSiteId(String siteId) {
        try {
            // Topography
            topographyURL = new URL(TOPOGRAPHY_PATTERN.replace("SITE", siteId));
            new ImageDownloadAsyncTask(
                    (ImageView) findViewById(R.id.topographyImageView),
                    topographyURL).execute();

            // Weather radar images
            weatherURL = new URL(WEATHER_PATTERN.replace("SITE", siteId));
            new ImageDownloadAsyncTask(
                    (ImageView) findViewById(R.id.weatherImageView),
                    weatherURL).execute();

            // County borders
            countiesURL = new URL(COUNTY_PATTERN.replace("SITE", siteId));
            new ImageDownloadAsyncTask(
                    (ImageView) findViewById(R.id.countyImageView),
                    countiesURL).execute();

            // City names
            citiesURL = new URL(CITY_PATTERN.replace("SITE", siteId));
            new ImageDownloadAsyncTask(
                    (ImageView) findViewById(R.id.cityImageView),
                    citiesURL).execute();
        } catch(MalformedURLException exception) {

        }
    }

    class ImageDownloadAsyncTask extends AsyncTask<Void, Bitmap, Bitmap> {
        private ImageView imageView;
        private URL url;

        public ImageDownloadAsyncTask(ImageView imageView, URL url) {
            this.imageView = imageView;
            this.url = url;
        }
        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmap = null;
            try {
                if(url == null) {
                    throw new Exception("Missing url");
                }
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch(Exception exception) {

            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(imageView == null) {
                return;
            }
            imageView.setImageBitmap(bitmap);
        }
    }
}
