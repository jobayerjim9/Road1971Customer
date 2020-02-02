package com.road.road1971user.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.road.road1971user.R;
import com.road.road1971user.controller.adapter.BidListAdapter;
import com.road.road1971user.controller.helper.FetchURL;
import com.road.road1971user.controller.helper.TaskLoadedCallback;
import com.road.road1971user.model.AppData;
import com.road.road1971user.model.BidDetailsModel;
import com.road.road1971user.model.CarRentData;
import com.road.road1971user.model.DriverProfile;
import com.road.road1971user.model.RentBikeData;
import com.road.road1971user.model.RentMicroData;
import com.road.road1971user.model.RentTruckData;

import java.util.ArrayList;

public class BidDetailsActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private TextView tripStartsEnd,typeBid,itemLabourBid,totalVehicleBid,customerRequirementBid;
    private String type;
    private ArrayList<BidDetailsModel> bidDetailsModels=new ArrayList<>();
    private ArrayList<DriverProfile> driverProfiles=new ArrayList<>();
    private BidListAdapter bidListAdapter;
    private String postKey;
    private GoogleMap mMap;
    private MarkerOptions source, destination;
    private Polyline currentPolyline;
    private String driverKey;
    Marker marker;
    private DriverProfile driverProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_details);
        type=getIntent().getStringExtra("type");
        Log.d("Type",type);
        initView();
        if(type.equals("Truck"))
        {
            setupAsTruck();
        }
        else if(type.equals("Car"))
        {
            setUpAsCar();
        }
        else if(type.equals("Micro"))
        {
            setUpAsMicro();
        }
        else
        {
            setUpAsOther();
        }


    }

    private void initView() {
        tripStartsEnd=findViewById(R.id.tripStartsEnd);
        typeBid=findViewById(R.id.typeBid);
        itemLabourBid=findViewById(R.id.itemLabourBid);
        totalVehicleBid=findViewById(R.id.totalVehicleBid);
        customerRequirementBid=findViewById(R.id.customerRequirementBid);
        RecyclerView recyclerView=findViewById(R.id.bidDetailsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bidListAdapter=new BidListAdapter(this,bidDetailsModels,driverProfiles,type);
        recyclerView.setAdapter(bidListAdapter);

    }

    private void setupAsTruck() {
        RentTruckData truckDataModel= AppData.getRentTruckData();
        source=new MarkerOptions().position(new LatLng(truckDataModel.getSource().getLat(),truckDataModel.getSource().getLng())).title(truckDataModel.getLoadLocation()+"(Start)").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        destination=new MarkerOptions().position(new LatLng(truckDataModel.getDestination().getLat(),truckDataModel.getDestination().getLng())).title(truckDataModel.getUnloadLocation()+"(End)").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        postKey=truckDataModel.getKey();
        String placeHolder="Trip Starts "+ truckDataModel.getTimeStamp().getDay()+"/"+truckDataModel.getTimeStamp().getMonth()+"/"+truckDataModel.getTimeStamp().getYear()+" At "+ truckDataModel.getTimeStamp().getHours()+":"+truckDataModel.getTimeStamp().getMinute();
        tripStartsEnd.setText(placeHolder);
        placeHolder="Must Be "+truckDataModel.getVehicleType() +" & "+truckDataModel.getVehicleSize();
        typeBid.setText(placeHolder);
        placeHolder="Carry " + truckDataModel.getItemType()+" Item & Need "+truckDataModel.getLabour()+ " Labour!";
        itemLabourBid.setText(placeHolder);
        placeHolder="Total "+truckDataModel.getTotalTruck()+" Truck Required";
        totalVehicleBid.setText(placeHolder);
        placeHolder="Customer: "+truckDataModel.getProductDescription();
        customerRequirementBid.setText(placeHolder);
        DatabaseReference bidRef=FirebaseDatabase.getInstance().getReference("BidPosts").child("Truck").child(truckDataModel.getKey()).child("DriversBid");
        getBidList(bidRef);

    }
    private void setUpAsCar() {
        CarRentData carDataModel=AppData.getCarRentData();
        source=new MarkerOptions().position(new LatLng(carDataModel.getSource().getLat(),carDataModel.getSource().getLng())).title(carDataModel.getSourceName()+"(Start)").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        destination=new MarkerOptions().position(new LatLng(carDataModel.getDestination().getLat(),carDataModel.getDestination().getLng())).title(carDataModel.getDestinationName()+"(End)").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        String placeHolder="Trip Starts "+ carDataModel.getTimeStamp().getDay()+"/"+ carDataModel.getTimeStamp().getMonth()+"/"+carDataModel.getTimeStamp().getYear()+" At "+ carDataModel.getTimeStamp().getHours()+":"+carDataModel.getTimeStamp().getMinute();
        tripStartsEnd.setText(placeHolder);
        placeHolder="Must Be "+carDataModel.getCarType();
        postKey=carDataModel.getKey();
        typeBid.setText(placeHolder);
        placeHolder="Required For " + carDataModel.getHoursRequired();
        itemLabourBid.setText(placeHolder);
        placeHolder="Total "+carDataModel.getCarRequired()+" Car Required";
        totalVehicleBid.setText(placeHolder);
        placeHolder="Customer: "+carDataModel.getDetails();
        customerRequirementBid.setText(placeHolder);
        DatabaseReference bidRef=FirebaseDatabase.getInstance().getReference("BidPosts").child("Car").child(carDataModel.getKey()).child("DriversBid");
        getBidList(bidRef);
    }

    private void setUpAsMicro() {
        RentMicroData microDataModel=AppData.getRentMicroData();
        source=new MarkerOptions().position(new LatLng(microDataModel.getSource().getLat(),microDataModel.getSource().getLng())).title(microDataModel.getSourceName()+"(Start)").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        destination=new MarkerOptions().position(new LatLng(microDataModel.getDestination().getLat(),microDataModel.getDestination().getLng())).title(microDataModel.getDestinationName()+"(End)").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        String placeHolder="Trip Starts "+ microDataModel.getTimeStamp().getDay()+"/"+ microDataModel.getTimeStamp().getMonth()+"/"+microDataModel.getTimeStamp().getYear()+" At "+ microDataModel.getTimeStamp().getHours()+":"+microDataModel.getTimeStamp().getMinute();
        tripStartsEnd.setText(placeHolder);
        placeHolder="Must Be "+microDataModel.getMicroType();
        typeBid.setText(placeHolder);
        postKey=microDataModel.getKey();
        placeHolder="Required For "+microDataModel.getHoursType();
        itemLabourBid.setText(placeHolder);
        placeHolder="Total "+microDataModel.getMicroRequired()+" Micro Required";
        totalVehicleBid.setText(placeHolder);
        placeHolder="Customer: "+microDataModel.getAdditional();
        customerRequirementBid.setText(placeHolder);
        DatabaseReference bidRef=FirebaseDatabase.getInstance().getReference("BidPosts").child("Micro").child(microDataModel.getKey()).child("DriversBid");
        getBidList(bidRef);
    }
    private void setUpAsOther() {
        RentBikeData bikeDataModel=AppData.getRentBikeData();
        source=new MarkerOptions().position(new LatLng(bikeDataModel.getSource().getLat(),bikeDataModel.getSource().getLng())).title(bikeDataModel.getSourceName()+"(Start)").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        destination=new MarkerOptions().position(new LatLng(bikeDataModel.getDestination().getLat(),bikeDataModel.getDestination().getLng())).title(bikeDataModel.getDestinationName()+"(End)").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        String placeHolder="Trip Starts "+ bikeDataModel.getTimeStamp().getDay()+"/"+ bikeDataModel.getTimeStamp().getMonth()+"/"+bikeDataModel.getTimeStamp().getYear()+" At "+ bikeDataModel.getTimeStamp().getHours()+":"+bikeDataModel.getTimeStamp().getMinute();
        tripStartsEnd.setText(placeHolder);
        typeBid.setVisibility(View.GONE);
        postKey=bikeDataModel.getKey();
        placeHolder="Required For " + bikeDataModel.getHoursType();
        itemLabourBid.setText(placeHolder);
        placeHolder="Total "+bikeDataModel.getBikeRequired()+" "+type+" Required";
        totalVehicleBid.setText(placeHolder);
        placeHolder="Customer: "+bikeDataModel.getAdditional();
        customerRequirementBid.setText(placeHolder);
        DatabaseReference bidRef=FirebaseDatabase.getInstance().getReference("BidPosts").child(type).child(bikeDataModel.getKey()).child("DriversBid");
        getBidList(bidRef);
    }
    private void getBidList(DatabaseReference bidRef)
    {
        bidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bidDetailsModels.clear();
                driverProfiles.clear();
                for (DataSnapshot post:dataSnapshot.getChildren())
                {
                    final BidDetailsModel bidDetailsModel=post.getValue(BidDetailsModel.class);
                    if (bidDetailsModel!=null) {
                        driverKey=post.getKey();
                        bidDetailsModel.setDriverId(driverKey);
                        bidDetailsModel.setPostKey(postKey);
                        final DatabaseReference driverProfile=FirebaseDatabase.getInstance().getReference("DriverProfile").child(bidDetailsModel.getDriverId());
                        driverProfile.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                DriverProfile driver=dataSnapshot.getValue(DriverProfile.class);
                                if(driver!=null)
                                {
                                    bidDetailsModels.add(bidDetailsModel);
                                    driverProfiles.add(driver);
                                    bidListAdapter.notifyDataSetChanged();

                                            new FetchURL(BidDetailsActivity.this).execute(getUrl(source.getPosition(), destination.getPosition(), "driving"), "driving");
                                            MapFragment mapFragment = (MapFragment) getFragmentManager()
                                                    .findFragmentById(R.id.mapNearBy);
                                            mapFragment.getMapAsync(BidDetailsActivity.this);
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
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Locations").child("Driver");
        final GeoFire geoFire=new GeoFire(databaseReference);
        databaseReference.child(driverKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                geoFire.getLocation(driverKey, new LocationCallback() {
                    @Override
                    public void onLocationResult(String key, GeoLocation location) {
                        if(location!=null) {
                            marker.remove();
                            source = new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title("Your Driver").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_truck));
                            marker = mMap.addMarker(source);
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

//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                LiveLocationModel liveLocationModel=dataSnapshot.getValue(LiveLocationModel.class);
//                if(liveLocationModel!=null)
//                {
//                    Log.d("LiveLocation",liveLocationModel.getLat()+"");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

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
}
