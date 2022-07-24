package com.example.smokingappdraft;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CommunityForumActivity extends AppCompatActivity {
    
    RequestQueue queue;
    String url_forum_readAll = "http://192.168.0.101/quit_smoking/community_forum.php?action=readAll";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_forum);

        ListView resultsListView = (ListView) findViewById(R.id.lvForum);

        HashMap<String, String> title_content = new HashMap<>();

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url_forum_readAll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // get settings row in database in json format
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        title_content.put(jObj.getString("title"), jObj.getString("content"));
                    }

                    List<HashMap<String, String>> listItems = new ArrayList<>();
                    SimpleAdapter adapter = new SimpleAdapter(CommunityForumActivity.this, listItems, R.layout.forum_list_item,
                            new String[]{"First Line", "Second Line"},
                            new int[]{R.id.forumTitle, R.id.forumContent});


                    Iterator it = title_content.entrySet().iterator();
                    while (it.hasNext())
                    {
                        HashMap<String, String> resultsMap = new HashMap<>();
                        Map.Entry pair = (Map.Entry)it.next();
                        resultsMap.put("First Line", pair.getKey().toString());
                        resultsMap.put("Second Line", pair.getValue().toString());
                        listItems.add(resultsMap);
                    }

                    resultsListView.setAdapter(adapter);

                } catch (JSONException e) {
                    Toast.makeText(CommunityForumActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CommunityForumActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("error",error.toString());
            }
        });
        queue.add(request);
    }
}