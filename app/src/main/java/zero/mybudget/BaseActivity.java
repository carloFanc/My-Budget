package zero.mybudget;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;



public class BaseActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    Toolbar toolbar;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private static String KEY_FIRST_RUN = "";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences sharedPref = getSharedPreferences("mypref", 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (!sharedPref.contains("KEY_FIRST_RUN")) {
            KEY_FIRST_RUN = "FirstTime";
            editor.putString("TitlePortfolio","Portfolio1" );
            editor.commit();
            getSupportActionBar().setTitle(sharedPref.getString("TitlePortfolio", ""));

        } else {
            getSupportActionBar().setTitle(sharedPref.getString("TitlePortfolio", ""));
        }

        editor.putString("KEY_FIRST_RUN", KEY_FIRST_RUN);
        editor.commit();

    }

    public void set(String[] navMenuTitles, TypedArray navMenuIcons) {
        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        navDrawerItems = new ArrayList<NavDrawerItem>();


        if (navMenuIcons == null) {
            for (int i = 0; i < navMenuTitles.length; i++) {
                navDrawerItems.add(new NavDrawerItem(navMenuTitles[i]));
            }
        } else {
            for (int i = 0; i < navMenuTitles.length; i++) {
                navDrawerItems.add(new NavDrawerItem(navMenuTitles[i],
                        navMenuIcons.getResourceId(i, -1)));
            }
        }

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());


        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setupDrawerToggle();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            public void onDrawerClosed(View view) {
                SharedPreferences sharedPref = getSharedPreferences("mypref", 0);
                getSupportActionBar().setTitle(sharedPref.getString("TitlePortfolio", ""));
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                SharedPreferences sharedPref = getSharedPreferences("mypref", 0);
                getSupportActionBar().setTitle(sharedPref.getString("TitlePortfolio", ""));
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    private void displayView(int position) {

        switch (position) {
            case 0:
                Intent intent = new Intent(this, BudgetActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
                break;
            case 1:
                Intent intent1 = new Intent(this, PortfoliosActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent1);
                overridePendingTransition(0,0);
                finish();
                break;
            case 2:
              Intent intent2 = new Intent(this, CategoryActivity.class);
              intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
              startActivity(intent2);
              overridePendingTransition(0,0);
              finish();
              break;
            case 3:
                Intent intentz = new Intent(this, RecurringActivity.class);
                intentz.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intentz);
                overridePendingTransition(0,0);
                finish();
                break;
             case 4:
             Intent intent3 = new Intent(this, StatisticsActivity.class);
                 intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
             startActivity(intent3);
                 overridePendingTransition(0,0);
                 finish();
             break;
             case 5:
             Intent intent4 = new Intent(this, HistoryActivity.class);
                 intent4.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                 startActivity(intent4);
                 overridePendingTransition(0,0);
             finish();
             break;
             case 6:
                 Intent intent5 = new Intent(this, LocationActivity.class);
                 intent5.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                 startActivity(intent5);
                 overridePendingTransition(0,0);
                 finish();
            default:
                break;
        }


        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);

        mDrawerToggle.syncState();
    }
}