package com.road.road1971user.view.activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.maps.android.SphericalUtil;
import com.road.road1971user.R;
import com.road.road1971user.controller.helper.FetchURL;
import com.road.road1971user.controller.helper.TaskLoadedCallback;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FixTripGmapActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, GoogleMap.OnMarkerDragListener {
    private GoogleMap mMap;
    private MarkerOptions source, destination;
    private Polyline currentPolyline;
    Marker sourceMarker,destinationMarker;
    private TextView sourcePick,destinationPick,fixTripTittle;
    private MapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME);
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    private Button viewDetailsButton;
    private String type;
    double distance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_trip_gmap);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        type=getIntent().getStringExtra("type");
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapNearBy);
        sourcePick=findViewById(R.id.sourcePick);
        fixTripTittle=findViewById(R.id.fixTripTittle);
        String placeHolder="Road "+type;
        fixTripTittle.setText(placeHolder);
        fixTripTittle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        destinationPick=findViewById(R.id.destinationPick);
        viewDetailsButton=findViewById(R.id.viewDetailsButton);
        mapFragment.getMapAsync(this);
        Places.initialize(getApplicationContext(), getResources().getString(R.string.api_key));
        sourcePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AUTOCOMPLETE_REQUEST_CODE=1;
                loadPlaceAutoComplete();
            }
        });
        destinationPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AUTOCOMPLETE_REQUEST_CODE=2;
                loadPlaceAutoComplete();
            }
        });
        viewDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FixTripGmapActivity.this,FixTripDetailsActivity.class);
                intent.putExtra("type",type);
                intent.putExtra("distance",distance);
                intent.putExtra("sourceLat",source.getPosition().latitude);
                intent.putExtra("sourceLng",source.getPosition().longitude);
                intent.putExtra("desLat",destination.getPosition().latitude);
                intent.putExtra("desLng",destination.getPosition().longitude);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null)
                {
                    source=new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).draggable(true);
                    sourceMarker=mMap.addMarker(source);
                    String place=getAddress(location.getLatitude(),location.getLongitude());
                    if (place!=null)
                    {
                        sourcePick.setText(place);
                    }
                    CameraUpdate cameraUpdate= CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),15.0f);
                    mMap.animateCamera(cameraUpdate);
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.setOnMarkerDragListener(FixTripGmapActivity.this);

                }
            }
        });
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
        distance=SphericalUtil.computeLength(currentPolyline.getPoints());
        Log.d("DistanceFixTrip",distance+"");
        viewDetailsButton.setVisibility(View.VISIBLE);

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.d("MarkerDragEnd",marker.getPosition().latitude+" "+marker.getPosition().longitude);
        String place=getAddress(marker.getPosition().latitude,marker.getPosition().longitude);
        if (place!=null)
        {
            sourcePick.setText(place);
        }
    }
    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

            Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();
            return add;
            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    private void loadPlaceAutoComplete()
    {
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .setCountry("bd")
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                if(AUTOCOMPLETE_REQUEST_CODE==1)
                {
                    sourcePick.setText(place.getName());
                    sourceMarker.remove();
                    source=new MarkerOptions().position(new LatLng(Objects.requireNonNull(place.getLatLng()).latitude,place.getLatLng().longitude)).title(place.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).draggable(true);
                    sourceMarker=mMap.addMarker(source);
                    CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom (new LatLng(place.getLatLng().latitude,place.getLatLng().longitude),15.0f );
                    mMap.animateCamera(cameraUpdate);
                    if(destination!=null)
                    {

                        new FetchURL(FixTripGmapActivity.this).execute(getUrl(source.getPosition(), destination.getPosition(), "driving"), "driving");
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(source.getPosition());
                        builder.include(destination.getPosition());
                        int padding = 50;
                        LatLngBounds bounds = builder.build();
                        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        mMap.animateCamera(cu);
                        mMap.setLatLngBoundsForCameraTarget(bounds);

                    }
                }
                else if(AUTOCOMPLETE_REQUEST_CODE==2)
                {
                    destinationPick.setText(place.getName());
                    if(destinationMarker!=null)
                    {
                        destinationMarker.remove();
                    }
                    destination=new MarkerOptions().position(new LatLng(Objects.requireNonNull(place.getLatLng()).latitude,place.getLatLng().longitude)).title(place.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).draggable(true);
                    destinationMarker=mMap.addMarker(destination);
                    try {
                        new FetchURL(FixTripGmapActivity.this).execute(getUrl(source.getPosition(), destination.getPosition(), "driving"), "driving");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(source.getPosition());
                    builder.include(destination.getPosition());
                    int padding = 50;
                    LatLngBounds bounds = builder.build();
                    final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mMap.animateCamera(cu);
                    mMap.setLatLngBoundsForCameraTarget(bounds);
                }
                Log.i("RentTruck", "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                assert status.getStatusMessage() != null;
                Log.i("RentTruck", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

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
