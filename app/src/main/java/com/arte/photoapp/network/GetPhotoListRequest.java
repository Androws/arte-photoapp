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
            //"https://api.flickr.com/services/rest/?method=flickr.interestingness.getList&api_key=36e8f057859a172dde7a2cbf29666358&format=json&nojsoncallback=1&auth_token=72157668515663996-775959859698873b&api_sig=95feb23803bdf7c83c1e5d5beebdf872";
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
                    JSONArray responsePhotos = response.getJSONArray("photos");

                    for (int i = 0; i < responsePhotos.length(); i++) {
                        Photo photo = new Photo();
                        try {
                            JSONObject currentObject = responsePhotos.getJSONObject(i);
                            //Log.i("el json " + i, currentObject.toString());
                            //Log.i("LA ID " + i, "" + currentObject.getString("id"));
                            photo.setId(currentObject.getString("id"));
                            photo.setTitle(currentObject.getString("name"));
                            photo.setThumbnailUrl(currentObject.getString("image_url"));
                            //photo.setFarm(currentObject.getInt("farm"));
                            //photo.setSecret(currentObject.getString("secret"));
                            //photo.setServerId(currentObject.getInt("server"));
                            //photo.generateUrls();
                            photoList.add(photo);
                            Log.i("la foto " + i, photo.getId() + " - " + photo.getThumbnailUrl() /*+ " - " + photo.getUrl()*/);
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
