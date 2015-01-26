package com.quasar_productions.phoenix_lib.requests;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.quasar_productions.phoenix_lib.AppController;
import com.quasar_productions.phoenix_lib.POJO.PostsResult;
import com.quasar_productions.phoenix_lib.POJO.parents.post.Author;
import com.quasar_productions.phoenix_lib.POJO.parents.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by prashant on 15/1/15.
 */
public class GetPostsBySearch {
    JsonObjectRequest jsonObjectRequest;
    String search;
    long timestamp_id;
    public GetPostsBySearch(String search,long timestamp_id) {
        this.search=search;
        this.timestamp_id=timestamp_id;
        FetchResults(search,1);
    }
    public GetPostsBySearch(String search, int page,long timestamp_id) {
        this.search=search;
        this.timestamp_id=timestamp_id;
        FetchResults(search,page);
    }

    public void FetchResults(String search, int Page){
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "http://quasar-academy.com/mypreciousapi/get_search_results/?search="+search+"&count=5&page="+Page+"&post_type", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d("VOLLEY", "Response: " + response.toString());
                //if (response != null) {
                   EventBus.getDefault().post(parseJsonFeed(response));
            //    }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VOLLEY", "Error: " + error.getMessage());
            }
        });
        Log.d("Search Request: ","http://quasar-academy.com/mypreciousapi/get_search_results/?search="+search+"&count=5&page="+Page+"&post_type");
        AppController.getInstance().addToRequestQueue(jsonObjectRequest,this.search.concat(String.valueOf(timestamp_id)));
    }




    private PostsResult parseJsonFeed(JSONObject response) {
        Gson gson=new Gson();
        PostsResult postsResult= gson.fromJson(response.toString(), PostsResult.class);
        postsResult.setFragment_name(search.concat(String.valueOf(timestamp_id)));
        return postsResult;
    }
}
