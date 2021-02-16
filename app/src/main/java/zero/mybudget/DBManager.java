package zero.mybudget;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DBManager {
    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert_portfolio(String name, String desc) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.SUBJECT, name);
        contentValue.put(DatabaseHelper.DESC, desc);
        database.insert(DatabaseHelper.TABLE_PORTFOLIOS, null, contentValue);
    }
    public void insert_category(String name ) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.SUBJECT, name);
        database.insert(DatabaseHelper.TABLE_CATEGORIES, null, contentValue);
    }
    public void insert_default_category(String name ) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.SUBJECT, name);
        database.replace(DatabaseHelper.TABLE_CATEGORIES, null, contentValue);
    }
    public void insert_cashflow(String date, String portf,String category, String desc, String location, float total) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.Q, total);
        contentValue.put(DatabaseHelper.PORTF, portf);
        contentValue.put(DatabaseHelper.CAT, category);
        contentValue.put(DatabaseHelper.DESC, desc);
        contentValue.put(DatabaseHelper.TIME, date);
        contentValue.put(DatabaseHelper.PLACE, location);
        database.insert(DatabaseHelper.TABLE_CASHFLOW, null, contentValue);
   }
    public void insert_recurring(String date, String portf,String category, String desc, String location, float total,String ntimes) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.Q, total);
        contentValue.put(DatabaseHelper.PORTF, portf);
        contentValue.put(DatabaseHelper.CAT, category);
        contentValue.put(DatabaseHelper.DESC, desc);
        contentValue.put(DatabaseHelper.TIME, date);
        contentValue.put(DatabaseHelper.PLACE, location);
        contentValue.put(DatabaseHelper.NTIMES, Integer.parseInt(ntimes));
        database.insert(DatabaseHelper.TABLE_RECURRENT, null, contentValue);
    }

    public Cursor fetch_category() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.SUBJECT };
        Cursor cursor = database.query(DatabaseHelper.TABLE_CATEGORIES, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor fetch_history(String datefrom,String dateto) {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.CAT, DatabaseHelper.DESC,DatabaseHelper.TIME,DatabaseHelper.Q };
        SharedPreferences sharedPref = context.getSharedPreferences("mypref", 0);
        final String portfolio = sharedPref.getString("TitlePortfolio", "");

        Cursor cursor = database.query(DatabaseHelper.TABLE_CASHFLOW, columns, "time_flow between ? and ? and portfolios = ?", new String[]{datefrom,dateto, portfolio}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor fetch_recurrent() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.CAT, DatabaseHelper.DESC,DatabaseHelper.TIME,DatabaseHelper.Q, DatabaseHelper.NTIMES };
        SharedPreferences sharedPref = context.getSharedPreferences("mypref", 0);
        final String portfolio = sharedPref.getString("TitlePortfolio", "");

        Cursor cursor = database.query(DatabaseHelper.TABLE_RECURRENT, columns, "portfolios = ?", new String[]{portfolio}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor fetch_map_history(String datefrom,String dateto) {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.CAT,DatabaseHelper.PLACE,DatabaseHelper.TIME,DatabaseHelper.Q };
        SharedPreferences sharedPref = context.getSharedPreferences("mypref", 0);
        final String portfolio = sharedPref.getString("TitlePortfolio", "");

        Cursor cursor = database.query(DatabaseHelper.TABLE_CASHFLOW, columns, "time_flow between ? and ? and portfolios = ?", new String[]{datefrom,dateto, portfolio}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetch_cash(String tabMode) {
        DateFormat df1=new SimpleDateFormat("dd-MM-yyyy");
        String dateToday= df1.format(Calendar.getInstance().getTime());

        SharedPreferences sharedPref = context.getSharedPreferences("mypref", 0);
        final String portfolio = sharedPref.getString("TitlePortfolio", "");
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.CAT, DatabaseHelper.DESC,DatabaseHelper.TIME,DatabaseHelper.Q };
        Cursor cursor = database.query(DatabaseHelper.TABLE_CASHFLOW, columns, "time_flow = ? and portfolios = ?", new String[]{dateToday, portfolio}, null, null, null);
        if(tabMode.equals("week")) {
            Calendar c = Calendar.getInstance();
            DateFormat df3=new SimpleDateFormat("dd-MM-yyyy");
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            String startDate = "", endDate = "";
            startDate = df3.format(c.getTime());
            c.add(Calendar.DATE, 6);
            endDate = df3.format(c.getTime());
            cursor = database.query(DatabaseHelper.TABLE_CASHFLOW, columns, "time_flow between ? and ? and portfolios = ?", new String[]{startDate,endDate, portfolio}, null, null, null);
        }else if(tabMode.equals("month")){
            String startDate = "", endDate = "";
            Calendar c = Calendar.getInstance();
            DateFormat df2=new SimpleDateFormat("dd-MM-yyyy");
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = 1;
            c.set(year, month, day);
            int numOfDaysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            startDate = df2.format(c.getTime());
            c.add(Calendar.DAY_OF_MONTH, numOfDaysInMonth-1);
            endDate = df2.format(c.getTime());
            cursor = database.query(DatabaseHelper.TABLE_CASHFLOW, columns, "time_flow between ? and ? and portfolios = ?", new String[]{startDate,endDate, portfolio}, null, null, null);
        }

            if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    public void update_portfolio(long _id, String name, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SUBJECT, name);
        contentValues.put(DatabaseHelper.DESC, desc);
        database.update(DatabaseHelper.TABLE_PORTFOLIOS, contentValues, DatabaseHelper._ID + " = " + _id, null);

    }
    public void update_category(long _id, String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SUBJECT, name);
         database.update(DatabaseHelper.TABLE_CATEGORIES, contentValues, DatabaseHelper._ID + " = " + _id, null);

    }
    public void update_cashflow(long _id, String desc, String cat, String total){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.DESC, desc);
        contentValues.put(DatabaseHelper.CAT, cat);
        contentValues.put(DatabaseHelper.Q, total);
        database.update(DatabaseHelper.TABLE_CASHFLOW, contentValues, DatabaseHelper._ID + " = " + _id, null);
    }
    public void update_recurring(long _id, String desc, String cat, String total, String ntimez){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.DESC, desc);
        contentValues.put(DatabaseHelper.CAT, cat);
        contentValues.put(DatabaseHelper.Q, total);
        contentValues.put(DatabaseHelper.NTIMES, Integer.parseInt(ntimez));
        database.update(DatabaseHelper.TABLE_RECURRENT, contentValues, DatabaseHelper._ID + " = " + _id, null);
    }
    public void updateSetFlagPortfolio(long _id){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.FLAG, 0);
        database.update(DatabaseHelper.TABLE_PORTFOLIOS, contentValues, null, null);
        contentValues.put(DatabaseHelper.FLAG, 1);
        database.update(DatabaseHelper.TABLE_PORTFOLIOS, contentValues, DatabaseHelper._ID + " = " + _id, null);
    }
    public void delete_portfolio(long _id) {
            database.delete(DatabaseHelper.TABLE_PORTFOLIOS, DatabaseHelper._ID + "=" + _id, null);
    }
    public void delete_category(long _id) {
        database.delete(DatabaseHelper.TABLE_CATEGORIES, DatabaseHelper._ID + "=" + _id, null);
    }
    public void delete_cashflow(long _id) {
        database.delete(DatabaseHelper.TABLE_CASHFLOW, DatabaseHelper._ID + "=" + _id, null);
    }
    public void delete_recurring(long _id) {
        database.delete(DatabaseHelper.TABLE_RECURRENT, DatabaseHelper._ID + "=" + _id, null);
    }
    public float getTotalHistory(String from, String to){
        float total=0;
        ArrayList<Float> intValues = new ArrayList<Float>();

        SharedPreferences sharedPref = context.getSharedPreferences("mypref", 0);
        final String portfolio = sharedPref.getString("TitlePortfolio", "");

        Cursor result = database.query(DatabaseHelper.TABLE_CASHFLOW,
                new String[] { DatabaseHelper.Q }, "time_flow between ? and ? and portfolios = ?", new String[]{from,to, portfolio}, null, null, null);

        if (result.moveToFirst()) {
            do {
                intValues.add(result.getFloat(result
                        .getColumnIndex(DatabaseHelper.Q)));
            } while (result.moveToNext());
        }
        int i;
        for(i = 0; i < intValues.size(); i++)
            total += intValues.get(i);
        return total;
    }

    public float getTotalDay(){
        float total=0;
        ArrayList<Float> intValues = new ArrayList<Float>();
        DateFormat df1=new SimpleDateFormat("dd-MM-yyyy");
        String dateToday= df1.format(Calendar.getInstance().getTime());

        SharedPreferences sharedPref = context.getSharedPreferences("mypref", 0);
        final String portfolio = sharedPref.getString("TitlePortfolio", "");

        Cursor result = database.query(DatabaseHelper.TABLE_CASHFLOW,
                new String[] { DatabaseHelper.Q }, "time_flow = ? and portfolios = ?", new String[]{dateToday, portfolio}, null, null, null);

        if (result.moveToFirst()) {
            do {

                intValues.add(result.getFloat(result
                        .getColumnIndex(DatabaseHelper.Q)));

            } while (result.moveToNext());
        }
        int i;
        for(i = 0; i < intValues.size(); i++)
            total += intValues.get(i);
        return total;
    }
    public float getTotalWeek(){
        float total=0;
        ArrayList<Float> intValues = new ArrayList<Float>();
        SharedPreferences sharedPref = context.getSharedPreferences("mypref", 0);
        final String portfolio = sharedPref.getString("TitlePortfolio", "");

        Calendar c = Calendar.getInstance();
        DateFormat df3=new SimpleDateFormat("dd-MM-yyyy");
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String startDate = "", endDate = "";
        startDate = df3.format(c.getTime());
        c.add(Calendar.DATE, 6);
        endDate = df3.format(c.getTime());
        Cursor result = database.query(DatabaseHelper.TABLE_CASHFLOW, new String[]{DatabaseHelper.Q}, "time_flow between ? and ? and portfolios = ?", new String[]{startDate,endDate, portfolio}, null, null, null);

        if (result.moveToFirst()) {
            do {
                intValues.add(result.getFloat(result
                        .getColumnIndex(DatabaseHelper.Q)));
            } while (result.moveToNext());
        }
        int i;
        for(i = 0; i < intValues.size(); i++)
            total += intValues.get(i);
        return total;
    }
    public float getTotalMonth(){
    float total=0;
    ArrayList<Float> intValues = new ArrayList<Float>();
    SharedPreferences sharedPref = context.getSharedPreferences("mypref", 0);
    final String portfolio = sharedPref.getString("TitlePortfolio", "");

    String startDate = "", endDate = "";
    Calendar c = Calendar.getInstance();
    DateFormat df2=new SimpleDateFormat("dd-MM-yyyy");
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = 1;
    c.set(year, month, day);
    int numOfDaysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
    startDate = df2.format(c.getTime());
    c.add(Calendar.DAY_OF_MONTH, numOfDaysInMonth-1);
    endDate = df2.format(c.getTime());
    Cursor result = database.query(DatabaseHelper.TABLE_CASHFLOW, new String[]{DatabaseHelper.Q}, "time_flow between ? and ? and portfolios = ?", new String[]{startDate,endDate, portfolio}, null, null, null);

    if (result.moveToFirst()) {
        do {
            intValues.add(result.getFloat(result
                    .getColumnIndex(DatabaseHelper.Q)));
        } while (result.moveToNext());
    }
    int i;
    for(i =0; i < intValues.size(); i++)
    total += intValues.get(i);
    return total;
}
public float[] getPercentageCategories(String from, String to){

    float total=0;
    ArrayList<Float> floatValues = new ArrayList<Float>();
    SharedPreferences sharedPref = context.getSharedPreferences("mypref", 0);
    final String portfolio = sharedPref.getString("TitlePortfolio", "");
    Cursor result1 = database.query(DatabaseHelper.TABLE_CASHFLOW, new String[]{DatabaseHelper.Q}, "time_flow between ? and ? and portfolios = ?", new String[]{from,to, portfolio}, null, null, null);
    if (result1.moveToFirst()) {
        do {
            floatValues.add(result1.getFloat(result1.getColumnIndex(DatabaseHelper.Q)));
        } while (result1.moveToNext());
    }
    int i;
    for(i =0; i < floatValues.size(); i++) {
        if(floatValues.get(i)<0){
            total += floatValues.get(i) * -1;
        }else{
            total += floatValues.get(i);
        }

    }

    String[] cat = getCategoriesList(from,to);
    ArrayList<Float> temp = new ArrayList<Float>();
    ArrayList<Float> singleCat = new ArrayList<Float>();
    for(i=0;i< cat.length;i++){
        float tempTotal = 0;
        temp.clear();
        Cursor result2 = database.query(DatabaseHelper.TABLE_CASHFLOW, new String[]{DatabaseHelper.Q}, "time_flow between ? and ? and portfolios = ? and category = ?", new String[]{from,to, portfolio,cat[i]}, null, null, null);

        if (result2.moveToFirst()) {
            do {
                temp.add(result2.getFloat(result2.getColumnIndex(DatabaseHelper.Q)));
            } while (result2.moveToNext());
        }
        for(int j =0; j < temp.size(); j++)
            if(temp.get(j) < 0){
                tempTotal += temp.get(j) * -1;
            }else{
                tempTotal += temp.get(j);
            }

        singleCat.add(tempTotal);
    }
    Float[] singleCatArray = new Float[singleCat.size()];
    singleCatArray = singleCat.toArray(singleCatArray);
    float[] per = new float[cat.length];
    for(i=0;i< cat.length;i++){
        per[i]= singleCatArray[i]/(total/100);

    }
    return per;

}
    public String[] getCategoriesList(String datefrom,String dateto) {
        String[] categories= {};
        ArrayList<String> catValues = new ArrayList<String>();
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.CAT };
        SharedPreferences sharedPref = context.getSharedPreferences("mypref", 0);
        final String portfolio = sharedPref.getString("TitlePortfolio", "");

        Cursor cursor = database.query(DatabaseHelper.TABLE_CASHFLOW, columns, "time_flow between ? and ? and portfolios = ?", new String[]{datefrom,dateto, portfolio}, DatabaseHelper.CAT, null, null);
        if (cursor.moveToFirst()) {
            do {
                catValues.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CAT)));
            } while (cursor.moveToNext());
        }
            categories =catValues.toArray(new String[catValues.size()]);

        return categories;
    }


    public String[] exportRecordPdf(String from, String to, String j){
        ArrayList<String> cat = new ArrayList<String>();
        ArrayList<String> port = new ArrayList<String>();
        ArrayList<String> quant = new ArrayList<String>();
        ArrayList<String> desc = new ArrayList<String>();
        ArrayList<String> time = new ArrayList<String>();
        ArrayList<String> place = new ArrayList<String>();

        SharedPreferences sharedPref = context.getSharedPreferences("mypref", 0);
        final String portfolio = sharedPref.getString("TitlePortfolio", "");
        Cursor result1 = database.query(DatabaseHelper.TABLE_CASHFLOW, new String[]{DatabaseHelper.CAT,DatabaseHelper.PORTF,DatabaseHelper.DESC,DatabaseHelper.Q,DatabaseHelper.PLACE,DatabaseHelper.TIME}, "time_flow between ? and ? and portfolios = ?", new String[]{from,to, portfolio}, null, null, null);
        if (result1 != null) {
            if (result1.moveToFirst()) {
                do {
                    cat.add(result1.getString(result1.getColumnIndex(DatabaseHelper.CAT)));
                    port.add(result1.getString(result1.getColumnIndex(DatabaseHelper.PORTF)));
                    quant.add(result1.getString(result1.getColumnIndex(DatabaseHelper.Q)));
                    desc.add(result1.getString(result1.getColumnIndex(DatabaseHelper.DESC)));
                    time.add(result1.getString(result1.getColumnIndex(DatabaseHelper.TIME)));
                    place.add(result1.getString(result1.getColumnIndex(DatabaseHelper.PLACE)));
                } while (result1.moveToNext());
            }
            int i;
            String[] arrayRecordPos = new String[cat.size()];
            String[] arrayRecordNeg = new String[cat.size()];
            for (i = 0; i < cat.size(); i++) {
                if(Float.parseFloat(quant.get(i))>=0) {
                    arrayRecordPos[i] = "Category: " + cat.get(i) + ", Portfolio: " + port.get(i) + ", Quantity: " + quant.get(i) + "€, Description: " + desc.get(i) + ", Time: " + time.get(i) + ", Location: " + place.get(i);
                }else{
                    arrayRecordNeg[i] = "Category: " + cat.get(i) + ", Portfolio: " + port.get(i) + ", Quantity: " + quant.get(i) + "€, Description: " + desc.get(i) + ", Time: " + time.get(i) + ", Location: " + place.get(i);
                }
                }
                if(j.equals("positive")){
                    return arrayRecordPos;
                }else{
                    return arrayRecordNeg;
                }

        }else{
            return null;
        }

    }

    public String[] getAllSpinnerContent() {
        String[] columns = new String[] {  DatabaseHelper.SUBJECT };
        open();
        Cursor cursor2 = database.query("CATEGORIES",columns , null, null, null, null, null);

        ArrayList<String> spinnerContent = new ArrayList<String>();
        if(cursor2.moveToFirst()){
            do{
                String word = cursor2.getString(cursor2.getColumnIndexOrThrow("subject"));
                spinnerContent.add(word);
            }while(cursor2.moveToNext());
        }
        cursor2.close();

        String[] allSpinner = new String[spinnerContent.size()];
        allSpinner = spinnerContent.toArray(allSpinner);

        return allSpinner;
    }
    public ArrayList<String[]> getTotalRecurrent(){
        float total=0;
        ArrayList<String[]> Values = new ArrayList<String[]>();
        ArrayList<String> id = new ArrayList<String>();
        ArrayList<String> cat = new ArrayList<String>();
        ArrayList<String> portf = new ArrayList<String>();
        ArrayList<String> desc = new ArrayList<String>();
        ArrayList<String> q = new ArrayList<String>();
        ArrayList<String> place = new ArrayList<String>();
        ArrayList<String> time = new ArrayList<String>();
        ArrayList<String> ntimes = new ArrayList<String>();
        SharedPreferences sharedPref = context.getSharedPreferences("mypref", 0);
        final String portfolio = sharedPref.getString("TitlePortfolio", "");

        Cursor result = database.query(DatabaseHelper.TABLE_RECURRENT,
                new String[] {  DatabaseHelper._ID,DatabaseHelper.CAT,DatabaseHelper.PORTF,DatabaseHelper.DESC,DatabaseHelper.Q,DatabaseHelper.PLACE,DatabaseHelper.TIME,DatabaseHelper.NTIMES}, "portfolios = ?", new String[]{portfolio}, null, null, null);

        if (result.moveToFirst()) {
            do {
                id.add(result.getString(result.getColumnIndex(DatabaseHelper._ID)));
                cat.add(result.getString(result.getColumnIndex(DatabaseHelper.CAT)));
                portf.add(result.getString(result.getColumnIndex(DatabaseHelper.PORTF)));
                desc.add(result.getString(result.getColumnIndex(DatabaseHelper.DESC)));
                q.add(result.getString(result.getColumnIndex(DatabaseHelper.Q)));
                place.add(result.getString(result.getColumnIndex(DatabaseHelper.PLACE)));
                time.add(result.getString(result.getColumnIndex(DatabaseHelper.TIME)));
                ntimes.add(result.getString(result.getColumnIndex(DatabaseHelper.NTIMES)));
            } while (result.moveToNext());
        }
        String[] id1 = new String[id.size()];
        String[] cat1 = new String[cat.size()];
        String[] portf1 = new String[portf.size()];
        String[] desc1 = new String[desc.size()];
        String[] q1 = new String[q.size()];
        String[] place1 = new String[place.size()];
        String[] time1 = new String[time.size()];
        String[] ntimes1 = new String[ntimes.size()];

        for (int i = 0; i < id.size(); i++) {
            id1[i] = id.get(i);
            cat1[i] = cat.get(i);
            portf1[i] = portf.get(i);
            desc1[i] = desc.get(i);
            q1[i] = q.get(i);
            place1[i] = place.get(i);
            time1[i] = time.get(i);
            ntimes1[i] = ntimes.get(i);
        }

        Values.add(id1);
        Values.add(cat1);
        Values.add(portf1);
        Values.add(desc1);
        Values.add(q1);
        Values.add(place1);
        Values.add(time1);
        Values.add(ntimes1);
        return Values;
    }

    public void updateRicurrentDate(long _id, String dates){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TIME, dates);
        database.update(DatabaseHelper.TABLE_RECURRENT, contentValues, DatabaseHelper._ID + " = " + _id, null);
    }
}