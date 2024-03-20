package com.example.realestatemanager.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.realestatemanager.MainActivity;
import com.example.realestatemanager.R;
import com.example.realestatemanager.adapter.DetailAdapter;
import com.example.realestatemanager.databinding.FragmentDetailBinding;
import com.example.realestatemanager.db.PhotoDao;
import com.example.realestatemanager.db.RoomDB;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.Property;
import com.example.realestatemanager.viewmodel.RoomViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DetailFragment extends Fragment {
    private RoomViewModel roomViewModel;
    private FragmentDetailBinding binding;
    private static final String ARG_ID = "id";
    private LatLng latLng;

    public DetailFragment() {
    }

    public static DetailFragment newInstance(int id) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomViewModel = new ViewModelProvider(requireActivity()).get(RoomViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            int propertyId = getArguments().getInt(ARG_ID);
            roomViewModel.getProperty(propertyId).observe(getViewLifecycleOwner(), this::populateRealEstate);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("SetTextI18n")
    private void populateRealEstate(Property property) {
        if (property != null) {
            binding.propertyTitle.setText("Title : " + property.getTitle());
            binding.propertyDescription.setText("Description : " + property.getDescription());
            binding.propertyPrice.setText("Price : $ " + property.getPrice());
            binding.propertySurface.setText("Surface : " + property.getSurface());
            binding.propertyAddress.setText("Address : " + property.getAddressLoc().getAddressLabel());
            binding.propertyAgent.setText("Agent : " + property.getAgent());
            binding.propertyRooms.setText("Rooms : " + property.getRooms());
            binding.propertyBedrooms.setText("Bedrooms : " + property.getBedrooms());
            binding.propertyBathrooms.setText("Bathrooms : " + property.getBathrooms());
            //binding.schoolCheckbox.setChecked(property.hasSchoolNearby());
            //binding.shoppingCheckbox.setChecked(property.hasShoppingNearby());
            //binding.transportCheckbox.setChecked(property.hasTransportNearby());
           // binding.poolCheckbox.setChecked(property.hasPoolNearby());
            binding.propertyTitle.setText("Title : " + property.getTitle());
            binding.propertyDescription.setText("Description : " + property.getDescription());
            binding.statusAutoCompleteTextView.setText(property.getStatus());

            if (property.getMarketDate() != null) {
                String marketDateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(property.getMarketDate());
                binding.marketDateEditText.setText(marketDateStr);
            } else {
                binding.marketDateEditText.setText("");
            }

            if ("Sold".equals(property.getStatus()) && property.getSoldDate() != null) {
                String soldDateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(property.getSoldDate());
                binding.soldDateEditText.setText(soldDateStr);
                binding.soldDateEditText.setVisibility(View.VISIBLE);
            } else {
                binding.soldDateEditText.setVisibility(View.GONE);
            }

            if (property.getAddressLoc() != null && property.getAddressLoc().getLatLng() != null) {
                this.latLng = property.getAddressLoc().getLatLng(); // Mise à jour correcte

                String apiKey = getString(R.string.Api_key);
                String mapUrl = "https://maps.googleapis.com/maps/api/staticmap?center=" +
                        latLng.latitude + "," + latLng.longitude +
                        "&zoom=15&size=200x200&maptype=roadmap" +
                        "&markers=color:red%7Clabel:S%7C" +
                        latLng.latitude + "," + latLng.longitude +
                        "&key=" + apiKey;

                Glide.with(this)
                        .load(mapUrl)
                        .placeholder(R.drawable.map_placeholder)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                                Log.e("DetailFragment", "Erreur de chargement de la carte", e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                                Log.i("DetailFragment", "Carte chargée avec succès");
                                return false;
                            }
                        })
                        .into(binding.propertyMap);
            }

            binding.propertyRooms.setText(property.getRooms());
            RoomDB db = RoomDB.getInstance(getContext());
            PhotoDao photoDao = db.getAllPhotos();
            photoDao.getAllPhotos().observe(this, photos -> {
                DetailAdapter imageAdapter = new DetailAdapter(getContext(), photos,property);
                binding.photosRecyclerView.setAdapter(imageAdapter);
                binding.photosRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            });
            binding.propertyMap.setOnClickListener(v -> {
                if (latLng != null) {
                    String geoUri = "geo:" + latLng.latitude + "," + latLng.longitude + "?q=" + latLng.latitude + "," + latLng.longitude + "(Label)";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                    startActivity(intent);
                }

            });
        }
    }
}
