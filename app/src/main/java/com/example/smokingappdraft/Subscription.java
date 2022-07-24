package com.example.smokingappdraft;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.google.android.gms.common.api.Response;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Subscription extends AppCompatActivity implements PaymentResultListener {

    private Button btnUnlockPro;
    RequestQueue queue;
    String url_user_update = "http://192.168.0.101/quit_smoking/user.php?action=update";
    String url_user_readone = "http://192.168.0.101/quit_smoking/user.php?action=readOne";
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription);

        btnUnlockPro = findViewById(R.id.btnUnlockPro);
        String strAmount = "6.66";
        int amount = Math.round(Float.parseFloat(strAmount) * 100);

        // check and disable unlock button if user already unlock PRO plan
        checkIsUserPro();

        // razorpay
        btnUnlockPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Checkout checkout = new Checkout();
                checkout.setKeyID("rzp_test_1Kz92Ma8b1iztx");
                checkout.setImage(R.drawable.clip_smart_speaker_notification);
                JSONObject object = new JSONObject();

                try {
                    object.put("name", "Pro Subscription");
//                    object.put("description", "Test Desc"); ********************** if din put will it err?
//                    object.put("send_sms_hash",true);
//                    object.put("allow_rotation", true);
                    //You can omit the image option to fetch the image from dashboard
                    object.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                    object.put("currency", "MYR");
                    object.put("amount", amount);

                    object.put("prefill.contact", "60164246699");
                    object.put("prefill.email", "chengxinye@gmail.com");

                    checkout.open(Subscription.this, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkIsUserPro() {
        String url = url_user_readone + "&user_id=1";

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // disable unlock btn if user is pro
                JSONArray arr = null;
                try {
                    arr = new JSONArray(response);
                    JSONObject jObj = arr.getJSONObject(0);
                    if (jObj.getInt("pro") == 2) {
                        btnUnlockPro.setEnabled(false);
                    }
                } catch (JSONException e) {
                    Toast.makeText(Subscription.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Subscription.this, error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("error",error.toString());
            }
        });
        queue.add(request);
    }

    @Override
    public void onPaymentSuccess(String s) {
        // show success message
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success");
        builder.setMessage("Payment is successful. You've unlock PRO subscription plan! \n\nPayment ID: " + s);
        builder.show();
        btnUnlockPro.setEnabled(false);

        // update user to PRO in database
        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url_user_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(Subscription.this, response.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Subscription.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", "1");
                params.put("pro", "2");
                return params;
            };
        };
        queue.add(request);

    }

    @Override
    public void onPaymentError(int i, String s) {
        // show error message
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Payment failed. " + s);
        builder.show();
    }
}
