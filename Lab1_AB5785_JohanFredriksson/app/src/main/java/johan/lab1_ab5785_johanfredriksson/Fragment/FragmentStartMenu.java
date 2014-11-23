package johan.lab1_ab5785_johanfredriksson.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import johan.lab1_ab5785_johanfredriksson.R;

/**
 * Created by johan on 2014-11-22.
 */
public class FragmentStartMenu extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.startmenu_layout, container, false);
        return v;
    }
}
