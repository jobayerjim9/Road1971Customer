package com.road.road1971user.view.fragment.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.road.road1971user.R;
import com.road.road1971user.model.RentBikeData;

import java.util.Objects;

public class RentBikePreviewDialog extends DialogFragment implements OnMapReadyCallback {
    private RentBikeData rentBikeData;
    private Context context;
    private MapView mapView;
    private GoogleMap mMap;
    public RentBikePreviewDialog(RentBikeData rentBikeData) {
        this.rentBikeData = rentBikeData;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View v = inflater.inflate(R.layout.rent_bike_post_preview, null);
        mapView=v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        TextView bikeReviewDate=v.findViewById(R.id.bikeReviewDate);
        TextView bikeReviewTime=v.findViewById(R.id.bikeReviewTime);
        TextView bikeReviewNoCar=v.findViewById(R.id.bikeReviewNoCar);
        TextView bikeReviewHours=v.findViewById(R.id.bikeReviewHours);
        TextView bikeReviewDetails=v.findViewById(R.id.bikeReviewDetails);


        String placeHolder=rentBikeData.getTimeStamp().getDay()+"/"+rentBikeData.getTimeStamp().getMonth()+"/"+rentBikeData.getTimeStamp().getYear();
        
        bikeReviewDate.setText(placeHolder);
        placeHolder=rentBikeData.getTimeStamp().getHours()+" : "+rentBikeData.getTimeStamp().getMinute();
        bikeReviewTime.setText(placeHolder);
        placeHolder=rentBikeData.getBikeRequired()+"";
        bikeReviewNoCar.setText(placeHolder);
        bikeReviewHours.setText(rentBikeData.getHoursType());
        bikeReviewDetails.setText(rentBikeData.getAdditional());

        builder.setView(v);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        addMarkers();
        addPolyObjects();
        LatLngBounds.Builder bound = new LatLngBounds.Builder();
        bound.include(rentBikeData.getSource());
        bound.include(rentBikeData.getDestination());
        LatLngBounds bounds = bound.build();
        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(cu);
            }
        });


    }

    private void addPolyObjects() {
        mMap.addPolyline((new PolylineOptions())
                .add(rentBikeData.getSource(), rentBikeData.getDestination())
                .color(R.color.colorPrimaryDark)
                .width(10f));
    }

    private void addMarkers() {
        mMap.addMarker(new MarkerOptions()
                .position(rentBikeData.getSource())
                .title(rentBikeData.getSourceName())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        mMap.addMarker(new MarkerOptions()
                .position(rentBikeData.getDestination())
                .title(rentBikeData.getDestinationName())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(slat, slng), 15.0f));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
