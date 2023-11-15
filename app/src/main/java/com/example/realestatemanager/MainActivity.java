package com.example.realestatemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestatemanager.adapter.PropertiesAdapter;
import com.example.realestatemanager.databinding.ActivityMainBinding;
import com.example.realestatemanager.models.Property;
import com.example.realestatemanager.viewmodel.FirebaseViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.realestatemanager.databinding.ActivityMainBinding activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        setSupportActionBar(findViewById(R.id.topAppBar));

        initViewModel(this,activityMainBinding.getRoot());

    }

    /*------------------------------------------------TOP APP BAR MENU---------------------------------------------*/

   private void initRecyclerView(Context context, View view, List<Property> properties){
       RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
       LinearLayoutManager layoutManager = new LinearLayoutManager(this);
       recyclerView.setLayoutManager(layoutManager);
       PropertiesAdapter propertiesAdapter = new PropertiesAdapter(context,this);
       recyclerView.setAdapter(propertiesAdapter);
       propertiesAdapter.setAdapter(properties);

   }
   private void initViewModel(Context context, View view){
       FirebaseViewModel viewModel = new ViewModelProvider(this).get(FirebaseViewModel.class);
       viewModel.getProperties().observe(this, properties -> initRecyclerView(context,view,properties));
   }
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

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    /*----------------------------------------------------RECYCLER VIEW--------------------------------------------------------*/

    private void launchDetailActivity(Property property) {
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra("Property",property);
        startActivity(intent);
    }
    @Override
    public void onItemClick(Property property) {
        launchDetailActivity(property);
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