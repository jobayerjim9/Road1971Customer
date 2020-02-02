package com.road.road1971user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.road.road1971user.R;
import com.road.road1971user.model.FixTripDetailsModel;
import com.road.road1971user.model.UserProfile;

public class HomeActivity extends AppCompatActivity {
    private FixTripDetailsModel fixTripDetailsModel;
    private AppBarConfiguration mAppBarConfiguration;
    private TextView userFullName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        userFullName=hView.findViewById(R.id.userFullName);
        getUserProfile();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                if(menuItem.getItemId()==R.id.nav_logout)
//                {
//                    FirebaseAuth.getInstance().signOut();
//                    startActivity(new Intent(HomeActivity.this,SignInActivity.class));
//                    finish();
//                }
//                return true;
//            }
//        });
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                Log.d("NavigationMenuClick",menuItem.getItemId()+"");
//                if(menuItem.getItemId()==R.id.nav_logout)
//                {
//                    FirebaseAuth.getInstance().signOut();
//                    startActivity(new Intent(HomeActivity.this,SignInActivity.class));
//                    finish();
//                    return true;
//                }
//                return true;
//            }
//        });
    }

    private void getUserProfile() {
        DatabaseReference user=FirebaseDatabase.getInstance().getReference("user").child("UserProfile").child(FirebaseAuth.getInstance().getUid());
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);
                if(userProfile!=null)
                {
                    userFullName.setText(userProfile.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        DatabaseReference liveTrip= FirebaseDatabase.getInstance().getReference("LiveTrip").child(FirebaseAuth.getInstance().getUid());
        liveTrip.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    menu.removeItem(R.id.runningTrip);
                }
                else
                {
                    menu.removeItem(R.id.logoutButton);
                    menu.removeItem(R.id.runningTrip);
                    getMenuInflater().inflate(R.menu.home, menu);
                    fixTripDetailsModel=dataSnapshot.getValue(FixTripDetailsModel.class);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Log.d("MenuNameHome",item.getItemId()+"");
        if (item.getItemId()==R.id.logoutButton)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this,SignInActivity.class));
            finish();
        }
        else if (item.getItemId()==R.id.runningTrip)
        {
            if(fixTripDetailsModel!=null)
            {
                Intent i=new Intent(HomeActivity.this,FixTripDetailsActivity.class);
                i.putExtra("type", fixTripDetailsModel.getType());
                i.putExtra("distance",fixTripDetailsModel.getDistance());
                i.putExtra("sourceLat",fixTripDetailsModel.getSourceLat());
                i.putExtra("sourceLng",fixTripDetailsModel.getSourceLng());
                i.putExtra("desLat",fixTripDetailsModel.getDesLat());
                i.putExtra("desLng",fixTripDetailsModel.getDesLng());
                startActivity(i);

            }
        }
        return super.onOptionsItemSelected(item);
    }

}
