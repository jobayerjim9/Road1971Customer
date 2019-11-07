package com.road.road1971user.view.navigation.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.road.road1971user.R;
import com.road.road1971user.view.activity.BikeRideActivity;
import com.road.road1971user.view.activity.RentBusActivity;
import com.road.road1971user.view.activity.RentCarActivity;
import com.road.road1971user.view.activity.RentCngActivity;
import com.road.road1971user.view.activity.RentCycleActivity;
import com.road.road1971user.view.activity.RentExcavatorActivity;
import com.road.road1971user.view.activity.RentMicroActivity;
import com.road.road1971user.view.activity.RentTruckActivity;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
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
                startActivity(new Intent(getContext(), RentTruckActivity.class));
            }
        });
        CardView rentCarCard=root.findViewById(R.id.rentCarCard);
        rentCarCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RentCarActivity.class));
            }
        });
        CardView rentMicroCard=root.findViewById(R.id.rentMicroCard);
        rentMicroCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RentMicroActivity.class));
            }
        });
        CardView rentBikeCard=root.findViewById(R.id.rentBikeCard);
        rentBikeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), BikeRideActivity.class));
            }
        });

        CardView rentBusCard=root.findViewById(R.id.rentBusCard);
        rentBusCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RentBusActivity.class));
            }
        });
        CardView rentCngCard=root.findViewById(R.id.rentCngCard);
        rentCngCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RentCngActivity.class));
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