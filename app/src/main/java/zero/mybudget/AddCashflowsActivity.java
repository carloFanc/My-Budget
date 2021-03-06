package zero.mybudget;

        import android.annotation.TargetApi;
        import android.app.Activity;
        import android.app.DatePickerDialog;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Paint;
        import android.icu.util.Calendar;
        import android.os.Build;
        import android.os.Bundle;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.Spinner;

        import java.text.DateFormat;
        import java.text.SimpleDateFormat;


public class AddCashflowsActivity extends Activity implements OnClickListener {

    private Button dateBtn;
    private Integer THRESHOLD = 2;
    private DelayAutoCompleteTextView geo_autocomplete;

    private int mYear, mMonth, mDay;

    private DBManager dbManager;
    private EditText description,Desc,Total;
    private Button btnCancel,btnSave;
    private Spinner spinner;
    private DelayAutoCompleteTextView Location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("");

        setContentView(R.layout.cashflow_add_record);

         spinner = (Spinner) findViewById(R.id.sp_categories);
        DBManager dbm = new DBManager(this);
        String[] spinnerLists = dbm.getAllSpinnerContent();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinnerLists);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                return;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

          Desc = (EditText) findViewById(R.id.et_description);
          Location = (DelayAutoCompleteTextView) findViewById(R.id.geo_autocomplete);
          Total = (EditText) findViewById(R.id.et_total);
          btnCancel = (Button) findViewById(R.id.btn_cancel_cash);
          btnSave = (Button) findViewById(R.id.btn_save_cash);
          dateBtn = (Button) findViewById(R.id.btn_date);
          description = (EditText) findViewById(R.id.et_description);
          geo_autocomplete = (DelayAutoCompleteTextView) findViewById(R.id.geo_autocomplete);
          geo_autocomplete.setPaintFlags(geo_autocomplete.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        DateFormat df1=new SimpleDateFormat("dd-MM-yyyy");
        String datef= df1.format(java.util.Calendar.getInstance().getTime());
        dateBtn.setText(datef);


          dateBtn.setOnClickListener(new OnClickListener()
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


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddCashflowsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar cal = Calendar.getInstance();
                                c.set(year, monthOfYear,dayOfMonth);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                String cdate = formatter.format(c.getTime());
                               dateBtn.setText(cdate);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        geo_autocomplete = (DelayAutoCompleteTextView) findViewById(R.id.geo_autocomplete);
        geo_autocomplete.setThreshold(THRESHOLD);
        geo_autocomplete.setAdapter(new GeoAutoCompleteAdapter(this)); // 'this' is Activity instance

        geo_autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                GeoSearchResult result = (GeoSearchResult) adapterView.getItemAtPosition(position);
                geo_autocomplete.setText(result.getAddress());
            }
        });
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        dbManager = new DBManager(this);
        dbManager.open();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_cash:

                final String date = dateBtn.getText().toString();
                final String category = spinner.getSelectedItem().toString();
                final String desc = Desc.getText().toString();
                final String location = Location.getText().toString();
                final float total = Float.valueOf(Total.getText().toString());

                SharedPreferences sharedPref = getSharedPreferences("mypref", 0);
                final String portfolio = sharedPref.getString("TitlePortfolio", "");

                dbManager.insert_cashflow(date,portfolio,category,desc,location,total);

                Intent main = new Intent(AddCashflowsActivity.this, BudgetActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                

                startActivity(main);

                break;
            case R.id.btn_cancel_cash:
                finish();
                break;
        }
    }

}