package com.example.realestatemanager;

import static android.service.controls.ControlsProviderService.TAG;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.realestatemanager.databinding.ActivityAddRealEstateBinding;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.Property;
import com.example.realestatemanager.viewmodel.FirebaseViewModel;
import com.example.realestatemanager.viewmodel.RetrofitViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.R)
public class AddRealEstateActivity extends AppCompatActivity{

    private static final int PICK_IMAGE_REQUEST = 36;
    private ActivityAddRealEstateBinding binding;
    private AutoCompleteTextView category, room1, room2, room3;
    private final List<Photo> images = new ArrayList<>();
    private ImageView image1, image2, image3;
    private EditText price, surface, rooms_number, beds_number, bathrooms, description;
    private TextView city;
    private TextInputLayout city_layout;
    private TextInputLayout download1, download2, download3;
    private int count = 0;
    private Button addImage;
    private final String[] responsesPredifinies = {"House", "Loft", "Building", "Apartment"};
    private final String[] imageType = {"Facade", "Kitchen", "Bathroom", "Lounge", "Bedroom", "Garage"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRealEstateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button save = binding.btnSave;

        category = binding.autocompletePopup;
        city = binding.autocompleteCity;
        city_layout = binding.cityPopupLayout;
        price = binding.pricePopup;
        surface = binding.surfacePopup;
        rooms_number = binding.roomsPopup;
        beds_number = binding.bedroomsPopup;
        bathrooms = binding.bathroomsPopup;
        description = binding.descriptionPopup;

        clearEditText(price);
        clearEditText(surface);
        clearEditText(rooms_number);
        clearEditText(beds_number);

        numberFilter(price);
        numberFilter(surface);
        numberFilter(rooms_number);
        numberFilter(beds_number);

        room1 = binding.typeImage1;
        room2 = binding.typeImage2;
        room3 = binding.typeImage3;

        download1 = binding.downloadZone1;
        download1.setVisibility(View.GONE);

        download2 = binding.downloadZone2;
        download2.setVisibility(View.GONE);

        download3 = binding.downloadZone3;
        download3.setVisibility(View.GONE);

        addImage = binding.uploadFileButton;

        image1 = binding.image1;

        image2 = binding.image2;

        image3 = binding.image3;

        addImage.setOnClickListener(view -> openGallery());
        save.setOnClickListener(view -> formIsComplete(images));

        city.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                autocompleteLauncher();
            }
        });

        listview_maker(category,responsesPredifinies);
        listview_maker(room1, imageType);
        listview_maker(room2, imageType);
        listview_maker(room3, imageType);

        close_activity();

    }
    private void numberFilter(EditText editText) {
        editText.setOnEditorActionListener((v, actionId, event) -> actionId == EditorInfo.IME_ACTION_DONE);
        editText.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }});
    }
    private void clearEditText(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0 && (editable.length() % 4) == 0) {
                    char lastChar = editable.charAt(editable.length() - 1);
                    if (Character.isDigit(lastChar)) {
                        editable.insert(editable.length() - 1, " ");
                    }
                }
            }
        });
    }
    private FirebaseViewModel initViewModel(){
        return new ViewModelProvider(this).get(FirebaseViewModel.class);
    }
    private void close_activity(){
        ImageView return_button = binding.activityCloser;
        return_button.setOnClickListener(view -> finish());
    }
    private void uploadPhoto(Uri uri){
        initViewModel().uploadPhoto(uri);
    }
    private void formIsComplete(List<Photo> images){
        String error = "This field must be completed";
        if(category.getText().toString().isEmpty()){
            category.setHint("Choose a category");
            category.setHintTextColor(Color.RED);
        }else if(price.getText().toString().isEmpty()){
            price.setHint(error);
            price.setHintTextColor(Color.RED);
        }else if(surface.getText().toString().isEmpty()){
            surface.setHint(error);
            surface.setHintTextColor(Color.RED);
        }else if(rooms_number.getText().toString().isEmpty()){
            rooms_number.setHint(error);
            rooms_number.setHintTextColor(Color.RED);
        }else if(beds_number.getText().toString().isEmpty()){
            beds_number.setHint(error);
            beds_number.setHintTextColor(Color.RED);
        }else if(bathrooms.getText().toString().isEmpty()){
            bathrooms.setHint(error);
            bathrooms.setHintTextColor(Color.RED);
        }else if(images.isEmpty()){
            Toast.makeText(this, "Select at least one image", Toast.LENGTH_SHORT).show();
        }else if(images.size()==1){
            if(room1.getText().toString().isEmpty()){
                room1.setHint(error);
                room1.setHintTextColor(Color.RED);
            }else{
                images.get(0).setDescription(room1.getText().toString());
                initViewModel().uploadPhoto(Uri.parse(images.get(0).getUrl()));
            }
        }else if(images.size()==2){
            if(room1.getText().toString().isEmpty()){
                room1.setHint(error);
                room1.setHintTextColor(Color.RED);
            }else if(room2.getText().toString().isEmpty()){
                room2.setHint(error);
                room2.setHintTextColor(Color.RED);
            }else{
                images.get(0).setDescription(room1.getText().toString());
                images.get(1).setDescription(room2.getText().toString());
                initViewModel().uploadPhoto(Uri.parse(images.get(0).getUrl()));
                initViewModel().uploadPhoto(Uri.parse(images.get(1).getUrl()));
            }
        }else if(images.size()==3){
            if(room1.getText().toString().isEmpty()){
                room1.setHint(error);
                room1.setHintTextColor(Color.RED);
            }else if(room2.getText().toString().isEmpty()){
                room2.setHint(error);
                room2.setHintTextColor(Color.RED);
            }else if(room3.getText().toString().isEmpty()){
                room3.setHint(error);
                room3.setHintTextColor(Color.RED);
            }else{
                images.get(0).setDescription(room1.getText().toString());
                images.get(1).setDescription(room2.getText().toString());
                images.get(2).setDescription(room3.getText().toString());
                initViewModel().uploadPhoto(Uri.parse(images.get(0).getUrl()));
                initViewModel().uploadPhoto(Uri.parse(images.get(1).getUrl()));
                initViewModel().uploadPhoto(Uri.parse(images.get(2).getUrl()));
            }
        }else{
            String cat = category.getText().toString();
            String cit = "city";
            String pri = price.getText().toString();
            String sur = surface.getText().toString();
            String roo = rooms_number.getText().toString();
            String bed = beds_number.getText().toString();
            String bath = bathrooms.getText().toString();
            submitProperty(cat,cit,pri,sur,roo,bed,bath,images);
        }
    }

    private void submitProperty(String category, String city, String price, String surface, String rooms_number, String beds_number,String bathrooms, List<Photo> images) {
        initViewModel().getProperties().observe(this, properties -> {
            String id = String.valueOf(properties.size());
            createProperty(id,category,city,price,surface,rooms_number,beds_number,bathrooms,images);
            uploadPhoto(Uri.parse(images.get(0).getUrl()));
            uploadPhoto(Uri.parse(images.get(1).getUrl()));
            uploadPhoto(Uri.parse(images.get(2).getUrl()));
        });

    }
    private void createProperty(String id,String category,String city,String price,String surface, String rooms_number, String beds_number, String bathrooms, List<Photo> images){
        Property property = new Property(
                id,
                category,
                city,
                Integer.parseInt(price),
                Integer.parseInt(surface),
                description.getText().toString(),
                Integer.parseInt(rooms_number),
                Integer.parseInt(beds_number),
                Integer.parseInt(bathrooms),
                images);
        initViewModel().setProperty(property);
        Toast.makeText(this, "Property has been created", Toast.LENGTH_SHORT).show();
        finish();
    }
    private void listview_maker(AutoCompleteTextView autoCompleteTextView,String[] list){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        autoCompleteTextView.setAdapter(adapter);
    }
    private void placeInitializer(String query){
        RetrofitViewModel viewModel = new ViewModelProvider(this).get(RetrofitViewModel.class);
        viewModel.initializePlaces(this,0,0,query);
    }
    private void autocompleteLauncher(){
        if (!Places.isInitialized()){
            Places.initialize(this,"AIzaSyBhBe93XEn4olaUwiB8X_o1kWvmX4mwOG4");
        }
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);

        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(this);
        startAutocomplete.launch(intent);
    }
    private final ActivityResultLauncher<Intent> startAutocomplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Place place = Autocomplete.getPlaceFromIntent(intent);
                        placeInitializer(place.getAddress());
                        city.setText(place.getAddress());
                        Log.i(TAG, "Place: ${place.getName()}, ${place.getId()}");
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    Log.i(TAG, "User canceled autocomplete");
                }
            });
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(reqCode == PICK_IMAGE_REQUEST){
                if(count==0) {
                    download1.setVisibility(View.VISIBLE);
                    image1.setImageURI(data.getData());
                    Photo photo = new Photo();
                    photo.setUrl(data.getData().toString());
                    images.add(photo);
                    count++;
                }
                else if(count==1){
                    download2.setVisibility(View.VISIBLE);
                    image2.setImageURI(data.getData());
                    Photo photo = new Photo();
                    photo.setUrl(data.getData().toString());
                    images.add(photo);
                    count++;
                }
                else{
                    download3.setVisibility(View.VISIBLE);
                    image3.setImageURI(data.getData());
                    Photo photo = new Photo();
                    photo.setUrl(data.getData().toString());
                    images.add(photo);
                    addImage.setVisibility(View.GONE);
                }
            }
        }
    }
}

/*List<PlacesResponse.Prediction> predictions = placesResponse.getPredictions();
                        for (PlacesResponse.Prediction prediction : predictions) {
                            String address = prediction.getDescription();
                            String placeId = prediction.getPlaceId();
                            // Faites quelque chose avec l'adresse et l'ID de l'endroit
                        }*/