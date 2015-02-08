package com.example.johan.assignment3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

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

    public EconomicDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static EconomicDB getInstance() {
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(
                            "CREATE TABLE income (" +
                                    "id INTEGER PRIMARY KEY," +
                                    "timestamp INTEGER DEFAULT (strftime('%s', 'now')));"

            );
        db.execSQL(
                "CREATE TABLE expense (" +
                        "id INTEGER PRIMARY KEY," +
                        "timestamp INTEGER DEFAULT (strftime('%s', 'now')));"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 2) {
            database.execSQL(
                    "CREATE TABLE income (" +
                            "id INTEGET PRIMARY KEY," +
                            "timestamp INTEGER DEFAULT (strftime('%s', 'now')));"
            );
        }
    }

    private void open() {
        database = getWritableDatabase();
    }

    public String loadIncomeContent(int id) {
        String content = null;
        Cursor cursor = database.rawQuery("SELECT content FROM income WHERE id = ?;", new String[]{Integer.toString(id)});
        if (cursor.moveToFirst()) {
            content = cursor.getString(0);
        }
        cursor.close();
        return content;

    }

    public String loadExpenseContent(int id) {
        String content = null;
        Cursor cursor = database.rawQuery("SELECT content FROM expense WHERE id = ?;", new String[]{Integer.toString(id)});
        if (cursor.moveToFirst()) {
            content = cursor.getString(0);
        }
        cursor.close();
        return content;
    }

    public synchronized boolean saveIncome(Data data) {
        ContentValues values = new ContentValues();
        values.put("id", data.GetId());
        values.put("amount", data.GetAmount());
        values.put("title", data.GetTitle());
        values.put("date", data.GetDate());
        boolean success = database.insert("income", null, values) != -1;
        return success;
    }

    public synchronized boolean saveExpense(Data data) {
        ContentValues values = new ContentValues();
        values.put("id", data.GetId());
        values.put("amount", data.GetAmount());
        values.put("title", data.GetTitle());
        values.put("date", data.GetDate());
        boolean success = database.insert("expense", null, values) != -1;
        return success;
    }

}
