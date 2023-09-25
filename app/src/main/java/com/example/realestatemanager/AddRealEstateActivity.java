package com.example.realestatemanager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.realestatemanager.databinding.ActivityAddRealEstateBinding;

public class AddRealEstateActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 36;
    ActivityAddRealEstateBinding binding;
    ImageView image1;
    Button addImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRealEstateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AutoCompleteTextView category = binding.autocompletePopup;
        addImage = binding.uploadFileButton;
        image1 = binding.image1;

        addImage.setOnClickListener(view -> openGallery());

        listview_maker(category);

        close_activity();

    }
    private void close_activity(){
        ImageView return_button = binding.activityCloser;
        return_button.setOnClickListener(view -> finish());
    }
    private void formIsComplete(){
        EditText cities = binding.cityPopup;
        EditText price = binding.pricePopup;

    }
    private void listview_maker(AutoCompleteTextView autoCompleteTextView){

        String[] responsesPredifinies = {"House", "Loft", "Building", "Apartment"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, responsesPredifinies);
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
                image1.setImageURI(data.getData());
            }
        }

    }
}