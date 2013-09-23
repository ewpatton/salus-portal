package edu.rpi.tw.mobilehealth;

import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.InputStream;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;

import edu.rpi.tw.calendar.Calendar;
import edu.rpi.tw.calendar.CalendarFactory;
import edu.rpi.tw.calendar.Event;
import edu.rpi.tw.calendar.Instance;
import edu.rpi.tw.calendar.android.AndroidCalendarFactory;
import edu.rpi.tw.mobilehealth.util.AppProperties;
import edu.rpi.tw.mobilehealth.util.CACertManager;
import edu.rpi.tw.mobilehealth.util.SemantEcoModuleCaller;
import edu.rpi.tw.mobilehealth.util.SemantEcoModuleCaller.Callback;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;
import static edu.rpi.tw.mobilehealth.util.SemantEcoModuleCaller.specializedMapClass;

public class HealthChartActivity extends Activity implements OnMenuItemClickListener {

    private static final String TAG = HealthChartActivity.class.getSimpleName();
    private XYPlot plot;
    private String serviceUrl = null;
    private List<Map<String, Object>> characteristics = null;

    protected class CharacteristicCallback implements
        Callback<List<Map<String, Object>>> {
        public void onSuccess(final List<Map<String, Object>> results) {
            HealthChartActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    characteristics = results;
                    Toast.makeText(getApplicationContext(), "Success!",
                            Toast.LENGTH_LONG).show();
                }
            });
            for( Map<String, Object> binding : results ) {
                Log.i( TAG, "Binding: " + binding );
            }
        }

        public void onError(final String error, final Exception ex) {
            HealthChartActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), error,
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private Callback<List<Map<String, Object>>> characteristicCallback =
            new CharacteristicCallback();

    public float dipToPixels(float dipValue) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue,
                metrics);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        CACertManager.installCertificate( getApplicationContext() );
        registerForContextMenu(findViewById(R.id.mySimpleXYPlot));

        InputStream propFile = null;
        try {
            propFile = getAssets().open( "app.properties",
                    AssetManager.ACCESS_BUFFER );
            AppProperties.init( propFile );
        } catch( IOException e ) {
            Log.w( TAG, "Unable to read properties stream.", e );
        } finally {
            if ( propFile != null ) {
                try {
                    propFile.close();
                } catch( IOException e ) {
                    Log.w( TAG, "Unable to close properties stream." );
                }
            }
        }
        serviceUrl = AppProperties.get().getProperty( "service.url" );
        Log.i( TAG, "serviceUrl = " + serviceUrl );
        assert( !serviceUrl.equals( "${service.url}") );

        SemantEcoModuleCaller.call("CharacteristicModule",
                "listCharacteristics",
                Collections.<String, Object>emptyMap(),
                characteristicCallback,
                specializedMapClass(String.class, Object.class));

        plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
        plot.setTitle("White Blood Cells / Neutrophils");
        Number[] series1Numbers = {7.5, 7.4, 6.3, 5.6, 6.8, 6.2};
        Number[] series2Numbers = {5.76, 5.64, 4.71, 4.27, 5.08, 4.5};
        Number[] xAxis = {new GregorianCalendar(2013, 6, 23).getTimeInMillis(),
                new GregorianCalendar(2013, 6, 30).getTimeInMillis(),
                new GregorianCalendar(2013, 7, 6).getTimeInMillis(),
                new GregorianCalendar(2013, 7, 13).getTimeInMillis(),
                new GregorianCalendar(2013, 7, 20).getTimeInMillis(),
                new GregorianCalendar(2013, 7, 27).getTimeInMillis()
        };
        
        XYSeries series1 = new SimpleXYSeries(Arrays.asList(xAxis),
                Arrays.asList(series1Numbers), "WBC");
        XYSeries series2 = new SimpleXYSeries(Arrays.asList(xAxis),
                Arrays.asList(series2Numbers), "Neutrophils");
        
        LineAndPointFormatter series1Format = new LineAndPointFormatter();
        series1Format.setPointLabelFormatter(new PointLabelFormatter());
        series1Format.configure(getApplicationContext(),
                R.xml.line_point_formatter_with_plf1);
        plot.addSeries(series1, series1Format);
        
        LineAndPointFormatter series2Format = new LineAndPointFormatter();
        series2Format.setPointLabelFormatter(new PointLabelFormatter());
        series2Format.configure(getApplicationContext(),
                R.xml.line_point_formatter_with_plf2);
        plot.addSeries(series2, series2Format);

        plot.setDomainStep(XYStepMode.SUBDIVIDE, xAxis.length);
        plot.setDomainValueFormat(new Format(){
            private static final long serialVersionUID = -5256457799048505381L;
            final SimpleDateFormat format = new SimpleDateFormat("MM-dd",
                    getResources().getConfiguration().locale);

            @Override
            public StringBuffer format(Object object, StringBuffer buffer,
                    FieldPosition field) {
                long value = ((Number)object).longValue();
                format.format(new Date(value), buffer, field);
                return buffer;
            }

            @Override
            public Object parseObject(String string, ParsePosition position) {
                return null;
            }
            
        });

        plot.setTicksPerRangeLabel(3);
        plot.getGraphWidget().setDomainLabelOrientation(-45);
        //plot.setMarkupEnabled(true);
        plot.getGraphWidget().setGridPaddingBottom(128f);
        plot.getGraphWidget().setMarginBottom(128f);
        plot.setRangeLabel("Thousand per Microliter");
        plot.setDomainLabel("Sample Date");
        Paint legendPaint = plot.getLegendWidget().getTextPaint();
        legendPaint.setTextSize(dipToPixels(12));
        plot.getLegendWidget().setTextPaint(legendPaint);
        Paint labelPaint = plot.getGraphWidget().getDomainLabelPaint();
        labelPaint.setTextSize(dipToPixels(12));
        labelPaint = plot.getGraphWidget().getDomainOriginLabelPaint();
        labelPaint.setTextSize(dipToPixels(12));
        labelPaint = plot.getGraphWidget().getRangeOriginLabelPaint();
        labelPaint.setTextSize(dipToPixels(12));
        labelPaint = plot.getGraphWidget().getRangeLabelPaint();
        labelPaint.setTextSize(dipToPixels(12));
        try {
            testCalendar();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public final void testCalendar() {
        CalendarFactory calendars = new AndroidCalendarFactory( getApplicationContext() );
        for( Calendar calendar : calendars.getAllCalendars() ) {
            System.out.println( "Calendar: " + calendar.toString() );
            System.out.println( "  Display Name: " + calendar.getDisplayName());
            for ( Event event : calendar.getUpcomingEvents() ) {
                System.out.println( "  Event: " + event.getTitle() );
                System.out.println( "    Organizer: " + event.getOrganizer() );
                System.out.println( "    Location: " + event.getLocation() );
                System.out.println( "    Availability: " + event.getAvailability());
                System.out.println( "    Start: " + event.getStart() );
                System.out.println( "    End: " + event.getEnd() );
                System.out.println( "    Duration: " + event.getDuration() );
                System.out.println( "    RRule: " + event.getRRule() );
                System.out.println( "    RDate: " + event.getRDate() );
                for ( Instance i : event ) {
                    System.out.println( "    Instance:");
                    System.out.println( "      Start: " + i.getStart());
                    System.out.println( "      End: " + i.getEnd());
                }
            }
            System.out.println();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.health_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
            ContextMenu.ContextMenuInfo info) {
        if ( characteristics == null ) {
            return;
        }
        menu.setHeaderTitle("Chart Options");
        SubMenu submenu = menu.addSubMenu("Select X Axis");
        MenuItem item = submenu.add(1, Menu.NONE, 0, "Time");
        item.setCheckable( true );
        item.setChecked( true );
        Intent intent = new Intent();
        intent.putExtra("uri", "http://www.w3.org/2001/XMLSchema#dateTime");
        item.setIntent(intent);
        int i = 1;
        for(Map<String, Object> items : characteristics) {
            item = submenu.add(1, Menu.NONE, i, (String)items.get("label"));
            item.setCheckable( true );
            item.setChecked( false );
            intent = new Intent().putExtra("uri", items.get("uri").toString());
            item.setIntent(intent);
            i++;
        }
        submenu.setGroupCheckable(1, true, true);
        submenu = menu.addSubMenu("Selecy Y Axis");
        for(Map<String, Object> items : characteristics) {
            item = submenu.add((String)items.get("label"));
            item.setCheckable( true );
            item.setChecked( item.getTitle().equals("White Blood Cells") || item.getTitle().equals("Neutrophil") );
            intent = new Intent().putExtra("uri", items.get("uri").toString());
            item.setIntent(intent);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent intent = item.getIntent();
        if ( intent == null ) {
            return super.onContextItemSelected(item);
        }
        item.setChecked(!item.isChecked());
        return true;
    }
}
