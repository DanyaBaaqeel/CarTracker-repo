package nyx.com.cartracker;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import nyx.com.cartracker.adapters.CarsAdapter;
import nyx.com.cartracker.adapters.DriversAdapter;
import nyx.com.cartracker.helper.APIUrl;
import nyx.com.cartracker.helper.ConnectionUtils;
import nyx.com.cartracker.helper.SharedPrefManager;
import nyx.com.cartracker.models.Car;
import nyx.com.cartracker.models.Driver;

public class MyCars extends RootActivity {
ArrayList<Car> data;
    JSONObject answer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cars);
        connect();


    }

    public void connect() {
        class JsonOpener extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {

                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    {
                        JSONArray arr =  // the json  now is a Java Array
                                answer.getJSONArray("body");
                        data = new ArrayList<>();
                        for(int i=0;i<arr.length();i++){
                            data.add(new Car(arr.getJSONObject(i).getString("id") ,arr.getJSONObject(i).getString("car_number")
                                    ,arr.getJSONObject(i).getString("car_details") ,
                                    arr.getJSONObject(i).getString("name")
                                    ,arr.getJSONObject(i).getString("owner_details")
                            ));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


                                CarsAdapter mAdapter = new CarsAdapter(data ,MyCars.this, true);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MyCars.this);
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                                findViewById(R.id.loading).setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);


                            }
                        });



                    }}catch(Exception e){
                    Log.e("MY CARS ERROR" ,e.getMessage());

                }
            }

            @Override
            protected String doInBackground(String... params) {
                //  BufferedReader bufferedReader = null;
                try {
                    HashMap<String, String> pars = new HashMap<String, String>();
                    pars.put("service" , "getownsercars"); // send this GET to api
                    pars.put("user_id" , SharedPrefManager.getInstance(MyCars.this).getUser().getUser_id());
                    pars.put("password" ,SharedPrefManager.getInstance(MyCars.this).getUser().getPassword());

                    String result = ConnectionUtils.sendPostRequest(APIUrl.SERVER, pars);// this is the json
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
}
