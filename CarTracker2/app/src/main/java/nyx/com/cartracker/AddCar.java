package nyx.com.cartracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import nyx.com.cartracker.adapters.DriversAdapter;
import nyx.com.cartracker.helper.APIUrl;
import nyx.com.cartracker.helper.ConnectionUtils;
import nyx.com.cartracker.helper.SharedPrefManager;
import nyx.com.cartracker.models.Driver;

public class AddCar extends RootActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        findViewById(R.id.go_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = ((EditText)findViewById(R.id.car_num)).getText().toString().trim();
                String details = ((EditText)findViewById(R.id.car_details)).getText().toString().trim();
                String name = ((EditText)findViewById(R.id.car_name)).getText().toString().trim();

                if(num.equals("") || name.equals("")){
                    Toast.makeText(AddCar.this, "please enter car number and name !", Toast.LENGTH_SHORT).show();
                    return;
                }
                add_car(num,details ,name);
            }
        });
    }






    JSONObject answer;


    public void add_car(final String car_number  , final String car_detials , final String name) {
        class JsonOpener extends AsyncTask<String, Void, String> {
            ProgressDialog loading = new ProgressDialog(AddCar.this);
            @Override
            protected void onPreExecute() {

                loading.setTitle("Adding car..");
                loading.show();;
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                    loading.dismiss();
                Toast.makeText(AddCar.this, s, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            protected String doInBackground(String... params) {
                //  BufferedReader bufferedReader = null;
                try {
                    HashMap<String, String> pars = new HashMap<String, String>();
                    pars.put("service" , "addcar");
                    pars.put("user_id" , SharedPrefManager.getInstance(AddCar.this).getUser().getUser_id());
                    pars.put("password" ,SharedPrefManager.getInstance(AddCar.this).getUser().getPassword());
                    pars.put("car_num" , car_number);
                    pars.put("car_details" ,car_detials);
                    pars.put("name" , name);
                    String result =ConnectionUtils.sendPostRequest(APIUrl.SERVER, pars);
                    answer = new JSONObject(result);
                    return answer.getString("body");
                } catch (Exception e) {
                    System.out.println("fetching cats error : " + e.getMessage());
                }

                return "error";


            }


        }
        JsonOpener ru = new JsonOpener();
        ru.execute();
    }

}
