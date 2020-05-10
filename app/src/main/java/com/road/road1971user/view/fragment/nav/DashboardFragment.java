package com.road.road1971user.view.fragment.nav;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.road.road1971user.R;
import com.road.road1971user.controller.adapter.DashboardAdapter;
import com.road.road1971user.model.DashboardModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private RecyclerView dashboardRecycler;
    private DashboardAdapter dashboardAdapter;
    private ArrayList<DashboardModel> dashboardModels = new ArrayList<>();
    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        dashboardRecycler = view.findViewById(R.id.dashboardRecycler);
        dashboardRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        dashboardAdapter = new DashboardAdapter(getContext(), dashboardModels);
        dashboardRecycler.setAdapter(dashboardAdapter);
        getDashboardContent();
        return view;
    }

    private void getDashboardContent() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DashboardContent");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dashboardModels.clear();
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    DashboardModel dashboardModel = value.getValue(DashboardModel.class);
                    if (dashboardModel != null) {
                        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(dashboardModel.getBannerLink());
                        storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri uri = task.getResult();
                                if (uri != null) {
                                    dashboardModel.setLoadUri(uri.toString());
                                    dashboardModels.add(dashboardModel);
                                    dashboardAdapter.notifyDataSetChanged();
                                }
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
