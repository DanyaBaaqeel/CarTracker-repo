package nyx.com.cartracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import nyx.com.cartracker.adapters.CarsSpinnerAdAPTER;
import nyx.com.cartracker.adapters.DriversAdapter;
import nyx.com.cartracker.helper.APIUrl;
import nyx.com.cartracker.helper.ConnectionUtils;
import nyx.com.cartracker.helper.SharedPrefManager;
import nyx.com.cartracker.models.Car;
import nyx.com.cartracker.models.Driver;

public class AddDriver extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);
        connect();
        findViewById(R.id.go_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Spinner spinner = (Spinner) findViewById(R.id.drivers_list);

                String car_id = drivers.get(spinner.getSelectedItemPosition()).getId();

                String email = ((EditText)findViewById(R.id.email_input)).getText().toString().trim();
                String name = ((EditText)findViewById(R.id.name_input)).getText().toString().trim();
                String phone = ((EditText)findViewById(R.id.phone_input)).getText().toString().trim();
                String password = ((EditText)findViewById(R.id.password_input)).getText().toString().trim();

                if(email.equals("")){
                    Toast.makeText(AddDriver.this, "driver email is empty !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(name.equals("")){
                    Toast.makeText(AddDriver.this, "driver name is empty !", Toast.LENGTH_SHORT).show();

                    return;
                }
                if(password.equals("")){
                    Toast.makeText(AddDriver.this, "driver password is empty !", Toast.LENGTH_SHORT).show();

                    return;
                }
                if(phone.equals("")){
                    Toast.makeText(AddDriver.this, "driver phone is empty !", Toast.LENGTH_SHORT).show();

                    return;
                }
               connect(email ,password ,name ,phone ,"2" ,car_id);
            }
        });
    }




    public void connect() {
        class JsonOpener extends AsyncTask<String, Void, String> {
            ProgressDialog loading = new ProgressDialog(AddDriver.this);
            @Override
            protected void onPreExecute() {

                loading.setTitle("loading..");
                loading.show();;
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    loading.dismiss();
                    {
                        JSONArray arr =  // the json now is a Java Array
                                answer.getJSONArray("body");
                        drivers = new ArrayList<>();
                        for(int i=0;i<arr.length();i++){
                            drivers.add(new Car(arr.getJSONObject(i).getString("id")
                                    ,arr.getJSONObject(i).getString("car_number"),arr.getJSONObject(i).getString("car_details")
                             ,arr.getJSONObject(i).getString("name") ,""));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // this will but the names inside a spinner (spinner = combobox in normal java)
                                final Spinner spinner = (Spinner) findViewById(R.id.drivers_list);
                                CarsSpinnerAdAPTER ca = new CarsSpinnerAdAPTER(AddDriver.this , drivers);
                                spinner.setAdapter(ca);
                            }
                        });



                    }}catch(Exception e){}
            }

            @Override
            protected String doInBackground(String... params) {
                //  BufferedReader bufferedReader = null;
                try {
                    HashMap<String, String> pars = new HashMap<String, String>();
                    pars.put("service" , "getownsercars"); // send this GET to api
                    pars.put("user_id" , SharedPrefManager.getInstance(AddDriver.this).getUser().getUser_id()); // send this GET to api
                    pars.put("password" , SharedPrefManager.getInstance(AddDriver.this).getUser().getPassword());
                    String result =ConnectionUtils.sendPostRequest(APIUrl.SERVER, pars);// this is the json
                    answer = new JSONObject(result);
                } catch (Exception e) {
                    System.out.println("fetching cats error : " + e.getMessage());
                }

                return "Ready";


            }


        }
        JsonOpener ru = new JsonOpener();
        ru.execute();
    }


    ArrayList<Car> drivers;
    JSONObject answer;

    public void connect(final String email , final String password ,final String name , final String phone  , final String type
            , final String car) {
        class JsonOpener extends AsyncTask<String, Void, String> {
            ProgressDialog loading = new ProgressDialog(AddDriver.this);
            boolean cont =true;
            @Override
            protected void onPreExecute() {

                loading.setTitle("creating account..");
                loading.show();;
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    loading.dismiss();
                    String syn = answer.getString("status");
                    if(syn.equals("error")){
                        Toast.makeText(AddDriver.this, "sign up failed !", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(AddDriver.this, "account craeted succesfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddDriver.this, MainActivity.class);
                        startActivity(intent);
                        finish();


                    }}catch(Exception e){}
            }

            @Override
            protected String doInBackground(String... params) {
                //  BufferedReader bufferedReader = null;
                try {
                    HashMap<String, String> pars = new HashMap<String, String>();
                    pars.put("service" , "signupdriver");
                    pars.put("email" , params[0]);
                    pars.put("password" , params[1]);
                    pars.put("name" , params[2]);
                    pars.put("phone" , params[3]);
                    pars.put("type" , params[4]);
                    pars.put("owner" , SharedPrefManager.getInstance(AddDriver.this).getUser().getUser_id());
                    pars.put("car" , params[5]);
                    String result =
                            ConnectionUtils.sendPostRequest(APIUrl.SERVER, pars);


                    answer = new JSONObject(result);
                } catch (Exception e) {
                    System.out.println("fetching cats error : " + e.getMessage());
                }

                return "Ready";


            }


        }
        JsonOpener ru = new JsonOpener();
        ru.execute(email , password ,name , phone , type , car);
    }

}
