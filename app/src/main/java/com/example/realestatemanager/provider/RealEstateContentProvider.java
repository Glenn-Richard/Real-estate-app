package com.example.realestatemanager.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.realestatemanager.db.RoomDB;
import com.example.realestatemanager.models.Property;

public class RealEstateContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.realestatemanager.provider";

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if (getContext() != null) {
            final Cursor cursor;
            cursor = RoomDB.getInstance(getContext()).getAllProperties().getItemsWithCursor();
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;

        }
        throw new IllegalArgumentException("Failed to query for uri: " + uri);
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        if (getContext() != null && contentValues != null) {
            final long id = RoomDB.getInstance(getContext()).getAllProperties().insert(Property.fromContentValues(contentValues));
            if (id != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }
        }
        throw new IllegalArgumentException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (getContext() != null) {
            final int count = RoomDB.getInstance(getContext()).getAllProperties().deleteById(ContentUris.parseId(uri));
            if (count > 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return count;
        }
        throw new IllegalArgumentException("Failed to delete row into " + uri);
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (getContext() != null && contentValues != null) {
            Property property = Property.fromContentValues(contentValues);
            property.setId((int) ContentUris.parseId(uri));
            int count = RoomDB.getInstance(getContext()).getAllProperties().update(property);
            if (count > 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return count;
        }
        throw new IllegalArgumentException("Failed to update row into " + uri);
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.item/" + AUTHORITY + ".RealEstate";
    }
}
