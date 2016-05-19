package com.arte.photoapp.network;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arte.photoapp.model.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetPhotoListRequest {

    private static final String PHOTO_LIST_URL = "https://api.500px.com/v1/photos?feature=popular&sort=created_at&rpp=100&image_size=2&include_store=store_download&include_states=voted&consumer_key=KGdEPGBOvUMRrJOlSkr68BQykfv16G6jFh2jyj5v";

    public interface Callbacks {
        void onGetPhotoListSuccess(List<Photo> photoList);

        void onGetPhotoListError();
    }

    private Context mContext;
    private Callbacks mCallbacks;

    public GetPhotoListRequest(Context context, Callbacks callbacks) {
        mContext = context;
        mCallbacks = callbacks;
    }

    public void execute() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, PHOTO_LIST_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                List<Photo> photoList = new ArrayList<>();
                try {
                    JSONArray responsePhotos = response.getJSONArray("photos");

                    for (int i = 0; i < responsePhotos.length(); i++) {
                        Photo photo = new Photo();
                        try {
                            JSONObject currentObject = responsePhotos.getJSONObject(i);
                            photo.setId(currentObject.getString("id"));
                            photo.setTitle(currentObject.getString("name"));
                            photo.setThumbnailUrl(currentObject.getString("image_url"));
                            photoList.add(photo);
                        } catch (JSONException e) {
                            Log.e(GetPhotoListRequest.class.getSimpleName(), "Error deserializando JSON");
                            mCallbacks.onGetPhotoListError();
                            return;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mCallbacks.onGetPhotoListSuccess(photoList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCallbacks.onGetPhotoListError();
            }
        });
        RequestQueueManager.getInstance(mContext).addToRequestQueue(request);
    }
}
