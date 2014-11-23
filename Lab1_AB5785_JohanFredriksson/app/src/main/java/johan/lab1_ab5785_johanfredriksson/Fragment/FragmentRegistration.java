package johan.lab1_ab5785_johanfredriksson.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import johan.lab1_ab5785_johanfredriksson.R;

/**
 * Created by johan on 2014-11-22.
 */
public class FragmentRegistration extends Fragment {
    /*@Override
    public void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("frag","enter fragment registration");
        View v = inflater.inflate(R.layout.registration_layout, container, false);
        return v;
    }
}
