package com.moarcodeplz.moarsensorlogger.activity.fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.moarcodeplz.moarsensorlogger.activity.EntryActivity;
import com.moarcodeplz.moarsensorlogger.application.LoggerApplication;
import com.example.moarsensorlogger.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class GraphFragment extends Fragment{
	private final Handler mHandler = new Handler();
	private Runnable mTimer1;
	private GraphView graphView;
	private GraphViewSeries series1;
	private GraphViewSeries series2;
	private GraphViewSeries series3;
	private int graph2LastXValue = 0;
	private Activity activity;

	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View mainView = inflater.inflate(R.layout.graph_fragment, container, false);	
		return mainView;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		activity = (EntryActivity) getActivity();
		
		// init example series data
		series1 = new GraphViewSeries("X", null, new GraphViewData[] {});
	    series1.getStyle().color = Color.RED;
		series1.getStyle().thickness = 3;
		
		series2 = new GraphViewSeries("Y", null, new GraphViewData[] {});
	    series2.getStyle().color = Color.BLUE;
		series2.getStyle().thickness = 3;
		
		series3 = new GraphViewSeries("Z", null, new GraphViewData[] {});
		series3.getStyle().color = Color.YELLOW;
		series3.getStyle().thickness = 3;
		

		graphView = new LineGraphView(activity, "Graph View");
		graphView.addSeries(series1); 
		graphView.addSeries(series2);
		graphView.addSeries(series3);		
		graphView.setBackgroundColor(Color.TRANSPARENT);
		graphView.setScalable(true);	
		graphView.setScrollable(true);
		graphView.setShowLegend(true);  
		graphView.setLegendAlign(LegendAlign.BOTTOM);  
		graphView.setLegendWidth(100);
		graphView.setViewPort(0, 100);
		
		graphView.getGraphViewStyle().setNumHorizontalLabels(10);  
		graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.BLACK);  

		
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.graph1);
		layout.addView(graphView);
		((LineGraphView) graphView).setDrawBackground(true);

	    mTimer1 = new Runnable() {
	    	@Override
	    	public void run() {
	    		if(LoggerApplication.logToWrite != null) {
	    			String[] write = LoggerApplication.logToWrite.split(",");	
					//Double src1 = Double.parseDouble(write[1]);
					Double src1 = Math.round(Double.parseDouble(write[2])*1000.0)/1000.0;
					Double src2 = Math.round(Double.parseDouble(write[3])*1000.0)/1000.0;
					Double src3 = Math.round(Double.parseDouble(write[4])*1000.0)/1000.0;					
					
					
					graph2LastXValue += 1;
	             
					series1.appendData(new GraphViewData(graph2LastXValue, src1), true, 100);
					series2.appendData(new GraphViewData(graph2LastXValue, src2), true, 100);
					series3.appendData(new GraphViewData(graph2LastXValue, src3), true, 100);

	                
					System.out.println(graph2LastXValue + "\t" + src1 + "\t" + src2 + "\t" + src3);
        	   }
                mHandler.postDelayed(this, 300);
           }
	   };
	   mHandler.postDelayed(mTimer1, 300);
	   
		
	}
}
