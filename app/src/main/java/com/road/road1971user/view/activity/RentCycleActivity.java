package com.road.road1971user.view.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.road.road1971user.R;
import com.road.road1971user.model.MyLatLng;
import com.road.road1971user.model.RentBikeData;
import com.road.road1971user.model.TimeStamp;
import com.road.road1971user.view.fragment.dialog.NumberPickerDialog;
import com.road.road1971user.view.fragment.dialog.RentBikePreviewDialog;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class RentCycleActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private RadioGroup cycleRequiredGroup;
    private static int cycleRequired;
    private List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME);
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    private int month,year,day,hours=-1,minute;
    private MyLatLng location;
    private String sourceName;
    private Button pickLocationCycle,pickDateCycle,pickTimeCycle,submitCycle;
    private EditText detailsCycle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_cycle);
        Toolbar rentCarToolbar=findViewById(R.id.rentCarToolbar);
        setSupportActionBar(rentCarToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        cycleRequiredGroup=findViewById(R.id.cycleRequiredGroup);
        cycleRequiredGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.cycleRequired1)
                {
                    cycleRequired=1;
                } else if (checkedId==R.id.cycleRequired2)
                {
                    cycleRequired=2;
                } else if (checkedId==R.id.cycleRequired3)
                {
                    cycleRequired=3;
                } else if (checkedId==R.id.cycleRequired4)
                {
                    cycleRequired=4;
                } else if (checkedId==R.id.cycleRequired5)
                {
                    cycleRequired=5;
                }
                else if(checkedId==R.id.cycleRequired5Plus)
                {
                    NumberPickerDialog numberPickerDialog=new NumberPickerDialog();
                    numberPickerDialog.setCancelable(false);
                    numberPickerDialog.show(getSupportFragmentManager(),"CycleNumberPicker");
                }
            }
        });
        Places.initialize(getApplicationContext(), getResources().getString(R.string.api_key));
        pickLocationCycle=findViewById(R.id.pickLocationCycle);
        detailsCycle=findViewById(R.id.detailsCycle);
        pickTimeCycle=findViewById(R.id.pickTimeCycle);
        pickDateCycle=findViewById(R.id.pickDateCycle);
        submitCycle=findViewById(R.id.submitCycle);
        pickLocationCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPlaceAutoComplete();
            }
        });
        pickDateCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialouge();
            }
        });
        pickTimeCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialouge();
            }
        });
        submitCycle.setOnClickListener(new View.OnClickListener() {
            String additional=detailsCycle.getText().toString();
            @Override
            public void onClick(View v) {
                if(cycleRequired==0)
                {
                    Toast.makeText(RentCycleActivity.this, "Pick How Many Cycle Do Tou Need?", Toast.LENGTH_SHORT).show();
                }
                else if(location==null)
                {
                    Toast.makeText(RentCycleActivity.this, "Select Your Location", Toast.LENGTH_SHORT).show();
                }
                else if(year==0)
                {
                    Toast.makeText(RentCycleActivity.this, "Select Your Date", Toast.LENGTH_SHORT).show();
                }
                else if(hours==-1)
                {
                    Toast.makeText(RentCycleActivity.this, "Select Your Time", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    TimeStamp timeStamp=new TimeStamp(day,month,year,hours,minute);
                    RentBikeData rentBikeData=new RentBikeData(location,location,sourceName,sourceName,"Not Applicable",additional,timeStamp,cycleRequired, FirebaseAuth.getInstance().getUid());
                    RentBikePreviewDialog rentBikePreviewDialog=new RentBikePreviewDialog(rentBikeData);
                    rentBikePreviewDialog.setCancelable(false);
                    rentBikePreviewDialog.show(getSupportFragmentManager(),"RentCyclePreview");
                }

            }
        });
       
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        finish();
        return super.onOptionsItemSelected(item);
    }

    public RadioGroup getCycleRequiredGroup() {
        return cycleRequiredGroup;
    }

    public void setCycleRequiredGroup(RadioGroup cycleRequiredGroup) {
        this.cycleRequiredGroup = cycleRequiredGroup;
    }

    public static int getCycleRequired() {
        return cycleRequired;
    }

    public static void setCycleRequired(int cycleRequired) {
        RentCycleActivity.cycleRequired = cycleRequired;
    }
    private void loadPlaceAutoComplete()
    {
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .setTypeFilter(TypeFilter.ADDRESS)
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
                    location=new MyLatLng(Objects.requireNonNull(place.getLatLng()));
                    sourceName=place.getName();
                    pickLocationCycle.setText(sourceName);
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year=year;
        this.month=month;
        this.day=dayOfMonth;
        String text=day+"/"+this.month+"/"+this.year;
        pickDateCycle.setText(text);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hours=hourOfDay;
        this.minute=minute;
    }
    private void showDatePickerDialouge() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(RentCycleActivity.this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    private void showTimePickerDialouge() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,this,Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }
}
