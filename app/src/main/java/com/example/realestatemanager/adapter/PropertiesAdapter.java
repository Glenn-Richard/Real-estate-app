package com.example.realestatemanager.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.realestatemanager.OnItemClickListener;
import com.example.realestatemanager.R;
import com.example.realestatemanager.models.Property;
import com.example.realestatemanager.viewmodel.FirebaseViewModel;

import java.util.List;

public class PropertiesAdapter extends RecyclerView.Adapter<PropertiesAdapter.PropertiesViewHolder> {

    private final Context context;
    private List<Property> properties;
    private final OnItemClickListener mListener;

    public PropertiesAdapter(Context context,OnItemClickListener mListener) {
        this.context = context;
        this.mListener = mListener;
    }
    public void setAdapter(List<Property> properties){
        this.properties = properties;
    }

    @NonNull
    @Override
    public PropertiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        return new PropertiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertiesViewHolder holder, int position) {

        FirebaseViewModel viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(FirebaseViewModel.class);
        Property property = properties.get(position);
        if(!(property.getImages().get(0)==null)){
            viewModel.getImage(Uri.parse(property.getImages().get(0).getUrl())).observe((LifecycleOwner) context, uri ->
                    Glide.with(context)
                    .load(uri)
                    .fitCenter()
                    .into(holder.photo));

        }
        holder.category.setText(property.getType());
        holder.city.setText(property.getLocation());
        holder.price.setText(String.valueOf(property.getPrice()));
        holder.itemView.setOnClickListener(v -> mListener.onItemClick(property));
    }

    @Override
    public int getItemCount() {
        if (properties == null){
            return 0;
        }else{
            return properties.size();
        }
    }

    public static class PropertiesViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView category;
        TextView city;
        TextView price;

        public PropertiesViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.image);
            category = itemView.findViewById(R.id.category);
            city = itemView.findViewById(R.id.city);
            price = itemView.findViewById(R.id.price);
        }
    }
}
