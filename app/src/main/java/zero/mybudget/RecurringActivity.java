package zero.mybudget;

import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


public class RecurringActivity extends BaseActivity{

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private DBManager dbManager;

    private ListView listView;

    private SimpleCursorAdapter adapter;

    final String[] from = new String[] { DatabaseHelper._ID,DatabaseHelper.CAT,
            DatabaseHelper.DESC,DatabaseHelper.TIME, DatabaseHelper.Q, DatabaseHelper.NTIMES};

    final int[] to = new int[] { R.id.id_category,R.id.tv_category, R.id.tv_description, R.id.tv_date1, R.id.tv_total, R.id.tv_ntimes};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurring);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fabRecurrent);

        dbManager = new DBManager(this);
        dbManager.open();

        Cursor cursor = dbManager.fetch_recurrent();

        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));

        adapter = new SimpleCursorAdapter(this, R.layout.layout_recurrentcash_view, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                TextView idTextView = (TextView) arg1.findViewById(R.id.id_category);
                TextView catTextView = (TextView) arg1.findViewById(R.id.tv_category);
                TextView descTextView = (TextView) arg1.findViewById(R.id.tv_description);
                TextView ntimesTextView = (TextView) arg1.findViewById(R.id.tv_ntimes);
                TextView totTextView = (TextView) arg1.findViewById(R.id.tv_total);

                String id1 = idTextView.getText().toString();
                String cat = catTextView.getText().toString();
                String desc = descTextView.getText().toString();
                String total = totTextView.getText().toString();
                String ntimes= ntimesTextView.getText().toString();

                Intent modify_intent = new Intent(RecurringActivity.this, ModifyRecurringActivity.class);
                modify_intent.putExtra("id", id1);
                modify_intent.putExtra("cat", cat);
                modify_intent.putExtra("desc", desc);
                modify_intent.putExtra("ntimes", ntimes);
                modify_intent.putExtra("total", total);

                startActivity(modify_intent);
                return true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecurringActivity.this, AddRecurringActivity.class);
                startActivity(intent);
            }
        });
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