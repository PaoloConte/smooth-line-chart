package org.paoloconte.smoothchart;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class SmoothLineChart extends View {
	
	private static final int CHART_COLOR = 0xFF0099CC;
	private static final int CIRCLE_SIZE = 8;
	private static final int STROKE_SIZE = 2;	
	private static final float SMOOTHNESS = 3.0f; // the lower the smoother, but don't go less than 2.0
	
	private final Paint mPaint;
	private final Path mPath;
	private final float mCircleSize;
	private final float mStrokeSize;
	private final float mBorder;
	
	private float[][] mValues;
	private float mMinY;
	private float mMaxY;
	

	public SmoothLineChart(Context context) {
		this(context, null, 0);
	}

	public SmoothLineChart(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SmoothLineChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		float scale = context.getResources().getDisplayMetrics().density;
		
		mCircleSize = scale * CIRCLE_SIZE;
		mStrokeSize = scale * STROKE_SIZE;
		mBorder = mCircleSize;
		
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(mStrokeSize);
		
		mPath = new Path();
	}
	
	public void setData(float[][] values) {
		mValues = values;		
		
		if (values != null && values.length > 0) {
			mMaxY = values[0][1];
			//mMinY = values[0][1];
			for (float[] point : values) {
				if (point[1] > mMaxY) 
					mMaxY = point[1];
				/*if (point[1] < mMinY)
					mMinY = point[1];*/
			}
		}
				
		invalidate();
	}
	
	public void draw(Canvas canvas) {
		super.draw(canvas);
		
		if (mValues == null || mValues.length == 0)
			return;

		int size = mValues.length;
		
		final float height = getMeasuredHeight() - 2*mBorder;	
		final float width = getMeasuredWidth() - 2*mBorder;
		
		final float left = mValues[0][0];
		final float right = mValues[mValues.length-1][0];
		final float dX = (right - left) > 0 ? (right - left) : (2);
		final float dY =  (mMaxY-mMinY) > 0 ? (mMaxY-mMinY) : (2);
		
		
		mPath.reset();

				
		List<float[]> points = new ArrayList<float[]>(size);
		
		for (float[] value : mValues) {
			float x = mBorder + (value[0]-left)*width/dX;
			float y = mBorder + height - (value[1]-mMinY)*height/dY; 
			points.add(new float[]{x,y});		
		}

		
		float lX = 0, lY = 0;
		for (int i=0; i<size; i++) {	
			float[] p = points.get(i);
			
			if (i == 0) {
				mPath.moveTo(p[0], p[1]);
				float[] p1 = points.get(i+1 < size ? i + 1 : i);
				float d1 = (float) Math.sqrt(Math.pow(p1[0] - p[0], 2)+Math.pow(p1[1]-p[1], 2));	// distance between points
				lX = (p1[0]-p[0])/d1/SMOOTHNESS;
				lY = (p1[1]-p[1])/d1/SMOOTHNESS;
			} else {
				// first control point
				float[] p0 = points.get(i-1);
				float d0 = (float) Math.sqrt(Math.pow(p[0] - p0[0], 2)+Math.pow(p[1]-p0[1], 2));
				float x1 = Math.min(p0[0] + lX*d0, (p0[0]+p[0])/2); 	// min is used to avoid going to much right
				float y1 = p0[1] + lY*d0;
	
				// second control point
				float[] p1 = points.get(i+1 < size ? i + 1 : i);
				float d1 = (float) Math.sqrt(Math.pow(p1[0] - p0[0], 2)+Math.pow(p1[1]-p0[1], 2));
				lX = (p1[0]-p0[0])/d1/SMOOTHNESS;
				lY = (p1[1]-p0[1])/d1/SMOOTHNESS;
				float x2 = Math.max(p[0] - lX*d0, (p0[0]+p[0])/2);		 // max is used to avoid going to much left
				float y2 = p[1] - lY*d0;
	
				// add line
				mPath.cubicTo(x1,y1,x2, y2, p[0], p[1]);				
			}		
		}
		

		// draw path
		mPaint.setColor(CHART_COLOR);
		mPaint.setStyle(Style.STROKE);
		canvas.drawPath(mPath, mPaint);
		
		// draw area
		if (size > 0) {
			mPaint.setStyle(Style.FILL);
			mPaint.setColor((CHART_COLOR & 0xFFFFFF) | 0x10000000);
			mPath.lineTo(points.get(size-1)[0], height+mBorder);	
			mPath.lineTo(points.get(0)[0], height+mBorder);	
			mPath.close();
			canvas.drawPath(mPath, mPaint);
		}

		// draw circles
		mPaint.setColor(CHART_COLOR);
		mPaint.setStyle(Style.FILL_AND_STROKE);
		for (float[] point : points) {
			canvas.drawCircle(point[0], point[1], mCircleSize/2, mPaint);
		}
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(Color.WHITE);
		for (float[] point : points) {
			canvas.drawCircle(point[0], point[1], (mCircleSize-mStrokeSize)/2, mPaint);
		}
		
	}
}
