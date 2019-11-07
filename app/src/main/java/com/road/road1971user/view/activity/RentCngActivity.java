package com.road.road1971user.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.road.road1971user.R;
import com.road.road1971user.model.RentBikeData;
import com.road.road1971user.model.TimeStamp;
import com.road.road1971user.view.fragment.dialog.NumberPickerDialog;
import com.road.road1971user.view.fragment.dialog.RentBikePreviewDialog;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class RentCngActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private static int cngRequired;
    private Button pickLocationCng,dropLocationCng,datePickCng,timePickCng;
    private int month,year,day,hours=-1,minute;
    private LatLng source,destination;
    private String sourceName,destinationName;
    private List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME);
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_cng);

        initializeView();
    }

    private void initializeView() {
        Toolbar rentCarToolbar=findViewById(R.id.rentCarToolbar);
        setSupportActionBar(rentCarToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        RadioGroup cngRequiredGroup=findViewById(R.id.cngRequiredGroup);
        pickLocationCng=findViewById(R.id.pickLocationCng);
        dropLocationCng=findViewById(R.id.dropLocationCng);
        datePickCng=findViewById(R.id.datePickCng);
        timePickCng=findViewById(R.id.timePickCng);
        cngRequiredGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.carRequired1)
                {
                    cngRequired=1;
                } else if (checkedId==R.id.carRequired2)
                {
                    cngRequired=2;
                } else if (checkedId==R.id.carRequired3)
                {
                    cngRequired=3;
                } else if (checkedId==R.id.carRequired4)
                {
                    cngRequired=4;
                } else if (checkedId==R.id.carRequired5)
                {
                    cngRequired=5;
                }
                else if(checkedId==R.id.carRequired5Plus)
                {
                    NumberPickerDialog numberPickerDialog=new NumberPickerDialog();
                    numberPickerDialog.setCancelable(false);
                    numberPickerDialog.show(getSupportFragmentManager(),"CngNumberPicker");
                }
            }
        });

        Places.initialize(getApplicationContext(), getResources().getString(R.string.api_key));
        pickLocationCng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AUTOCOMPLETE_REQUEST_CODE=1;
                loadPlaceAutoComplete();
            }
        });
        dropLocationCng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AUTOCOMPLETE_REQUEST_CODE=2;
                loadPlaceAutoComplete();
            }
        });
        timePickCng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialouge();
            }
        });
        datePickCng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialouge();
            }
        });
        Button previewCng=findViewById(R.id.previewCng);
        previewCng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText additionalBike=findViewById(R.id.additionalCng);
                String additionalBikeText=additionalBike.getText().toString().trim();
                if(additionalBikeText.isEmpty())
                {
                    additionalBikeText="Not Applicable";
                }

                if(cngRequired==0)
                {
                    Toast.makeText(RentCngActivity.this, "Select How Many Cng Do You Need", Toast.LENGTH_SHORT).show();
                }
                else if(source==null)
                {
                    Toast.makeText(RentCngActivity.this, "Select Pickup Location!", Toast.LENGTH_SHORT).show();
                }
                else if(destination==null)
                {
                    Toast.makeText(RentCngActivity.this, "Select Drop Location!", Toast.LENGTH_SHORT).show();
                }
                else if(year==0)
                {
                    Toast.makeText(RentCngActivity.this, "Please Select Date", Toast.LENGTH_SHORT).show();
                }
                else if(hours==-1)
                {
                    Toast.makeText(RentCngActivity.this, "Please Select Time", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    TimeStamp timeStamp=new TimeStamp(day,month,year,hours,minute);
                    RentBikeData rentBikeData=new RentBikeData(source,destination,sourceName,destinationName,"Not Applicable",additionalBikeText,timeStamp,cngRequired);
                    RentBikePreviewDialog rentBikePreviewDialog=new RentBikePreviewDialog(rentBikeData);
                    rentBikePreviewDialog.setCancelable(true);
                    rentBikePreviewDialog.show(getSupportFragmentManager(),"RentBikePreview");

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year=year;
        this.month=month+1;
        this.day=dayOfMonth;
        String text=day+"/"+this.month+"/"+this.year;
        datePickCng.setText(text);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hours=hourOfDay;
        this.minute=minute;
        String text=this.hours+" : "+this.minute;
        timePickCng.setText(text);
    }
    private void showDatePickerDialouge() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
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
                    source=place.getLatLng();
                    sourceName=place.getName();
                    pickLocationCng.setText(sourceName);
                }
                else if(AUTOCOMPLETE_REQUEST_CODE==2)
                {
                    destination=place.getLatLng();
                    destinationName=place.getName();
                    dropLocationCng.setText(destinationName);
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
}
