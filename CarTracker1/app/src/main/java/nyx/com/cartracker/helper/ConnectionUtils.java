package nyx.com.cartracker.helper;

import android.util.Log;




import java.util.HashMap;

import java.io.IOException;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;

/**
 * Created by Luminance on 2/11/2018.
 */

public class ConnectionUtils {



    public static String sendPostRequest(String requestURL,
                                  HashMap<String, String> params) {

        String response = "";
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder mb =  new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry : params.entrySet()) {

            mb.addFormDataPart(entry.getKey(), entry.getValue());
        }
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(requestURL)
                  .post(mb.build())
                .build();
        okhttp3.Response responses = null;

        try {
            responses = client.newCall(request).execute();
        } catch (IOException e) {

          Log.d("RET ERR "  , e.getMessage());
        }
        try {
            response = responses.body().string();
            Log.d("OKHTTP3 : "  ,response);

        }catch (IOException e){    Log.d("RET2 ERR "  , e.getMessage());}
   return response;
    }

}
