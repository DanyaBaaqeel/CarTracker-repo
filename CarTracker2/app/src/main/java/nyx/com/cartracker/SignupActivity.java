package nyx.com.cartracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import nyx.com.cartracker.helper.APIUrl;
import nyx.com.cartracker.helper.ConnectionUtils;
import nyx.com.cartracker.helper.SharedPrefManager;
public class SignupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ((Button)findViewById(R.id.go_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(SignupActivity.this , LoginActivity.class));
                finish();
            }
        });


        ((Button)findViewById(R.id.go_signup)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  startActivity(new Intent(LoginActivity.this , MainActivity.class));
                //  attemptLogin();

                String email = ((EditText)findViewById(R.id.email_input)).getText().toString().trim();
                String password = ((EditText)findViewById(R.id.password_input)).getText().toString().trim();
                String name = ((EditText)findViewById(R.id.name_input)).getText().toString().trim();
                String phone = ((EditText)findViewById(R.id.phone_input)).getText().toString().trim();
                String user_type = "1";
                if(email.equals("")){
                    Toast.makeText(SignupActivity.this, "your email is empty !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.equals("")){
                    Toast.makeText(SignupActivity.this, "your password is empty !", Toast.LENGTH_SHORT).show();

                    return;
                }
                if(name.equals("")){
                    Toast.makeText(SignupActivity.this, "your name is empty !", Toast.LENGTH_SHORT).show();

                    return;
                }
                if(phone.equals("")){
                    Toast.makeText(SignupActivity.this, "your phone is empty !", Toast.LENGTH_SHORT).show();

                    return;
                }
                connect(email ,password , name , phone , user_type);

            }
        });

    }




    JSONObject answer;

    public void connect(final String email , final String password ,final String name , final String phone  , final String type) {
        class JsonOpener extends AsyncTask<String, Void, String> {
            ProgressDialog loading = new ProgressDialog(SignupActivity.this);
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
                        Toast.makeText(SignupActivity.this, "sign up failed !", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(SignupActivity.this, "account craeted succesfully", Toast.LENGTH_SHORT).show();
                        SharedPrefManager.getInstance(SignupActivity.this).userLogin(answer.getJSONObject("body"), password);
                        if(SharedPrefManager.getInstance(SignupActivity.this).getUser().getUser_type().equals("2")) {
                            Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }}catch(Exception e){}
            }

            @Override
            protected String doInBackground(String... params) {
                //  BufferedReader bufferedReader = null;
                try {
                    HashMap<String, String> pars = new HashMap<String, String>();
                    pars.put("service" , "signup");
                    pars.put("email" , params[0]);
                    pars.put("password" , params[1]);
                    pars.put("name" , params[2]);
                    pars.put("phone" , params[3]);
                    pars.put("type" , params[4]);
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
        ru.execute(email , password ,name , phone , type);
    }
}
