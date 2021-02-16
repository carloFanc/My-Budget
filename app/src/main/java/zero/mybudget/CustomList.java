package zero.mybudget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<String>{

    private final Activity context;
    private final ArrayList<String> lunghezzaListview;
    public CustomList(Activity context,
                      ArrayList<String> lunghezzaListview) {
        super(context, R.layout.portfolios_view_record,lunghezzaListview);
        this.context = context;
        this.lunghezzaListview=lunghezzaListview;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {


            ArrayList<ArrayList<String>> listOfPortfolios = new ArrayList<ArrayList<String>>(Utils.getMyStaticListOfPortfolios());


            LayoutInflater inflater = context.getLayoutInflater();
            View rowView= inflater.inflate(R.layout.portfolios_view_record, null, true);
            TextView txtId = (TextView) rowView.findViewById(R.id.id);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.title);
            TextView txtDesc = (TextView) rowView.findViewById(R.id.desc);
            Button imgSet = (Button) rowView.findViewById(R.id.btnport);

            txtId.setText(listOfPortfolios.get(0).get(position));
            txtTitle.setText(listOfPortfolios.get(1).get(position));
            txtDesc.setText(listOfPortfolios.get(2).get(position));
        if(listOfPortfolios.get(3).get(position).equals("1")){
            imgSet.setVisibility(view.VISIBLE);
        }
             return rowView;

    }
}