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

public class CategoryActivity extends BaseActivity {
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private DBManager dbManager;

    private ListView listView;

    private SimpleCursorAdapter adapter;

    final String[] from = new String[] { DatabaseHelper._ID,
            DatabaseHelper.SUBJECT };

    final int[] to = new int[] { R.id.id_category, R.id.title_category };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);
        FloatingActionButton fabCategory = (FloatingActionButton)findViewById(R.id.fabCategory);
        dbManager = new DBManager(this);
        dbManager.open();

        Cursor cursor = dbManager.fetch_category();

        listView = (ListView) findViewById(R.id.list_view_category);
        listView.setEmptyView(findViewById(R.id.empty_category));

        adapter = new SimpleCursorAdapter(this, R.layout.category_view_record, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                TextView idTextView = (TextView) arg1.findViewById(R.id.id_category);
                TextView titleTextView = (TextView) arg1.findViewById(R.id.title_category);

                String id1 = idTextView.getText().toString();
                String title = titleTextView.getText().toString();

                Intent modify_intent = new Intent(getApplicationContext(), ModifyCategoriesActivity.class);
                modify_intent.putExtra("title", title);
                modify_intent.putExtra("id", id1);

                startActivity(modify_intent);
                return true;
            }
        });
        fabCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, AddCategoriesActivity.class);
                startActivity(intent);
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

}