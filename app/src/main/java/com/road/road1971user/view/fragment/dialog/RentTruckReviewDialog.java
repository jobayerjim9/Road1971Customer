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
import com.road.road1971user.model.RentTruckData;

import java.util.Objects;

public class RentTruckReviewDialog extends DialogFragment implements OnMapReadyCallback {
    private Context context;
    private GoogleMap mMap;
    private RentTruckData rentTruckData;
   private MapView mapView;
    public RentTruckReviewDialog(RentTruckData rentTruckData) {
        this.rentTruckData=rentTruckData;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View v = inflater.inflate(R.layout.rent_truck_post_preview, null);
        mapView=v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        TextView truckReviewDate,truckReviewTime,truckReviewType,truckReviewSize,truckReviewProduct,truckReviewLabour,truckReviewDetails,truckReviewNoTruck;
        truckReviewDate=v.findViewById(R.id.truckReviewDate);
        truckReviewTime=v.findViewById(R.id.truckReviewTime);
        truckReviewType=v.findViewById(R.id.truckReviewType);
        truckReviewSize=v.findViewById(R.id.truckReviewSize);
        truckReviewProduct=v.findViewById(R.id.truckReviewProduct);
        truckReviewNoTruck=v.findViewById(R.id.truckReviewNoTruck);
        truckReviewLabour=v.findViewById(R.id.truckReviewLabour);
        truckReviewDetails=v.findViewById(R.id.truckReviewDetails);
        String placeHolder=rentTruckData.getTimeStamp().getDay()+"/"+rentTruckData.getTimeStamp().getMonth()+"/"+rentTruckData.getTimeStamp().getYear();
        truckReviewDate.setText(placeHolder);
        placeHolder=rentTruckData.getTimeStamp().getHours()+" : "+rentTruckData.getTimeStamp().getMinute();
        truckReviewTime.setText(placeHolder);
        truckReviewType.setText(rentTruckData.getVehicleType());
        truckReviewSize.setText(rentTruckData.getVehicleSize());
        truckReviewProduct.setText(rentTruckData.getItemType());
        placeHolder=rentTruckData.getLabour()+"";
        truckReviewLabour.setText(placeHolder);
        truckReviewDetails.setText(rentTruckData.getProductDescription());
        placeHolder=rentTruckData.getTotalTruck()+"";
        truckReviewNoTruck.setText(placeHolder);
        Button truckReviewPublish=v.findViewById(R.id.truckReviewPublish);
        truckReviewPublish.setOnClickListener(new View.OnClickListener() {
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
        mapView.getMapAsync(this);

//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
//                .findFragmentById(R.id.map);

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
        bound.include(rentTruckData.getSource());
        bound.include(rentTruckData.getDestination());
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
                .add(rentTruckData.getSource(), rentTruckData.getDestination())
                .color(R.color.colorPrimaryDark)
                .width(10f));
    }

    private void addMarkers() {
        mMap.addMarker(new MarkerOptions()
                .position(rentTruckData.getSource())
                .title(rentTruckData.getLoadLocation())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        mMap.addMarker(new MarkerOptions()
                .position(rentTruckData.getDestination())
                .title(rentTruckData.getUnloadLocation())
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
