package com.example.realestatemanager.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.realestatemanager.R;
import com.example.realestatemanager.Utils;
import com.example.realestatemanager.viewholder.ImageViewHolder;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    private final Context context;
    private final List<String> imageList;

    public ImageAdapter(Context context, List<String> imageUris) {
        this.context = context;
        this.imageList = imageUris;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String base64Image = imageList.get(position);
        Bitmap bitmap = Utils.base64ToBitmap(base64Image);
        if (bitmap != null) {
            Glide.with(context).load(bitmap).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public List<String> getImages() {
        return imageList;
    }
}
