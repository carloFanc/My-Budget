package zero.mybudget;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class StatisticsActivity extends BaseActivity{

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private Button btnFrom,btnTo,btnSearch;
    private TextView Total;
    private int mYear, mMonth, mDay;
    private DBManager dbManager;

    PieChart pieChart;

    final String[] from = new String[] { DatabaseHelper.CAT,
            DatabaseHelper.DESC, DatabaseHelper.Q };

    final int[] to = new int[] { R.id.tv_category, R.id.tv_description, R.id.tv_total };
    private ListView listView;
    String[] XcategoryListArray;
    float[] Ypercentage4Category;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);// load icons from
        set(navMenuTitles, navMenuIcons);

        listView = (ListView) findViewById(R.id.list_history);
        btnFrom = (Button) findViewById(R.id.btn_date_from);
        btnTo = (Button) findViewById(R.id.btn_date_to);
        Total = (TextView) findViewById(R.id.tv_total);
        btnSearch = (Button) findViewById(R.id.search_history);

        DateFormat df1=new SimpleDateFormat("dd-MM-yyyy");
        String datef= df1.format(java.util.Calendar.getInstance().getTime());
        btnFrom.setText(datef);
        btnTo.setText(datef);
        Total.setText("");

        btnFrom.setOnClickListener(new View.OnClickListener()
        {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v)
            {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(StatisticsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar cal = Calendar.getInstance();
                                c.set(year, monthOfYear,dayOfMonth);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                String cdate = formatter.format(c.getTime());
                                btnFrom.setText(cdate);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnTo.setOnClickListener(new View.OnClickListener()
        {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v)
            {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(StatisticsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar cal = Calendar.getInstance();
                                c.set(year, monthOfYear,dayOfMonth);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                String cdate = formatter.format(c.getTime());
                                btnTo.setText(cdate);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        dbManager = new DBManager(this);
        dbManager.open();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String fromZ, toZ;
                fromZ = btnFrom.getText().toString();
                toZ = btnTo.getText().toString();
                XcategoryListArray = dbManager.getCategoriesList(fromZ, toZ);
                Ypercentage4Category = dbManager.getPercentageCategories(fromZ, toZ);

                pieChart = (PieChart) findViewById(R.id.pieChart);

                pieChart.setRotationEnabled(true);
                pieChart.setHoleRadius(25f);
                pieChart.setTransparentCircleAlpha(0);
                pieChart.setCenterText("Categories");
                pieChart.setCenterTextSize(10);
                pieChart.setDescription(null);
                pieChart.setUsePercentValues(true);
                addDataSet();

                Legend l = pieChart.getLegend();
                l.setPosition(LegendPosition.RIGHT_OF_CHART);
                l.setXEntrySpace(7);
                l.setYEntrySpace(5);
                pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e,int dataSetIndex, Highlight h) {
                        if (e == null)
                            return;
                        Toast.makeText(StatisticsActivity.this,
                                XcategoryListArray[e.getXIndex()] + " = " + e.getVal() + "%", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected() {

                    }
                });
            }
        });
    }
    private void addDataSet(){
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < Ypercentage4Category.length; i++)
            yVals1.add(new Entry(Ypercentage4Category[i], i));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < XcategoryListArray.length; i++)
            xVals.add(XcategoryListArray[i]);

        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);


        PieData data = new PieData(xVals,dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        pieChart.setData(data);

        pieChart.highlightValues(null);

        pieChart.invalidate();
    }
}
