package nyx.com.cartracker;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import nyx.com.cartracker.helper.SharedPrefManager;

public class SplashActivity extends Activity {
    private MediaPlayer song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SharedPrefManager.getInstance(this).IsUserLoggedIn()){
            if(SharedPrefManager.getInstance(this).getUser().getUser_type()
                    .equals("2"))
                startActivity(new Intent(SplashActivity.this , HomeActivity.class));
            else
            startActivity(new Intent(SplashActivity.this , MainActivity.class));
            finish();
        }else {

            setContentView(R.layout.activity_splash);

            song = MediaPlayer.create(SplashActivity.this, R.raw.start);

            song.start();
            Thread timer = new Thread() {
                public void run() {
                    try {
                        sleep(6000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    } finally {
                        Intent s = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(s);
                        finish();
                    }
                }
            };
            timer.start();
        }
    }
}
