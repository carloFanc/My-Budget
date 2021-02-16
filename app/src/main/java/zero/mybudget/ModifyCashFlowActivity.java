package zero.mybudget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class ModifyCashFlowActivity extends Activity implements View.OnClickListener { private EditText titleText;
    private Button updateBtn, deleteBtn;


    private long _id;

    private DBManager dbManager;
    private Spinner spinner;
    private EditText descText,total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Modify Record");

        setContentView(R.layout.cashflow_modify_record);

        dbManager = new DBManager(this);
        dbManager.open();

        descText = (EditText) findViewById(R.id.et_desc);
        spinner = (Spinner) findViewById(R.id.sp_cat);
        total = (EditText) findViewById(R.id.et_total);

        updateBtn = (Button) findViewById(R.id.btn_update_);
        deleteBtn = (Button) findViewById(R.id.btn_delete_);
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

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String cat = intent.getStringExtra("cat");
        String desc = intent.getStringExtra("desc");
        String total = intent.getStringExtra("total");

        _id = Long.parseLong(id);

        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update_:
                String catNew = spinner.getSelectedItem().toString();
                String descNew = descText.getText().toString();
                String totalNew = total.getText().toString();
                if(!catNew.equals("") && !descNew.equals("") && !totalNew.equals("") )
                    dbManager.update_cashflow(_id,descNew, catNew,totalNew);

                this.returnHome();
                break;

            case R.id.btn_delete_:
                dbManager.delete_cashflow(_id);
                this.returnHome();
                break;
        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), BudgetActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}

