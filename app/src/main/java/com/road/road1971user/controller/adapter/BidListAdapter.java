package com.road.road1971user.controller.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.road.road1971user.R;
import com.road.road1971user.model.BidDetailsModel;
import com.road.road1971user.model.DriverProfile;

import java.util.ArrayList;

public class BidListAdapter extends RecyclerView.Adapter<BidListAdapter.BidListViewHolder> {
    private Context context;
    private ArrayList<BidDetailsModel> bidDetailsModels;
    private ArrayList<DriverProfile> driverProfiles;
    private String type;

    public BidListAdapter(Context context, ArrayList<BidDetailsModel> bidDetailsModels, ArrayList<DriverProfile> driverProfiles, String type) {
        this.context = context;
        this.bidDetailsModels = bidDetailsModels;
        this.driverProfiles = driverProfiles;
        this.type = type;
    }

    @NonNull
    @Override
    public BidListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BidListViewHolder(LayoutInflater.from(context).inflate(R.layout.bid_list_item,parent,false));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final BidListViewHolder holder, final int position) {
        holder.nameBidList.setText(driverProfiles.get(position).getName());
        holder.bidAmount.setText(bidDetailsModels.get(position).getAmount()+"");
        holder.commentBidList.setText(bidDetailsModels.get(position).getComment());
        if(bidDetailsModels.get(position).isChoosed()!=null)
        {
            if(!bidDetailsModels.get(position).isChoosed())
            {
                holder.buttonLayoutBidList.setVisibility(View.GONE);
            }
            else
            {
                holder.buttonLayoutBidList.setVisibility(View.GONE);
                holder.bidListCard.setCardBackgroundColor(context.getColor(R.color.colorPrimaryBackground));
            }
        }
        holder.acceptBidList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmBidder(bidDetailsModels.get(position),driverProfiles.get(position));
            }
        });
        holder.declineBidList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineBidder(bidDetailsModels.get(position));
            }
        });
        holder.callButtonDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri u = Uri.parse("tel:" + driverProfiles.get(position).getMobile());
                // Create the intent and set the data for the
                // intent as the phone number.
                Intent i = new Intent(Intent.ACTION_DIAL, u);

                try {
                    context.startActivity(i);
                } catch (SecurityException s) {
                    Toast.makeText(context ,s.getLocalizedMessage(), Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void declineBidder(final BidDetailsModel bidDetailsModel) {
        final ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Selecting Driver!");
        progressDialog.show();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("BidPosts").child(type).child(bidDetailsModel.getPostKey()).child("DriversBid").child(bidDetailsModel.getDriverId());
        databaseReference.child("choosed").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    progressDialog.dismiss();
                }
                else
                {
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void confirmBidder(final BidDetailsModel bidDetailsModel, DriverProfile driverProfile) {
       final ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Selecting Driver!");
        progressDialog.show();
        Log.d("CrashCheckBid",type+" "+bidDetailsModel.getPostKey()+" "+bidDetailsModel.getPostKey()+" "+bidDetailsModel.getDriverId());
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("BidPosts").child(type).child(bidDetailsModel.getPostKey()).child("DriversBid").child(bidDetailsModel.getDriverId());
        databaseReference.child("choosed").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    progressDialog.dismiss();
                    DatabaseReference postRef= FirebaseDatabase.getInstance().getReference("BidPosts").child(type).child(bidDetailsModel.getPostKey());
                    postRef.child("status").setValue("Running").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Driver Selected!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
                else
                {
                    progressDialog.dismiss();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return bidDetailsModels.size();
    }

    class BidListViewHolder extends RecyclerView.ViewHolder
    {
        TextView nameBidList,bidAmount,commentBidList;
        Button acceptBidList,declineBidList;
        LinearLayout buttonLayoutBidList;
        CardView bidListCard;
        ImageView callButtonDriver;
        public BidListViewHolder(@NonNull View itemView) {
            super(itemView);
            nameBidList=itemView.findViewById(R.id.nameBidList);
            bidAmount=itemView.findViewById(R.id.bidAmount);
            commentBidList=itemView.findViewById(R.id.commentBidList);
            acceptBidList=itemView.findViewById(R.id.acceptBidList);
            buttonLayoutBidList=itemView.findViewById(R.id.buttonLayoutBidList);
            declineBidList=itemView.findViewById(R.id.declineBidList);
            bidListCard=itemView.findViewById(R.id.bidListCard);
            callButtonDriver=itemView.findViewById(R.id.callButtonDriver);
        }
    }
}
