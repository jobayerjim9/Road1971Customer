package com.road.road1971user.view.fragment.nav;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.road.road1971user.R;
import com.road.road1971user.view.activity.RentCycleActivity;
import com.road.road1971user.view.activity.RentExcavatorActivity;
import com.road.road1971user.view.fragment.dialog.BidFixDialog;


public class HomeFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        CardView rentTruckCard=root.findViewById(R.id.rentTruckCard);
        rentTruckCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BidFixDialog bidFixDialog=new BidFixDialog("Truck");
                bidFixDialog.setCancelable(true);
                bidFixDialog.show(getChildFragmentManager(),"Truck");

            }
        });
        CardView rentCarCard=root.findViewById(R.id.rentCarCard);
        rentCarCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BidFixDialog bidFixDialog=new BidFixDialog("Car");
                bidFixDialog.setCancelable(true);
                bidFixDialog.show(getChildFragmentManager(),"Car");
            }
        });
        CardView rentMicroCard=root.findViewById(R.id.rentMicroCard);
        rentMicroCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BidFixDialog bidFixDialog=new BidFixDialog("Micro");
                bidFixDialog.setCancelable(true);
                bidFixDialog.show(getChildFragmentManager(),"Micro");

            }
        });
        CardView rentBikeCard=root.findViewById(R.id.rentBikeCard);
        rentBikeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BidFixDialog bidFixDialog=new BidFixDialog("Bike");
                bidFixDialog.setCancelable(true);
                bidFixDialog.show(getChildFragmentManager(),"Bike");

            }
        });

        CardView rentBusCard=root.findViewById(R.id.rentBusCard);
        rentBusCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BidFixDialog bidFixDialog=new BidFixDialog("Bus");
                bidFixDialog.setCancelable(true);
                bidFixDialog.show(getChildFragmentManager(),"Bus");
            }
        });
        CardView rentCngCard=root.findViewById(R.id.rentCngCard);
        rentCngCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BidFixDialog bidFixDialog=new BidFixDialog("CNG");
                bidFixDialog.setCancelable(true);
                bidFixDialog.show(getChildFragmentManager(),"CNG");
            }
        });
        CardView rentCycleCard=root.findViewById(R.id.rentCycleCard);
        rentCycleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RentCycleActivity.class));
            }
        });
        CardView rentExcavatorCard=root.findViewById(R.id.rentExcavatorCard);
        rentExcavatorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RentExcavatorActivity.class));
            }
        });
        return root;
    }
}