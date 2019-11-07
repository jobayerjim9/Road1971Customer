package com.road.road1971user.view.navigation.request;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.road.road1971user.R;


public class RequestFragment extends Fragment {

    private RequestViewModel requestViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        requestViewModel =
                ViewModelProviders.of(this).get(RequestViewModel.class);
        View root = inflater.inflate(R.layout.fragment_request, container, false);

        return root;
    }
}