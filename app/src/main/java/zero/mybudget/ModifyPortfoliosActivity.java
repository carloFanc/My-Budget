package zero.mybudget;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class ModifyPortfoliosActivity extends Activity implements OnClickListener {

    private EditText titleText;
    private Button updateBtn, deleteBtn,setBtn;
    private EditText descText;

    private long _id;

    private DBManager dbManager;
    public static boolean snackbarActive;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Modify Record");

        setContentView(R.layout.portfolios_modify_record);
        dbManager = new DBManager(this);
        dbManager.open();

        titleText = (EditText) findViewById(R.id.subject_edittext);
        descText = (EditText) findViewById(R.id.description_edittext);

        updateBtn = (Button) findViewById(R.id.btn_update);
        deleteBtn = (Button) findViewById(R.id.btn_delete);
        setBtn = (Button) findViewById(R.id.btn_set);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");

        _id = Long.parseLong(id);

        titleText.setText(name);
        descText.setText(desc);

        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        setBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_update:
                String title = titleText.getText().toString();
                String desc = descText.getText().toString();
                if(!title.equals(""))
                dbManager.update_portfolio(_id, title, desc);

                this.returnHome();
                break;

            case R.id.btn_delete:

                title = titleText.getText().toString();
                sharedPref = getSharedPreferences("mypref", 0);
                if( !title.equals(sharedPref.getString("TitlePortfolio", ""))){
                    dbManager.delete_portfolio(_id);
                }else{
                Utils.setMySnackbarActive(true);
                }
                this.returnHome();
                break;
            case R.id.btn_set:

                title = titleText.getText().toString();
                sharedPref = getSharedPreferences("mypref", 0);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("TitlePortfolio", title);
                editor.commit();
                dbManager.updateSetFlagPortfolio(_id);

                this.returnHome();
                break;
        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), PortfoliosActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(0,0);
        startActivity(home_intent);
    }

}
