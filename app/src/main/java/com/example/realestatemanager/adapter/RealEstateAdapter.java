package com.example.realestatemanager.adapter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.realestatemanager.R;
import com.example.realestatemanager.Utils;
import com.example.realestatemanager.callback.OnItemClickListener;
import com.example.realestatemanager.models.Property;
import com.example.realestatemanager.viewholder.PropertyViewHolder;

import java.util.List;

public class RealEstateAdapter extends RecyclerView.Adapter<PropertyViewHolder> {
    private final List<Property> properties;
    private final OnItemClickListener listener;

    public RealEstateAdapter(List<Property> properties, OnItemClickListener listener) {
        this.properties = properties;
        this.listener = listener;
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

        if (property.getImageUrls() != null && !property.getImageUrls().isEmpty()) {
            Uri uri = Uri.parse(property.getImageUrls().get(0));
            if (uri != null) {
                Glide.with(holder.itemView.getContext())
                        .load(uri)
                        .into(holder.imageViewPropertyPhoto);
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
