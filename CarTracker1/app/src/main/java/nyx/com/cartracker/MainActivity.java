package nyx.com.cartracker;

import android.app.Activity;
import android.os.Bundle;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import nyx.com.cartracker.helper.SharedPrefManager;

public class MainActivity extends RootActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);


        ((TextView)findViewById(R.id.welcome_text)).setText("Welcome " +
                SharedPrefManager.getInstance(this).getUser().getName()
        );
        ((TextView)findViewById(R.id.user_type)).setText(
                SharedPrefManager.getInstance(this).getUser().getUser_type().equals("1")?"Owner":"Driver"
        );
        ((Button)findViewById(R.id.add_car)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , AddCar.class));
            }
        });


        ((Button)findViewById(R.id.go_speed)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , HomeActivity.class));
            }
        });
        ((Button)findViewById(R.id.go_location)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , MapsActivity.class));
            }
        });

        ((Button)findViewById(R.id.add_driver)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , AddDriver.class));
            }
        });

        ((Button)findViewById(R.id.my_cars_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , MyCars.class));
            }
        });

//        ((Button)findViewById(R.id.driver_cars_btn)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this , DriverCars.class));
//            }
//        });


        ((Button)findViewById(R.id.go_logout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           SharedPrefManager.getInstance(MainActivity.this).Logout();
                Toast.makeText(MainActivity.this, "you are logged out..", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this , LoginActivity.class));
                finish();
            }
        });


    }
}
