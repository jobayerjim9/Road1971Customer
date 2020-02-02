package com.road.road1971user.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.road.road1971user.R;
import com.road.road1971user.model.AppData;
import com.road.road1971user.model.CarRentData;
import com.road.road1971user.view.activity.BidDetailsActivity;

import java.util.ArrayList;

public class CarRequestAdapter extends RecyclerView.Adapter<CarRequestAdapter.MyRequestViewHolder> {
    private Context context;
    private ArrayList<CarRentData> rentTruckData;
    GoogleMap thisMap;
    public CarRequestAdapter(Context context, ArrayList<CarRentData> rentTruckData) {
        this.context = context;
        this.rentTruckData = rentTruckData;
    }

    @NonNull
    @Override
    public MyRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyRequestViewHolder(LayoutInflater.from(context).inflate(R.layout.my_request_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyRequestViewHolder holder, int position) {
        LatLng source,des;
        String place=rentTruckData.get(position).getTimeStamp().getDay()+"-"+rentTruckData.get(position).getTimeStamp().getMonth()+"-"+rentTruckData.get(position).getTimeStamp().getYear();
        holder.dateRequest.setText(place);
        source=new LatLng(rentTruckData.get(position).getSource().getLat(),rentTruckData.get(position).getSource().getLng());
        des=new LatLng(rentTruckData.get(position).getDestination().getLat(),rentTruckData.get(position).getDestination().getLng());
        thisMap = holder.mapCurrent;
        if(thisMap != null) {
            thisMap.moveCamera(CameraUpdateFactory.newLatLngZoom(source, 13f));
            thisMap.addPolyline((new PolylineOptions())
                    .add(source, des)
                    .color(R.color.colorPrimaryDark)
                    .width(10f));
            thisMap.addMarker(new MarkerOptions()
                    .position(source)
                    .title(rentTruckData.get(position).getSourceName())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            thisMap.addMarker(new MarkerOptions()
                    .position(des)
                    .title(rentTruckData.get(position).getDestinationName())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            // Set the map type back to normal.
            thisMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            LatLngBounds.Builder bound = new LatLngBounds.Builder();
            bound.include(source);
            bound.include(des);
            LatLngBounds bounds = bound.build();
            final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
            thisMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    thisMap.animateCamera(cu);
                }
            });
        }
        holder.viewDetailsRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, BidDetailsActivity.class);
                i.putExtra("type","Car");
                AppData.setCarRentData(rentTruckData.get(position));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rentTruckData.size();
    }

    class MyRequestViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback
    {
        MapView requestMap;
        GoogleMap mapCurrent;
        Button viewDetailsRequest;
        TextView dateRequest;
        MyRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            requestMap=itemView.findViewById(R.id.requestMap);
            viewDetailsRequest=itemView.findViewById(R.id.viewDetailsRequest);
            dateRequest=itemView.findViewById(R.id.dateRequest);
            if (requestMap != null) {
                // Initialise the MapView
                requestMap.onCreate(null);
                requestMap.onResume();
                // Set the map ready callback to receive the GoogleMap object
                requestMap.getMapAsync(this);
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(context);
            mapCurrent = googleMap;
            notifyDataSetChanged();
        }
    }
    @Override
    public void onViewRecycled(MyRequestViewHolder holder)
    {
        // Cleanup MapView here?
        if (holder.mapCurrent != null)
        {
            holder.mapCurrent.clear();
            holder.mapCurrent.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }
}
