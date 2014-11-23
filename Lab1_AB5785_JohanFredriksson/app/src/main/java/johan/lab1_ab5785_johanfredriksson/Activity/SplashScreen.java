package johan.lab1_ab5785_johanfredriksson.Activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import johan.lab1_ab5785_johanfredriksson.R;

/**
 * Created by johan on 2014-11-23.
 */
public class SplashScreen extends Activity {
    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_layout);

        new Handler().postDelayed(new Runnable() {
            @Override
        public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                SplashScreen.this.startActivity(i); //make it possible to move on to next layout.
                SplashScreen.this.finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
