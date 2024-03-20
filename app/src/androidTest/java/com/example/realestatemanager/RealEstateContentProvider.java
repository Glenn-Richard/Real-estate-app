package com.example.realestatemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.provider.ProviderTestRule;

import com.example.realestatemanager.db.RoomDB;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RealEstateContentProvider {

    // Rule that provides testing support for ContentProvider operations.
    @Rule
    public ProviderTestRule providerTestRule = new ProviderTestRule.Builder(com.example.realestatemanager.provider.RealEstateContentProvider.class, com.example.realestatemanager.provider.RealEstateContentProvider.AUTHORITY).build();

    private Context context; // Context used in tests
    private RoomDB testDb; // In-memory database for testing
    private Uri lastInsertedUri; // Stores the Uri of the last inserted item

    @Before
    // Setup method that initializes context, database, and sets the test instance for the database.
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        // Creates an in-memory version of the AppDatabase to not affect the actual database contents.
        testDb = Room.inMemoryDatabaseBuilder(context, RoomDB.class)
                .allowMainThreadQueries() // Allows queries on the main thread for testing purposes
                .build();
        RoomDB.setTestInstance(testDb);
    }

    private void insertTestProperty() {
        // Prepares a property to insert into the content provider
        final ContentValues values = new ContentValues();
        values.put("title", "Test Property");
        values.put("price", "250000");

        // Performs the insertion and stores the Uri of the inserted item
        lastInsertedUri = providerTestRule.getResolver().insert(Uri.parse(com.example.realestatemanager.provider.RealEstateContentProvider.AUTHORITY), values);
        assertNotNull("Uri should not be null after insertion", lastInsertedUri);
    }

    @Test
    // Tests that insertion and querying of a property work as expected.
    public void testInsertAndQuery() {
        insertTestProperty();

        // Queries the content provider for the inserted property
        Cursor cursor = providerTestRule.getResolver().query(lastInsertedUri, null, null, null, null);
        assertNotNull("Cursor should not be null", cursor);
        assertTrue("Cursor should contain at least one result", cursor.moveToFirst());
        cursor.close(); // Closes the cursor to free resources
    }

    @Test
    // Tests that the delete operation works as expected.
    public void testDelete() {
        insertTestProperty();

        // Deletes the inserted property
        int count = providerTestRule.getResolver().delete(lastInsertedUri, null, null);
        assertEquals("Delete operation failed", 1, count);
    }

    @Test
    // Tests that the update operation works as expected.
    public void testUpdate() {
        insertTestProperty();

        // Prepares the content values for the update operation
        ContentValues values = new ContentValues();
        values.put("price", "300000");

        // Performs the update operation
        int count = providerTestRule.getResolver().update(lastInsertedUri, values, null, null);
        assertEquals("Update operation failed", 1, count);
    }
}
