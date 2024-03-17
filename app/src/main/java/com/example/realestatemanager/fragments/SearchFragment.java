package com.example.realestatemanager.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.realestatemanager.adapter.SearchResultsAdapter;
import com.example.realestatemanager.databinding.FragmentSearchBinding;
import com.example.realestatemanager.db.PropertyDao;
import com.example.realestatemanager.db.RoomDB;
import com.example.realestatemanager.models.Property;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

        RoomDB db = RoomDB.getInstance(getContext());
        PropertyDao dao = db.getAllProperties();
        LiveData<List<Property>> results = dao.searchProperties(minSurface, maxSurface, minPrice, maxPrice, minRooms, maxRooms, startDateTimestamp, endDateTimestamp);

        results.observe(getViewLifecycleOwner(), this::updateSearchResults);
    }


    private Integer getIntegerFromEditText(EditText editText) {
        if (!TextUtils.isEmpty(editText.getText())) {
            return Integer.parseInt(editText.getText().toString());
        }
        return null;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateSearchResults(List<Property> properties) {
        if (searchResultsAdapter != null) {
            searchResultsAdapter.setRealEstates(properties);
            searchResultsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}