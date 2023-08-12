package com.example.realestatemanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.realestatemanager.databinding.ActivityMainBinding;
import com.example.realestatemanager.databinding.PopupBinding;

public class MainActivity extends AppCompatActivity {

    private PopupBinding popupBinding;
    private static final int REQUEST_SELECT_IMAGE = 1;
    private ImageView imageView;
    private Button file_popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.realestatemanager.databinding.ActivityMainBinding activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        popupBinding = PopupBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        setSupportActionBar(findViewById(R.id.topAppBar));
        upload_file();
    }

    /*------------------------------------------------TOP APP BAR MENU---------------------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorite:
                showToast("Item 1 cliqué");
                return true;
            case R.id.search:
                showToast("Item 2 cliqué");
                return true;
            case R.id.add_real_estate:
                showPopup();

        }
        return super.onOptionsItemSelected(item);
    }

    /*----------------------------------------------------POP UP--------------------------------------------------------*/
    private void showPopup() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View popupView = inflater.inflate(R.layout.popup, null);

        PopupWindow popupWindow = new PopupWindow(popupView, 900, 1400);

        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(findViewById(R.id.mainActivity_container), Gravity.CENTER, 0, 0);

        final AutoCompleteTextView category = popupView.findViewById(R.id.autocomplete_popup);
        final EditText city = popupView.findViewById(R.id.city_popup);
        final EditText price = popupView.findViewById(R.id.price_popup);
        final ImageView close = popupView.findViewById(R.id.popup_closer);
        final Button btnSave = popupView.findViewById(R.id.btnSave);

        listview_maker(category);


        file_popup.setOnClickListener(view -> openGallery());
        btnSave.setOnClickListener(v -> popupWindow.dismiss());
        close.setOnClickListener(view -> popupWindow.dismiss());

    }

    private void listview_maker(AutoCompleteTextView autoCompleteTextView){

        String[] responsesPredifinies = {"House", "Loft", "Building", "Apartment"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, responsesPredifinies);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedResponse = (String) parent.getItemAtPosition(position);
        });
    }

    private void upload_file(){
        imageView = popupBinding.popupCloser;
        file_popup = popupBinding.uploadFileButton;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            file_popup.setVisibility(View.GONE);
            imageView.setImageURI(selectedImageUri);
        }
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }






//@SuppressLint("SetTextI18n")
//    private void configureTextViewMain(){
//        this.textViewMain.setTextSize(15);
//        this.textViewMain.setText("Le premier bien immobilier enregistré vaut ");
//    }

//    private void configureTextViewQuantity(){
//        int quantity = Utils.convertDollarToEuro(100);
//        this.textViewQuantity.setTextSize(20);
//        this.textViewQuantity.setText(String.valueOf(quantity));
//    }
}