package com.example.johan.assignment3;

import android.graphics.Color;

import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;



public class PieChart {

    public void OpenChart() {

        // Pie Chart Section Names
        String[] code = new String[]{
                "Eclair & Older", "Froyo", "Gingerbread", "Honeycomb",
                "IceCream Sandwich", "Jelly Bean"
        };

        // Pie Chart Section Value
        double[] distribution = {3.9, 12.9, 55.8, 1.9, 23.7, 1.8};

        // Color of each Pie Chart Sections
        int[] colors = {Color.BLUE, Color.MAGENTA, Color.GREEN, Color.CYAN, Color.RED,
                Color.YELLOW};

        // Instantiating CategorySeries to plot Pie Chart
        CategorySeries distributionSeries = new CategorySeries(" Android version distribution as on October 1, 2012");
        for (int i = 0; i < distribution.length; i++) {
            // Adding a slice with its values and name to the Pie Chart
            distributionSeries.add(code[i], distribution[i]);
        }

        // Instantiating a renderer for the Pie Chart
        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for (int i = 0; i < distribution.length; i++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);
            seriesRenderer.setDisplayChartValues(true);
            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        defaultRenderer.setChartTitle("Android version distribution as on October 1, 2012 ");
        defaultRenderer.setChartTitleTextSize(20);
        defaultRenderer.setZoomButtonsVisible(true);

        // Creating an intent to plot bar chart using dataset and multipleRenderer
        //Intent intent = ChartFactory.getPieChartIntent(getBaseContext(), distributionSeries, defaultRenderer, "AChartEnginePieChartDemo");

    }
}
