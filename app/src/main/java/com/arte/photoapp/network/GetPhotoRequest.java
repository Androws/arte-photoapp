package com.arte.photoapp.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arte.photoapp.model.Photo;

import org.json.JSONException;
import org.json.JSONObject;

public class GetPhotoRequest {

    private static final String GET_PHOTO_URL_BASE = "https://api.500px.com/v1/photos/";

    public interface Callbacks {
        void onGetPhotoSuccess(Photo photo);

        void onGetPhotoError();
    }

    private Context mContext;
    private Callbacks mCallbacks;
    private String mId;

    public GetPhotoRequest(Context context, Callbacks callbacks, String id) {
        mContext = context;
        mCallbacks = callbacks;
        mId = id;
    }

    public void execute() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getPhotoURL(mId), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject photoObject = response.getJSONObject("photo");
                            Photo photo = new Photo();
                            photo.setId(photoObject.getString("id"));
                            photo.setTitle(photoObject.getString("name"));
                            photo.setUrl(photoObject.getString("image_url"));
                            mCallbacks.onGetPhotoSuccess(photo);
                        } catch (JSONException e) {
                            Log.e(GetPhotoRequest.class.getSimpleName(), "Error deserializando JSON", e);
                            mCallbacks.onGetPhotoError();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCallbacks.onGetPhotoError();
            }
        });

        RequestQueueManager.getInstance(mContext).addToRequestQueue(request);
    }

    private String getPhotoURL(String id) {
        return GET_PHOTO_URL_BASE + id + "?image_size=600&comments=1&consumer_key=KGdEPGBOvUMRrJOlSkr68BQykfv16G6jFh2jyj5v";
    }
}
