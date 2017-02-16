package com.reality.escape.one_one;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.reality.escape.one_one.Adapters.ContactAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    public String downloadUrl;
    private File localFile;
    private RecyclerView recyclerView;
    private String sender;
    private FirebaseDatabase database;
    private List<String> contacts = new ArrayList<>();
    private List<Bitmap> profilepic = new ArrayList<>();
    private StorageReference mStorageRef;
    int i=0,count=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navdrawer);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       // downloadUrl=getIntent().getStringExtra("download_link");
        //Log.e("Success",downloadUrl);


        init();
        loadContacts();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadRecycler() {
        Log.e("count value",String.valueOf(count));
            Log.e("i value",String.valueOf(i));
            //============================================================
            StorageReference riversRef = mStorageRef.child(contacts.get(i)+".jpg");


            try {
                localFile = File.createTempFile("images"+i, "jpg");
                i++;
            } catch (IOException e) {
                Log.e("Error opening file","sorry");
                e.printStackTrace();
            }
            riversRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Successfully downloaded data to local file
                            Log.e(localFile.getAbsolutePath().toString(),String.valueOf(i));
                            profilepic.add( BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                            Log.e(String.valueOf(profilepic.size()),String.valueOf(contacts.size()));
                            if(profilepic.size()==contacts.size()){
                                for (Bitmap bitmap:profilepic) {

                                    Log.e(String.valueOf(bitmap.getHeight()),"saf");
                                }

                                ContactAdapter adapter = new ContactAdapter(getApplicationContext(),contacts,profilepic);
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
                            else
                            {
                                loadRecycler();

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("Failed","DownloadFailed");
                }
            });
            //============================================================




    }

    private void loadContacts() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contacts.clear();

                Log.e("Count",""+dataSnapshot.getChildrenCount());
                count=(int)dataSnapshot.getChildrenCount();
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){

                   Log.e("Name",""+postSnapshot.getKey());
                    contacts.add(postSnapshot.getKey());
                    Log.e("size",""+contacts.size());
                }
                i=0;
                profilepic.clear();
                loadRecycler();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error",""+databaseError.getMessage());
            }
        });
    }

    private void init() {



        Intent intent = getIntent();
        sender = intent.getExtras().getString("sender");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


    }

}