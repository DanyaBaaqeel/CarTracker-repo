package nyx.com.cartracker.adapters;

/**
 * Created by Luminance on 2/24/2018.
 */


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import nyx.com.cartracker.AddCar;
import nyx.com.cartracker.R;
import nyx.com.cartracker.helper.APIUrl;
import nyx.com.cartracker.helper.ConnectionUtils;
import nyx.com.cartracker.helper.SharedPrefManager;
import nyx.com.cartracker.models.Car;


public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.MyViewHolder> {

    private List<Car> moviesList;
    private Activity C;
    private boolean allowDelete;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView num, details , owner , driver;
        LinearLayout body;
        Button delete;


        public ImageView icon;

        public MyViewHolder(View view) {
            super(view);
            num = (TextView) view.findViewById(R.id.car_number);
            details = (TextView) view.findViewById(R.id.car_details);
            owner = (TextView) view.findViewById(R.id.car_owner);
            driver = (TextView) view.findViewById(R.id.car_driver);
            body = (LinearLayout) view.findViewById(R.id.car_layout);
            delete = (Button) view.findViewById(R.id.delete_car);
         ;

        }
    }


    public CarsAdapter(List<Car> moviesList , Activity c , boolean a) {
        this.moviesList = moviesList;
        this.C = c;
        this.allowDelete=a;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_car_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
      final  Car r = moviesList.get(position);

        holder.num.setText("Car number : " +  r.getNumber());
        holder.details.setText( r.getDetails());
        holder.owner.setText("Car name : " + r.getName());
        holder.driver.setText(r.getOwner_details() );
        if(allowDelete)
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
delete_car(r.getId() , position);
            }
        });
        else
        {
            holder.delete.setVisibility(View.GONE);
            holder.body.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(C, "updating location..", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
    public void delete_car(final String car_id , final int pos) {
        class JsonOpener extends AsyncTask<String, Void, String> {
            ProgressDialog loading = new ProgressDialog(C);
            @Override
            protected void onPreExecute() {

                loading.setTitle("deleting car..");
                loading.show();;
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(C, s, Toast.LENGTH_SHORT).show();
                moviesList.remove(pos);
                CarsAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected String doInBackground(String... params) {
                //  BufferedReader bufferedReader = null;
                try {
                    HashMap<String, String> pars = new HashMap<String, String>();
                    pars.put("service" , "delcar");
                    pars.put("user_id" , SharedPrefManager.getInstance(C).getUser().getUser_id());
                    pars.put("password" ,SharedPrefManager.getInstance(C).getUser().getPassword());
                    pars.put("car_id" , car_id);

                    String result = ConnectionUtils.sendPostRequest(APIUrl.SERVER, pars);
                    JSONObject   answer = new JSONObject(result);
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