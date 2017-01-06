package org.paoloconte.smoothchart.sample;

import org.paoloconte.smoothchart.SmoothLineChart;
import org.paoloconte.smoothchart.SmoothLineChartEquallySpaced;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.PointF;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SmoothLineChart chart = (SmoothLineChart) findViewById(R.id.smoothChart);
		chart.setData(new PointF[] { 
				new PointF(15, 39), // {x, y}
				new PointF(20, 21), 
				new PointF(28, 9), 
				new PointF(37, 21),
				new PointF(40, 25), 
				new PointF(50, 31), 
				new PointF(62, 24), 
				new PointF(80, 28)
			}, true);
		
		SmoothLineChartEquallySpaced chartES = (SmoothLineChartEquallySpaced) findViewById(R.id.smoothChartES);
		chartES.setData(new float[] { 
				15,
				21,
				9,
				21,
				25,
				35,
				24,
				28
			}, false);
		
	}

	
}
