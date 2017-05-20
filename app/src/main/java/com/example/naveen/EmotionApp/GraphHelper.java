package com.example.naveen.EmotionApp;

import android.content.Context;
import android.graphics.Color;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Naveen on 3/26/2017.
 */

public class GraphHelper {

    /***
     * This method showStatistics in the given graphView.
     * @param list list of emotions to be showed on the graph. list must be sorted
     * @param graphView graphview where to show the graph-statistics
     * @param context application context
     */
    public static void showStatistics(List<Emotion> list , GraphView graphView, Context context){
        try {
            graphView.removeAllSeries();
            //graphView.getGridLabelRenderer().reloadStyles();
            DataPoint[] sadness = new DataPoint[list.size()];
            DataPoint[] happiness = new DataPoint[list.size()];
            DataPoint[] neutral = new DataPoint[list.size()];

            for (int i = list.size() -1 ; i >= 0 ; i--) {
                Date X =  new SimpleDateFormat("yyyy-MM-dd").parse(list.get(i).date);
                double Y = list.get(i).sadness + list.get(i).anger + list.get(i).contempt + list.get(i).disgust + list.get(i).fear ;
                sadness[list.size()- 1 - i] = new DataPoint(X, Y);

                Y = list.get(i).happiness + list.get(i).surprise;
                happiness[list.size()- 1 - i] = new DataPoint(X, Y);

                Y = list.get(i).neutral;
                neutral[list.size() -1 -i] = new DataPoint(X, Y);
            }


            LineGraphSeries<DataPoint> sadnessSeries = new LineGraphSeries<DataPoint>(sadness);
            sadnessSeries.setTitle("Sadness");
            sadnessSeries.setDataPointsRadius(5);
            sadnessSeries.setDrawDataPoints(true);
            sadnessSeries.setColor(Color.RED);
            sadnessSeries.setAnimated(true);

            LineGraphSeries<DataPoint> happinessSeries = new LineGraphSeries<>(happiness);
            happinessSeries.setTitle("Happiness");
            happinessSeries.setDrawDataPoints(true);
            happinessSeries.setDataPointsRadius(5);
            happinessSeries.setColor(Color.BLUE);
            happinessSeries.setAnimated(true);

            LineGraphSeries<DataPoint> neutralSeries = new LineGraphSeries<>();
            neutralSeries.setTitle("Neutral");
            neutralSeries.setDrawDataPoints(true);
            neutralSeries.setDataPointsRadius(5);
            neutralSeries.setColor(Color.YELLOW);
            neutralSeries.setAnimated(true);
//        LineGraphSeries<DataPoint> sadSeries = new LineGraphSeries<>();
//        LineGraphSeries<DataPoint> surpriseSeries = new LineGraphSeries<>();

            graphView.getViewport().setXAxisBoundsManual(true);
            Date minX = new SimpleDateFormat("yyyy-MM-dd").parse(list.get(list.size() - 1).date);
            Date maxX = new SimpleDateFormat("yyyy-MM-dd").parse(list.get(0).date);
            graphView.getViewport().setMinX(minX.getTime());
            graphView.getViewport().setMaxX(maxX.getTime());
            graphView.getGridLabelRenderer().setNumHorizontalLabels(list.size());

            graphView.getViewport().setYAxisBoundsManual(true);
            graphView.getViewport().setMinY(0);
            graphView.getViewport().setMaxY(1);

            graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graphView.getContext()));
            graphView.getGridLabelRenderer().setHorizontalAxisTitle("Day");
            graphView.getGridLabelRenderer().setHorizontalLabelsAngle(110);
            graphView.getGridLabelRenderer().setVerticalAxisTitle("Socres");

            //to show legends at the top
            graphView.getLegendRenderer().setVisible(true);
            graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

            graphView.getGridLabelRenderer().setHumanRounding(false);

            graphView.addSeries(sadnessSeries);
            graphView.addSeries(happinessSeries);
            graphView.addSeries(neutralSeries);
        }catch (Exception e){

        }

    }

//    //date format yyyy-MM-dd'T'HH:mm:ss
//    public static String getDay(String date) {
//        int startIndex = date.lastIndexOf('-') + 1;
//        return date.substring(startIndex, startIndex + 2);
//    }

    public static void testGraph(GraphView graphView, Context context){
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();

        GraphView graph = graphView;

// you can directly pass Date objects to DataPoint-Constructor
// this will convert the Date to double via Date#getTime()
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, 1),
                new DataPoint(d2, 5),
                new DataPoint(d3, 3)
        });

        graph.addSeries(series);

// set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graphView.getContext()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

// set manual x bounds to have nice steps
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d3.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);

    }
}
