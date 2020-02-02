package com.road.road1971user.view.fragment.nav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.road.road1971user.R;
import com.road.road1971user.controller.adapter.BikeRequestAdapter;
import com.road.road1971user.controller.adapter.CarRequestAdapter;
import com.road.road1971user.controller.adapter.MicroRequestAdapter;
import com.road.road1971user.controller.adapter.TruckRequestAdapter;
import com.road.road1971user.model.CarRentData;
import com.road.road1971user.model.RentBikeData;
import com.road.road1971user.model.RentMicroData;
import com.road.road1971user.model.RentTruckData;

import java.util.ArrayList;


public class RequestFragment extends Fragment {
    private ArrayList<RentTruckData> rentTruckDatas=new ArrayList<>();
    private ArrayList<CarRentData> carRentDatas=new ArrayList<>();
    private ArrayList<RentMicroData> microRentDatas=new ArrayList<>();
    private ArrayList<RentBikeData> bikeRentDatas=new ArrayList<>();
    private ArrayList<RentMicroData> busRentDatas=new ArrayList<>();
    private ArrayList<RentBikeData> cngRentDatas=new ArrayList<>();
    private ArrayList<RentBikeData> cycleRentDatas=new ArrayList<>();
    TruckRequestAdapter adapter;
    CarRequestAdapter carRequestAdapter;
    MicroRequestAdapter microRequestAdapter;
    BikeRequestAdapter bikeRequestAdapter;
    MicroRequestAdapter busRequestAdapter;
    BikeRequestAdapter cngRequestAdapter;
    BikeRequestAdapter cycleRequestAdapter;
    private TextView truckHeader,carHeader,microHeader,bikeHeader,busHeader,cngHeader,cyclerHeader;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_request, container, false);
        truckHeader=root.findViewById(R.id.truckHeader);
        carHeader=root.findViewById(R.id.carHeader);
        microHeader=root.findViewById(R.id.microHeader);
        bikeHeader=root.findViewById(R.id.bikeHeader);
        busHeader=root.findViewById(R.id.busHeader);
        cngHeader=root.findViewById(R.id.cngHeader);
        cyclerHeader=root.findViewById(R.id.cyclerHeader);

        RecyclerView truckRecyclerView=root.findViewById(R.id.truckRecyclerView);
        truckRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter=new TruckRequestAdapter(getContext(),rentTruckDatas);
        truckRecyclerView.setAdapter(adapter);
        getTruckPost();

        RecyclerView carRecyclerView=root.findViewById(R.id.carRecyclerView);
        carRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        carRequestAdapter=new CarRequestAdapter(getContext(),carRentDatas);
        carRecyclerView.setAdapter(carRequestAdapter);
        getCarPost();

        RecyclerView microRecyclerView=root.findViewById(R.id.microRecyclerView);
        microRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        microRequestAdapter=new MicroRequestAdapter(getContext(),microRentDatas);
        microRecyclerView.setAdapter(microRequestAdapter);
        getMicroPost();

        RecyclerView bikeRecyclerView=root.findViewById(R.id.bikeRecyclerView);
        bikeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        bikeRequestAdapter=new BikeRequestAdapter(getContext(),bikeRentDatas,"Bike");
        bikeRecyclerView.setAdapter(bikeRequestAdapter);
        getBikePost();

        RecyclerView busRecyclerView=root.findViewById(R.id.busRecyclerView);
        busRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        busRequestAdapter=new MicroRequestAdapter(getContext(),busRentDatas);
        busRecyclerView.setAdapter(busRequestAdapter);
        getBusPost();

        RecyclerView cngRecyclerView=root.findViewById(R.id.cngRecyclerView);
        cngRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        cngRequestAdapter=new BikeRequestAdapter(getContext(),cngRentDatas,"CNG");
        cngRecyclerView.setAdapter(cngRequestAdapter);
        getCngPost();

        RecyclerView cycleRecyclerView=root.findViewById(R.id.cycleRecyclerView);
        cycleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        cycleRequestAdapter=new BikeRequestAdapter(getContext(),cycleRentDatas,"Cycle");
        cycleRecyclerView.setAdapter(cycleRequestAdapter);
        getCyclePost();
        return root;
    }
    private void getTruckPost()
    {
        DatabaseReference truck= FirebaseDatabase.getInstance().getReference().child("BidPosts").child("Truck");
        truck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    RentTruckData truckData=dataSnapshot1.getValue(RentTruckData.class);
                    if(truckData!=null) {
                        try {
                            if (truckData.getUid().equals(FirebaseAuth.getInstance().getUid()) && !truckData.getStatus().equals("Completed")) {
                                truckData.setKey(dataSnapshot1.getKey());
                                rentTruckDatas.add(truckData);
                                truckHeader.setVisibility(View.VISIBLE);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void getCarPost()
    {
        DatabaseReference truck= FirebaseDatabase.getInstance().getReference().child("BidPosts").child("Car");
        truck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    CarRentData carRentData=dataSnapshot1.getValue(CarRentData.class);
                    if(carRentData!=null) {
                        try {
                        if (carRentData.getUid().equals(FirebaseAuth.getInstance().getUid()) && !carRentData.getStatus().equals("Completed"))
                        {
                            carRentData.setKey(dataSnapshot1.getKey());
                            carHeader.setVisibility(View.VISIBLE);
                            carRentDatas.add(carRentData);
                            carRequestAdapter.notifyDataSetChanged();
                        }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void getMicroPost()
    {
        DatabaseReference truck= FirebaseDatabase.getInstance().getReference().child("BidPosts").child("Micro");
        truck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    RentMicroData rentMicroData=dataSnapshot1.getValue(RentMicroData.class);
                    if(rentMicroData!=null) {
                        try {
                        if (rentMicroData.getUid().equals(FirebaseAuth.getInstance().getUid()) && !rentMicroData.getStatus().equals("Completed") )
                        {
                            rentMicroData.setKey(dataSnapshot1.getKey());
                            microHeader.setVisibility(View.VISIBLE);
                            microRentDatas.add(rentMicroData);
                            microRequestAdapter.notifyDataSetChanged();
                        }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getBikePost()
    {
        DatabaseReference truck= FirebaseDatabase.getInstance().getReference().child("BidPosts").child("Bike");
        truck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    RentBikeData rentBikeData=dataSnapshot1.getValue(RentBikeData.class);
                    if(rentBikeData!=null) {
                        try {
                        if (rentBikeData.getUid().equals(FirebaseAuth.getInstance().getUid()) && !rentBikeData.getStatus().equals("Completed") )
                        {
                            rentBikeData.setKey(dataSnapshot1.getKey());
                            bikeHeader.setVisibility(View.VISIBLE);
                            bikeRentDatas.add(rentBikeData);
                            bikeRequestAdapter.notifyDataSetChanged();
                        }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getBusPost()
    {
        DatabaseReference truck= FirebaseDatabase.getInstance().getReference().child("BidPosts").child("Bus");
        truck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    RentMicroData rentBusData=dataSnapshot1.getValue(RentMicroData.class);
                    if(rentBusData!=null) {
                        try {
                            if (rentBusData.getUid().equals(FirebaseAuth.getInstance().getUid()) && !rentBusData.getStatus().equals("Completed")) {
                                rentBusData.setKey(dataSnapshot1.getKey());
                                busHeader.setVisibility(View.VISIBLE);
                                busRentDatas.add(rentBusData);
                                busRequestAdapter.notifyDataSetChanged();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void getCngPost()
    {
        DatabaseReference truck= FirebaseDatabase.getInstance().getReference().child("BidPosts").child("CNG");
        truck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {

                    RentBikeData rentCngData=dataSnapshot1.getValue(RentBikeData.class);
                    if(rentCngData!=null) {
                        try {
                        if (rentCngData.getUid().equals(FirebaseAuth.getInstance().getUid()) && !rentCngData.getStatus().equals("Completed")  )
                        {
                            rentCngData.setKey(dataSnapshot1.getKey());
                            cngHeader.setVisibility(View.VISIBLE);
                            cngRentDatas.add(rentCngData);
                            cngRequestAdapter.notifyDataSetChanged();
                        }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void getCyclePost()
    {
        DatabaseReference truck= FirebaseDatabase.getInstance().getReference().child("BidPosts").child("Cycle");
        truck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    RentBikeData rentCycleData=dataSnapshot1.getValue(RentBikeData.class);
                    if(rentCycleData!=null) {
                        try {
                            if (rentCycleData.getUid().equals(FirebaseAuth.getInstance().getUid()) && !rentCycleData.getStatus().equals("Completed")) {
                                rentCycleData.setKey(dataSnapshot1.getKey());
                                cyclerHeader.setVisibility(View.VISIBLE);
                                cycleRentDatas.add(rentCycleData);
                                cycleRequestAdapter.notifyDataSetChanged();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}