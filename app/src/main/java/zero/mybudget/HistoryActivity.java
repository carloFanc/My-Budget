package zero.mybudget;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HistoryActivity extends BaseActivity {

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private Button btnFrom,btnTo,btnSearch;
    private TextView Total;
    private int mYear, mMonth, mDay;
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;

    final String[] from = new String[] { DatabaseHelper.CAT,
            DatabaseHelper.DESC,DatabaseHelper.TIME, DatabaseHelper.Q };

    final int[] to = new int[] { R.id.tv_category, R.id.tv_description, R.id.tv_date1, R.id.tv_total };
    private ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
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
        Total.setText("0€");

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


                DatePickerDialog datePickerDialog = new DatePickerDialog(HistoryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar c = Calendar.getInstance();
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


                DatePickerDialog datePickerDialog = new DatePickerDialog(HistoryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar c = Calendar.getInstance();
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
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date dateFrom = sdf.parse(fromZ);
                    Date dateTo = sdf.parse(toZ);
                if(dateFrom.after(dateTo) ){
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.drawer_layout), "Incorrect Dates!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else{
                    Cursor cursor = dbManager.fetch_history(btnFrom.getText().toString(),btnTo.getText().toString());

                    adapter = new SimpleCursorAdapter(HistoryActivity.this, R.layout.layout_cashflow_item, cursor, from, to, 0);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    Total.setText(Float.toString(dbManager.getTotalHistory(btnFrom.getText().toString(),btnTo.getText().toString()))+ "€");
                 }
                }catch (java.text.ParseException e){
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
