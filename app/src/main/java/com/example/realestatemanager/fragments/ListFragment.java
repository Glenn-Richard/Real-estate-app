package com.example.realestatemanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.realestatemanager.callback.OnItemClickListener;
import com.example.realestatemanager.adapter.RealEstateAdapter;
import com.example.realestatemanager.callback.OnListItemSelectedListener;
import com.example.realestatemanager.databinding.FragmentListBinding;
import com.example.realestatemanager.models.Property;
import com.example.realestatemanager.viewmodel.RoomViewModel;

import java.util.List;

public class ListFragment extends Fragment implements OnItemClickListener,OnListItemSelectedListener {
    private FragmentListBinding binding;
    private RoomViewModel roomViewModel;
    private OnListItemSelectedListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnListItemSelectedListener) {
            listener = (OnListItemSelectedListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnListItemSelectedListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomViewModel = new ViewModelProvider(requireActivity()).get(RoomViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeRealtyLists();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listener != null) {
            listener.onListFragmentDisplayed(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (listener != null) {
            listener.onListFragmentDisplayed(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void observeRealtyLists() {
        roomViewModel.getAllProperties().observe(getViewLifecycleOwner(), this::observePhotosForEachRealty);
    }

    private void observePhotosForEachRealty(List<Property> properties) {
        if (properties.isEmpty()) {
            binding.messageText.setVisibility(View.VISIBLE);
        } else {
            binding.messageText.setVisibility(View.GONE);

            RealEstateAdapter adapter = new RealEstateAdapter(properties, this);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
            binding.recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onItemClick(Property property) {
        if (listener != null) {
            listener.onPropertySelected(property.getId());
        }


    }

    @Override
    public void onListFragmentDisplayed(boolean displayed) {

    }

    @Override
    public void onPropertySelected(int id) {

    }
}