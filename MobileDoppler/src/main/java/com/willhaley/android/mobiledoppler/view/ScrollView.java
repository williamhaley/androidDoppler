package com.willhaley.android.mobiledoppler.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;

import com.willhaley.android.mobiledoppler.R;

/**
 * Created by will on 8/11/13.
 */
public class ScrollView extends android.widget.ScrollView {
    private ViewGroup container;
    private HorizontalScrollView horizontalScrollView;
    private Activity context;

    public ScrollView(Activity context) {
        super(context);
        this.context = context;

        container = new RelativeLayout(context);
        /*
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        */
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                600,
                550
        );

        container.setLayoutParams(params);

        horizontalScrollView = new HorizontalScrollView(context);
        setLayoutParams(params);
        horizontalScrollView.setLayoutParams(params);
        super.addView(horizontalScrollView);
        horizontalScrollView.addView(container);
    }

    public void printShit() {
        System.out.println("Decor View: " + context.getWindow().getDecorView().getWidth() + " " + context.getWindow().getDecorView().getHeight());
        RelativeLayout r = (RelativeLayout) context.findViewById(R.id.radarSiteLayout);
        System.out.println("Root Layout: " + r.getWidth() + " " + r.getHeight());
        System.out.println("Scroll: " + getWidth() + " " + getHeight());
        System.out.println("Horizontal: " + horizontalScrollView.getWidth() + " " + horizontalScrollView.getHeight());
        System.out.println("Container: " + container.getWidth() + " " + container.getHeight());
    }

    public void addView(View view) {
        container.addView(view);
    }
}
