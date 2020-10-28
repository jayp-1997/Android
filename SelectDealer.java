package com.example.watersprinkle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectDealer extends AppCompatActivity {

    private ListView lst;
    String nd = null;
    ArrayList<String> dealerlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dealer);

        lst =(ListView)findViewById(R.id.list_dealer);

        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter a = new ArrayAdapter<String>(SelectDealer.this,R.layout.mylist,list);
        lst.setAdapter(a);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Dealer");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                list.clear();

                for(DataSnapshot snapshot : datasnapshot.getChildren()){
                    //String a = snapshot.child("companyName").getValue().toString();
                    list.add(snapshot.child("companyName").getValue().toString());
                    nd = snapshot.getKey().toString();
                }
                a.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String dlna = (String) adapterView.getItemAtPosition(i);
                int id = i;
                Intent n = new Intent(SelectDealer.this,DealerDetails.class);
                n.putExtra("dealer", String.valueOf(dlna));
                n.putExtra("id",nd);
                startActivity(n);

            }
        });

    }
}