package org.paoloconte.smoothchart.sample;

import org.paoloconte.smoothchart.SmoothLineChart;

import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SmoothLineChart chart = (SmoothLineChart) findViewById(R.id.smoothChart);
		chart.setData(new float [][] { 
				{15, 39}, // {x, y}
				{20, 21}, 
				{28, 9}, 
				{40, 25}, 
				{50, 31}, 
				{62, 24}, 
				{80, 28}
			}); 
		
	}

	
}
