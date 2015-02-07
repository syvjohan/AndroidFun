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
import android.database.sqlite.*;


public class MainActivity extends ActionBarActivity implements
        IncomeFragment.OnFragmentInteractionListener,
        ExpensesFragment.OnFragmentInteractionListener,
        SummaryFragment.OnFragmentInteractionListener {

    @Override
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

            //INCOME
            case 0:
                IncomeFragment incomeFragment = IncomeFragment.newInstance("", "");
                FragmentManager fMI = getFragmentManager();
                FragmentTransaction fTI = fMI.beginTransaction();
                fTI.replace(R.id.container_main, incomeFragment, null);
                fTI.addToBackStack("got to Income fragment");
                fTI.commit();
                break;

            //EXPENSE
            case 1:
                ExpensesFragment expensesFragment = ExpensesFragment.newInstance("", "");
                FragmentManager fME = getFragmentManager();
                FragmentTransaction fTE = fME.beginTransaction();
                fTE.replace(R.id.container_main, expensesFragment, null);
                fTE.addToBackStack("got to Income fragment");
                fTE.commit();
                break;

            //SUMMARY
            case 2:
                ExpensesFragment summaryFragment = ExpensesFragment.newInstance("", "");
                FragmentManager fMS = getFragmentManager();
                FragmentTransaction fTS = fMS.beginTransaction();
                fTS.replace(R.id.container_main, summaryFragment, null);
                fTS.addToBackStack("got to Income fragment");
                fTS.commit();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.add("Income");
        menu.add("Expense");
        menu.add("Summary");

        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
