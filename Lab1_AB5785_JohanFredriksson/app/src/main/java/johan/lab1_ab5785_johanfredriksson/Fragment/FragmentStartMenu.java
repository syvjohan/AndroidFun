package johan.lab1_ab5785_johanfredriksson.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import johan.lab1_ab5785_johanfredriksson.R;

/**
 * Created by johan on 2014-11-22.
 */
public class FragmentStartMenu extends Fragment {
    Button btnLogin;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.startmenu_layout, container, false);
        return v;

         //Skapar knappen och findview byid samt onClick eventet.
         btnLogin = (Button)findViewById(R.id.btn_login);

        //Login fragment
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                FragmentMenu fMenu = new FragmentMenu();
                ft.replace(R.id.frag_startmenu, fMenu); //här ska fragmentet instansierar (newInstance) BlnankFragment1.newInstance("",""), "fragment1);
                ft.addToBackStack("frag login");
                ft.commit();
                Log.d("login", "login btn was pressed");
            }
        });
    }
}
