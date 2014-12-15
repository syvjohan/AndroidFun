package com.example.johan.firstapplication.SecondActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.johan.firstapplication.R;

/**
 * Created by johan on 11/18/2014.
 */
public class SecondActivity extends Activity {
    private Button btnBack;
    SecondActivity sa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toast_layout);

        btnBack = (Button)findViewById(R.id.btn_back);
        sa = this;

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sa.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
