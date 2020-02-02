package com.road.road1971user.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.road.road1971user.R;
import com.road.road1971user.controller.helper.FetchURL;
import com.road.road1971user.controller.helper.TaskLoadedCallback;
import com.road.road1971user.model.DriverProfile;
import com.road.road1971user.model.FixTripDetailsModel;

import java.util.ArrayList;

public class FixTripDetailsActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    String type;
    double distance,disBackup,sourceLat,sourceLng,desLat,desLng;
    int fare;
    TextView fareText,searchStatus,driverNameFixTrip;
    private GoogleMap mMap;
    private MarkerOptions source, destination;
    private Polyline currentPolyline;
    Button confirmRide;
    Button cancelRide;
    ImageView backButton;
    Button searchAgain;
    ImageView callButtonFixTrip;
    FixTripDetailsModel fixTripDetailsModel;
    ArrayList<String> driversFound=new ArrayList<>();
    int radius=3;
    private boolean run=true;
    Marker marker;
    LinearLayout driverLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_trip_details);
        type=getIntent().getStringExtra("type");
        distance=getIntent().getDoubleExtra("distance",0);
        disBackup=distance;
        distance=distance/1000;
        sourceLat=getIntent().getDoubleExtra("sourceLat",0);
        sourceLng=getIntent().getDoubleExtra("sourceLng",0);
        desLat=getIntent().getDoubleExtra("desLat",0);
        desLng=getIntent().getDoubleExtra("desLng",0);
        Log.d("Source",sourceLat+" "+sourceLng);
        source=new MarkerOptions().position(new LatLng(sourceLat,sourceLng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        destination=new MarkerOptions().position(new LatLng(desLat,desLng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        if(type.equals("Truck"))
        {
            fare=(int)(distance*150)+300;
        }
        else if(type.equals("Car"))
        {
            fare=(int)(distance*17)+30;
        }
        else if(type.equals("Micro"))
        {
            fare=(int)(distance*40)+120;
        }
        else if(type.equals("Bike"))
        {
            fare=(int)(distance*11)+25;
        }
        else if(type.equals("Bus"))
        {
            fare=(int)(distance*150)+300;
        }
        fareText=findViewById(R.id.fareText);
        searchAgain=findViewById(R.id.searchAgain);
        backButton=findViewById(R.id.backButton);
        driverLayout=findViewById(R.id.driverLayout);
        driverNameFixTrip=findViewById(R.id.driverNameFixTrip);
        callButtonFixTrip=findViewById(R.id.callButtonFixTrip);
        searchStatus=findViewById(R.id.searchStatus);
        cancelRide=findViewById(R.id.cancelRide);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String place="Estimated Fare "+fare+" BDT";
        fareText.setText(place);
        new FetchURL(FixTripDetailsActivity.this).execute(getUrl(source.getPosition(), destination.getPosition(), "driving"), "driving");
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(FixTripDetailsActivity.this);
        confirmRide=findViewById(R.id.confirmRide);
        confirmRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fixTripDetailsModel=new FixTripDetailsModel(sourceLat,sourceLng,desLat,desLng,disBackup,fare,FirebaseAuth.getInstance().getUid(),type);
                    searchStatus.setVisibility(View.VISIBLE);
                    searchStatus.setText("Please Wait! Searching For Driver....!");
                    searchDriver(fixTripDetailsModel);
                    confirmRide.setVisibility(View.GONE);
                    cancelRide.setVisibility(View.VISIBLE);
                    cancelRide.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(FixTripDetailsActivity.this, "Trip Canceled!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

            }
        });
        searchAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fixTripDetailsModel!=null) {
                    searchDriver(fixTripDetailsModel);
                }
            }
        });
        setUpEvent();
    }

    private void searchDriver(final FixTripDetailsModel fixTripDetailsModel) {
        driversFound.clear();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Locations").child("Driver");
        final DatabaseReference liveTrip=FirebaseDatabase.getInstance().getReference("LiveTrip");
        GeoFire geoFire=new GeoFire(databaseReference);
        GeoQuery geoQuery=geoFire.queryAtLocation(new GeoLocation(sourceLat,sourceLng),radius);
        geoQuery.removeAllListeners();
        liveTrip.child(FirebaseAuth.getInstance().getUid()).setValue(fixTripDetailsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("PostedUser","Success");
                geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                    @Override
                    public void onKeyEntered(String key, GeoLocation location) {
                        DatabaseReference select=databaseReference.child(key).child("type");
                        select.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String typeDr=dataSnapshot.getValue(String.class);
                                if (typeDr!=null)
                                {
                                    if(typeDr.equals(type))
                                    {
                                        liveTrip.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(!dataSnapshot.exists())
                                                {
                                                    driversFound.add(key);
                                                    liveTrip.child(key).setValue(fixTripDetailsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful())
                                                            {
                                                                liveTrip.child(FirebaseAuth.getInstance().getUid()).child("driversFound").setValue(driversFound);
                                                            }
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onKeyExited(String key) {

                    }

                    @Override
                    public void onKeyMoved(String key, GeoLocation location) {

                    }

                    @Override
                    public void onGeoQueryReady() {
                        setUpEvent();
                    }

                    @Override
                    public void onGeoQueryError(DatabaseError error) {
                    }
                });
            }
        });


    }
    private void setUpEvent() {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("LiveTrip").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FixTripDetailsModel fixTripDetailsModel=dataSnapshot.getValue(FixTripDetailsModel.class);
                if (fixTripDetailsModel!=null)
                {
                    confirmRide.setVisibility(View.GONE);
                    searchStatus.setVisibility(View.VISIBLE);
                    if (fixTripDetailsModel.getDriversFound()==null)
                    {
                        searchStatus.setText("No Driver Found Around You!");
                        cancelRide.setVisibility(View.VISIBLE);
                        searchAgain.setVisibility(View.VISIBLE);

                        cancelRide.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(FixTripDetailsActivity.this, "Trip Cancelled", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                    else
                    {
                        searchAgain.setVisibility(View.GONE);
                        searchStatus.setText("Waiting For Driver Confirmation!");
                        cancelRide.setVisibility(View.VISIBLE);
                        cancelRide.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                databaseReference.removeValue();
                                for (String key:fixTripDetailsModel.getDriversFound())
                                {
                                    DatabaseReference cancelRef=FirebaseDatabase.getInstance().getReference("LiveTrip").child(key);
                                    cancelRef.removeValue();
                                }
                                finish();
                                Toast.makeText(FixTripDetailsActivity.this, "Trip Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                    if (fixTripDetailsModel.isAccept())
                    {
                        cancelRide.setVisibility(View.GONE);
                        searchAgain.setVisibility(View.GONE);
                        Log.d("Driver Found","Done");
                        if (fixTripDetailsModel.getDriverId()!=null)
                        {
                            confirmRide.setVisibility(View.GONE);
                            cancelRide.setVisibility(View.GONE);
                            DatabaseReference driverProfileRef=FirebaseDatabase.getInstance().getReference("DriverProfile").child(fixTripDetailsModel.getDriverId());
                            driverProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    DriverProfile driverProfile=dataSnapshot.getValue(DriverProfile.class);
                                    if(driverProfile!=null)
                                    {
                                        driverNameFixTrip.setText(driverProfile.getName());
                                        callButtonFixTrip.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Uri u = Uri.parse("tel:" + driverProfile.getMobile());

                                                // Create the intent and set the data for the
                                                // intent as the phone number.
                                                Intent i = new Intent(Intent.ACTION_DIAL, u);

                                                try {
                                                   startActivity(i);
                                                } catch (SecurityException s) {

                                                    Toast.makeText(FixTripDetailsActivity.this, s.getLocalizedMessage(), Toast.LENGTH_LONG)
                                                            .show();
                                                }
                                            }
                                        });
                                        driverLayout.setVisibility(View.VISIBLE);
                                        searchStatus.setText("Driver Found! He Is On The Way!");
                                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Locations").child("Driver");
                                        final GeoFire geoFire=new GeoFire(databaseReference);
                                        databaseReference.child(fixTripDetailsModel.getDriverId()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                geoFire.getLocation(fixTripDetailsModel.getDriverId(), new LocationCallback() {
                                                    @Override
                                                    public void onLocationResult(String key, GeoLocation location) {
                                                        if (location!=null) {
                                                            marker.remove();
//                                                            if (type.equals("Truck")) {
                                                                source = new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title("Your Driver").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_driver));
//                                                            } else if (type.equals("Car")) {
//                                                                source = new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title("Your Driver").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car));
//                                                            } else if (type.equals("Micro")) {
//                                                                source = new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title("Your Driver").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_rent_micro));
//                                                            } else if (type.equals("Bike")) {
//                                                                source = new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title("Your Driver").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bike));
//                                                            } else if (type.equals("Bus")) {
//                                                                source = new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title("Your Driver").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop));
//                                                            }
                                                            marker = mMap.addMarker(source);
                                                            Log.d("DistanceFromDest", distance(location.latitude, location.longitude, desLat, desLng) + "");
                                                            if(distance(location.latitude,location.longitude,desLat,desLng)<0.1)
                                                            {
                                                                searchStatus.setText("Trip Ended! Please Pay Your Driver!");
                                                            }
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                    else
                    {
                        Log.d("Driver Found","Cancel");
                        cancelRide.setVisibility(View.VISIBLE);
                        cancelRide.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (fixTripDetailsModel.getDriversFound()!=null) {
                                    for (String key : fixTripDetailsModel.getDriversFound()) {
                                        DatabaseReference cancelRef = FirebaseDatabase.getInstance().getReference("LiveTrip").child(key);
                                        cancelRef.removeValue();
                                    }
                                    databaseReference.removeValue();
                                }
                                else
                                {
                                    databaseReference.removeValue();
                                }

                                finish();
                                Toast.makeText(FixTripDetailsActivity.this, "Trip Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mylog", "Added Markers");
        marker=mMap.addMarker(source);
        mMap.addMarker(destination);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(source.getPosition());
        builder.include(destination.getPosition());
        int padding = 50;
        LatLngBounds bounds = builder.build();
        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setLatLngBoundsForCameraTarget(bounds);
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.api_key);
        return url;
    }
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
