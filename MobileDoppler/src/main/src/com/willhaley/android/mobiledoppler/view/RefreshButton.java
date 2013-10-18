package com.willhaley.android.mobiledoppler.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class RefreshButton extends View {

	private Paint paint;

	public RefreshButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setColor(Color.BLUE);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// MUST do this if we implement onMeasure or get an exception
		this.setMeasuredDimension(100, 100);
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawCircle(50, 50, 50, paint);
		paint.setColor(Color.BLACK);
		canvas.drawLine(0, 0, 100, 100, paint);
	}

}
