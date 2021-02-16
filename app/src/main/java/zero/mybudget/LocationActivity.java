package zero.mybudget;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class LocationActivity extends BaseActivity implements OnMapReadyCallback{

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private GoogleMap mMap;
    private LatLngBounds bounds;
    private LatLngBounds.Builder builder;
    private DBManager dbManager;
    private ListView listView;
    private Button btnFrom,btnTo,btnSearch;
    private TextView Total;
    private int mYear, mMonth, mDay;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);// load icons from
        set(navMenuTitles, navMenuIcons);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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


                DatePickerDialog datePickerDialog = new DatePickerDialog(LocationActivity.this,
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


                DatePickerDialog datePickerDialog = new DatePickerDialog(LocationActivity.this,
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
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        final Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String fromZ, toZ;
                fromZ = btnFrom.getText().toString();
                toZ = btnTo.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date dateFrom = sdf.parse(fromZ);
                    Date dateTo = sdf.parse(toZ);
                    if (dateFrom.after(dateTo)) {
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.drawer_layout), "Incorrect Dates!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else {
                        Cursor cursor = dbManager.fetch_map_history(btnFrom.getText().toString(), btnTo.getText().toString());

                        cursor.moveToFirst();
                        ArrayList<String> time = new ArrayList<String>();
                        ArrayList<String> category = new ArrayList<String>();
                        ArrayList<String> location = new ArrayList<String>();
                        ArrayList<String> quantity = new ArrayList<String>();
                        while (!cursor.isAfterLast()) {
                            quantity.add(cursor.getString(cursor.getColumnIndex("quantity")));
                            time.add(cursor.getString(cursor.getColumnIndex("time_flow")));
                            location.add(cursor.getString(cursor.getColumnIndex("place")));
                            category.add(cursor.getString(cursor.getColumnIndex("category")));
                            cursor.moveToNext();
                        }
                        cursor.close();

                        mMap = googleMap;
                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        builder = new LatLngBounds.Builder();

                        for (int i = 0; i < quantity.size(); i++) {
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocationName(location.get(i), 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Address address = addresses.get(0);
                            double longitude = address.getLongitude();
                            double latitude = address.getLatitude();


                            drawMarker(new LatLng(latitude, longitude), category.get(i),quantity.get(i), time.get(i));

                            bounds = builder.build();
                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
                            mMap.animateCamera(cu);
                        }
                        }
                    }catch(java.text.ParseException e){
                        e.printStackTrace();

                }
            }
        });
    }


    private void drawMarker(LatLng point, String title, String quantity, String time) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point).title(title).snippet("Quantity: "+ quantity+ "€"+" Time: "+time).icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow));
        mMap.addMarker(markerOptions);
        builder.include(markerOptions.getPosition());

    }
}
