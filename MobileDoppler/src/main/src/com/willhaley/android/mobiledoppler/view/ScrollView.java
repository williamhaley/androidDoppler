package com.willhaley.android.mobiledoppler.view;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;

/**
 * Created by will on 8/11/13.
 * This class is a vertical scroll view containing a horizontal scroll view which holds all subviews
 */
public class ScrollView extends android.widget.ScrollView {
    private ViewGroup container;
    private HorizontalScrollView horizontalScrollView;
    private Activity context;

    public ScrollView(Activity context) {
        super(context);
        this.context = context;

        container = new RelativeLayout(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        container.setLayoutParams(params);

        /**
         * rant: the idea that Android can't handle vertical AND horizontal scrolling at once is
         * ridiculous.  I can't believe this ugly hack, this class, is required to create the illusion
         * of scrolling on both axes.
         */
        horizontalScrollView = new HorizontalScrollView(context);
        setLayoutParams(params);
        horizontalScrollView.setLayoutParams(params);
        super.addView(horizontalScrollView);
        horizontalScrollView.addView(container);
    }

    public void addView(View view) {
        container.addView(view);
    }
}
