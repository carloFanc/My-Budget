package zero.mybudget;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public final class Utils {
    static ArrayList<ArrayList<String>> listOfPortfolios = new ArrayList<ArrayList<String>>();
    static ArrayList<String> _id = new ArrayList<String>();
    static ArrayList<String> subject = new ArrayList<String>();
    static ArrayList<String> desk = new ArrayList<String>();
    static ArrayList<String> flgSet = new ArrayList<String>();
    static boolean snackbarActive = false;


    private Utils() {
    }

    public static void setMyStaticListOfPortfolios(Context c) {
        setupAndClean();
        DatabaseHelper dbHelper;
        dbHelper = new DatabaseHelper(c);
        SQLiteDatabase database;
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query("PORTFOLIOS", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                listOfPortfolios.get(0).add(cursor.getString(0));
                listOfPortfolios.get(1).add(cursor.getString(1));
                listOfPortfolios.get(2).add(cursor.getString(2));
                listOfPortfolios.get(3).add(cursor.getString(3));

            }
            while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    private static void setupAndClean() {
       if(!_id.isEmpty()){
        _id.clear();
        subject.clear();
        desk.clear();
        flgSet.clear();
       }
        if (listOfPortfolios.isEmpty()) {
            listOfPortfolios.add(_id);
            listOfPortfolios.add(subject);
            listOfPortfolios.add(desk);
            listOfPortfolios.add(flgSet);
        }
    }

    public static ArrayList<ArrayList<String>> getMyStaticListOfPortfolios() {
        return listOfPortfolios;
    }

    public static void setMySnackbarActive(boolean flag) {
        snackbarActive = flag;
    }

    public static boolean getMySnackbarActive() {
        return snackbarActive;
    }
    public static void recurrentTransaction(Context co) {
        ArrayList<String[]> AllValues ;
        DBManager dbManager= new DBManager(co);
        dbManager.open();
        DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");

        SharedPreferences sharedPref = co.getSharedPreferences("actday", 0);
        String daypreference = sharedPref.getString("ActualDay", "");
        AllValues = dbManager.getTotalRecurrent();
        boolean flag=false;
        for(int i= 0; i< AllValues.get(1).length;i++){

            try {
                Date startDate,finalDate,ActualDayDate;
                do{

                    startDate = df1.parse(AllValues.get(6)[i]); //data da input

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(startDate);
                    cal.add(Calendar.DATE, Integer.parseInt(AllValues.get(7)[i])); //aggiunto tot giorni
                    finalDate = cal.getTime(); // data in cui deve scattare l'inserimento
                    ActualDayDate = df1.parse(daypreference); //data di oggi
                    if(finalDate.compareTo(ActualDayDate)<0 || finalDate.compareTo(ActualDayDate)==0) {
                        dbManager.insert_cashflow(df1.format(finalDate), AllValues.get(2)[i], AllValues.get(1)[i], AllValues.get(3)[i], AllValues.get(5)[i], Float.parseFloat(AllValues.get(4)[i]));
                        dbManager.updateRicurrentDate(Long.parseLong(AllValues.get(0)[i]), df1.format(finalDate));
                        AllValues.get(6)[i] = df1.format(finalDate);
                        flag= true;
                    }else{
                        flag=false;
                    }
                }while(flag);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
    }
}