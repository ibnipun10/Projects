package com.UI.Common.library;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.content.Context;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.UI.mybuddyapp.library.GraphActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;


public class CGraphChart {

	private List<FacebookFriend> m_lFBFriend;
	private Map<String, List<FacebookFriend>> m_mapFriends;
	private Context m_context;

	public CGraphChart(List<FacebookFriend> lfbFriend, Context context)
	{
		m_lFBFriend = lfbFriend;
		m_context = context;
	}

	private enum eGraph
	{
		PIE_CHART,
		BAR_GRAPH
	}

	private CategorySeries getCategorySeries()
	{
		m_mapFriends = new Hashtable<String, List<FacebookFriend>>();
		CategorySeries series = new CategorySeries("Pie"); // adding series to charts.

		for(int i =0; i< m_lFBFriend.size(); i++)
		{
			LocationClass objLocation = m_lFBFriend.get(i).location;

			String key = Utilities.GetKey(objLocation.lattitude, objLocation.longitude);

			List<FacebookFriend> value = m_mapFriends.get(key);
			if(value == null)
				value = new ArrayList<FacebookFriend>();

			value.add(m_lFBFriend.get(i));
			m_mapFriends.put(key, value);

		}

		Set<String> keys = m_mapFriends.keySet();
		
		//Sset the total count of locations in GraphActivity
		GraphActivity.m_iLocationCount = keys.size() - 1;

		for(String key : keys)
		{
			List<FacebookFriend> lfbFriend = m_mapFriends.get(key);
			LocationClass objLocation = lfbFriend.get(0).location;

			String locationName = objLocation.name;
			if(locationName == null)
				locationName = Constants.UNMAPEED_FRIEND;

			series.add(locationName, lfbFriend.size());

		}



		return series;

	}

	public GraphicalView getPieChartView(){

		CategorySeries series = getCategorySeries();

		// set style for series
		Set<String> keys = m_mapFriends.keySet();;
		DefaultRenderer renderer = new DefaultRenderer();

		for(String key : keys){
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			Random rnd = new Random();
			int color = Color.argb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256),
					rnd.nextInt(256));
			r.setColor(color);
			r.setDisplayBoundingPoints(true);
			r.setDisplayChartValuesDistance(5);
			r.setDisplayChartValues(false);
			r.setChartValuesTextSize(30);
			r.setShowLegendItem(false);
			renderer.addSeriesRenderer(r);

		}

		renderer.setClickEnabled(true);
		renderer.setSelectableBuffer(10);
		renderer.isInScroll();
		renderer.setZoomButtonsVisible(true);   //set zoom button in Graph
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(Color.BLACK); //set background color
		renderer.setChartTitle(Constants.PIE_CHART_TITLE);
		renderer.setChartTitleTextSize((float) 60);
		renderer.setShowLabels(false);  
		renderer.setDisplayValues(true);
		renderer.setStartAngle(45);

		GraphicalView gView = ChartFactory.getPieChartView(m_context, series, renderer);
		gView.setOnClickListener(new TapOnPieChartSection(gView, renderer, series));
		

		//return ChartFactory.getPieChartIntent(m_context, series, renderer, "PieChart");
		return gView;
	}
	
	private class TapOnPieChartSection implements View.OnClickListener
	{
		GraphicalView m_gView;
		DefaultRenderer m_rederer;
		CategorySeries m_series;
		
		TapOnPieChartSection(GraphicalView view, DefaultRenderer renderer, CategorySeries series)
		{
			m_gView = view;
			m_rederer = renderer;
			m_series = series;
		}
		@Override
		public void onClick(View v) {
			SeriesSelection seriesSelection = m_gView.getCurrentSeriesAndPoint();
			if (seriesSelection == null) {
				
				for(int i=0; i<m_series.getItemCount(); i++)
				{					
					m_rederer.getSeriesRendererAt(i).setHighlighted(false);
				}
				GraphActivity.showToast(m_context, "No chart element was clicked", Toast.LENGTH_LONG, true);						
				
			} else {
				
				int iPointIndex = seriesSelection.getPointIndex();
				for(int i=0; i<m_series.getItemCount(); i++)
				{					
					m_rederer.getSeriesRendererAt(i).setHighlighted(i == iPointIndex);
				}
				m_gView.repaint();
				
				String locationName = m_series.getCategory(iPointIndex);
				double friendsCount = m_series.getValue(iPointIndex);
				
				GraphActivity.showToast(m_context, 
						"" + locationName + " : " + (int)friendsCount, 
						Toast.LENGTH_LONG, 
						true);				
			}

		}
	}


	public Intent getBarGraphIntent(Context context)
	{

		CategorySeries series = getCategorySeries();

		XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();  // collection of series under one object.,there could any

		for(int i=0; i<series.getItemCount(); i++)
		{
			CategorySeries tempSeries = new CategorySeries("i");
			tempSeries.add(series.getCategory(i), series.getValue(i));
			dataSet.addSeries(tempSeries.toXYSeries());                            // number of series
		}

		//customization of the chart
		// set style for series
		Set<String> keys = m_mapFriends.keySet();;
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();   // collection multiple values for one renderer or series

		for(String key : keys){
			XYSeriesRenderer r = new XYSeriesRenderer();     // one renderer for one series
			Random rnd = new Random();
			int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256),
					rnd.nextInt(256));
			r.setColor(color);
			r.setDisplayChartValuesDistance(5);
			r.setDisplayChartValues(true);
			r.setChartValuesTextSize(15);
			r.setLineWidth((float) 10.5d);
			r.setChartValuesSpacing((float) 5.5d);
			mRenderer.addSeriesRenderer(r);
		}


		mRenderer.setChartTitle("Demo Graph");
		//        mRenderer.setXTitle("xValues");
		mRenderer.setYTitle("Friends Count");
		mRenderer.setZoomButtonsVisible(true);    mRenderer.setShowLegend(true);
		mRenderer.setShowGridX(true);      // this will show the grid in  graph
		mRenderer.setShowGridY(true);              
		//        mRenderer.setAntialiasing(true);
		mRenderer.setBarSpacing(.5);   // adding spacing between the line or stacks
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.BLACK);
		mRenderer.setXAxisMin(0);
		//        mRenderer.setYAxisMin(.5);
		mRenderer.setXAxisMax(5);
		mRenderer.setYAxisMax(50);
		//    
		mRenderer.setXLabels(0);
		mRenderer.setPanEnabled(true, true);    // will fix the chart position
		Intent intent = ChartFactory.getBarChartIntent(context, dataSet, mRenderer,Type.STACKED);

		return intent;
	}
	
}

