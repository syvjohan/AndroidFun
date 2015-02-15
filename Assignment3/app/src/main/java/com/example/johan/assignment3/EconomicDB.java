package com.example.johan.assignment3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by johan on 2/7/2015.
 */
public class EconomicDB extends SQLiteOpenHelper {

    private static final String TAG = EconomicDB.class.getSimpleName();
    private static String DB_NAME = "economicdb";
    private static int DB_VERSION = 1;

    private SQLiteDatabase database;

    public EconomicDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (DB_VERSION == 1) {
            db.execSQL(
                    "CREATE TABLE income" +
                            "(id TEXT PRIMARY KEY, amount TEXT, title TEXT, date TEXT)"
            );

            db.execSQL(
                    "CREATE TABLE expense" +
                            "(id TEXT PRIMARY KEY, amount TEXT, title TEXT, date TEXT)"
            );

            DB_VERSION = 2;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion < DB_VERSION) {
            this.onCreate(db);
        }
        System.out.println(DB_VERSION);
    }

    public ArrayList getAllIncomeContent() {
        ArrayList<Data> arrIncome = new ArrayList<>();
        Data data = new Data();

        openRead();
        Cursor cursor = database.rawQuery("SELECT * FROM income", null);
        int count = 0;
        if(cursor.getCount() > 0 && cursor.getCount() >= count) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                data.SetId(cursor.getString(0));
                data.SetAmount(cursor.getString(1));
                data.SetTitle(cursor.getString(2));
                data.SetDate(cursor.getString(3));

                arrIncome.add(data);
                count++;
            }
        }

        cursor.close();
        dbClose();

        return arrIncome;
    }

    public Data loadIncomeContent(String id) {
        Data data = new Data();
        String[] args={id};
        Cursor cursor = database.rawQuery("SELECT * FROM income WHERE id = ?", args);

        if(cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                data.SetId(cursor.getString(0));
                data.SetAmount(cursor.getString(1));
                data.SetTitle(cursor.getString(2));
                data.SetDate(cursor.getString(3));
            }
        }

        cursor.close();
        dbClose();

        return data;

    }

    public Data loadExpenseContent(String id) {
        Data data = new Data();
        String[] args={id};
        Cursor cursor = database.rawQuery("SELECT * FROM expense WHERE id = ?", args);

        if(cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                data.SetId(cursor.getString(0));
                data.SetAmount(cursor.getString(1));
                data.SetTitle(cursor.getString(2));
                data.SetDate(cursor.getString(3));
            }
        }

        cursor.close();
        dbClose();

        return data;
    }

    public synchronized boolean saveIncome(Data data) {
        ContentValues values = new ContentValues();
        values.put("id", data.GetId());
        values.put("amount", data.GetAmount());
        values.put("title", data.GetTitle());
        values.put("date", data.GetDate());
        boolean success = database.insertOrThrow("income", null, values) != -1;
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

    //Help methods!
    public void openWrite() {
        database = getWritableDatabase();
    }

    public void openRead() {
        database = getReadableDatabase();
    }

    public void dbClose() {
        database.close();
    }

}
