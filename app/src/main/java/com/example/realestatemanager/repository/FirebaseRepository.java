package com.example.realestatemanager.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.realestatemanager.models.Property;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FirebaseRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reference = db.collection("property");
    public final MutableLiveData<List<Property>> properties = new MutableLiveData<>();

    public void setProperty(Property property){
        reference.document(property.getId())
                .set(property)
                .addOnSuccessListener(documentReference ->
                        Log.d("FIRESTORE_WRITING","PROPERTY_ADDED"))
                .addOnFailureListener(e ->
                        Log.d("FIRESTORE_ERROR",e.toString()));
    }
    public void getProperties(){
        reference.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        List<Property> properties = task.getResult().toObjects(Property.class);
                        this.properties.setValue(properties);
                    }
                })
                .addOnFailureListener(e -> Log.e("GET_PROP",e.toString()));
    }
}
