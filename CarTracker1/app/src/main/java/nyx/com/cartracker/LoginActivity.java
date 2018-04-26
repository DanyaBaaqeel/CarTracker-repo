package nyx.com.cartracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import nyx.com.cartracker.helper.APIUrl;
import nyx.com.cartracker.helper.ConnectionUtils;
import nyx.com.cartracker.helper.SharedPrefManager;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((Button)findViewById(R.id.go_signup)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this , SignupActivity.class));
                finish();
            }
        });




        // this is the button we saw
        ((Button)findViewById(R.id.go_login)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //  startActivity(new Intent(LoginActivity.this , MainActivity.class));
                //  attemptLogin();
//this is java controllers
                String email = ((EditText)findViewById(R.id.email_input)).getText().toString().trim();
                String password = ((EditText)findViewById(R.id.password_input)).getText().toString().trim();
                if(email.equals("")){
                    Toast.makeText(LoginActivity.this, "Enter email please", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.equals("")){
                    Toast.makeText(LoginActivity.this, "Enter password please", Toast.LENGTH_SHORT).show();
                    return;
                }
                connect(email ,password);

            }
        });

    }




    JSONObject answer;
// this is the function that conncts to the server
    public void connect(final String email , final String password) {
        class JsonOpener extends AsyncTask<String, Void, String> {
            ProgressDialog loading = new ProgressDialog(LoginActivity.this);
            boolean cont =true;
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
                    // here i recive the response
                    loading.dismiss();
                    String syn = answer.getString("status");
                    if(syn.equals("error")){
                        Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(LoginActivity.this, "login succesfull", Toast.LENGTH_SHORT).show();
                        SharedPrefManager.getInstance(LoginActivity.this).userLogin(answer.getJSONObject("body"), password);
                        if(SharedPrefManager.getInstance(LoginActivity.this).getUser().getUser_type().equals("2")) {
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }}catch(Exception e){}
            }

            @Override
            protected String doInBackground(String... params) {
                //  BufferedReader bufferedReader = null;
               //here i send (service , email  , password
                // ) to a (PHP file)
                try {
                    HashMap<String, String> pars = new HashMap<String, String>();
                    pars.put("service" , "login");
                    pars.put("email" , params[0]);
                    pars.put("password" , params[1]);
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
        ru.execute(email , password);
    }



}
