package zero.mybudget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class tabFragment1 extends Fragment {

    private DBManager dbManager;
    private ListView List;
    private SimpleCursorAdapter adapter;
    final String[] from = new String[] { DatabaseHelper._ID,DatabaseHelper.CAT,
            DatabaseHelper.DESC,DatabaseHelper.TIME, DatabaseHelper.Q };

    final int[] to = new int[] { R.id.id_category,R.id.tv_category, R.id.tv_description, R.id.tv_date1,R.id.tv_total };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_fragment1, container, false);

        dbManager = new DBManager(container.getContext());
        dbManager.open();
        Cursor cursor = dbManager.fetch_cash("day");

        List = (ListView) v.findViewById(R.id.list_view_tab1);

        adapter = new SimpleCursorAdapter(container.getContext(), R.layout.layout_cashflow_item, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                int getIndex = cursor.getColumnIndex(DatabaseHelper.Q);
                String empname = cursor.getString(getIndex);
                return false;
            }
        });
        List.setAdapter(adapter);

        List.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                TextView idTextView = (TextView) arg1.findViewById(R.id.id_category);
                TextView catTextView = (TextView) arg1.findViewById(R.id.tv_category);
                TextView descTextView = (TextView) arg1.findViewById(R.id.tv_description);
                TextView totTextView = (TextView) arg1.findViewById(R.id.tv_total);

                String id1 = idTextView.getText().toString();
                String cat = catTextView.getText().toString();
                String desc = descTextView.getText().toString();
                String total = totTextView.getText().toString();

                Intent modify_intent = new Intent(getActivity().getApplicationContext(), ModifyCashFlowActivity.class);
                modify_intent.putExtra("id", id1);
                modify_intent.putExtra("cat", cat);
                modify_intent.putExtra("desc", desc);
                modify_intent.putExtra("total", total);

                startActivity(modify_intent);
                return true;
            }
        });
        return v;


    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
