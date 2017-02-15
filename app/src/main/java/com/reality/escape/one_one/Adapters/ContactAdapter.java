package com.reality.escape.one_one.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reality.escape.one_one.R;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>{
    //private final String name;
    private final String preview;
    private static ClickListener clickListener;
    private List<String> contacts = new ArrayList<>();

    Context context;

    public ContactAdapter(Context context,List<String> contacts) {
        this.contacts = contacts;
        preview = "Test Data";

    }
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       // holder.dp.setImageDrawable(name);
        holder.name.setText(contacts.get(position));
        holder.description.setText(preview);
    }

    @Override
    public int getItemCount() {
        return 3;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //private ImageView dp;
        private TextView name;
        private TextView description;



        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(this);


            //dp = (ImageView) view.findViewById(R.id.list_avatar);
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



