package com.reality.escape.one_one;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.reality.escape.one_one.Adapters.ContactAdapter;

public class MainActivity extends AppCompatActivity  {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();
    }

    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        ContactAdapter adapter = new ContactAdapter(getApplicationContext());
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
                        //Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                        //intent.putExtra("title", title);
                        //startActivity(intent);


            }
        });

    }

}