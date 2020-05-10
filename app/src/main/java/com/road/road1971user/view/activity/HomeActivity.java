package com.road.road1971user.view.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.road.road1971user.R;
import com.road.road1971user.model.FixTripDetailsModel;
import com.road.road1971user.model.UserProfile;
import com.road.road1971user.model.VersionControl;
import com.road.road1971user.view.fragment.dialog.AppUpdateDialog;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    private FixTripDetailsModel fixTripDetailsModel;
    private AppBarConfiguration mAppBarConfiguration;
    private TextView userFullName;
    private ImageView profileImage;
    AppUpdateDialog appUpdateDialog;
    int version = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appUpdateDialog = new AppUpdateDialog();
        appUpdateDialog.setCancelable(false);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionCode;
            Log.d("VersionCode", version + "");
            checkVersion();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        userFullName=hView.findViewById(R.id.userFullName);
        profileImage = hView.findViewById(R.id.profileImage);
        getUserProfile();
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });
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
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken", newToken);
                updateTokenToDatabase(newToken);
            }
        });
    }

    private void checkVersion() {
        DatabaseReference vs = FirebaseDatabase.getInstance().getReference("Version");
        vs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                VersionControl versionControl = dataSnapshot.getValue(VersionControl.class);
                if (versionControl != null) {
                    if (version < versionControl.getUserApp()) {
                        appUpdateDialog.show(getSupportFragmentManager(), "update");
                    } else {
                        if (appUpdateDialog.isAdded())
                            appUpdateDialog.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void updateTokenToDatabase(final String token) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child("UserProfile");
        databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("token").setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Token Updated", token);
                } else {
                    Log.e("Token Error", task.getException().getMessage());
                }

            }
        });
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
        if (item.getItemId() == R.id.runningTrip)
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
