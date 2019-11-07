package com.road.road1971user.view.fragment.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.road.road1971user.model.RentMicroData;

import java.util.Objects;

public class RentMicroPreviewDialog extends DialogFragment implements OnMapReadyCallback {
    private RentMicroData rentMicroData;
    private Context context;
    private MapView mapView;
    private GoogleMap mMap;
    public RentMicroPreviewDialog(RentMicroData rentMicroData) {
        this.rentMicroData = rentMicroData;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View v = inflater.inflate(R.layout.rent_micro_post_preview, null);
        mapView=v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        TextView microReviewDate=v.findViewById(R.id.microReviewDate);
        TextView microReviewTime=v.findViewById(R.id.microReviewTime);
        TextView microReviewType=v.findViewById(R.id.microReviewType);
        TextView microReviewHours=v.findViewById(R.id.microReviewHours);
        TextView microReviewNoCar=v.findViewById(R.id.microReviewNoCar);
        TextView microSeatType=v.findViewById(R.id.microSeatType);
        TextView microReviewDetails=v.findViewById(R.id.microReviewDetails);



        String placeHolder=rentMicroData.getTimeStamp().getDay()+"/"+rentMicroData.getTimeStamp().getMonth()+"/"+rentMicroData.getTimeStamp().getYear();
        microReviewDate.setText(placeHolder);
        placeHolder=rentMicroData.getTimeStamp().getHours()+" : "+rentMicroData.getTimeStamp().getMinute();
        microReviewTime.setText(placeHolder);
        microReviewType.setText(rentMicroData.getMicroType());
        placeHolder=rentMicroData.getMicroRequired()+"";
        microReviewHours.setText(rentMicroData.getHoursType());
        microReviewNoCar.setText(placeHolder);
        microSeatType.setText(rentMicroData.getSeatType());
        microReviewDetails.setText(rentMicroData.getAdditional());
        Button microReviewPublish=v.findViewById(R.id.microReviewPublish);

        microReviewPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog=new ProgressDialog(context);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please Wait!");
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Posted Successfully", Toast.LENGTH_SHORT).show();
                        dismiss();
                        ((Activity) context).finish();
                    }
                },3000);
            }
        });
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
        bound.include(rentMicroData.getSource());
        bound.include(rentMicroData.getDestination());
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
                .add(rentMicroData.getSource(), rentMicroData.getDestination())
                .color(R.color.colorPrimaryDark)
                .width(10f));
    }

    private void addMarkers() {
        mMap.addMarker(new MarkerOptions()
                .position(rentMicroData.getSource())
                .title(rentMicroData.getSourceName())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        mMap.addMarker(new MarkerOptions()
                .position(rentMicroData.getDestination())
                .title(rentMicroData.getDestinationName())
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
