package com.example.johan.assignment3;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import java.lang.reflect.Field;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements
        IncomeFragment.OnFragmentInteractionListener,
        ExpenseFragment.OnFragmentInteractionListener,
        SummaryFragment.OnFragmentInteractionListener {

    private final int INCOME_MENU_ITEM = Menu.FIRST;
    private final int EXPENSE_MENU_ITEM = INCOME_MENU_ITEM + 1;
    private final int SUMMARY_MENU_ITEM = EXPENSE_MENU_ITEM + 1;
    private double income = 0;
    private double expense = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getOverflowMenu();
    }

    private void getOverflowMenu() {

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

            case INCOME_MENU_ITEM:
                IncomeFragment incomeFragment = IncomeFragment.newInstance("", "");
                FragmentManager fMI = getFragmentManager();
                FragmentTransaction fTI = fMI.beginTransaction();
                fTI.replace(R.id.container_main, incomeFragment, null);
                fTI.addToBackStack("got to Income fragment");
                fTI.commit();
                break;

            case EXPENSE_MENU_ITEM:
                ExpenseFragment expenseFragment = ExpenseFragment.newInstance("", "");
                FragmentManager fME = getFragmentManager();
                FragmentTransaction fTE = fME.beginTransaction();
                fTE.replace(R.id.container_main, expenseFragment, null);
                fTE.addToBackStack("got to Expense fragment");
                fTE.commit();
                break;

            case SUMMARY_MENU_ITEM:
                SummaryFragment summaryFragment = SummaryFragment.newInstance("", "");

                Bundle b = new Bundle();
                b.putDouble("income", income);
                b.putDouble("expense", expense);
                summaryFragment.setArguments(b);

                FragmentManager fMS = getFragmentManager();
                FragmentTransaction fTS = fMS.beginTransaction();
                fTS.replace(R.id.container_main, summaryFragment, null);
                fTS.addToBackStack("got to Summary fragment");
                fTS.commit();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void GetData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.add(0, INCOME_MENU_ITEM, 0 ,"Income");
        menu.add(0, EXPENSE_MENU_ITEM, 0,"Expense");
        menu.add(0, SUMMARY_MENU_ITEM, 0, "Summary");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
