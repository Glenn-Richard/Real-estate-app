package com.example.realestatemanager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(AndroidJUnit4.class)
@SmallTest // Marks this as a small test, implying it runs in milliseconds
public class NetworkConnectionTest {

    @Mock
    private ConnectivityManager connectivityManager;

    @Mock
    private NetworkInfo networkInfo;

    @Test
    public void testInternetAvailable() {
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        when(networkInfo.isConnectedOrConnecting()).thenReturn(true);

        Context context = null; // You can provide a valid context here if necessary
        boolean isAvailable = Utils.isInternetAvailable(context);

        assertTrue(isAvailable);
    }

    @Test
    public void testNoInternetAvailable() {
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(null);

        Context context = null; // You can provide a valid context here if necessary
        boolean isAvailable = Utils.isInternetAvailable(context);

        assertFalse(isAvailable);
    }
}
