package org.paoloconte.smoothchart;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

public class SmoothLineChartEquallySpaced extends View {
	
	private static final int CHART_COLOR = 0xFF0099CC;
	private static final int CIRCLE_SIZE = 8;
	private static final int STROKE_SIZE = 2;	
	private static final float SMOOTHNESS = 0.35f; // the higher the smoother, but don't go over 0.5
	
	private final Paint mPaint;
	private final Path mPath;
	private final float mCircleSize;
	private final float mStrokeSize;
	private final float mBorder;
	
	private float[] mValues;
	private float mMinY;
	private float mMaxY;

	private boolean textCaptionEnable;

	public SmoothLineChartEquallySpaced(Context context) {
		this(context, null, 0);
	}

	public SmoothLineChartEquallySpaced(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SmoothLineChartEquallySpaced(Context context, AttributeSet attrs, int defStyle) {
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
	
	public void setData(float[] values, boolean textCaptionEnable) {
		mValues = values;
		this.textCaptionEnable = textCaptionEnable;
		
		if (values != null && values.length > 0) {
			mMaxY = values[0];
			//mMinY = values[0].y;
			for (float y : values) {
				if (y > mMaxY) 
					mMaxY = y;
				/*if (y < mMinY)
					mMinY = y;*/
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
		
		final float dX = mValues.length > 1 ? mValues.length-1  : (2);	
		final float dY = (mMaxY-mMinY) > 0 ? (mMaxY-mMinY) : (2);
				
		mPath.reset();
				
		// calculate point coordinates
		List<PointF> points = new ArrayList<PointF>(size);		
		for (int i=0; i<size; i++) {
			float x = mBorder + i*width/dX;
			float y = mBorder + height - (mValues[i]-mMinY)*height/dY; 
			points.add(new PointF(x,y));		
		}

		// calculate smooth path
		float lX = 0, lY = 0;
		mPath.moveTo(points.get(0).x, points.get(0).y);
		for (int i=1; i<size; i++) {	
			PointF p = points.get(i);	// current point
			
			// first control point
			PointF p0 = points.get(i-1);	// previous point
			float x1 = p0.x + lX; 	
			float y1 = p0.y + lY;
	
			// second control point
			PointF p1 = points.get(i+1 < size ? i+1 : i);	// next point
			lX = (p1.x-p0.x)/2*SMOOTHNESS;		// (lX,lY) is the slope of the reference line 
			lY = (p1.y-p0.y)/2*SMOOTHNESS;
			float x2 = p.x - lX;	
			float y2 = p.y - lY;

			// add line
			mPath.cubicTo(x1,y1,x2, y2, p.x, p.y);		
		}
		

		// draw path
		mPaint.setColor(CHART_COLOR);
		mPaint.setStyle(Style.STROKE);
		canvas.drawPath(mPath, mPaint);
		
		// draw area
		if (size > 0) {
			mPaint.setStyle(Style.FILL);
			mPaint.setColor((CHART_COLOR & 0xFFFFFF) | 0x10000000);
			mPath.lineTo(points.get(size-1).x, height+mBorder);	
			mPath.lineTo(points.get(0).x, height+mBorder);	
			mPath.close();
			canvas.drawPath(mPath, mPaint);
		}

		// draw circles
		mPaint.setColor(CHART_COLOR);
		mPaint.setStyle(Style.FILL_AND_STROKE);
		for (PointF point : points) {
			canvas.drawCircle(point.x, point.y, mCircleSize/2, mPaint);
		}
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(Color.WHITE);
		for (PointF point : points) {
			canvas.drawCircle(point.x, point.y, (mCircleSize-mStrokeSize)/2, mPaint);
		}

		if (textCaptionEnable) {
			Rect textBox = new Rect();
			tPaint.getTextBounds(String.valueOf(mValues[i]), 0, String.valueOf(mValues[i]).length(), textBox);
			// Calculate Middle point of Caption and distance between Caption and Chart Line
			float textX= (float) textBox.width();
			float textY = changeUnit(17, "sp");

			// If there is not enough space between Caption and Chart, Caption's position set below the Chart
			for (int i = 0; i < points.size(); i++) {
				if (changeUnit((int) (mMaxY - todayPoint.y), "sp") > textY) {
					canvas.drawText(String.valueOf(String.valueOf(mValues[i])), points.get(i).x - (textX / 2), points.get(i).y - textY, tPaint);
				} else {
					canvas.drawText(String.valueOf(String.valueOf(mValues[i])), points.get(i).x - (textX / 2), points.get(i).y + textY, tPaint);
				}
			}
		}
		
	}
}
