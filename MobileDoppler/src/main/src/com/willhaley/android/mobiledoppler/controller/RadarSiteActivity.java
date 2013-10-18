package com.willhaley.android.mobiledoppler.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.willhaley.android.mobiledoppler.Application;
import com.willhaley.android.mobiledoppler.R;
import com.willhaley.android.mobiledoppler.model.RadarSite;
import com.willhaley.android.mobiledoppler.view.ScrollView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RadarSiteActivity extends Activity {

	private URL topographyURL;
	private URL weatherURL;
	private URL countiesURL;
	private URL citiesURL;

	private String siteId;

	private static final String TOPOGRAPHY_PATTERN = "http://radar.weather.gov/Overlays/Topo/Short/SITE_Topo_Short.jpg";
	private static final String WEATHER_PATTERN = "http://radar.weather.gov/RadarImg/N0R/SITE_N0R_0.gif";
	private static final String COUNTY_PATTERN = "http://radar.weather.gov/Overlays/County/Short/SITE_County_Short.gif";
	private static final String CITY_PATTERN = "http://radar.weather.gov/Overlays/Cities/Short/SITE_City_Short.gif";

	private static final int IMAGE_WIDTH = 600;
	private static final int IMAGE_HEIGHT = 550;
	private static final float IMAGE_SCALE = 1.5f;
	private static Point imageViewDimensions;

	private ScrollView scrollView;
	private Context context;

	private ArrayList<ImageView> imageViews;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radar_site);

		context = this;

		imageViewDimensions = new Point();
		// Images are 600x550, increasing those dimensions so it's easier to read more detail
		// I wish NOAA had some higher res images...
		imageViewDimensions.set(
				(int)(IMAGE_WIDTH * IMAGE_SCALE),
				(int)(IMAGE_HEIGHT * IMAGE_SCALE));

		RadarSite radarSite = (RadarSite) getIntent().getSerializableExtra("RadarSite");
		siteId = radarSite.siteId;
		
		imageViews = new ArrayList<ImageView>();
		loadImages();

		scrollView = (ScrollView)findViewById(R.id.scrollView);
		Application.getGaTracker().set(Fields.SCREEN_NAME, String.format("%s/%s", "Radar Site", radarSite.siteId));
		((TextView)findViewById(R.id.title)).setText(radarSite.area);
	}

	@Override
	public void onStart() {
		super.onStart();
		Application.getGaTracker().send(MapBuilder.createAppView().build());
	}

	private void loadImages() {
		// No guarantee regarding what order these image will load
		try {
			// Topography
			topographyURL = new URL(TOPOGRAPHY_PATTERN.replace("SITE", siteId));
			new ImageDownloadAsyncTask(
					topographyURL).execute();

			// Weather radar images
			weatherURL = new URL(WEATHER_PATTERN.replace("SITE", siteId));
			new ImageDownloadAsyncTask(
					weatherURL).execute();

			// County borders
			countiesURL = new URL(COUNTY_PATTERN.replace("SITE", siteId));
			new ImageDownloadAsyncTask(
					countiesURL).execute();

			// City names
			citiesURL = new URL(CITY_PATTERN.replace("SITE", siteId));
			new ImageDownloadAsyncTask(
					citiesURL).execute();
		} catch(MalformedURLException exception) {

		}
	}

	class ImageDownloadAsyncTask extends AsyncTask<Void, Bitmap, Bitmap> {
		private URL url;

		public ImageDownloadAsyncTask(URL url) {
			this.url = url;
		}
		@Override
		protected Bitmap doInBackground(Void... voids) {
			Bitmap bitmap = null;
			try {
				if(url == null) {
					return null;
				}
				bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			} catch(Exception exception) {

			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			ImageView imageView = new ImageView(context);
			// Moments like this make Android's relatively large (compared to iOS) resolutions
			// and wide range of resolutions annoying.  If I could rely on 320x480 I'd know what my user's will
			// see everywhere.  With Android, I'll just cross my fingers that this imageView doesn't
			// look macro/microscopic on some devices.
			imageView.setLayoutParams(new ViewGroup.LayoutParams(
					imageViewDimensions.x,
					imageViewDimensions.y
					));
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			imageView.setImageBitmap(bitmap);
			scrollView.addView(imageView);
			imageViews.add(imageView);
			// Once any image has loaded, hide the progress bar
			findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
			// And stop the animation on the refresh button if it's running
			findViewById(R.id.refresh).clearAnimation();
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

	public void refresh(View view) {
		for(ImageView imageView : imageViews) {
			((ViewGroup)imageView.getParent()).removeView(imageView);
		}
		imageViews.clear();
		view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_indefinitely));
		findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
		loadImages();
	}
}
