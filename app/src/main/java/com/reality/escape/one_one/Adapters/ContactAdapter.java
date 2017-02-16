package com.reality.escape.one_one.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.reality.escape.one_one.R;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>{
    //private final String name;
    private final String preview;
    private static ClickListener clickListener;
    private List<String> contacts = new ArrayList<>();
    List<Bitmap> profilepic = new ArrayList<>();
    private StorageReference mStorageRef;
    private File localFile;
    Bitmap bitmap;

    Context context;

    public ContactAdapter(Context context,List<String> contacts,List<Bitmap> profilepic) {
        this.contacts = contacts;
        this.profilepic = profilepic;
        preview = "Test Data";
        mStorageRef = FirebaseStorage.getInstance().getReference();

    }
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list,parent,false);
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.dp.setImageBitmap(profilepic.get(position));
        holder.name.setText(contacts.get(position));
        holder.description.setText(preview);



    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView dp;
        private TextView name;
        private TextView description;



        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(this);


            dp = (ImageView) view.findViewById(R.id.dp);
            name = (TextView) view.findViewById(R.id.contact_name);
            description = (TextView) view.findViewById(R.id.list_desc);



        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(),view);
        }
    }
    public void setOnItemClickListener(ClickListener clickListener){
        ContactAdapter.clickListener = clickListener;
    }


    public interface ClickListener{
        void onItemClick(int position,View view);
    }
}



