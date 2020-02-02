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

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.road.road1971user.R;
import com.road.road1971user.model.CarRentData;
import com.road.road1971user.model.MyLatLng;
import com.road.road1971user.model.TimeStamp;
import com.road.road1971user.view.fragment.dialog.NumberPickerDialog;
import com.road.road1971user.view.fragment.dialog.RentCarPreviewDialog;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class RentCarActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private RadioGroup carRequiredGroup;
    private static int carRequired;
    private int month,year,day,hours=-1,minute;
    private List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME);
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    private MyLatLng source,destination;
    private String sourceName,destinationName,hoursNeed,carType;
    private Button pickLocationCar,dropLocationCar,timePickCar,datePickCar,reviewButtonCar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_car);
        Toolbar rentCarToolbar=findViewById(R.id.rentCarToolbar);
        setSupportActionBar(rentCarToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        initializeView();
        setSpinner();


    }

    private void initializeView() {
        carRequiredGroup=findViewById(R.id.carRequiredGroup);
        carRequiredGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.carRequired1)
                {
                    carRequired=1;
                } else if (checkedId==R.id.carRequired2)
                {
                    carRequired=2;
                } else if (checkedId==R.id.carRequired3)
                {
                    carRequired=3;
                } else if (checkedId==R.id.carRequired4)
                {
                    carRequired=4;
                } else if (checkedId==R.id.carRequired5)
                {
                    carRequired=5;
                }
                else if(checkedId==R.id.carRequired5Plus)
                {
                    NumberPickerDialog numberPickerDialog=new NumberPickerDialog();
                    numberPickerDialog.setCancelable(false);
                    numberPickerDialog.show(getSupportFragmentManager(),"CarNumberPicker");
                }
            }
        });
        Places.initialize(getApplicationContext(), getResources().getString(R.string.api_key));

// Create a new Places client instance
        PlacesClient placesClient = Places.createClient(this);
        pickLocationCar=findViewById(R.id.pickLocationCar);
        dropLocationCar=findViewById(R.id.dropLocationCar);
        timePickCar=findViewById(R.id.timePickCar);
        datePickCar=findViewById(R.id.datePickCar);
        reviewButtonCar=findViewById(R.id.reviewButtonCar);
        reviewButtonCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText detailsCar=findViewById(R.id.detailsCar);
                String detailsCarText=detailsCar.getText().toString().trim();
                if(detailsCarText.isEmpty())
                {
                    detailsCarText="Not Applicable";
                }
                if(source==null)
                {
                    Toast.makeText(RentCarActivity.this, "Please Select Your Pickup Location", Toast.LENGTH_LONG).show();
                }
                else if(destination==null)
                {
                    Toast.makeText(RentCarActivity.this, "Please Select Your Drop Location", Toast.LENGTH_LONG).show();
                }
                else if(carRequired==0)
                {
                    Toast.makeText(RentCarActivity.this, "Please Select How Many Cars Do You Need", Toast.LENGTH_LONG).show();
                }
                else if(year==0)
                {
                    Toast.makeText(RentCarActivity.this, "Please Select Date", Toast.LENGTH_LONG).show();
                }
                else if(hours==-1)
                {
                    Toast.makeText(RentCarActivity.this, "Please Select Time", Toast.LENGTH_LONG).show();
                }
                else if(carType==null)
                {
                    Toast.makeText(RentCarActivity.this, "Please Specify Your Car Type", Toast.LENGTH_LONG).show();
                }
                else
                {
                    TimeStamp timeStamp=new TimeStamp(day,month,year,hours,minute);
                    CarRentData carRentData=new CarRentData(source,destination,sourceName,destinationName,hoursNeed,carType,detailsCarText,timeStamp,carRequired, FirebaseAuth.getInstance().getUid());
                    RentCarPreviewDialog rentCarPreviewDialog=new RentCarPreviewDialog(carRentData);
                    rentCarPreviewDialog.setCancelable(true);
                    rentCarPreviewDialog.show(getSupportFragmentManager(),"CarRentPreview");
                }


            }
        });
        pickLocationCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AUTOCOMPLETE_REQUEST_CODE=1;
                loadPlaceAutoComplete();
            }
        });
        dropLocationCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AUTOCOMPLETE_REQUEST_CODE=2;
                loadPlaceAutoComplete();
            }
        });
        timePickCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialouge();
            }
        });
        datePickCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialouge();
            }
        });
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
    private void setSpinner() {
        Spinner hoursSpinnerCar = findViewById(R.id.hoursSpinnerCar);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.hoursCar,  R.layout.spinner_item);
        hoursSpinnerCar.setAdapter(adapter);
        hoursSpinnerCar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    hoursNeed="Not Applicable";
                }
                else
                {
                    hoursNeed= Objects.requireNonNull(adapter.getItem(position)).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner typeSpinnerCar = findViewById(R.id.typeSpinnerCar);
        final ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.typeCar,  R.layout.spinner_item);
        typeSpinnerCar.setAdapter(adapter2);
        typeSpinnerCar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    carType=null;
                }
                else
                {
                    carType= Objects.requireNonNull(adapter2.getItem(position)).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        finish();
        return super.onOptionsItemSelected(item);
    }

    public static int getCarRequired() {
        return carRequired;
    }

    public static void setCarRequired(int carRequired) {
        RentCarActivity.carRequired = carRequired;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year=year;
        this.month=month+1;
        this.day=dayOfMonth;
        String text=day+"/"+this.month+"/"+this.year;
        datePickCar.setText(text);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hours=hourOfDay;
        this.minute=minute;
        String text=this.hours+" : "+this.minute;
        timePickCar.setText(text);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                if(AUTOCOMPLETE_REQUEST_CODE==1)
                {
                    source=new MyLatLng(Objects.requireNonNull(place.getLatLng()));

                    sourceName=place.getName();
                    pickLocationCar.setText(sourceName);
                }
                else if(AUTOCOMPLETE_REQUEST_CODE==2)
                {
                    destination=new MyLatLng(Objects.requireNonNull(place.getLatLng()));

                    destinationName=place.getName();
                    dropLocationCar.setText(destinationName);
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
    private void showDatePickerDialouge() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(RentCarActivity.this,
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
