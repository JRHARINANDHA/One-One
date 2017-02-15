package com.reality.escape.one_one;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reality.escape.one_one.Adapters.ContactAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private RecyclerView recyclerView;
    private String sender;
    private FirebaseDatabase database;
    private List<String> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadContacts();

        init();

    }

    private void loadContacts() {
        DatabaseReference reference = database.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Count",""+dataSnapshot.getChildrenCount());
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                   Log.e("Name",""+postSnapshot.getKey());
                    contacts.add(postSnapshot.getKey());

                    // Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
                   // List<Objects> contact = td.values();
                    Log.e("size",""+contacts.size());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error",""+databaseError.getMessage());
            }
        });
    }

    private void init() {

        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        sender = intent.getExtras().getString("sender");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        ContactAdapter adapter = new ContactAdapter(getApplicationContext(),contacts);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter.setOnItemClickListener(new ContactAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View view) {


                TextView textView = (TextView) view.findViewById(R.id.contact_name);
                String title = textView.getText().toString();
                Log.d("success",title);
                Toast.makeText(getApplicationContext(),title,Toast.LENGTH_SHORT);
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("sender",sender);
                startActivity(intent);


            }
        });

    }

}