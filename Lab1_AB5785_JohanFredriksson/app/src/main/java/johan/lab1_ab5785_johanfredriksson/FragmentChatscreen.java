package johan.lab1_ab5785_johanfredriksson;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by johan on 2014-11-22.
 */
public class FragmentChatscreen extends Fragment {
    @Override
    public void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chatscreen_layout, container, false);
        return v;
    }
}
