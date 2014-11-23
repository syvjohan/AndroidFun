package johan.lab1_ab5785_johanfredriksson;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startmenu_layout);

        btnLogin = (Button)findViewById(R.id.btn_login);
        btnRegister = (Button)findViewById(R.id.btn_register_new_user);

        //Login fragment
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);

                FragmentStartMenu fStartMenu = new FragmentStartMenu();
                ft.add(R.id.frag_menu , fStartMenu);
                ft.commit();
            }
        });

        //Register fragment
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);

                FragmentRegistration fRegister = new FragmentRegistration();
                ft.add(R.id.frag_reg, fRegister);
                ft.commit();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;

        //Inflates the actionbar
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.menu_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
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
