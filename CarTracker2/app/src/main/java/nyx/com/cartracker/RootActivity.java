package nyx.com.cartracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import nyx.com.cartracker.helper.SharedPrefManager;

public class RootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if(SharedPrefManager.getInstance(this).IsUserLoggedIn()){
         if(SharedPrefManager.getInstance(this).getUser().getUser_type().equals("1")){
             inflater.inflate(R.menu.main_menu_owner, menu);
             getSupportActionBar().setDisplayHomeAsUpEnabled(true);
             getSupportActionBar().setDisplayShowHomeEnabled(true);
         }else{
             inflater.inflate(R.menu.main_menu_driver, menu);
         }

        }
//        MenuItem refresh = menu.getItem(R.id.menu_refresh);
//        refresh.setEnabled(true);
        return true;
    }

    /**
     * the menu layout has the 'add/new' menu item
     */

    /**
     * react to the user tapping/selecting an options menu item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // TODO put your code here to respond to the button tap
                finish();
                return true;
            case R.id.menu_home:
                // TODO put your code here to respond to the button tap
                if(SharedPrefManager.getInstance(this).getUser().getUser_type()
                        .equals("2")){
                    startActivity(new Intent(this , HomeActivity.class));
                }else{
                    startActivity(new Intent(this , MainActivity .class));
                }


                finish();
                return true;
            case R.id.menu_speed:
                // TODO put your code here to respond to the button tap
                startActivity(new Intent(this , HomeActivity.class));
                finish();
                return true;
            case R.id.menu_add_car:
                // TODO put your code here to respond to the button tap
                startActivity(new Intent(this , AddCar.class));
                return true;


            case R.id.violeation_guide:
                // TODO put your code here to respond to the button tap
                startActivity(new Intent(this , Guide.class));
                return true;

            case R.id.points_guide:
                // TODO put your code here to respond to the button tap
                startActivity(new Intent(this , Guide.class));
                return true;


            case R.id.my_position:
                // TODO put your code here to respond to the button tap
                startActivity(new Intent(this , MapsActivity.class));
                return true;
            case R.id.menu_add_driver:
                // TODO put your code here to respond to the button tap
                startActivity(new Intent(this , AddDriver.class));
                return true;
            case R.id.menu_logout:
                // TODO put your code here to respond to the button tap
             SharedPrefManager.getInstance(this).Logout();
                startActivity(new Intent(this , LoginActivity.class));
                return true;
            case R.id.menu_exit:
                // TODO put your code here to respond to the button tap
               System.exit(0);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
