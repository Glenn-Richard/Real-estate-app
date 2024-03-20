package com.example.realestatemanager.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.realestatemanager.R;

public class DetailViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;

    public DetailViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
    }
}
