package com.example.realestatemanager.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.realestatemanager.adapter.SearchResultsAdapter;
import com.example.realestatemanager.databinding.FragmentSearchBinding;
import com.example.realestatemanager.db.PhotoDao;
import com.example.realestatemanager.db.PointsOfInterestDao;
import com.example.realestatemanager.db.PropertyDao;
import com.example.realestatemanager.db.RoomDB;
import com.example.realestatemanager.models.Property;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchResultsAdapter searchResultsAdapter;
    private String selectedStartDate;
    private String selectedEndDate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        setupSearchButton();
        setupRecyclerView();
        setupDatePickers();
        return binding.getRoot();
    }

    private void setupDatePickers() {
        binding.btnStartDate.setOnClickListener(v -> showDatePicker(true));
        binding.btnEndDate.setOnClickListener(v -> showDatePicker(false));
    }

    private void showDatePicker(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
            String selectedDate = String.format(Locale.US, "%d-%02d-%02d", year1, month1 + 1, dayOfMonth);
            if (isStartDate) {
                selectedStartDate = selectedDate;
                binding.btnStartDate.setText(selectedDate);
            } else {
                selectedEndDate = selectedDate;
                binding.btnEndDate.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void setupRecyclerView() {
        searchResultsAdapter = new SearchResultsAdapter();
        binding.searchResultsRecyclerView.setAdapter(searchResultsAdapter);
        binding.searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setupSearchButton() {
        binding.btnSearch.setOnClickListener(v -> performSearch());
    }

    private void performSearch() {
        Integer minSurface = getIntegerFromEditText(binding.minSurfaceEditText);
        Integer maxSurface = getIntegerFromEditText(binding.maxSurfaceEditText);
        Integer minPrice = getIntegerFromEditText(binding.minPriceEditText);
        Integer maxPrice = getIntegerFromEditText(binding.maxPriceEditText);
        Integer minRooms = getIntegerFromEditText(binding.minRoomsEditText);
        Integer maxRooms = getIntegerFromEditText(binding.maxRoomsEditText);
        boolean pool = getBooleanFromCheckbox(binding.poolCheckbox);
        boolean shop = getBooleanFromCheckbox(binding.shoppingCheckbox);
        boolean scho = getBooleanFromCheckbox(binding.schoolCheckbox);
        boolean tran = getBooleanFromCheckbox(binding.transportCheckbox);
        List<String> type = new ArrayList<>();


        Long startDateTimestamp = null;
        Long endDateTimestamp = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            if (selectedStartDate != null && !selectedStartDate.isEmpty()) {
                Date startDate = sdf.parse(selectedStartDate);
                startDateTimestamp = (startDate != null) ? startDate.getTime() : null;
            }
            if (selectedEndDate != null && !selectedEndDate.isEmpty()) {
                Date endDate = sdf.parse(selectedEndDate);
                endDateTimestamp = (endDate != null) ? endDate.getTime() : null;
            }
        } catch (ParseException e) {
            e.printStackTrace();

        }
        if(pool){
            type.add("Swimming Pool");
        }
        if(shop){
            type.add("Shopping");
        }if(scho){
            type.add("School");
        }if(tran){
            type.add("Transport");
        }

        RoomDB db = RoomDB.getInstance(getContext());
        PropertyDao dao = db.getAllProperties();
        PointsOfInterestDao interestDao = db.getAllPoi();
        LiveData<List<Property>> results = dao.searchProperties(minSurface, maxSurface, minPrice, maxPrice, minRooms, maxRooms, startDateTimestamp, endDateTimestamp);
        LiveData<List<Property>> results2 = interestDao.searchPoi(type);

    }


    private Integer getIntegerFromEditText(EditText editText) {
        if (!TextUtils.isEmpty(editText.getText())) {
            return Integer.parseInt(editText.getText().toString());
        }
        return null;
    }
    private boolean getBooleanFromCheckbox(CheckBox checkBox){
        return !checkBox.isChecked();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateSearchResults(List<Property> properties) {
        if (searchResultsAdapter != null) {
            RoomDB db = RoomDB.getInstance(getContext());
            PhotoDao photoDao = db.getAllPhotos();
            photoDao.getAllPhotos().observe(getViewLifecycleOwner(), photos -> {
                searchResultsAdapter.setRealEstates(properties,photos);
                searchResultsAdapter.notifyDataSetChanged();
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}