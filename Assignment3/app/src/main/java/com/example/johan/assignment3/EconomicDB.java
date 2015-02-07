package com.example.johan.assignment3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by johan on 2/7/2015.
 */
public class EconomicDB extends SQLiteOpenHelper {

    private static final String TAG = EconomicDB.class.getSimpleName();
    private static final String DB_NAME = "economicdb";
    private static final int DB_VERSION = 2;

    private static EconomicDB instance;
    private SQLiteDatabase database;

    public static void init(Context context) {
        instance = new EconomicDB(context.getApplicationContext());
        instance.open();
    }

    public static EconomicDB getInstance() {
        return instance;
    }

    private EconomicDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(
                            "CREATE TABLE data (" +
                                    "id INTEGER PRIMARY KEY," +

            );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void open() {
        database = getWritableDatabase();
    }

}
