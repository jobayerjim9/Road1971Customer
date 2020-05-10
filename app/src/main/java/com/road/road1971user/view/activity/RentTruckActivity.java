package com.road.road1971user.view.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.road.road1971user.R;
import com.road.road1971user.model.MyLatLng;
import com.road.road1971user.model.RentTruckData;
import com.road.road1971user.model.TimeStamp;
import com.road.road1971user.view.fragment.dialog.NumberPickerDialog;
import com.road.road1971user.view.fragment.dialog.RentTruckReviewDialog;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class RentTruckActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private int month,year,day,hours=-1,minute,labour;
    private double slat,slng,dlat,dlng;

    //private double slat,slng,dlat,dlng;
    private String loadLocation,unloadLocation,truckSize,truckType,productType;

    private static int truckReckquired=0;
    private Button loadLocationTruck,unloadLocationTruck,pickDateTruck,pickTimeTruck;
    private List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME);
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    private RadioGroup trackRequiredGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_truck);
        Toolbar rentTruckToolbar=findViewById(R.id.rentTruckToolbar);
        setSupportActionBar(rentTruckToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        initializeView();
        setSpinner();
    }

    private void initializeView() {
        Places.initialize(getApplicationContext(), getResources().getString(R.string.api_key));
        Button submitButtonTruck=findViewById(R.id.submitButtonTruck);
        submitButtonTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText productDetailsTruck=findViewById(R.id.productDetailsTruck);
                String productDetails=productDetailsTruck.getText().toString().trim();
                try {
                    Log.d("TruckRecquired ", truckReckquired + "");
                    Log.d("truckType ", truckType);
                    Log.d("truckSize ", truckSize);
                    Log.d("productType ", productType + "");
                    Log.d("year ", year + "");
                    Log.d("hours ", hours + "");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                if (slat==0 && slng==0)
                {
                    Toast.makeText(RentTruckActivity.this, "Please Select Your Source Location", Toast.LENGTH_LONG).show();
                }
                else if(dlat==0 && dlng==0)
                {
                    Toast.makeText(RentTruckActivity.this, "Please Select Your Destination Location", Toast.LENGTH_LONG).show();
                }
                else if(truckReckquired==0)
                {
                    trackRequiredGroup.clearCheck();
                    Toast.makeText(RentTruckActivity.this, "Please Select How Many Truck Do You Need!", Toast.LENGTH_LONG).show();
                }
                else if(truckType==null)
                {
                    Toast.makeText(RentTruckActivity.this, "Please Select Which Type Of Truck Do You Need", Toast.LENGTH_LONG).show();
                }
                else if( truckSize==null)
                {
                    Toast.makeText(RentTruckActivity.this, "Please Select Which Size Of Truck Do You Need", Toast.LENGTH_LONG).show();
                }
                else if(productType==null)
                {
                    Toast.makeText(RentTruckActivity.this, "Please Specify Your Product Type", Toast.LENGTH_LONG).show();
                }
                else if(productDetails.isEmpty())
                {
                    Toast.makeText(RentTruckActivity.this, "Please Write Something About Your Product", Toast.LENGTH_LONG).show();
                }
                else if(year==0)
                {
                    Toast.makeText(RentTruckActivity.this, "Please Select Date", Toast.LENGTH_LONG).show();
                }
                else if(hours==-1)
                {
                    Toast.makeText(RentTruckActivity.this, "Please Select Time", Toast.LENGTH_LONG).show();
                }
                else  {
                    TimeStamp timeStamp=new TimeStamp(day,month,year,hours,minute);
                    RentTruckData rentTruckData=new RentTruckData(truckReckquired,labour,new MyLatLng(slat,slng),new MyLatLng(dlat,dlng),timeStamp,truckType,truckSize,productType,productDetails,loadLocation,unloadLocation, FirebaseAuth.getInstance().getUid());

                    RentTruckReviewDialog rentTruckReviewDialog = new RentTruckReviewDialog(rentTruckData);

                   // rentTruckReviewDialog.setCancelable(false);
                    rentTruckReviewDialog.show(getSupportFragmentManager(),"ReviewTruck");
                }
            }
        });
        trackRequiredGroup=findViewById(R.id.trackRequiredGroup);
        trackRequiredGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.truckNeed1)
                {
                    truckReckquired=1;
                } else if (checkedId==R.id.truckNeed2)
                {
                    truckReckquired=2;
                } else if (checkedId==R.id.truckNeed3)
                {
                    truckReckquired=3;
                } else if (checkedId==R.id.truckNeed4)
                {
                    truckReckquired=4;
                } else if (checkedId==R.id.truckNeed5)
                {
                    truckReckquired=5;
                }
                else if(checkedId==R.id.truckNeed5Plus)
                {
                    NumberPickerDialog numberPickerDialog=new NumberPickerDialog();
                    numberPickerDialog.setCancelable(false);
                    numberPickerDialog.show(getSupportFragmentManager(),"TruckNumberPicker");
                }
            }
        });
// Initialize the SDK


// Create a new Places client instance
      // PlacesClient placesClient = Places.createClient(this);
        loadLocationTruck=findViewById(R.id.loadLocationTruck);
        pickDateTruck=findViewById(R.id.pickDateTruck);
        pickTimeTruck=findViewById(R.id.pickTimeTruck);
        unloadLocationTruck=findViewById(R.id.unloadLocationTruck);
        loadLocationTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AUTOCOMPLETE_REQUEST_CODE=1;
                loadPlaceAutoComplete();
            }
        });
        unloadLocationTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AUTOCOMPLETE_REQUEST_CODE=2;
                loadPlaceAutoComplete();
            }
        });
        pickDateTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialouge();
            }
        });
        pickTimeTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialouge();
            }
        });
        final ElegantNumberButton number_button=findViewById(R.id.number_button);
        number_button.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                labour=newValue;
            }
        });
    }

    private void setSpinner() {
        Spinner vehicleTypeSpinner = findViewById(R.id.vehicleTypeSpinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.vehicleType,  R.layout.spinner_item);
        vehicleTypeSpinner.setAdapter(adapter);
        vehicleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TruckType", Objects.requireNonNull(adapter.getItem(position)).toString());
                if(position==0)
                {
                    truckType=null;
                }
                else
                {
                    truckType= Objects.requireNonNull(adapter.getItem(position)).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner vehicleSizeSpinner = findViewById(R.id.vehicleSizeSpinner);
        final ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.vehicleSize,  R.layout.spinner_item);
        vehicleSizeSpinner.setAdapter(adapter2);
        vehicleSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TruckSize", Objects.requireNonNull(adapter2.getItem(position)).toString());
                if(position==0)
                {
                   truckSize=null;
                }
                else
                {
                    truckSize= Objects.requireNonNull(adapter2.getItem(position)).toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner selectProductType = findViewById(R.id.selectProductType);
        final ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.itemType, R.layout.spinner_item);
        // adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectProductType.setAdapter(adapter3);
        selectProductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Product Type", Objects.requireNonNull(adapter3.getItem(position)).toString());
                if(position==0)
                {
                    productType=null;
                }
                else
                {
                    productType= Objects.requireNonNull(adapter3.getItem(position)).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                    slat= Objects.requireNonNull(place.getLatLng()).latitude;
                    slng=place.getLatLng().longitude;
                    loadLocation=place.getName();
                    loadLocationTruck.setText(loadLocation);
                }
                else if(AUTOCOMPLETE_REQUEST_CODE==2)
                {
                    dlat= Objects.requireNonNull(place.getLatLng()).latitude;
                    dlng= place.getLatLng().longitude;
                    unloadLocation=place.getName();
                    unloadLocationTruck.setText(unloadLocation);
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
        pickDateTruck.setText(text);


    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hours=hourOfDay;
        this.minute=minute;
        String text=this.hours+" : "+this.minute;
        pickTimeTruck.setText(text);

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

    public static int getTruckReckquired() {
        return truckReckquired;
    }

    public static void setTruckReckquired(int truckReckquired) {
        RentTruckActivity.truckReckquired = truckReckquired;
    }
}
