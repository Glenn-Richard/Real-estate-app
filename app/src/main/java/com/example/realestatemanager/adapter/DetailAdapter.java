package com.example.realestatemanager.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.realestatemanager.R;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.Property;
import com.example.realestatemanager.viewholder.DetailViewHolder;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailViewHolder> {
    private final Context context;
    private final List<Photo> photos;
    private final Property property;

    public DetailAdapter(Context context, List<Photo> photos, Property property) {
        this.context = context;
        this.photos = photos;
        this.property = property;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        if(!photos.isEmpty()){
            if(photos.get(position).getPropertyId()==property.getId()){
                Glide.with(context)
                        .load(photos.get(position).getUrl())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                                Log.e("ERROR",e.getMessage());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into((ImageView) holder.itemView.findViewById(R.id.imageView));
            }
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public List<Photo> getImages() {
        return photos;
    }
}
