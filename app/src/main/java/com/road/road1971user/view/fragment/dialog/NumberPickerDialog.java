package com.road.road1971user.view.fragment.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.road.road1971user.view.activity.BikeRideActivity;
import com.road.road1971user.view.activity.RentBusActivity;
import com.road.road1971user.view.activity.RentCarActivity;
import com.road.road1971user.view.activity.RentCycleActivity;
import com.road.road1971user.view.activity.RentMicroActivity;
import com.road.road1971user.view.activity.RentTruckActivity;

public class NumberPickerDialog extends DialogFragment {

    private Context context;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
//        View v = inflater.inflate(R.layout.layout_number_picker, null);

        final NumberPicker numberPicker = new NumberPicker(context);
        Log.d("NumberPickerTag",getTag());
        numberPicker.setMinValue(6);
        numberPicker.setMaxValue(20);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Value");
        builder.setMessage("Choose a number :");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(getTag().equals("TruckNumberPicker")) {
                    RentTruckActivity.setTruckReckquired(numberPicker.getValue());
                }
                else if(getTag().equals("CarNumberPicker"))
                {
                    RentCarActivity.setCarRequired(numberPicker.getValue());
                }
                else if(getTag().equals("MicroNumberPicker"))
                {
                    RentMicroActivity.setMicroRequired(numberPicker.getValue());
                }else if(getTag().equals("BikeNumberPicker"))
                {
                    BikeRideActivity.setBikeRequired(numberPicker.getValue());
                }else if(getTag().equals("BusNumberPicker"))
                {
                    RentBusActivity.setBusRecquired(numberPicker.getValue());
                }else if(getTag().equals("CycleNumberPicker"))
                {
                    RentCycleActivity.setCycleRequired(numberPicker.getValue());
                }

            }
        });

        builder.setView(numberPicker);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }
}
