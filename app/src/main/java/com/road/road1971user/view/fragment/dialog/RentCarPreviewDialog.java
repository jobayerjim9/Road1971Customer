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
import com.road.road1971user.model.CarRentData;

import java.util.Objects;

public class RentCarPreviewDialog extends DialogFragment implements OnMapReadyCallback {
    private CarRentData carRentData;
    private Context context;
    private MapView mapView;
    private GoogleMap mMap;
    private LatLng source,destination;
    public RentCarPreviewDialog(CarRentData carRentData) {
        this.carRentData = carRentData;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View v = inflater.inflate(R.layout.rent_car_post_preview, null);
        source=new LatLng(carRentData.getSource().getLat(),carRentData.getSource().getLng());
        destination=new LatLng(carRentData.getDestination().getLat(),carRentData.getDestination().getLng());
        mapView=v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        TextView carReviewDate=v.findViewById(R.id.carReviewDate);
        TextView carkReviewTime=v.findViewById(R.id.carkReviewTime);
        TextView carReviewType=v.findViewById(R.id.carReviewType);
        TextView carReviewHours=v.findViewById(R.id.carReviewHours);
        TextView carReviewNoCar=v.findViewById(R.id.carReviewNoCar);
        TextView carReviewDetails=v.findViewById(R.id.carReviewDetails);
        String placeHolder=carRentData.getTimeStamp().getDay()+"/"+carRentData.getTimeStamp().getMonth()+"/"+carRentData.getTimeStamp().getYear();
        carReviewDate.setText(placeHolder);
        placeHolder=carRentData.getTimeStamp().getHours()+" : "+carRentData.getTimeStamp().getMinute();
        carkReviewTime.setText(placeHolder);
        carReviewType.setText(carRentData.getCarType());
        carReviewHours.setText(carRentData.getHoursRequired());
        placeHolder=carRentData.getCarRequired()+"";
        carReviewNoCar.setText(placeHolder);
        carReviewDetails.setText(carRentData.getDetails());
        Button carReviewPublish=v.findViewById(R.id.carReviewPublish);
        carReviewPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog=new ProgressDialog(context);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please Wait!");
                progressDialog.show();
               if(getTag().contains("CarRentPreview"))
               {
                   final DatabaseReference truckPost= FirebaseDatabase.getInstance().getReference().child("BidPosts").child("Car");
                   String key=truckPost.push().getKey();
                   truckPost.child(key).setValue(carRentData).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                .add(source, destination)
                .color(R.color.colorPrimaryDark)
                .width(10f));
    }

    private void addMarkers() {
        mMap.addMarker(new MarkerOptions()
                .position(source)
                .title(carRentData.getSourceName())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        mMap.addMarker(new MarkerOptions()
                .position(destination)
                .title(carRentData.getDestinationName())
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
