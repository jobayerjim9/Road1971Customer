package com.road.road1971user.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.road.road1971user.R;
import com.road.road1971user.model.DashboardModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder> {
    private Context context;
    private ArrayList<DashboardModel> dashboardModels;

    public DashboardAdapter(Context context, ArrayList<DashboardModel> dashboardModels) {
        this.context = context;
        this.dashboardModels = dashboardModels;
    }

    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DashboardViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardViewHolder holder, int position) {
        Picasso.get().load(dashboardModels.get(position).getLoadUri()).into(holder.dashboardImage);
        holder.dashboardTittle.setText(dashboardModels.get(position).getTittle());
        holder.dashboardDescription.setText(dashboardModels.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return dashboardModels.size();
    }

    class DashboardViewHolder extends RecyclerView.ViewHolder {
        ImageView dashboardImage;
        TextView dashboardTittle, dashboardDescription;

        public DashboardViewHolder(@NonNull View itemView) {
            super(itemView);
            dashboardImage = itemView.findViewById(R.id.dashboardImage);
            dashboardTittle = itemView.findViewById(R.id.dashboardTittle);
            dashboardDescription = itemView.findViewById(R.id.dashboardDescription);
        }
    }
}
