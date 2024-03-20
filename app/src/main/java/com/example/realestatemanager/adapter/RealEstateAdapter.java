package com.example.realestatemanager.adapter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.realestatemanager.R;
import com.example.realestatemanager.callback.OnItemClickListener;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.Property;
import com.example.realestatemanager.viewholder.PropertyViewHolder;

import java.util.List;

public class RealEstateAdapter extends RecyclerView.Adapter<PropertyViewHolder> {
    private final List<Property> properties;
    private final List<Photo> photos;
    private final OnItemClickListener listener;

    public RealEstateAdapter(List<Property> properties, List<Photo> photos, OnItemClickListener listener) {
        this.properties = properties;
        this.listener = listener;
        this.photos = photos;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.realty_list_item, parent, false);
        return new PropertyViewHolder(view, properties, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property property = properties.get(position);
        holder.textViewTitle.setText(property.getTitle());
        if (property.getAddressLoc() != null) {
            holder.textViewAddress.setText(property.getAddressLoc().getAddressLabel());
        }

        String formattedPrice = "$" + property.getPrice();
        holder.textViewPrice.setText(formattedPrice);

        if (!photos.isEmpty()) {
            for (Photo photo : photos) {
                if(photo.getPropertyId()==property.getId()){
                    Glide.with(holder.itemView.getContext())
                            .load(photo.getUrl())
                            .into(holder.imageViewPropertyPhoto);
                }
            }
        }

        Resources resources = holder.itemView.getContext().getResources();
        if (property.getStatus().equals(resources.getString(R.string.sold))) {
            holder.soldOutImageView.setVisibility(View.VISIBLE);
        } else {
            holder.soldOutImageView.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return properties.size();
    }
}
