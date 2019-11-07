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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.road.road1971user.R;
import com.road.road1971user.model.RentMicroData;
import com.road.road1971user.model.TimeStamp;
import com.road.road1971user.view.fragment.dialog.NumberPickerDialog;
import com.road.road1971user.view.fragment.dialog.RentMicroPreviewDialog;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class RentMicroActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private RadioGroup microRequiredGroup;
    private Button pickLocationMicro,dropLocationMicro,timePickMicro,pickDateMicro,previewMicro;
    private LatLng source,destination;
    private String sourceName,destinationName,hoursRequired,microType,seatType;
    private static int microRequired;
    private int month,year,day,hours=-1,minute;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    private List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_micro);
        Toolbar rentCarToolbar=findViewById(R.id.rentCarToolbar);
        setSupportActionBar(rentCarToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        initializeView();
        setSpinner();
    }

    private void initializeView() {
        Places.initialize(getApplicationContext(), getResources().getString(R.string.api_key));

// Create a new Places client instance
        PlacesClient placesClient = Places.createClient(this);
        microRequiredGroup=findViewById(R.id.microRequiredGroup);
        previewMicro=findViewById(R.id.previewMicro);
        pickLocationMicro=findViewById(R.id.pickLocationMicro);
        dropLocationMicro=findViewById(R.id.dropLocationMicro);
        timePickMicro=findViewById(R.id.timePickMicro);
        pickDateMicro=findViewById(R.id.pickDateMicro);
        previewMicro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText additionalMicro=findViewById(R.id.additionalMicro);
                String additionalMicroText=additionalMicro.getText().toString().trim();
                if(additionalMicroText.isEmpty())
                {
                    additionalMicroText="Not Applicable";
                }
                if(microRequired==0)
                {
                    Toast.makeText(RentMicroActivity.this, "Select How Many Micro Required!", Toast.LENGTH_LONG).show();
                }
                else if(source==null)
                {
                    Toast.makeText(RentMicroActivity.this, "Pick Source Location!", Toast.LENGTH_LONG).show();
                }
                else if(destination==null)
                {
                    Toast.makeText(RentMicroActivity.this, "Pick Destination Location!", Toast.LENGTH_LONG).show();
                }
                else if(year==0)
                {
                    Toast.makeText(RentMicroActivity.this, "Pick Date!", Toast.LENGTH_LONG).show();
                }
                else if(hours==-1)
                {
                    Toast.makeText(RentMicroActivity.this, "Pick Time!", Toast.LENGTH_LONG).show();
                }
                else if(microType==null)
                {
                    Toast.makeText(RentMicroActivity.this, "Select Your Preferred Micro Type!", Toast.LENGTH_LONG).show();
                }
                else if(seatType==null)
                {
                    Toast.makeText(RentMicroActivity.this, "Select Your Preferred Seat Type!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    TimeStamp timeStamp=new TimeStamp(day,month,year,hours,minute);
                    RentMicroData rentMicroData=new RentMicroData(source,destination,sourceName,destinationName,hoursRequired,microType,seatType,additionalMicroText,timeStamp,microRequired);
                    RentMicroPreviewDialog rentMicroPreviewDialog=new RentMicroPreviewDialog(rentMicroData);
                    rentMicroPreviewDialog.setCancelable(true);
                    rentMicroPreviewDialog.show(getSupportFragmentManager(),"RentMicroPreview");
                }
            }
        });
        microRequiredGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.microRequired1)
                {
                    microRequired=1;
                } else if (checkedId==R.id.microRequired2)
                {
                    microRequired=2;
                } else if (checkedId==R.id.microRequired3)
                {
                    microRequired=3;
                } else if (checkedId==R.id.microRequired4)
                {
                    microRequired=4;
                } else if (checkedId==R.id.microRequired5)
                {
                    microRequired=5;
                }
                else if(checkedId==R.id.microRequired5Plus)
                {
                    NumberPickerDialog numberPickerDialog=new NumberPickerDialog();
                    numberPickerDialog.setCancelable(false);
                    numberPickerDialog.show(getSupportFragmentManager(),"MicroNumberPicker");
                }
            }
        });
        pickLocationMicro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AUTOCOMPLETE_REQUEST_CODE=1;
                loadPlaceAutoComplete();
            }
        });
        dropLocationMicro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AUTOCOMPLETE_REQUEST_CODE=2;
                loadPlaceAutoComplete();
            }
        });
        timePickMicro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialouge();
            }
        });
        pickDateMicro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialouge();
            }
        });


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
                    hoursRequired="Not Applicable";
                }
                else
                {
                    hoursRequired= Objects.requireNonNull(adapter.getItem(position)).toString();
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
                    microType=null;
                }
                else
                {
                    microType= Objects.requireNonNull(adapter2.getItem(position)).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner seatNumberSpinner = findViewById(R.id.seatNumberSpinner);
        final ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.microSeat,  R.layout.spinner_item);
        seatNumberSpinner.setAdapter(adapter3);
        seatNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    seatType=null;
                }
                else
                {
                    seatType= Objects.requireNonNull(adapter3.getItem(position)).toString();
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



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year=year;
        this.month=month+1;
        this.day=dayOfMonth;
        String text=day+"/"+this.month+"/"+this.year;
        pickDateMicro.setText(text);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hours=hourOfDay;
        this.minute=minute;
        String text=this.hours+" : "+this.minute;
        timePickMicro.setText(text);
    }
    private void showDatePickerDialouge() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(RentMicroActivity.this,
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
    public static int getMicroRequired() {
        return microRequired;
    }

    public static void setMicroRequired(int microRequired) {
        RentMicroActivity.microRequired = microRequired;
    }
    private void loadPlaceAutoComplete()
    {
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                //.setTypeFilter(TypeFilter.ADDRESS)
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
                    pickLocationMicro.setText(sourceName);
                }
                else if(AUTOCOMPLETE_REQUEST_CODE==2)
                {
                    destination=place.getLatLng();
                    destinationName=place.getName();
                    dropLocationMicro.setText(destinationName);
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
