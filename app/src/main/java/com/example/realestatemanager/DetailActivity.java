package com.example.realestatemanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestatemanager.carrousel.ImageAdapter;
import com.example.realestatemanager.databinding.ActivityDetailBinding;
import com.example.realestatemanager.models.Property;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityDetailBinding binding;
    private TextView surface,rooms,bathrooms,bedrooms,location;
    private GoogleMap mMap;
    private Property property;
    private SupportMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        assignView();
        mapFragment.onCreate(savedInstanceState);
        mapFragment.getMapAsync(this);
        property = getProperty();
        assert property != null;
        configureCarrousel(property);
        fillContent();
    }
    private void assignView(){
        surface = binding.surfaceContent;
        rooms = binding.roomsContent;
        bathrooms = binding.bathroomsContent;
        bedrooms = binding.bedroomsContent;
        location = binding.locationContent;
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
    }
    private Property getProperty(){
        Intent intent = getIntent();
        if (intent != null) {
            return (Property) intent.getSerializableExtra("Property");
        }
        return null;
    }
    @SuppressLint("SetTextI18n")
    private void fillContent(){
        surface.setText(property.getSurface() +"m2");
        rooms.setText(String.valueOf(property.getNumber_of_rooms()));
        bedrooms.setText(String.valueOf(property.getNumber_of_bedrooms()));
        location.setText(property.getLocation());
    }
    private void configureCarrousel(Property property) {
        RecyclerView recyclerView = findViewById(R.id.recycler);

        ImageAdapter adapter = new ImageAdapter(DetailActivity.this, property.getImages());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng position = new LatLng(37.7749, -122.4194);
        Marker marker = mMap.addMarker(new MarkerOptions().position(position).title(location.getText().toString()));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
    }

}