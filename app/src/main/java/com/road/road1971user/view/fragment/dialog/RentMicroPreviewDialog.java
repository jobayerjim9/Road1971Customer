package com.road.road1971user.view.fragment.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.road.road1971user.R;
import com.road.road1971user.model.RentMicroData;

import java.util.Objects;

public class RentMicroPreviewDialog extends DialogFragment implements OnMapReadyCallback {
    private RentMicroData rentMicroData;
    private Context context;
    private MapView mapView;
    private GoogleMap mMap;
    private LatLng source,destination;
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
        source=new LatLng(rentMicroData.getSource().getLat(),rentMicroData.getSource().getLng());
        destination=new LatLng(rentMicroData.getDestination().getLat(),rentMicroData.getDestination().getLng());
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

               if(getTag().contains("RentMicroPreview"))
               {
                   final DatabaseReference truckPost= FirebaseDatabase.getInstance().getReference().child("BidPosts").child("Micro");
                   uploadToDatabase(truckPost);
               }
               else if(getTag().contains("RentBusPreview"))
               {
                   final DatabaseReference truckPost= FirebaseDatabase.getInstance().getReference().child("BidPosts").child("Bus");
                   uploadToDatabase(truckPost);
               }
            }
        });
        builder.setView(v);
        return builder.create();
    }

    private void uploadToDatabase(@NonNull DatabaseReference truckPost) {
        final ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait!");
        progressDialog.show();
        String key=truckPost.push().getKey();
        assert key != null;
        truckPost.child(key).setValue(rentMicroData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(context, "Request Has Been Posted For Bid!", Toast.LENGTH_SHORT).show();
                    try {
                        Objects.requireNonNull(getActivity()).finish();
                    } catch (NullPointerException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(context, "Unsuccessful", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {

                progressDialog.dismiss();
            }
        });
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
        bound.include(source);
        bound.include(destination);
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
                .add(source,destination)
                .color(R.color.colorPrimaryDark)
                .width(10f));
    }

    private void addMarkers() {
        mMap.addMarker(new MarkerOptions()
                .position(source)
                .title(rentMicroData.getSourceName())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        mMap.addMarker(new MarkerOptions()
                .position(destination)
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
