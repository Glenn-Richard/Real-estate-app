package com.example.realestatemanager;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.realestatemanager.databinding.ActivityEditPropertyBinding;
import com.example.realestatemanager.models.AddressLoc;
import com.example.realestatemanager.models.Property;
import com.example.realestatemanager.viewmodel.RoomViewModel;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditPropertyActivity extends AppCompatActivity {
    private ActivityEditPropertyBinding binding;
    private RoomViewModel realEstateViewModel;
    private Property currentRealEstate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realEstateViewModel = new ViewModelProvider(this).get(RoomViewModel.class);

        initView();
        initTopBar();
        initListeners();
        loadPropertyDetails();
        handleFormSubmission();
        setupDeleteButton();
    }

    private void initView() {
        binding = ActivityEditPropertyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void initTopBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void initListeners() {
        binding.switchSold.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.editTextSoldDate.setEnabled(isChecked);
            if (!isChecked) {
                binding.editTextSoldDate.setText("");
            }
        });
    }

    private void setupDeleteButton() {
        binding.buttonDeleteProperty.setOnClickListener(v -> {
            if (currentRealEstate != null) {
                realEstateViewModel.deleteProperty(currentRealEstate);
                Toast.makeText(EditPropertyActivity.this, getString(R.string.successfully_deleted_property), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditPropertyActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(EditPropertyActivity.this, getString(R.string.error_delete), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPropertyDetails() {
        int propertyId = getIntent().getIntExtra("PROPERTY_ID", -1);
        realEstateViewModel.getProperty(propertyId).observe(this, realEstate -> {
            if (realEstate != null) {
                currentRealEstate = realEstate;
                binding.editTextTitle.setText(realEstate.getTitle());
                binding.editTextPrice.setText(realEstate.getPrice());
                binding.editTextDescription.setText(realEstate.getDescription());
                binding.editTextSurface.setText(realEstate.getSurface());
                binding.editTextRooms.setText(String.valueOf(realEstate.getRooms()));
                binding.editTextBedrooms.setText(realEstate.getBedrooms());
                binding.editTextBathrooms.setText(realEstate.getBathrooms());
                binding.editTextAddress.setText(realEstate.getAddressLoc().getAddressLabel());
                binding.editTextAgent.setText(realEstate.getAgent());
                if (realEstate.getMarketDate() != null) {
                    binding.editTextMarketDate.setText(formatDate(realEstate.getMarketDate()));
                }
               // binding.checkBoxSchool.setChecked(realEstate.hasSchoolNearby());
                //binding.checkBoxShopping.setChecked(realEstate.hasShoppingNearby());
                //binding.checkBoxPublicTransport.setChecked(realEstate.hasTransportNearby());
                //binding.checkBoxPool.setChecked(realEstate.hasPoolNearby());
            }
        });
    }

    private String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
    }

    private void handleFormSubmission() {
        binding.buttonUpdateProperty.setOnClickListener(v -> {
            if (validateInput()) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    try {
                        Geocoder geocoder = new Geocoder(EditPropertyActivity.this);
                        List<Address> addresses = geocoder.getFromLocationName(binding.editTextAddress.getText().toString(), 1);
                        if (!addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            runOnUiThread(() -> updateRealEstateWithNewLocation(address.getLatitude(), address.getLongitude()));
                        } else {
                            runOnUiThread(() -> Toast.makeText(EditPropertyActivity.this, R.string.error_address, Toast.LENGTH_LONG).show());
                        }
                    } catch (IOException e) {
                        runOnUiThread(() -> Toast.makeText(EditPropertyActivity.this, R.string.geocoder_error, Toast.LENGTH_LONG).show());
                    }
                });
            } else {
                Toast.makeText(this, getString(R.string.check_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput() {
        return !TextUtils.isEmpty(binding.editTextTitle.getText()) &&
                !TextUtils.isEmpty(binding.editTextPrice.getText()) &&
                !TextUtils.isEmpty(binding.editTextSurface.getText()) &&
                !TextUtils.isEmpty(binding.editTextRooms.getText()) &&
                !TextUtils.isEmpty(binding.editTextDescription.getText()) &&
                !TextUtils.isEmpty(binding.editTextBedrooms.getText()) &&
                !TextUtils.isEmpty(binding.editTextBathrooms.getText()) &&
                !TextUtils.isEmpty(binding.editTextMarketDate.getText());
    }

    private void updateRealEstateWithNewLocation(double latitude, double longitude) {
        AddressLoc addressLoc = currentRealEstate.getAddressLoc() != null ? currentRealEstate.getAddressLoc() : new AddressLoc();
        addressLoc.setAddressLabel(binding.editTextAddress.getText().toString());
        addressLoc.setLatLng(new com.google.android.gms.maps.model.LatLng(latitude, longitude));
        currentRealEstate.setAddressLoc(addressLoc);
        currentRealEstate.setTitle(binding.editTextTitle.getText().toString());
        currentRealEstate.setPrice(binding.editTextPrice.getText().toString());
        currentRealEstate.setSurface(binding.editTextSurface.getText().toString());
        currentRealEstate.setDescription(binding.editTextDescription.getText().toString());
        currentRealEstate.setRooms(binding.editTextRooms.getText().toString());
        currentRealEstate.setBedrooms(binding.editTextBedrooms.getText().toString());
        currentRealEstate.setBathrooms(binding.editTextBathrooms.getText().toString());
        currentRealEstate.setAgent(binding.editTextAgent.getText().toString());

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            currentRealEstate.setMarketDate(sdf.parse(Objects.requireNonNull(binding.editTextMarketDate.getText()).toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<String> poi = new ArrayList<>();
        if (binding.checkBoxSchool.isChecked()) poi.add(getString(R.string.school));
        if (binding.checkBoxShopping.isChecked()) poi.add(getString(R.string.shopping));
        if (binding.checkBoxPublicTransport.isChecked()) poi.add(getString(R.string.transport));
        if (binding.checkBoxPool.isChecked()) poi.add(getString(R.string.swimming_pool));
        //currentRealEstate.setPointsOfInterest(poi);

        if (binding.switchSold.isChecked()) {
            currentRealEstate.setStatus(getString(R.string.sold));
            try {
                currentRealEstate.setSoldDate(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(Objects.requireNonNull(binding.editTextSoldDate.getText()).toString()));
            } catch (ParseException e) {
                Toast.makeText(this, R.string.format_date_error, Toast.LENGTH_SHORT).show();
            }
        } else {
            currentRealEstate.setStatus("Available");
            currentRealEstate.setSoldDate(null);
        }

        realEstateViewModel.updateProperty(currentRealEstate);
        Toast.makeText(this, R.string.update_property, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}