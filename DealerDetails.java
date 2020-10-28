package com.example.watersprinkle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DealerDetails extends AppCompatActivity {
    private ImageView img_dlr;
    private TextView dlr_name,dlradd,dlrMobile,dlrCity,dlrState;
    private ListView lst;
    private Button btn_dlr_send_requent;
    String str,id;
    private String mCurrentStatus;
    private DatabaseReference reference,mRequest;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_details);

        dlr_name = (TextView)findViewById(R.id.dlr_name);
        img_dlr = (ImageView)findViewById(R.id.img_dlr);
        dlradd = (TextView)findViewById(R.id.dlradd);
        dlrMobile = (TextView)findViewById(R.id.dlrMobile);
        dlrCity = (TextView)findViewById(R.id.dlrCity);
        dlrState = (TextView)findViewById(R.id.dlrState);
        btn_dlr_send_requent = (Button)findViewById(R.id.btn_dlrsltd);

        mCurrentStatus = "no_friends";

        Intent n = getIntent();
        str =n.getStringExtra("dealer");
        dlr_name.setText(str);
        id = n.getStringExtra("id");


        reference = FirebaseDatabase.getInstance().getReference().child("Dealer");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();


        reference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dlradd.setText(dataSnapshot.child("address").getValue().toString());
                dlrMobile.setText(dataSnapshot.child("mobileNo").getValue().toString());
                dlrCity.setText(dataSnapshot.child("selCy").getValue().toString());
                dlrState.setText(dataSnapshot.child("selSt").getValue().toString());
                Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(img_dlr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_dlr_send_requent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map requestMap = new HashMap();
                requestMap.put("Request/" + id + "/" + mCurrentUser.getUid() + "/request_type", "sent");
                requestMap.put("Request/" + mCurrentUser.getUid() + "/" + id + "/request_type", "received");

                reference.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        if(databaseError != null){

                            Toast.makeText(DealerDetails.this, "There was some error in sending request", Toast.LENGTH_SHORT).show();

                        } else {

                            btn_dlr_send_requent.setText("Cancel Friend Request");

                        }
                        btn_dlr_send_requent.setEnabled(true);
                    }
                });

                /*if (mCurrentStatus.equals("no_friends"))
                {
                    mRequest.child(mCurrentUser.getUid()).child(id).child("request_type").setValue("sent")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        mRequest.child(id).child(mCurrentUser.getUid()).child("request_type").setValue("Received")
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(DealerDetails.this,"success",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                    else
                                    {
                                        Toast.makeText(DealerDetails.this,"failed",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }*/

            }
        });

    }
}