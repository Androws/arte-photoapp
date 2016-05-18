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

    private static final String GET_PHOTO_URL_BASE = "https://api.flickr.com/services/rest/?method=flickr.photos.getInfo&api_key=d4a47ff42274335c76b940e3ef520dcd&photo_id=";

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
        // do JSONObject volley request
        // transform JSONObject to Photo
        // call mCallbacks methods
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getPhotoURL(mId), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject photoObject = response.getJSONObject("photo");
                            Log.i("el json", response.toString());
                            Photo photo = new Photo();
                            photo.setId(photoObject.getString("id"));
                            photo.setTitle(photoObject.getJSONObject("title").getString("_content"));
                            photo.setFarm(photoObject.getInt("farm"));
                            photo.setSecret(photoObject.getString("secret"));
                            photo.setServerId(photoObject.getInt("server"));
                            photo.generateUrls();
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
        return GET_PHOTO_URL_BASE + id + "&format=json&nojsoncallback=1";
    }
}
