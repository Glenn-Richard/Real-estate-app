package com.example.realestatemanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.realestatemanager.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMG = 36;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.realestatemanager.databinding.ActivityMainBinding activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        setSupportActionBar(findViewById(R.id.topAppBar));
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
                addRealEstateActivity();

        }
        return super.onOptionsItemSelected(item);
    }

    /*----------------------------------------------------ADD REAL ESTATE--------------------------------------------------------*/

    private void addRealEstateActivity(){
        Intent intent = new Intent(MainActivity.this,
                AddRealEstateActivity.class);
        startActivity(intent);

    }
    private void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
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