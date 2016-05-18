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

    private static final String PHOTO_LIST_URL = "https://api.flickr.com/services/rest/?method=flickr.interestingness.getList&api_key=d4a47ff42274335c76b940e3ef520dcd&format=json&nojsoncallback=1";
            //"https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=d4a47ff42274335c76b940e3ef520dcd&format=json&nojsoncallback=1&auth_token=72157668477107556-2ea9f3789d154150&api_sig=6ec395f0645d09ab4db53dd6e75ad3eb";

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
        //  do JSONArray volley request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, PHOTO_LIST_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                List<Photo> photoList = new ArrayList<>();
                //Log.i("el json", response.getJSONObject("photos").get("photo").toString());
                try {
                    JSONArray responsePhotos = (JSONArray) response.getJSONObject("photos").get("photo");

                    for (int i = 0; i < responsePhotos.length(); i++) {
                        Photo photo = new Photo();
                        try {
                            JSONObject currentObject = responsePhotos.getJSONObject(i);
                            //Log.i("el json " + i, currentObject.toString());
                            //Log.i("LA ID " + i, "" + currentObject.getString("id"));
                            photo.setId(currentObject.getString("id"));
                            photo.setTitle(currentObject.getString("title"));
                            photo.setFarm(currentObject.getInt("farm"));
                            photo.setSecret(currentObject.getString("secret"));
                            photo.setServerId(currentObject.getInt("server"));
                            photo.generateUrls();
                            photoList.add(photo);
                            //Log.i("la foto " + i, photo.getId() + " - " + photo.getThumbnailUrl() + " - " + photo.getUrl());
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

        /*JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, PHOTO_LIST_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // transform JSONArray to List<Photo>
                // call mCallbacks methods
                List<Photo> photoList = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    Photo photo = new Photo();
                    try {
                        JSONObject currentObject = response.getJSONObject(i);
                        Log.i("el json", currentObject.toString());
                        photo.setId("" + currentObject.getInt("id"));
                        photo.setTitle(currentObject.getString("title"));
                        photo.setUrl(currentObject.getString("url"));
                        photo.setThumbnailUrl(currentObject.getString("thumbnailUrl"));
                        photoList.add(photo);
                    } catch (JSONException e) {
                        Log.e(GetPhotoListRequest.class.getSimpleName(), "Error deserializando JSON");
                        mCallbacks.onGetPhotoListError();
                        return;
                    }
                }

                mCallbacks.onGetPhotoListSuccess(photoList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCallbacks.onGetPhotoListError();
            }
        });*/

        RequestQueueManager.getInstance(mContext).addToRequestQueue(request);
    }
}
