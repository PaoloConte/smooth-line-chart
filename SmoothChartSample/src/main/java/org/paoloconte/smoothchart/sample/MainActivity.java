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

		SmoothLineChart.Line xyline1 = new SmoothLineChart.Line(0xFF0099CC, new PointF[] {
				new PointF(15, 39), // {x, y}
				new PointF(20, 21),
				new PointF(28, 9),
				new PointF(37, 21),
				new PointF(40, 25),
				new PointF(50, 31),
				new PointF(62, 24),
				new PointF(80, 28)
		});
		SmoothLineChart.Line xyline2 = new SmoothLineChart.Line(0xFFFF9900, new PointF[] {
				new PointF(15, 23), // {x, y}
				new PointF(20, 65),
				new PointF(28, 34),
				new PointF(37, 15),
				new PointF(40, 64),
				new PointF(50, 34),
				new PointF(62, 23),
				new PointF(80, 43)
		});

		chart.setData(new SmoothLineChart.Line[] { xyline1, xyline2 });

		float[] data1 = new float[] { 15, 21, 9, 21, 25, 35, 24, 28	};
		SmoothLineChartEquallySpaced.Line line1 = new SmoothLineChartEquallySpaced.Line(0xFF0099CC, data1);

		float[] data2 = new float[] { 54, 28, 42, 23, 12, 15, 53, 11 };
		SmoothLineChartEquallySpaced.Line line2 = new SmoothLineChartEquallySpaced.Line(0xFFFF9900, data2);

		SmoothLineChartEquallySpaced chartES = (SmoothLineChartEquallySpaced) findViewById(R.id.smoothChartES);
		chartES.setData(new SmoothLineChartEquallySpaced.Line[] { line1, line2 });
		
	}

	
}
