package com.example.realestatemanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.realestatemanager.databinding.ActivityAddRealEstateBinding;
import com.example.realestatemanager.models.Property;
import com.example.realestatemanager.viewmodel.FirebaseViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AddRealEstateActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 36;
    private ActivityAddRealEstateBinding binding;
    private AutoCompleteTextView category;
    private final List<String> images = new ArrayList<>();
    private ImageView image1,image2,image3;
    private EditText city,price,surface,rooms_number,beds_number,description;
    private TextInputLayout download1,download2,download3;
    private int count = 0;
    private Button addImage;
    private final String[] responsesPredifinies = {"House", "Loft", "Building", "Apartment"};
    private final String[] imageType = {"Facade","Kitchen","Bathroom","Lounge","Bedroom","Garage"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRealEstateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button save = binding.btnSave;

        category = binding.autocompletePopup;
        city = binding.cityPopup;
        price = binding.pricePopup;
        surface = binding.surfacePopup;
        rooms_number = binding.roomsPopup;
        beds_number = binding.bedroomsPopup;
        description = binding.descriptionPopup;

        AutoCompleteTextView room1 = binding.typeImage1;
        AutoCompleteTextView room2 = binding.typeImage2;
        AutoCompleteTextView room3 = binding.typeImage3;

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


        listview_maker(category,responsesPredifinies);
        listview_maker(room1,imageType);
        listview_maker(room2,imageType);
        listview_maker(room3,imageType);

        close_activity();

    }
    private void visibilityController(){
        download1.setVisibility(View.GONE);
        download2.setVisibility(View.GONE);
        download3.setVisibility(View.GONE);
    }
    private void close_activity(){
        ImageView return_button = binding.activityCloser;
        return_button.setOnClickListener(view -> finish());
    }
    private void formIsComplete(List<String> images){
        String error = "This field must be completed";
        if(category.getText()==category.getHint()){
            category.setHint("Choose a category");
            category.setHintTextColor(Color.RED);
        }if(city.getText().toString().isEmpty()) {
            city.setHint(error);
            city.setHintTextColor(Color.RED);
        }if(price.getText().toString().isEmpty()){
            price.setHint(error);
            price.setHintTextColor(Color.RED);
        }if(surface.getText().toString().isEmpty()){
            surface.setHint(error);
            surface.setHintTextColor(Color.RED);
        }if(rooms_number.getText().toString().isEmpty()){
            rooms_number.setHint(error);
            rooms_number.setHintTextColor(Color.RED);
        }if(beds_number.getText().toString().isEmpty()){
            beds_number.setHint(error);
            beds_number.setHintTextColor(Color.RED);
        }
        else{
            String cat = category.getText().toString();
            String cit = city.getText().toString();
            String pri = price.getText().toString();
            String sur = surface.getText().toString();
            String roo = rooms_number.getText().toString();
            String bed = beds_number.getText().toString();
            submitProperty(cat,cit,pri,sur,roo,bed,images);
        }
    }

    private void submitProperty(String category, String city, String price, String surface, String rooms_number, String beds_number,List<String> images) {
        FirebaseViewModel viewModel = new ViewModelProvider(this).get(FirebaseViewModel.class);
        viewModel.getProperties().observe(this, properties -> {
            String id = String.valueOf(properties.size());
            createProperty(id,category,city,price,surface,rooms_number,beds_number,images);
        });

    }
    private void createProperty(String id,String category,String city,String price,String surface, String rooms_number, String beds_number, List<String> images){
        FirebaseViewModel viewModel = new ViewModelProvider(this).get(FirebaseViewModel.class);
        Property property = new Property(
                id,
                category,
                city,
                Integer.parseInt(price),
                Integer.parseInt(surface),
                description.getText().toString(),
                Integer.parseInt(rooms_number),
                Integer.parseInt(beds_number),
                images);
        viewModel.setProperty(property);
    }
    private void listview_maker(AutoCompleteTextView autoCompleteTextView,String[] list){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedResponse = (String) parent.getItemAtPosition(position);
        });
    }
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
                    images.add(data.getData().toString());
                    count++;
                }
                else if(count==1){
                    download2.setVisibility(View.VISIBLE);
                    image2.setImageURI(data.getData());
                    images.add(data.getData().toString());
                    count++;
                }
                else{
                    download3.setVisibility(View.VISIBLE);
                    image3.setImageURI(data.getData());
                    images.add(data.getData().toString());
                    addImage.setVisibility(View.GONE);
                }
            }
        }
    }
}