package com.arte.photoapp.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.arte.photoapp.R;
import com.arte.photoapp.activities.PhotoDetailActivity;
import com.arte.photoapp.activities.PhotoListActivity;
import com.arte.photoapp.model.Photo;
import com.arte.photoapp.network.GetPhotoRequest;
import com.arte.photoapp.network.RequestQueueManager;

public class PhotoDetailFragment extends Fragment implements GetPhotoRequest.Callbacks{

    public static final String ARG_PHOTO_ID = "photo_id";

    private Photo mPhoto;
    private NetworkImageView mImage;
    private ProgressDialog mProgressDialog;

    public PhotoDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_PHOTO_ID)) {
            String photoId = getArguments().getString(ARG_PHOTO_ID);
            GetPhotoRequest request = new GetPhotoRequest(getActivity(), this, photoId);
            request.execute();
            // get photo from API
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.photo_detail_loading));
        mProgressDialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photo_detail, container, false);

        mImage = (NetworkImageView) rootView.findViewById(R.id.photo_image);
        if (mPhoto != null) {
            loadPhotoDetails(mPhoto);
        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        mProgressDialog.dismiss();
    }

    private void loadPhotoDetails(Photo photo) {
        mPhoto = photo;
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(mPhoto.getTitle());
        }
        mImage.setImageUrl(photo.getUrl(), RequestQueueManager.getInstance(activity).getImageLoader());
    }

    @Override
    public void onGetPhotoSuccess(Photo photo) {
        mProgressDialog.hide();
        loadPhotoDetails(photo);
    }

    @Override
    public void onGetPhotoError() {
        mProgressDialog.hide();
        Toast.makeText(getActivity(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
    }
}
