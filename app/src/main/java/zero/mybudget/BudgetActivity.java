package zero.mybudget;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.content.res.TypedArray;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BudgetActivity extends BaseActivity {
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private TextView tvDate;
    private TextView tvTotal;
    private DBManager dbManager;
    private static String RECURRENT_FIRST_RUN = "";
    private ArrayList<String[]> AllValues ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);
        initVariables();

        SharedPreferences sharedPref = getSharedPreferences("actday", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        DateFormat df1=new SimpleDateFormat("dd-MM-yyyy");
        String dateToday= df1.format(Calendar.getInstance().getTime());
        if (!sharedPref.contains("RECURRENT_FIRST_RUN")) {
            RECURRENT_FIRST_RUN = "FirstTime";
            editor.putString("ActualDay", dateToday);
            editor.commit();
            editor.putString("RECURRENT_FIRST_RUN", RECURRENT_FIRST_RUN);
            editor.commit();
        }
        tabLayout.addTab(tabLayout.newTab().setText("TODAY"));
        tabLayout.addTab(tabLayout.newTab().setText("WEEK"));
        tabLayout.addTab(tabLayout.newTab().setText("MONTH"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final pagerAdapter adapter = new pagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    setCurrentDate("DAY");
                    tvTotal = (TextView) findViewById(R.id.toolbar_number_balance);
                    tvTotal.setText(Float.toString(dbManager.getTotalDay())+"€");
                }
                if (tab.getPosition() == 1) {
                    setCurrentDate("WEEK");
                    tvTotal = (TextView) findViewById(R.id.toolbar_number_balance);
                    tvTotal.setText(Float.toString(dbManager.getTotalWeek())+"€");
                }
                if (tab.getPosition() == 2) {
                    setCurrentDate("MONTH");
                    tvTotal = (TextView) findViewById(R.id.toolbar_number_balance);
                    tvTotal.setText(Float.toString(dbManager.getTotalMonth())+"€");
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
        setCurrentDate("DAY");

        dbManager = new DBManager(this);
        dbManager.open();
        tvTotal.setText(Float.toString(dbManager.getTotalDay())+"€");


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BudgetActivity.this, AddCashflowsActivity.class);

                startActivity(intent);
            }
        });

        Calendar c = Calendar.getInstance();
        DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df2.format(c.getTime());
        SharedPreferences sharedPrefs = getSharedPreferences("actday", 0);
        String daypreference = sharedPrefs.getString("ActualDay", "");
        if (!formattedDate.equals(daypreference)) {
            Utils.recurrentTransaction(this);
            SharedPreferences.Editor editors = sharedPrefs.edit();
            editors.putString("ActualDay", formattedDate);
            editors.commit();
        }
    }

    private void initVariables() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        fab = (FloatingActionButton) findViewById(R.id.fabCash);
        tvDate = (TextView) findViewById(R.id.time_date);
        tvTotal = (TextView) findViewById(R.id.toolbar_number_balance);
    }

    public void setCurrentDate(String date) {
        if ("DAY".equals(date)) {
            DateFormat df1=new SimpleDateFormat("dd/MM/yyyy");
            String datef= df1.format(Calendar.getInstance().getTime());
            tvDate.setText(datef);
        }
        if("WEEK".equals(date)){
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String startDate = "", endDate = "";

            startDate = df.format(c.getTime());
            c.add(Calendar.DATE, 6);
            endDate = df.format(c.getTime());

            tvDate.setText( startDate + " - " + endDate);
        }
        if("MONTH".equals(date)){
            String startDate = "", endDate = "";
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = 1;
            c.set(year, month, day);
            int numOfDaysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            startDate = df.format(c.getTime());
            c.add(Calendar.DAY_OF_MONTH, numOfDaysInMonth-1);
            endDate = df.format(c.getTime());
            tvDate.setText( startDate + " - " + endDate);

        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_budget, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.export_pdf:
                Intent intent = new Intent(BudgetActivity.this, ExportPdfActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


