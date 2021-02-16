package zero.mybudget;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_PORTFOLIOS = "PORTFOLIOS";
    public static final String TABLE_CATEGORIES = "CATEGORIES";
    public static final String TABLE_CASHFLOW = "CASHFLOW";
    public static final String TABLE_RECURRENT = "RECURRENT";

    public static final String _ID = "_id";
    public static final String SUBJECT = "subject";
    public static final String DESC = "description";
    public static final String Q = "quantity";
    public static final String PORTF = "portfolios";
    public static final String FLAG = "flagSet";
    public static final String TIME = "time_flow";
    public static final String CAT = "category";
    public static final String PLACE = "place";
    public static final String NTIMES = "ntimes";

    static final String DB_NAME = "BUDGETTRACKER.DB";


    static final int DB_VERSION = 1;


    private static final String CREATE_TABLE_PORTFOLIO = "create table " + TABLE_PORTFOLIOS + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUBJECT + " TEXT NOT NULL, " + DESC + " TEXT, "+ FLAG + " INTEGER DEFAULT 0 );";
    private static final String CREATE_TABLE_CATEGORIES = "create table " + TABLE_CATEGORIES + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUBJECT + " TEXT NOT NULL );";
    private static final String CREATE_TABLE_CASHFLOW = "create table " + TABLE_CASHFLOW + " ( " + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Q + " REAL NOT NULL, " + PORTF + " TEXT NOT NULL, "
            + CAT+" TEXT NOT NULL, " + DESC +" TEXT, " + TIME+" TEXT, " + PLACE + " TEXT, " + " FOREIGN KEY ("+PORTF+") REFERENCES " + TABLE_PORTFOLIOS+"("+_ID+"),"+
            " FOREIGN KEY ("+CAT+") REFERENCES " + TABLE_CATEGORIES+ " ( " +_ID+ " ));";
    private static final String CREATE_TABLE_RECURRENT = "create table " + TABLE_RECURRENT + " ( " + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Q + " REAL NOT NULL, " + PORTF + " TEXT NOT NULL, "
            + CAT+" TEXT NOT NULL, " + DESC +" TEXT, " + TIME+" TEXT, " + PLACE + " TEXT, " + NTIMES +" INTEGER NOT NULL, "+ " FOREIGN KEY ("+PORTF+") REFERENCES " + TABLE_PORTFOLIOS+"("+_ID+"),"+
            " FOREIGN KEY ("+CAT+") REFERENCES " + TABLE_CATEGORIES+ " ( " +_ID+ " ));";
    private static final String CREATE_DEFAULT_PORTFOLIO = "INSERT OR REPLACE INTO " + TABLE_PORTFOLIOS +
            " (_id, subject,description,flagSet)" +
            " VALUES ('0','Portfolio1','Default Portfolio', 1); ";
    private static final String CREATE_DEFAULT_CATEGORIES = "INSERT OR REPLACE INTO " + TABLE_CATEGORIES +
            " (_id, subject)" +
            " VALUES ('0','Food'), ('1','Rent'), ('2','Salary'),('3','Savings'),('4','Bills'); ";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PORTFOLIO);
        db.execSQL(CREATE_DEFAULT_PORTFOLIO);
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_DEFAULT_CATEGORIES);
        db.execSQL(CREATE_TABLE_CASHFLOW);
        db.execSQL(CREATE_TABLE_RECURRENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PORTFOLIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CASHFLOW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECURRENT);
        onCreate(db);
    }

}