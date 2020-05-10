package com.road.road1971user.view.fragment.nav;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.road.road1971user.R;


public class ContactFragment extends Fragment {
    private Context context;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_contact, container, false);
        ImageView facebookContact = root.findViewById(R.id.facebookContact);
        ImageView messangerContact = root.findViewById(R.id.messangerContact);
        ImageView gmailContact = root.findViewById(R.id.gmailContact);
        ImageView phoneContact = root.findViewById(R.id.phoneContact);

        facebookContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com/road1971bd/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        messangerContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://m.me/road1971bd"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        gmailContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@road1971.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Contacted Through App");
                intent.putExtra(Intent.EXTRA_TEXT, "Support Ticket " + FirebaseAuth.getInstance().getUid());
                startActivity(Intent.createChooser(intent, ""));
            }
        });
        phoneContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri u = Uri.parse("tel:" + "01889992271");
                // Create the intent and set the data for the
                // intent as the phone number.
                Intent i = new Intent(Intent.ACTION_DIAL, u);

                try {
                    context.startActivity(i);
                } catch (SecurityException s) {
                    Toast.makeText(context, s.getLocalizedMessage(), Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}