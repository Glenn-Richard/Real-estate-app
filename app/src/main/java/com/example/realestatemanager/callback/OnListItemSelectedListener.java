package com.example.realestatemanager.callback;

public interface OnListItemSelectedListener {
    void onListFragmentDisplayed(boolean displayed);

    void onPropertySelected(int id);
}
