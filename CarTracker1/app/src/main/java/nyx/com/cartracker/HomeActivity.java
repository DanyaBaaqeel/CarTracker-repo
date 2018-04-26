package nyx.com.cartracker;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import nyx.com.cartracker.helper.SharedPrefManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
public class HomeActivity extends RootActivity implements IBaseGpsListener {
double ALLOWED_SPEED=16.6667;

    public void init(){

        String permission = Manifest.permission.ACCESS_COARSE_LOCATION;
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        boolean b1= (res == PackageManager.PERMISSION_GRANTED);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.updateSpeed(null);

        CheckBox chkUseMetricUntis = (CheckBox) this.findViewById(R.id.chkMetricUnits);
        chkUseMetricUntis.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                HomeActivity.this.updateSpeed(null);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                 init();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(HomeActivity.this, "لا يمكن المتابعة بدون هذه السماحية", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }}

    private boolean check_READ_PHONE_STATE_Permission()
    {
        String permission = Manifest.permission.ACCESS_COARSE_LOCATION;
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        boolean b1= (res == PackageManager.PERMISSION_GRANTED);

        permission = Manifest.permission.ACCESS_FINE_LOCATION;
        res = getApplicationContext().checkCallingOrSelfPermission(permission);
        boolean b2= (res == PackageManager.PERMISSION_GRANTED);

        permission = Manifest.permission.VIBRATE;
        res = getApplicationContext().checkCallingOrSelfPermission(permission);
        boolean b3= (res == PackageManager.PERMISSION_GRANTED);
        permission = Manifest.permission.WAKE_LOCK;
        res = getApplicationContext().checkCallingOrSelfPermission(permission);
        boolean b4= (res == PackageManager.PERMISSION_GRANTED);

        return b1 && b2 && b3;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        findViewById(R.id.chkMetricUnits).setVisibility(View.GONE);

        if( SharedPrefManager.getInstance(this).IsUserLoggedIn() &&
                SharedPrefManager.getInstance(this).getUser().getUser_type()
        .equals("2")){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }else{
            Toast.makeText(this, "you are not driving now.", Toast.LENGTH_SHORT).show();
        }
        ((TextView)findViewById(R.id.welcome_text)).setText("Welcome " +
                SharedPrefManager.getInstance(this).getUser().getName()
        );
        ((TextView)findViewById(R.id.user_type)).setText(
                SharedPrefManager.getInstance(this).getUser().getUser_type().equals("1")?"Owner":"Driver"
        );
        ((TextView)findViewById(R.id.max_speed_allowed)).setText(
                "Allowed speed : " + ALLOWED_SPEED + " Meters/second"
        );



        if(!check_READ_PHONE_STATE_Permission()) {
            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION ,
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.VIBRATE
                            , Manifest.permission.WAKE_LOCK

                          },
                    1);
        }else
            init();
    }

    public void finish()
    {
        super.finish();
    }

    boolean overspeed = false;
    private void updateSpeed(CLocation location) {
        // TODO Auto-generated method stub
        float nCurrentSpeed = 0;

        if(location != null)
        {
            location.setUseMetricunits(this.useMetricUnits());
            nCurrentSpeed = location.getSpeed();
        }

        Formatter fmt = new Formatter(new StringBuilder());
        fmt.format(Locale.US, "%5.1f", nCurrentSpeed);
        String strCurrentSpeed = fmt.toString();
        strCurrentSpeed = strCurrentSpeed.replace(' ', '0');

        String strUnits = "miles/hour";
        if(this.useMetricUnits())
        {
            strUnits = "meters/second";
        }

        TextView txtCurrentSpeed = (TextView) this.findViewById(R.id.txtCurrentSpeed);
        txtCurrentSpeed.setText(strCurrentSpeed + " " + strUnits);

        if(Double.parseDouble(strCurrentSpeed)>ALLOWED_SPEED-(1.6)){
            Toast.makeText(this, "you are 10 lms away from excedding the speed limit !", Toast.LENGTH_SHORT).show();
            ((TextView) this.findViewById(R.id.txtCurrentSpeed)).setTextColor(Color.MAGENTA);
        }else{
            ((TextView) this.findViewById(R.id.txtCurrentSpeed)).setTextColor(Color.BLUE);
        }

        if(Double.parseDouble(strCurrentSpeed)>ALLOWED_SPEED){
        ((TextView) this.findViewById(R.id.txtCurrentSpeed)).setTextColor(Color.RED);
            if(!overspeed) {
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//                if (alarmSound == null) {
//                    alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//                    if (alarmSound == null) {
//                        alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                    }
//                }
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.rental_icon)

                        //example for large icon
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setContentTitle("تحذير")
                        .setContentText("لقد تجاوزت السرعة المحددة ! " + "\r\n" + (new Date().toString()))
                        .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alert))
                        .setOngoing(false)
                        //Vibration
                        .setVibrate(new long[]{1000, 2000, 1000, 2000, 1000})

                        //LED
                        .setLights(Color.RED, 3000, 3000)


                        .setPriority(NotificationCompat.PRIORITY_HIGH)


                        .setAutoCancel(true);
                Intent i = new Intent(this, HomeActivity.class);
                PendingIntent pendingIntent =
                        PendingIntent.getActivity(
                                this,
                                15,
                                i,
                                PendingIntent.FLAG_ONE_SHOT
                        );
                // example for blinking LED
                mBuilder.setContentIntent(pendingIntent);
                mNotifyMgr.notify(12345, mBuilder.build());
                PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
                boolean isScreenOn = pm.isScreenOn();
                // Log.e("screen on.................................", ""+isScreenOn);
                if (isScreenOn == false) {
                    PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                            | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
                    wl.acquire(10000);
                    PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");

                    wl_cpu.acquire(10000);
                }
            overspeed=true;
            }

        }else
        {((TextView) this.findViewById(R.id.txtCurrentSpeed)).setTextColor(Color.BLUE);
            overspeed=false;
        }




    }

    private boolean useMetricUnits() {
        // TODO Auto-generated method stub
       return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        if(location != null)
        {
            CLocation myLocation = new CLocation(location, this.useMetricUnits());
            this.updateSpeed(myLocation);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGpsStatusChanged(int event) {
        // TODO Auto-generated method stub

    }



}