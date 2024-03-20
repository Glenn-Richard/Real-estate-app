package com.example.realestatemanager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.realestatemanager.adapter.ImageAdapter;
import com.example.realestatemanager.databinding.ActivityAddRealEstateBinding;
import com.example.realestatemanager.models.AddressLoc;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.PointsOfInterest;
import com.example.realestatemanager.models.Property;
import com.example.realestatemanager.viewmodel.RoomViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.R)
public class AddRealEstateActivity extends AppCompatActivity{


    private final String TAG = AddRealEstateActivity.class.getName();
    private ActivityAddRealEstateBinding binding;
    private static final int REQUEST_PICK_IMAGE = 2;
    private final List<String> imageList = new ArrayList<>();
    private final List<Photo> photoList = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private RoomViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RoomViewModel.class);
        initView();
        initToolBar();
        setupListeners();
        initAgentList();
        setupStatusDropdown();
        setupDatePickers();
    }

    private void setupStatusDropdown() {
        String[] statuses = {getString(R.string.on_sale), getString(R.string.sold)};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, statuses);
        binding.statusAutoCompleteTextView.setAdapter(statusAdapter);
    }

    private void setupDatePickers() {
        binding.marketDateButton.setOnClickListener(view -> showDatePickerDialog(true));
        binding.soldDateButton.setOnClickListener(view -> showDatePickerDialog(false));
    }

    private void showDatePickerDialog(boolean isMarketDate) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    // Format de la date: AAAA-MM-JJ
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    if (isMarketDate) {
                        binding.marketDateButton.setText(selectedDate);
                    } else {
                        binding.soldDateButton.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void initView() {
        binding = ActivityAddRealEstateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void initToolBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.add_property));
        }
    }

    private void setupListeners() {
        binding.editPrice.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isUpdating) {
                    isUpdating = true;
                    String str = s.toString();
                    if (!str.startsWith("$")) {
                        str = "$" + str;
                        binding.editPrice.setText(str);
                        binding.editPrice.setSelection(str.length());
                    }
                    isUpdating = false;
                }
            }
        });

        binding.buttonPicture.setOnClickListener(v -> showPictureDialog());
        binding.addButton.setOnClickListener(v -> submitProperty());
    }

    private void initAgentList() {
        AutoCompleteTextView agentAutoCompleteTextView = findViewById(R.id.edit_agent);
        String[] agents = getResources().getStringArray(R.array.real_estate_agents);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, agents);
        agentAutoCompleteTextView.setAdapter(adapter);
    }

    private void showPictureDialog() {
        CharSequence[] items = {getString(R.string.take_photo), getString(R.string.choose_from_gallery), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.add_photo));

        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals(getString(R.string.take_photo))) {
                dispatchTakePictureIntent();
            } else if (items[item].equals(getString(R.string.choose_from_gallery))) {
                choosePhotoFromGallery();
            } else if (items[item].equals(getString(R.string.cancel))) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void choosePhotoFromGallery() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhotoIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(pickPhotoIntent, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    if (bitmap != null) {
                        addImageToList(data);
                    }
                }
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        addImageToList(data);
                    }
                } else if (data.getData() != null) {
                    addImageToList(data);
                }
            }
            updateRecyclerView();
        }
    }

    private void addImageToList(Intent data) {
        String urlToAdd = String.valueOf(data.getData());
        imageList.add(urlToAdd);

        showAddPhotoDialog(urlToAdd);
    }


    @SuppressLint("NotifyDataSetChanged")
    private void updateRecyclerView() {
        if (imageAdapter == null) {
            imageAdapter = new ImageAdapter(this, imageList);
            binding.photosRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.photosRecyclerView.setAdapter(imageAdapter);
        } else {
            imageAdapter.notifyDataSetChanged();
        }
    }

    private void submitProperty() {
        String title = Objects.requireNonNull(binding.editTitle.getText()).toString();
        String price = Objects.requireNonNull(binding.editPrice.getText()).toString().replaceAll("[$]", "");
        String surface = Objects.requireNonNull(binding.editSurface.getText()).toString();
        String address = Objects.requireNonNull(binding.editAddress.getText()).toString();
        String rooms = Objects.requireNonNull(binding.roomsInput.getText()).toString();
        String bedrooms = Objects.requireNonNull(binding.bedroomsInput.getText()).toString();
        String bathrooms = Objects.requireNonNull(binding.bathroomsInput.getText()).toString();
        String description = binding.descriptionInput.getText().toString();
        String agent = binding.editAgent.getText().toString();
        String status = binding.statusAutoCompleteTextView.getText().toString();
        String marketDateStr = binding.marketDateButton.getText().toString();
        String soldDateStr = binding.soldDateButton.getText().toString();
        Date marketDate = Utils.convertStringToDate(marketDateStr);
        Date soldDate = Utils.convertStringToDate(soldDateStr);

        if (validateInput(title, price, surface, address, rooms, bedrooms, bathrooms, description, agent, status, marketDateStr, soldDateStr) || imageList.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        AddressLoc addressLoc = new AddressLoc();
        addressLoc.setAddressLabel(address);
        addressLoc.setLatLng(Utils.
                getLocationFromAddress(this, address));

        Property property = new Property();
        property.setTitle(title);
        property.setPrice(price);
        property.setSurface(surface);
        property.setAddressLoc(addressLoc);
        property.setRooms(rooms);
        property.setBedrooms(bedrooms);
        property.setBathrooms(bathrooms);
        property.setDescription(description);
        property.setAgent(agent);
        property.setStatus(status);
        property.setMarketDate(marketDate);
        property.setSoldDate(soldDate);

        List<String> poi = getPoi();
        for (String point:poi) {
            PointsOfInterest pointsOfInterest = new PointsOfInterest();
            pointsOfInterest.setType(point);
            pointsOfInterest.setPropertyId(property.getId());
            viewModel.insertPoi(pointsOfInterest);
        }

        viewModel.insertProperty(property).observe(this, id -> {
            for(Photo photo : photoList) {
                photo.setPropertyId(id);
            }
            viewModel.insertPhotos(photoList);
        });

        Utils.displayNotification(AddRealEstateActivity.this, getString(R.string.successfully_added_property));
        finish();
    }

    private List<String> getPoi() {
        List<String> pois = new ArrayList<>();
        if (binding.schoolCheckbox.isChecked()) {
            pois.add(getString(R.string.school));
        }
        if (binding.shoppingCheckbox.isChecked()) {
            pois.add(getString(R.string.shopping));
        }
        if (binding.transportCheckbox.isChecked()) {
            pois.add(getString(R.string.transport));
        }
        if (binding.poolCheckbox.isChecked()) {
            pois.add(getString(R.string.swimming_pool));
        }
        return pois;
    }

    private boolean validateInput(String title, String price, String surface, String address, String rooms, String bedrooms, String bathrooms, String description, String agent, String status, String marketDateStr, String soldDateStr) {
        if (title.isEmpty() || price.isEmpty() || surface.isEmpty() || address.isEmpty() || rooms.isEmpty() || bedrooms.isEmpty() || bathrooms.isEmpty() || description.isEmpty() || agent.isEmpty() || marketDateStr.isEmpty()) {
            return true;
        }
        return "Sold".equals(status) && soldDateStr.isEmpty();// Passed all validations
    }

    private void showAddPhotoDialog(final String imageUri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_photo, null);
        builder.setView(dialogView);

        final EditText editTextDescription = dialogView.findViewById(R.id.editText);
        Button buttonSubmit = dialogView.findViewById(R.id.submit);

        final AlertDialog dialog = builder.create();

        buttonSubmit.setOnClickListener(v -> {
            String description = editTextDescription.getText().toString();
            addPhotoToList(imageUri, description);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void addPhotoToList(String imageUri, String description) {
        Photo photo = new Photo();
        photo.setUrl(imageUri);
        photo.setDescription(description);
        photoList.add(photo);
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
