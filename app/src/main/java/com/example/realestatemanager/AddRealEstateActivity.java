package com.example.realestatemanager;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.realestatemanager.databinding.ActivityAddRealEstateBinding;
import com.example.realestatemanager.models.Photo;
import com.example.realestatemanager.models.Property;
import com.example.realestatemanager.viewmodel.FirebaseViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AddRealEstateActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 36;
    private ActivityAddRealEstateBinding binding;
    private AutoCompleteTextView category, room1, room2, room3;
    private final List<Photo> images = new ArrayList<>();
    private ImageView image1, image2, image3;
    private EditText city, price, surface, rooms_number, beds_number, description;
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
        city = binding.cityPopup;
        price = binding.pricePopup;
        surface = binding.surfacePopup;
        rooms_number = binding.roomsPopup;
        beds_number = binding.bedroomsPopup;
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


        listview_maker(category, responsesPredifinies);
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
        }else if(city.getText().toString().isEmpty()) {
            city.setHint(error);
            city.setHintTextColor(Color.RED);
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
            String cit = city.getText().toString();
            String pri = price.getText().toString();
            String sur = surface.getText().toString();
            String roo = rooms_number.getText().toString();
            String bed = beds_number.getText().toString();
            submitProperty(cat,cit,pri,sur,roo,bed,images);
        }
    }

    private void submitProperty(String category, String city, String price, String surface, String rooms_number, String beds_number,List<Photo> images) {
        initViewModel().getProperties().observe(this, properties -> {
            String id = String.valueOf(properties.size());
            createProperty(id,category,city,price,surface,rooms_number,beds_number,images);
            uploadPhoto(Uri.parse(images.get(0).getUrl()));
            uploadPhoto(Uri.parse(images.get(1).getUrl()));
            uploadPhoto(Uri.parse(images.get(2).getUrl()));
        });

    }
    private void createProperty(String id,String category,String city,String price,String surface, String rooms_number, String beds_number, List<Photo> images){
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
        initViewModel().setProperty(property);
        Toast.makeText(this, "Property has been created", Toast.LENGTH_SHORT).show();
        finish();
    }
    private void listview_maker(AutoCompleteTextView autoCompleteTextView,String[] list){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        autoCompleteTextView.setAdapter(adapter);
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