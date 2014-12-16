package johan.lab1_ab5785_johanfredriksson.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import johan.lab1_ab5785_johanfredriksson.Fragment.FragmentLogin;
import johan.lab1_ab5785_johanfredriksson.Fragment.FragmentMenu;
import johan.lab1_ab5785_johanfredriksson.Fragment.FragmentRegistration;
import johan.lab1_ab5785_johanfredriksson.R;

/**
 * Created by johan on 2014-11-23.
 */
public class StartMenuActivity extends Activity implements FragmentLogin.OnFragmentInteractionListener {
    Button btnRegisterNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startmenu_layout);


        btnRegisterNewUser = (Button)findViewById(R.id.btn_register_new_user);



        //Register fragment
        btnRegisterNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fM = getFragmentManager();
                FragmentTransaction fT = fM.beginTransaction();
                FragmentRegistration fRegister = new FragmentRegistration();

                fT.replace(R.id.frag_startmenu, fRegister);
                fT.addToBackStack("frag reg new user");
                fT.commit();
                Log.d("reg","register new user btn was pressed");
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
