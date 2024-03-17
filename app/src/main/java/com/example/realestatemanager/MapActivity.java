package com.example.realestatemanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.realestatemanager.models.AddressLoc;
import com.example.realestatemanager.models.Property;
import com.example.realestatemanager.viewmodel.MapViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;
    private GoogleMap mMap;
    private MapViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        viewModel = new ViewModelProvider(this).get(MapViewModel.class);

        viewModel.getLocationPermissionGranted().observe(this, this::updateMapUI);
        viewModel.getRealtyList().observe(this, this::addPropertyMarkers);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        initToolBar();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        setupMap();

        LatLng newYork = new LatLng(40.7128, -74.0060);
        mMap.addMarker(new MarkerOptions().position(newYork).title("New York"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newYork, 10));
    }

    @SuppressLint("PotentialBehaviorOverride")
    private void setupMap() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnMarkerClickListener(marker -> {
            Integer propertyId = (Integer) marker.getTag();
            if (propertyId != null) {
                Intent intent = new Intent(MapActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.EXTRA_PROPERTY_ID, propertyId);
                startActivity(intent);
            } else {
                Log.e("MapActivity", "L'ID de la propriété est nul.");
            }

            return false;
        });


    }

    private void updateMapUI(Boolean isGranted) {
        if (isGranted != null && isGranted && mMap != null) {
            setupMap();
        } else {
            Toast.makeText(this, "Permission de localisation refusée", Toast.LENGTH_SHORT).show();
        }
    }

    private void addPropertyMarkers(List<Property> properties) {
        if (mMap == null) return;
        mMap.clear();

        for (Property property : properties) {
            AddressLoc addressLoc = property.getAddressLoc();
            if (addressLoc != null && addressLoc.getLatLng() != null) {
                LatLng location = addressLoc.getLatLng();
                Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(property.getTitle()));
                assert marker != null;
                marker.setTag(property.getId());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            boolean isGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            viewModel.updateLocationPermissionStatus(isGranted);
        }
    }

    private void initToolBar() {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            getSupportActionBar().setTitle(" Map");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}