package com.road.road1971user.view.fragment.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.road.road1971user.R;
import com.road.road1971user.view.activity.BikeRideActivity;
import com.road.road1971user.view.activity.FixTripGmapActivity;
import com.road.road1971user.view.activity.RentBusActivity;
import com.road.road1971user.view.activity.RentCarActivity;
import com.road.road1971user.view.activity.RentCngActivity;
import com.road.road1971user.view.activity.RentMicroActivity;
import com.road.road1971user.view.activity.RentTruckActivity;

import java.util.Objects;

public class BidFixDialog extends DialogFragment {
    private Context context;
    private String type;

    public BidFixDialog(String type) {
        this.type = type;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View v = inflater.inflate(R.layout.bid_fix_dialog, null);
        Log.d("TypeBidFix",type);
        Button bid=v.findViewById(R.id.bidButton);
        bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("Truck")) {
                    startActivity(new Intent(getContext(), RentTruckActivity.class));
                }
                else if (type.equals("Car"))
                {
                    startActivity(new Intent(getContext(), RentCarActivity.class));
                }
                else if (type.equals("Micro"))
                {
                    startActivity(new Intent(getContext(), RentMicroActivity.class));
                }
                else if (type.equals("Bike"))
                {
                    startActivity(new Intent(getContext(), BikeRideActivity.class));
                }
                else if (type.equals("Bus"))
                {
                    startActivity(new Intent(getContext(), RentBusActivity.class));
                }
                else if (type.equals("CNG"))
                {
                    startActivity(new Intent(getContext(), RentCngActivity.class));
                }

                dismiss();
            }
        });
        Button fix=v.findViewById(R.id.fixButton);
        fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checkTrip() ;
            }
        });
        builder.setView(v);
        return builder.create();
    }

    private void checkTrip() {
        DatabaseReference tripRef= FirebaseDatabase.getInstance().getReference("LiveTrip").child(FirebaseAuth.getInstance().getUid());
        tripRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    Intent i = new Intent(context, FixTripGmapActivity.class);
                    i.putExtra("type", type);
                    startActivity(i);
                    dismiss();

                }
                else
                {
                    Toast.makeText(context, "You Already Have A Running Trip!", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }
}
