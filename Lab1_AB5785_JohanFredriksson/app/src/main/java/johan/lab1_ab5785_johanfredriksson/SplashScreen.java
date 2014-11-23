package johan.lab1_ab5785_johanfredriksson;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by johan on 2014-11-23.
 */
public class SplashScreen extends Activity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_layout);

        new Handler().postDelayed(new Runnable() {

            @Override
        public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
