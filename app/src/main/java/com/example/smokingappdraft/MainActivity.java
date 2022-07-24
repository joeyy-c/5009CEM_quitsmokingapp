package com.example.smokingappdraft;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView tvNotificationAlert;
    private TextView tvNotificationContent;
    RequestQueue queue;
    String url_settings_readOne = "http://192.168.0.101/quit_smoking/settings.php?action=readOne&setting_type=notification";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showNotificationAlert();

        tvNotificationAlert = findViewById(R.id.tvNotificationAlert);
        tvNotificationContent = findViewById(R.id.tvNotificationContent);
    }

    public void showNotificationAlert() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.notification_alert);
        dialog.show();

        TextView tvNotificationAlert = dialog.findViewById(R.id.tvNotificationAlert);
        TextView tvNotificationContent = dialog.findViewById(R.id.tvNotificationContent);

        dialog.findViewById(R.id.btnNotificationAlert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url_settings_readOne, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // get settings row in database in json format
                    JSONArray arrRow = new JSONArray(response);
                    JSONObject jObjRow = arrRow.getJSONObject(0);

                    // get content of notification settings in json format
                    String settingContentStr = jObjRow.getString("setting_content");
                    JSONObject jObjSettingContent = new JSONObject(settingContentStr);
                    tvNotificationAlert.setText(jObjSettingContent.getString("title"));
                    tvNotificationContent.setText(jObjSettingContent.getString("content"));

                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("error",error.toString());
            }
        });
        queue.add(request);
    }

    public void btnSubscriptionOnClick(View view) {
        startActivity(new Intent(MainActivity.this, Subscription.class));
    }

    public void btnBannerSettingOnClick(View view) {
        startActivity(new Intent(MainActivity.this, BannerSetting.class));
    }

    public void btnCreateOwnGoalActOnClick(View view) {
        startActivity(new Intent(MainActivity.this, CreateOwnGoal.class));
    }

    public void btnNotiSettingOnclick(View view) {
        startActivity(new Intent(MainActivity.this, AdminNotificationSettingsActivity.class));
    }

    public void btnCommunitySettingOnclick(View view) {
        startActivity(new Intent(MainActivity.this, AdminCommunitySettingsActivity.class));
    }

    public void btnCommunityForumOnclick(View view) {
        startActivity(new Intent(MainActivity.this, CommunityForumActivity.class));
    }
}