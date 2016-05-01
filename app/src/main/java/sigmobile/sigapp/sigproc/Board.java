package sigmobile.sigapp.sigproc;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.ArrayList;
import java.util.Random;

import sigmobile.sigapp.R;


public class Board extends AppCompatActivity {
    private static final int SERIES_NR = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout l = (LinearLayout) findViewById(R.id.admin);
        if(Balise.droit.equals("Admin") || Balise.droit.equals("Superadmin") ) {
            // Si admin/superadmin, on affiche les 2 graphique
            assert l != null;
            l.setVisibility(View.VISIBLE);
        }
        // 1 seul graphique est affiché sinon (RSSI/temps)
        XYMultipleSeriesRenderer renderer = getTruitonBarRenderer();
        myChartSettings(renderer);
        Intent intent = ChartFactory.getBarChartIntent(this, getTruitonBarDataset(), renderer, BarChart.Type.DEFAULT);
        //startActivity(intent);
    }

    private XYMultipleSeriesDataset getTruitonBarDataset() {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        final int nr = 4;
        Random r = new Random();
        ArrayList<String> legendTitles = new ArrayList<String>();
        legendTitles.add("Sales");
        legendTitles.add("Expenses");
        for (int i = 0; i < SERIES_NR; i++) {
            CategorySeries series = new CategorySeries(legendTitles.get(i));
            for (int k = 0; k < nr; k++) {
                series.add(100 + r.nextInt() % 100);
            }
            dataset.addSeries(series.toXYSeries());
        }
        return dataset;
    }

    public XYMultipleSeriesRenderer getTruitonBarRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setMargins(new int[] { 30, 40, 15, 0 });
        SimpleSeriesRenderer r = new SimpleSeriesRenderer();
        r.setColor(Color.BLUE);
        renderer.addSeriesRenderer(r);
        r = new SimpleSeriesRenderer();
        r.setColor(Color.RED);
        renderer.addSeriesRenderer(r);
        return renderer;
    }

    private void myChartSettings(XYMultipleSeriesRenderer renderer) {
        renderer.setChartTitle("Truiton's Performance by AChartEngine BarChart");
        renderer.setXAxisMin(0.5);
        renderer.setXAxisMax(10.5);
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(210);
        renderer.addXTextLabel(1, "2010");
        renderer.addXTextLabel(2, "2011");
        renderer.addXTextLabel(3, "2012");
        renderer.addXTextLabel(4, "2013");
        renderer.setYLabelsAlign(Paint.Align.RIGHT);
        renderer.setBarSpacing(0.5);
        renderer.setXTitle("Years");
        renderer.setYTitle("Performance");
        renderer.setShowGrid(true);
        renderer.setGridColor(Color.GRAY);
        renderer.setXLabels(0); // sets the number of integer labels to appear
    }
}
