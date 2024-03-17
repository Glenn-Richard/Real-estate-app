package com.example.realestatemanager.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestatemanager.R;
import com.example.realestatemanager.callback.OnItemClickListener;
import com.example.realestatemanager.models.Property;

import java.util.List;

public class PropertyViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageViewPropertyPhoto, soldOutImageView;
    public TextView textViewTitle, textViewAddress, textViewPrice;

    public PropertyViewHolder(@NonNull View itemView, List<Property> properties, final OnItemClickListener listener) {
        super(itemView);
        soldOutImageView = itemView.findViewById(R.id.soldeOutImage);
        imageViewPropertyPhoto = itemView.findViewById(R.id.imageViewPropertyPhoto);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewAddress = itemView.findViewById(R.id.textViewAddress);
        textViewPrice = itemView.findViewById(R.id.textViewPrice);

        itemView.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(properties.get(position));
            }
        });
    }
}