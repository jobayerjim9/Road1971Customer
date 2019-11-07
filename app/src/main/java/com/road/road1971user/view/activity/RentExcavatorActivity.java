package com.road.road1971user.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.road.road1971user.R;

import java.util.Objects;

public class RentExcavatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_excavator);
        Toolbar rentCarToolbar=findViewById(R.id.rentCarToolbar);
        setSupportActionBar(rentCarToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Spinner hoursSpinnerCar = findViewById(R.id.hoursSpinnerCar);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.hoursExcavator,  R.layout.spinner_item);
        hoursSpinnerCar.setAdapter(adapter);
        hoursSpinnerCar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner typeSpinnerCar = findViewById(R.id.typeSpinnerCar);
        final ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.typeExcavator,  R.layout.spinner_item);
        typeSpinnerCar.setAdapter(adapter2);
        typeSpinnerCar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
}
