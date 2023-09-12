package com.example.realestatemanager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.realestatemanager.databinding.ActivityAddRealEstateBinding;

public class AddRealEstateActivity extends AppCompatActivity {

    ActivityAddRealEstateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRealEstateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AutoCompleteTextView category = binding.autocompletePopup;
        EditText cities = binding.cityPopup;
        EditText price = binding.pricePopup;

        listview_maker(category);

        close_activity();

    }
    private void close_activity(){
        ImageView return_button = binding.activityCloser;
        return_button.setOnClickListener(view -> finish());
    }
    private void listview_maker(AutoCompleteTextView autoCompleteTextView){

        String[] responsesPredifinies = {"House", "Loft", "Building", "Apartment"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, responsesPredifinies);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedResponse = (String) parent.getItemAtPosition(position);
        });
    }
}