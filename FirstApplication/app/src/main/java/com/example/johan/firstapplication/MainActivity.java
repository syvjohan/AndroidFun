package com.example.johan.firstapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Context;

public class MainActivity extends Activity {

    private Button btnTrue;
    private Button btnFalse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFalse = (Button)findViewById(R.id.btn_false);
        btnTrue = (Button)findViewById(R.id.btn_true);

        btnTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                CharSequence msg = "Yeay! true was pressed";
                Toast.makeText(context, msg, duration).show();

                Log.e("true", "true button was pressed");
            }
        });

        btnFalse.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               Context context = getApplicationContext();
               int duration = Toast.LENGTH_SHORT;
               CharSequence msg = "Yeay! false was pressed";
               Toast.makeText(context, msg, duration).show();

               Log.e("false","false button was pressed");
            }
        });

    }

   /* Context context = getApplicationContext();
    CharSequence text = "Hello toast!";
    int duration = Toast.LENGTH_SHORT;

    Toast toast = Toast.makeText(context, text, duration);
    toast.show();*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
