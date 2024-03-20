package com.example.realestatemanager;

import static org.junit.Assert.assertEquals;

import android.os.Build;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import com.example.realestatemanager.db.PropertyDao;
import com.example.realestatemanager.db.RoomDB;
import com.example.realestatemanager.models.Property;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class RealEstateDaoTest {

    private RoomDB db;
    private PropertyDao dao;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        db = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.application, RoomDB.class)
                .allowMainThreadQueries()
                .build();
        dao = db.getAllProperties();
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void writeRealEstateAndReadInList() throws InterruptedException {
        Property property = new Property();

        property.setId(1);
        dao.insert(property);

        LiveData<List<Property>> liveData = dao.getAllProperties();
        List<Property> realEstateList = getValue(liveData);

        assertEquals(realEstateList.get(0).getId(), property.getId());

    }

    public static <T> T getValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T t) {
                data[0] = t;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        latch.await(2, TimeUnit.SECONDS);
        //noinspection unchecked
        return (T) data[0];
    }
}
