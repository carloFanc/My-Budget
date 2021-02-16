package zero.mybudget;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PortfoliosActivity extends BaseActivity {
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private DBManager dbManager;

    private ListView listView;

    private SimpleCursorAdapter adapter;

    final String[] from = new String[] { DatabaseHelper._ID,
            DatabaseHelper.SUBJECT, DatabaseHelper.DESC };

    final int[] to = new int[] { R.id.id, R.id.title, R.id.desc };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolios);

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);// load icons from
        set(navMenuTitles, navMenuIcons);
        FloatingActionButton fabPortfolios = (FloatingActionButton)findViewById(R.id.fabPortfolios);
        dbManager = new DBManager(this);
        dbManager.open();

        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));
        Utils.setMyStaticListOfPortfolios(this);
        ArrayList<ArrayList<String>> listOfPortfolios = new ArrayList<ArrayList<String>>(Utils.getMyStaticListOfPortfolios());
        Log.d("sizelistOfPort",Integer.toString(listOfPortfolios.get(0).size()));
        CustomList adapter = new CustomList(this, listOfPortfolios.get(0));
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                TextView idTextView = (TextView) view.findViewById(R.id.id);
                TextView titleTextView = (TextView) view.findViewById(R.id.title);
                TextView descTextView = (TextView) view.findViewById(R.id.desc);

                String id = idTextView.getText().toString();
                String title = titleTextView.getText().toString();
                String desc = descTextView.getText().toString();


                Intent modify_intent = new Intent(getApplicationContext(), ModifyPortfoliosActivity.class);
                modify_intent.putExtra("title", title);
                modify_intent.putExtra("desc", desc);
                modify_intent.putExtra("id", id);
                startActivity(modify_intent);
            }
        });
        fabPortfolios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PortfoliosActivity.this, AddPortfoliosActivity.class);
                startActivity(intent);
            }
        });
        if(Utils.getMySnackbarActive() == true ) {
            Snackbar.make(findViewById(R.id.drawer_layout), "You can't remove the set portfolio", Snackbar.LENGTH_LONG)
                    .show();
            ModifyPortfoliosActivity.snackbarActive = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}