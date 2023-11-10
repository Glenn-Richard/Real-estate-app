package com.example.realestatemanager.repository;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.realestatemanager.models.Property;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class FirebaseRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reference = db.collection("property");
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageRef = storage.getReference("images");
    public final MutableLiveData<List<Property>> properties = new MutableLiveData<>();
    public final MutableLiveData<Uri> photo = new MutableLiveData<>();

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
    public void uploadImage(Uri uri){

        StorageReference imageRef = storageRef.child(uri.getPath());
        imageRef.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> Log.d("UPLOAD_SUCCESS",taskSnapshot.toString()))
                .addOnFailureListener(e -> Log.e("FAIL_UPLOAD",e.getLocalizedMessage()));
    }
    public void getImage(String ref){
        StorageReference imageRef = storageRef.child(ref);
        imageRef.getDownloadUrl()
                .addOnSuccessListener(photo::setValue)
                .addOnFailureListener(e -> Log.e("PIC_NOT_LOAD",e.getLocalizedMessage()));
    }
}
